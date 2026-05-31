package com.cloudpicture.infrastructure.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudpicture.domain.picture.entity.Picture;
import com.cloudpicture.domain.picture.repository.PictureRepository;
import com.cloudpicture.infrastructure.mapper.PictureMapper;
import org.springframework.stereotype.Service;

/**
 * 图片仓储实现
 */
@Service
public class PictureRepositoryImpl extends ServiceImpl<PictureMapper, Picture> implements PictureRepository {
}