package com.chieh.dream.system.facade;

import com.chieh.umdp.generic.been.TableResult;
import com.chieh.umdp.generic.been.TableState;

/**
 * @description 查詢ui使用的錯誤訊息
 * @author Jason.Liu
 * @since 2017年8月25日
 */
public interface IErrorMsgFacade {

  /**
   * @description 輸入語言代碼取得所有ui錯誤訊息
   * @note Created On 2017年8月25日
   * @author Jason Liu
   * @param tableState (langCode語言代碼)
   * @return tableState (ui錯誤訊息列表)
   */
  TableResult getAllMsg(TableState tableState);

}
