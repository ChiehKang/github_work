package com.chieh.dream.system.facade;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.chieh.umdp.generic.been.TableResult;

public interface UserFacade {
	TableResult login(HttpServletRequest request, Map<String, Object> tableState);
}
