package com.chieh.dream.system.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.chieh.dream.system.facade.IErrorMsgFacade;
import com.chieh.umdp.generic.been.TableResult;
import com.chieh.umdp.generic.been.TableState;

@Controller
@Scope(value = "prototype")
@RequestMapping("/ErrorMsg")
public class ErrorMsgAction {

  @Autowired
  private IErrorMsgFacade errorMsgFacade;

  /**
   * @description 輸入語言代碼取得所有ui錯誤訊息
   * @note Created On 2018年2月7日
   * @author JayKang
   * @param tableState (langCode語言代碼)
   * @return tableState (ui錯誤訊息列表)
   */
  @RequestMapping(value = "/getAllMsg", method = RequestMethod.POST)
  public TableResult getAllMsg(@RequestBody TableState tableState) {
	  return errorMsgFacade.getAllMsg(tableState);
  }

}
