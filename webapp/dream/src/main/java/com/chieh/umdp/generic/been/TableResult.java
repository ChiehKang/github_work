package com.chieh.umdp.generic.been;

import java.util.Map;

public class TableResult {
	private long dataCount;
	private Map<String, Object> datas;
	private String errMsg;
	
	public TableResult() {}
	
	public long getDataCount() {
		return dataCount;
	}
	public void setDataCount(long dataCount) {
		this.dataCount = dataCount;
	}
	public Map<String, Object> getDatas() {
		return datas;
	}
	public void setDatas(Map<String, Object> datas) {
		this.datas = datas;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	
}
