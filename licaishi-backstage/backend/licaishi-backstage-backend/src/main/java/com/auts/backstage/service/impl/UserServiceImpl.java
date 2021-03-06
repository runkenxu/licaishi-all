package com.auts.backstage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auts.backstage.dao.UserMapper;
import com.auts.backstage.model.dao.AccountModel;
import com.auts.backstage.service.UserService;
import com.auts.backstage.util.EntryUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserMapper userMapper;
	
	@Override
	public AccountModel queryLoginUser(String username, String password) {
		return userMapper.queryLoginUser(username, EntryUtils.getMd5(password));
	}

}