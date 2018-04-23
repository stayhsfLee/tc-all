package com.thenorthw.tc.web.controller.notification;

import com.thenorthw.blog.common.ResponseCode;
import com.thenorthw.blog.common.ResponseModel;
import com.thenorthw.blog.common.constants.BlogConstant;
import com.thenorthw.blog.face.form.notification.NotificationAddForm;
import com.thenorthw.blog.web.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @autuor theNorthW
 * @date 12/10/2017.
 * blog: theNorthW.net
 */
@Controller
public class NotificationController {

	@Autowired
	NotificationService notificationService;
	@Autowired
	HttpServletRequest httpServletRequest;

	/**
	 *
	 * @return
	 */
	@RequestMapping(value = "notification", method = RequestMethod.POST)
	@ResponseBody
	public ResponseModel addNotification(NotificationAddForm addForm){
		ResponseModel responseModel = new ResponseModel();


		Long accountIdL = (Long)httpServletRequest.getSession().getAttribute(BlogConstant.ACCOUNT_ID);

		int res = notificationService.addNotification(accountIdL,addForm.getName(),addForm.getContent(),Integer.parseInt(addForm.getGrammar()));

		return responseModel;
	}

	/**
	 *
	 * @return
	 */
	@RequestMapping(value = "notification", method = RequestMethod.GET)
	@ResponseBody
	public ResponseModel getRecentNotifications(){
		ResponseModel responseModel = new ResponseModel();

		responseModel.setData(notificationService.getRecentNotifications(10));

		return responseModel;
	}

	/**
	 *
	 * @return
	 */
	@RequestMapping(value = "notification/all", method = RequestMethod.GET)
	@ResponseBody
	public ResponseModel getAllNotifications(){
		ResponseModel responseModel = new ResponseModel();

		responseModel.setData(notificationService.getAllNotifications());

		return responseModel;
	}

	/**
	 *
	 * @return
	 */
	@RequestMapping(value = "notification", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseModel deleteNotifications(Long id){
		ResponseModel responseModel = new ResponseModel();

		int res = notificationService.deleteNotification(id);

		if(res == 0){
			responseModel.setResponseCode(ResponseCode.NO_SUCH_NOTIFICATION.getCode());
			responseModel.setMessage(ResponseCode.NO_SUCH_NOTIFICATION.getMessage());
		}

		return responseModel;
	}
}
