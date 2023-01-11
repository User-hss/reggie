package com.hss.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.reggie.pojo.User;
import com.hss.reggie.service.UserService;
import com.hss.reggie.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author master
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2023-01-05 19:30:28
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




