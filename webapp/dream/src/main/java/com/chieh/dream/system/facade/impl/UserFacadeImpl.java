package com.chieh.dream.system.facade.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.chieh.dream.system.facade.UserFacade;
import com.chieh.dream.system.service.UserService;
import com.chieh.umdp.generic.been.TableResult;

@Component
@Transactional
public class UserFacadeImpl implements UserFacade{

	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	@Override
	public TableResult login(HttpServletRequest request, Map<String, Object> tableState) {
		// TODO Auto-generated method stub
		return userService.login(request, tableState);
	}

}
