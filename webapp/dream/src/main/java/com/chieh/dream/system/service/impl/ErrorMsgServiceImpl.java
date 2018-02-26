package com.chieh.dream.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chieh.dream.system.service.IErrorMsgService;
import com.chieh.umdp.generic.been.TableResult;
import com.chieh.umdp.generic.been.TableState;
import com.chieh.umdp.system.dao.IMsgDao;

@Service
public class ErrorMsgServiceImpl implements IErrorMsgService {

	@Autowired
	IMsgDao msgDao;

	public TableResult getAllMsg(TableState tableState) {
//		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
//		Map<String, Object> cfgMsgMap = new HashMap<String, Object>();
//		String langCd = getTableStateSearchValue(tableState, "langCd");
//		Map<String, Object> tableStateMap = tableState.getSearch().getPredicateObject();
//		String[] msgArray = SystemSetting.UI_ERR_MSG.split(",");
//		List<String> msgList = Arrays.asList(msgArray);
//		List<CfgMsg> cfgMsgList = iMsgDao.getUIMsgByMsgCode(langCd, msgList);
//		for (CfgMsg cfgMsg : cfgMsgList) {
//			cfgMsgMap = new HashMap<String, Object>();
//			cfgMsgMap.put(cfgMsg.getId().getMsgCd(), cfgMsg.getMsg());
//			resoult.add(cfgMsgMap);
//		}
//		return new TableResult(new Long(resoult.size()), result);
		return null;
	}

	private String getTableStateSearchValue(TableState tableState, String key) {
		String result = null;
		try {
			//result = tableState.getSearch().getPredicateObject().get(key).toString();
		} catch (Exception e) {
			e.printStackTrace();
			//throw new UmingSysException("MSG_SYS_0017", ErrorMsgLocation.SYSTEM);
		}
		return result;
	}

}
