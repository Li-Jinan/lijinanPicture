package com.cloudpicture.backend.manager.upload;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.CIObject;
import com.qcloud.cos.model.ciModel.persistence.ImageInfo;
import com.qcloud.cos.model.ciModel.persistence.ProcessResults;
import com.cloudpicture.backend.config.CosClientConfig;
import com.cloudpicture.backend.exception.BusinessException;
import com.cloudpicture.backend.exception.ErrorCode;
import com.cloudpicture.backend.manager.CosManager;
import com.cloudpicture.backend.model.dto.file.UploadPictureResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

/**
 * 图片上传模板
 */
@Slf4j
public abstract class PictureUploadTemplate {

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private CosManager cosManager;

    @Value("${local.upload.dir:/app/uploads}")
    private String localUploadDir;

    @Value("${local.upload.url-prefix:/uploads}")
    private String localUploadUrlPrefix;

    /**
     * 上传图片
     *
     * @param inputSource      文件
     * @param uploadPathPrefix 上传路径前缀
     * @return
     */
    public UploadPictureResult uploadPicture(Object inputSource, String uploadPathPrefix) {
        // 1. 校验图片
        validPicture(inputSource);
        // 2. 图片上传地址
        String uuid = RandomUtil.randomString(16);
        String originalFilename = getOriginFilename(inputSource);
        // 自己拼接文件上传路径，而不是使用原始文件名称，可以增强安全性
        String uploadFilename = String.format("%s_%s.%s", DateUtil.formatDate(new Date()), uuid,
                FileUtil.getSuffix(originalFilename));
        String uploadPath = String.format("/%s/%s", uploadPathPrefix, uploadFilename);
        File file = null;
        try {
            // 3. 创建临时文件，获取文件到服务器
            file = File.createTempFile("picture-upload-", "." + FileUtil.getSuffix(originalFilename));
            // 处理文件来源
            processFile(inputSource, file);
            if (isLocalUploadEnabled()) {
                return uploadToLocal(originalFilename, file, uploadPath);
            }
            // 4. 上传图片到对象存储
            PutObjectResult putObjectResult = cosManager.putPictureObject(uploadPath, file);
            // 5. 获取图片信息对象，封装返回结果
            ImageInfo imageInfo = putObjectResult.getCiUploadResult().getOriginalInfo().getImageInfo();
            // 获取到图片处理结果
            ProcessResults processResults = putObjectResult.getCiUploadResult().getProcessResults();
            List<CIObject> objectList = processResults.getObjectList();
            if (CollUtil.isNotEmpty(objectList)) {
                // 获取压缩之后得到的文件信息
                CIObject compressedCiObject = objectList.get(0);
                // 缩略图默认等于压缩图
                CIObject thumbnailCiObject = compressedCiObject;
                // 有生成缩略图，才获取缩略图
                if (objectList.size() > 1) {
                    thumbnailCiObject = objectList.get(1);
                }
                // 封装压缩图的返回结果
                return buildResult(originalFilename, compressedCiObject, thumbnailCiObject, imageInfo);
            }
            return buildResult(originalFilename, file, uploadPath, imageInfo);
        } catch (Exception e) {
            log.error("图片上传到对象存储失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            // 6. 临时文件清理
            this.deleteTempFile(file);
        }

    }

    private boolean isLocalUploadEnabled() {
        return StrUtil.hasBlank(cosClientConfig.getHost(), cosClientConfig.getSecretId(),
                cosClientConfig.getSecretKey(), cosClientConfig.getRegion(), cosClientConfig.getBucket());
    }

    private UploadPictureResult uploadToLocal(String originalFilename, File file, String uploadPath) throws Exception {
        String normalizedPath = uploadPath.startsWith("/") ? uploadPath.substring(1) : uploadPath;
        File targetFile = new File(localUploadDir, normalizedPath);
        FileUtil.mkParentDirs(targetFile);
        Files.copy(file.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        BufferedImage image = ImageIO.read(targetFile);
        if (image == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "图片解析失败");
        }
        UploadPictureResult uploadPictureResult = new UploadPictureResult();
        String urlPrefix = localUploadUrlPrefix.endsWith("/")
                ? localUploadUrlPrefix.substring(0, localUploadUrlPrefix.length() - 1)
                : localUploadUrlPrefix;
        uploadPictureResult.setUrl(urlPrefix + "/" + normalizedPath);
        uploadPictureResult.setThumbnailUrl(uploadPictureResult.getUrl());
        uploadPictureResult.setPicName(FileUtil.mainName(originalFilename));
        uploadPictureResult.setPicSize(FileUtil.size(targetFile));
        uploadPictureResult.setPicWidth(image.getWidth());
        uploadPictureResult.setPicHeight(image.getHeight());
        uploadPictureResult.setPicScale(NumberUtil.round(image.getWidth() * 1.0 / image.getHeight(), 2).doubleValue());
        uploadPictureResult.setPicFormat(FileUtil.getSuffix(originalFilename));
        uploadPictureResult.setPicColor(getAverageColor(image));
        return uploadPictureResult;
    }

    private String getAverageColor(BufferedImage image) {
        long red = 0;
        long green = 0;
        long blue = 0;
        int count = 0;
        int stepX = Math.max(1, image.getWidth() / 64);
        int stepY = Math.max(1, image.getHeight() / 64);
        for (int y = 0; y < image.getHeight(); y += stepY) {
            for (int x = 0; x < image.getWidth(); x += stepX) {
                int rgb = image.getRGB(x, y);
                red += (rgb >> 16) & 0xff;
                green += (rgb >> 8) & 0xff;
                blue += rgb & 0xff;
                count++;
            }
        }
        if (count == 0) {
            return "#FFFFFF";
        }
        return String.format("#%02X%02X%02X", red / count, green / count, blue / count);
    }

    /**
     * 校验输入源（本地文件或 URL）
     */
    protected abstract void validPicture(Object inputSource);

    /**
     * 获取输入源的原始文件名
     */
    protected abstract String getOriginFilename(Object inputSource);

    /**
     * 处理输入源并生成本地临时文件
     */
    protected abstract void processFile(Object inputSource, File file) throws Exception;

    /**
     * 封装返回结果
     *
     * @param originalFilename   原始文件名
     * @param compressedCiObject 压缩后的对象
     * @param thumbnailCiObject 缩略图对象
     * @param imageInfo 图片信息
     * @return
     */
    private UploadPictureResult buildResult(String originalFilename, CIObject compressedCiObject, CIObject thumbnailCiObject,
                                            ImageInfo imageInfo) {
        // 计算宽高
        int picWidth = compressedCiObject.getWidth();
        int picHeight = compressedCiObject.getHeight();
        double picScale = NumberUtil.round(picWidth * 1.0 / picHeight, 2).doubleValue();
        // 封装返回结果
        UploadPictureResult uploadPictureResult = new UploadPictureResult();
        // 设置压缩后的原图地址
        uploadPictureResult.setUrl(cosClientConfig.getHost() + "/" + compressedCiObject.getKey());
        uploadPictureResult.setPicName(FileUtil.mainName(originalFilename));
        uploadPictureResult.setPicSize(compressedCiObject.getSize().longValue());
        uploadPictureResult.setPicWidth(picWidth);
        uploadPictureResult.setPicHeight(picHeight);
        uploadPictureResult.setPicScale(picScale);
        uploadPictureResult.setPicFormat(compressedCiObject.getFormat());
        uploadPictureResult.setPicColor(imageInfo.getAve());
        // 设置缩略图地址
        uploadPictureResult.setThumbnailUrl(cosClientConfig.getHost() + "/" + thumbnailCiObject.getKey());
        // 返回可访问的地址
        return uploadPictureResult;
    }

    /**
     * 封装返回结果
     *
     * @param originalFilename
     * @param file
     * @param uploadPath
     * @param imageInfo        对象存储返回的图片信息
     * @return
     */
    private UploadPictureResult buildResult(String originalFilename, File file, String uploadPath, ImageInfo imageInfo) {
        // 计算宽高
        int picWidth = imageInfo.getWidth();
        int picHeight = imageInfo.getHeight();
        double picScale = NumberUtil.round(picWidth * 1.0 / picHeight, 2).doubleValue();
        // 封装返回结果
        UploadPictureResult uploadPictureResult = new UploadPictureResult();
        uploadPictureResult.setUrl(cosClientConfig.getHost() + "/" + uploadPath);
        uploadPictureResult.setPicName(FileUtil.mainName(originalFilename));
        uploadPictureResult.setPicSize(FileUtil.size(file));
        uploadPictureResult.setPicWidth(picWidth);
        uploadPictureResult.setPicHeight(picHeight);
        uploadPictureResult.setPicScale(picScale);
        uploadPictureResult.setPicFormat(imageInfo.getFormat());
        uploadPictureResult.setPicColor(imageInfo.getAve());
        // 返回可访问的地址
        return uploadPictureResult;
    }

    /**
     * 清理临时文件
     *
     * @param file
     */
    public void deleteTempFile(File file) {
        if (file == null) {
            return;
        }
        // 删除临时文件
        boolean deleteResult = file.delete();
        if (!deleteResult) {
            log.error("file delete error, filepath = {}", file.getAbsolutePath());
        }
    }
}












