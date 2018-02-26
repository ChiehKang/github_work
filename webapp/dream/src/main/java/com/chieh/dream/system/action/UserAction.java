package com.chieh.dream.system.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.chieh.dream.system.facade.UserFacade;
import com.chieh.umdp.generic.been.TableResult;

@RestController
@Scope(value = "prototype")
@RequestMapping("/user")
public class UserAction {
	
	@Autowired
	@Qualifier("userFacadeImpl")
	private UserFacade userFacade;
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public Map<String, Object> mappingTest(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", "Hi, "+request.getSession().getAttribute("userName")+".");

		return resultMap;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public TableResult login(HttpServletRequest request, @RequestBody Map<String, Object> tableState) {
		return userFacade.login(request, tableState);	
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public Map<String, String> logout(HttpServletRequest request) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("SYS_MSG", "已经成功登出");
		request.getSession().invalidate();
		return resultMap;
	}
}
