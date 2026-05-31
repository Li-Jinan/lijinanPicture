package com.cloudpicture.infrastructure.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudpicture.domain.space.entity.Space;
import com.cloudpicture.domain.space.repository.SpaceRepository;
import com.cloudpicture.infrastructure.mapper.SpaceMapper;
import org.springframework.stereotype.Service;

/**
 * 空间仓储实现
 */
@Service
public class SpaceRepositoryImpl extends ServiceImpl<SpaceMapper, Space> implements SpaceRepository {
}