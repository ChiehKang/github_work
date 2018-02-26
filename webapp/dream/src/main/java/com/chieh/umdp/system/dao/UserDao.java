package com.chieh.umdp.system.dao;

import java.util.List;

import com.chieh.umdp.generic.entity.CurUser;

public interface UserDao {
	
	CurUser GetSingleAccount (String account, String password) throws Exception;
	
	CurUser getUserInfo (String account) throws Exception;

	List<Long> getAllUserId();
}
