package com.huzaifa.app.ws.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.huzaifa.app.ws.UserRepository;
import com.huzaifa.app.ws.io.entity.UserEntity;
import com.huzaifa.app.ws.service.UserService;
import com.huzaifa.app.ws.shared.Utilities;
import com.huzaifa.app.ws.shared.dto.UserDto;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	Utilities utils;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public UserDto createUser(UserDto user) {
		if(userRepository.findByEmail(user.getEmail())!=null) throw new RuntimeException("Record already exists");
		UserEntity userEntity = new UserEntity();
//		userEntity.setEmail(user.getEmail());
//		userEntity.setFirstName(user.getFirstName());
//		userEntity.setLastName(user.getLastName());
//		userEntity.setEmailVerificationStatus(user.getEmailVerificationStatus());
		BeanUtils.copyProperties(user, userEntity);
		
		String publicUserId = utils.generateUserId(30);
		userEntity.setUserId(publicUserId);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		UserEntity storedUserDetails = userRepository.save(userEntity);
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(storedUserDetails, returnValue);
		
		
		return returnValue;
	}

}
