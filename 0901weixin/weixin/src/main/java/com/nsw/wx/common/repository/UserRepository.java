package com.nsw.wx.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nsw.wx.common.model.User;



public interface UserRepository  extends JpaRepository<User, Long> {
	
	User findByUserNameAndPassWord(String userName, String passWord);
	
	User findByUserName(String username);
	
	List<User> findByUserNameContaining(String username);
}
