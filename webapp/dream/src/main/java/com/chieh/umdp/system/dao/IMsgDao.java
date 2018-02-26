package com.chieh.umdp.system.dao;

import java.util.Map;

public interface IMsgDao {
	Map getAllMsg();
	Map getErrorMsg(String ErrorCode);
}