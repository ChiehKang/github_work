package com.chieh.dream.system.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.chieh.umdp.generic.been.TableResult;

public interface UserService {
	
	TableResult login (HttpServletRequest request, Map<String, Object> tableState);
}
