package com.cloudpicture.infrastructure.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudpicture.domain.space.entity.SpaceUser;
import com.cloudpicture.domain.space.repository.SpaceUserRepository;
import com.cloudpicture.infrastructure.mapper.SpaceUserMapper;
import org.springframework.stereotype.Service;

/**
 * 空间用户仓储实现
 */
@Service
public class SpaceUserRepositoryImpl extends ServiceImpl<SpaceUserMapper, SpaceUser> implements SpaceUserRepository {
}