package com.cloudpicture.interfaces.assembler;

import com.cloudpicture.domain.space.entity.SpaceUser;
import com.cloudpicture.interfaces.dto.spaceuser.SpaceUserAddRequest;
import com.cloudpicture.interfaces.dto.spaceuser.SpaceUserEditRequest;
import org.springframework.beans.BeanUtils;

/**
 * 空间用户对象转换
 */
public class SpaceUserAssembler {

    public static SpaceUser toSpaceUserEntity(SpaceUserAddRequest request) {
        SpaceUser spaceUser = new SpaceUser();
        BeanUtils.copyProperties(request, spaceUser);
        return spaceUser;
    }

    public static SpaceUser toSpaceUserEntity(SpaceUserEditRequest request) {
        SpaceUser spaceUser = new SpaceUser();
        BeanUtils.copyProperties(request, spaceUser);
        return spaceUser;
    }
}