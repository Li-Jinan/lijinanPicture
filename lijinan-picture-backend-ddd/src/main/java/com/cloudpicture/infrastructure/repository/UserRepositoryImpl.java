package com.cloudpicture.infrastructure.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudpicture.domain.user.entity.User;
import com.cloudpicture.domain.user.repository.UserRepository;
import com.cloudpicture.infrastructure.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * 用户仓储实现
 */
@Service
public class UserRepositoryImpl extends ServiceImpl<UserMapper, User> implements UserRepository {
}