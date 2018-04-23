package com.thenorthw.tc.web.controller.tag;

import com.thenorthw.blog.common.ResponseCode;
import com.thenorthw.blog.common.ResponseModel;
import com.thenorthw.blog.common.constants.BlogConstant;
import com.thenorthw.blog.common.model.tag.Tag;
import com.thenorthw.blog.face.form.tag.TagAddForm;
import com.thenorthw.blog.web.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @autuor theNorthW
 * @date 20/09/2017.
 * blog: thenorthw.com
 */
@Controller
@RequestMapping(value = "/web/v1")
public class TagController {
	@Autowired
	HttpServletRequest httpServletRequest;
	@Autowired
	TagService tagService;

	@RequestMapping(value = "/tag",method = RequestMethod.GET)
	@ResponseBody
	public ResponseModel getAllTags(){
		ResponseModel responseModel = new ResponseModel();

		List<Tag> tags = tagService.getAllTags();
		responseModel.setData(tags);
		return responseModel;
	}

	@RequestMapping(value = "/tag", method = RequestMethod.POST)
	@ResponseBody
	public ResponseModel addTag(TagAddForm tagAddForm){
		ResponseModel responseModel = new ResponseModel();

		//需要从jwt中获取user id，从而知晓creator
		Tag tag = new Tag();
		Date now = new Date();
		tag.setName(tagAddForm.getName());
		tag.setDescription(tagAddForm.getDescription());
		tag.setCreator((Long)httpServletRequest.getSession().getAttribute(BlogConstant.ACCOUNT_ID));
		tag.setGmtCreate(now);
		tag.setGmtModified(now);

		int result = tagService.addTag(tag);

		if(result == -1){
			responseModel.setResponseCode(ResponseCode.TAG_ADD_ERROR.getCode());
			responseModel.setMessage(ResponseCode.TAG_ADD_ERROR.getMessage());
			return responseModel;
		}

		responseModel.setData(tagService.getAllTags());
		return responseModel;
	}

	@RequestMapping(value = "/tag",method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseModel deleteTag(@RequestParam(value = "id") String id){
		ResponseModel responseModel = new ResponseModel();
		Long idL = Long.parseLong(id);

		int result = tagService.deleteTag(idL);

		if(result == -1){
			responseModel.setResponseCode(ResponseCode.TAG_DELETE_ERROR.getCode());
			responseModel.setMessage(ResponseCode.TAG_DELETE_ERROR.getMessage());
		}else if(result == -2){
			responseModel.setResponseCode(ResponseCode.NO_SUCH_TAG.getCode());
			responseModel.setMessage(ResponseCode.NO_SUCH_TAG.getMessage());
		}

		return responseModel;
	}



}
