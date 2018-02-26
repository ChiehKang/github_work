package com.chieh.dream.system.facade.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chieh.dream.system.facade.IErrorMsgFacade;
import com.chieh.dream.system.service.IErrorMsgService;
import com.chieh.umdp.generic.been.TableResult;
import com.chieh.umdp.generic.been.TableState;

/**
 * @description
 * @author JayKang
 * @since 2018年2月7日
 */
@Component
@Transactional
public class ErrorMsgFacadeImpl implements IErrorMsgFacade {

  @Autowired
  private IErrorMsgService errorMsgService;

  @Override
  public TableResult getAllMsg(TableState tableState) {
    return errorMsgService.getAllMsg(tableState);
  }

}
