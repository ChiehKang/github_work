package com.chieh.dream.system.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.chieh.dream.system.service.UserService;
import com.chieh.umdp.generic.been.TableResult;
import com.chieh.umdp.generic.entity.CurUser;
import com.chieh.umdp.system.dao.UserDao;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	@Qualifier("userDaoImpl")
	private UserDao userDao;

	@Override
	public TableResult login(HttpServletRequest request, Map<String, Object> tableState) {
		// TODO Auto-generated method stub
		// password 加密

		TableResult tableResult = new TableResult();
		try {
			String account = tableState.get("account").toString();
			String password = tableState.get("password").toString();
			
			CurUser curUser = userDao.GetSingleAccount(account, password);
			
			if (curUser != null) {
				HashMap<String, Object> userData = new HashMap<String, Object>();

				userData.put("userName", curUser.getUserName());
				userData.put("aothority", curUser.getAothority());

				tableResult.setDataCount(2);
				tableResult.setDatas(userData);
				
				request.getSession(true);
				HttpSession session = request.getSession();
				
				session.setAttribute("userName", curUser.getUserName());
				session.setAttribute("aothority", curUser.getAothority());
			}
			else {
				tableResult.setDataCount(0);
				tableResult.setErrMsg("您輸入的帳號不存在或者密碼錯誤!");
			}
			
		} catch(Exception e) {
			
		}
		return tableResult;
	}
}
