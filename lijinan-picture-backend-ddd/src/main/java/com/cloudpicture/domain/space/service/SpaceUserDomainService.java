package com.cloudpicture.domain.space.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloudpicture.domain.space.entity.SpaceUser;
import com.cloudpicture.interfaces.dto.spaceuser.SpaceUserQueryRequest;

/**
 * 维护者：管理员
 * @description 针对表【space_user(空间用户关联)】的数据库操作Service
 * @createDate 2025-01-02 20:07:15
 */
public interface SpaceUserDomainService {

    /**
     * 获取查询对象
     *
     * @param spaceUserQueryRequest
     * @return
     */
    QueryWrapper<SpaceUser> getQueryWrapper(SpaceUserQueryRequest spaceUserQueryRequest);
}
