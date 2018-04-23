package com.thenorthw.tc.web.controller.group;

import com.thenorthw.blog.common.ResponseCode;
import com.thenorthw.blog.common.ResponseModel;
import com.thenorthw.blog.common.annotation.AdminAllowed;
import com.thenorthw.blog.common.annotation.LoginNeed;
import com.thenorthw.blog.common.model.group.Group;
import com.thenorthw.blog.face.form.group.GroupPostForm;
import com.thenorthw.blog.face.form.group.GroupUpdateForm;
import com.thenorthw.blog.web.service.group.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

/**
 * @autuor theNorthW
 * @date 19/09/2017.
 * blog: thenorthw.com
 */
@Controller
@RequestMapping(value = "/web/v1")
public class GroupController {

	@Autowired
	GroupService groupService;

	/**
	 * 这个方法是直接获取所有的group（二维数组形式返回给前端），所以不需要参数
	 * service此处还会做一个缓存操作，因为article group不会有太大变动，这样就可以减轻数据库压力
	 * @return
	 */
	@RequestMapping(value = "/group", method = RequestMethod.GET)
	@ResponseBody
	public ResponseModel getAllArticleGroups(){
		ResponseModel responseModel = new ResponseModel();

		List<Group> articleGroupList = groupService.getAllGroups();

		//是否以树形结构输出，感觉不需要呀 - -

		if(articleGroupList == null){
			responseModel.setResponseCode(ResponseCode.INTERNAL_SERVER_ERROR.getCode());
			responseModel.setMessage("Get failed, unknown error.");
		}else{
			responseModel.setData(articleGroupList);
		}

		return responseModel;
	}

	/**
	 * 不允许重名的group出现
	 * @param groupPostForm
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "/group", method = RequestMethod.POST)
	@ResponseBody
	@LoginNeed
	@AdminAllowed
	public ResponseModel addArticleGroups(@Valid GroupPostForm groupPostForm, BindingResult bindingResult){
		ResponseModel responseModel = new ResponseModel();

		Group articleGroup = new Group();
		articleGroup.setName(groupPostForm.getName());
		articleGroup.setEn(groupPostForm.getEn());
		articleGroup.setParentGroupId(groupPostForm.getParentGroupId());

		int res = groupService.addGroup(articleGroup);

		if(res == -1){
			responseModel.setResponseCode(ResponseCode.INTERNAL_SERVER_ERROR.getCode());
			responseModel.setMessage(ResponseCode.INTERNAL_SERVER_ERROR.getMessage());
		}else if(res == -2){
			responseModel.setResponseCode(ResponseCode.NO_SUCH_PARENT_GROUP.getCode());
			responseModel.setMessage(ResponseCode.NO_SUCH_PARENT_GROUP.getMessage());
		}else if (res == -3){
			responseModel.setResponseCode(ResponseCode.PARENT_LEVEL_ILLEGAL.getCode());
			responseModel.setMessage(ResponseCode.PARENT_LEVEL_ILLEGAL.getMessage());
		}else if(res == -4){
			responseModel.setResponseCode(ResponseCode.DUPLICATED_GROUP.getCode());
			responseModel.setMessage(ResponseCode.DUPLICATED_GROUP.getMessage());
		}else if(res == -5){
			responseModel.setResponseCode(ResponseCode.DUPLICATED_GROUP_EN.getCode());
			responseModel.setMessage(ResponseCode.DUPLICATED_GROUP_EN.getMessage());
		}

		responseModel.setData(groupService.getAllGroups());

		return responseModel;
	}


	/**
	 * 更新时候也不允许重名
	 * @param groupUpdateForm
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "/group/info", method = RequestMethod.POST)
	@ResponseBody
	@LoginNeed
	@AdminAllowed
	public ResponseModel updateArticleGroup(GroupUpdateForm groupUpdateForm, BindingResult bindingResult){
		ResponseModel responseModel = new ResponseModel();

		Group articleGroup = new Group();
		articleGroup.setId(groupUpdateForm.getId());
		articleGroup.setName(groupUpdateForm.getName());
		articleGroup.setEn(groupUpdateForm.getEn());
		articleGroup.setParentGroupId(groupUpdateForm.getParentGroupId());

		int res = groupService.updateGroup(articleGroup);

		if(res == -1){
			responseModel.setResponseCode(ResponseCode.INTERNAL_SERVER_ERROR.getCode());
			responseModel.setMessage(ResponseCode.INTERNAL_SERVER_ERROR.getMessage());
		}else if(res == -4){
			responseModel.setResponseCode(ResponseCode.NO_SUCH_GROUP.getCode());
			responseModel.setMessage(ResponseCode.NO_SUCH_GROUP.getMessage());
		}else if(res == -2){
			responseModel.setResponseCode(ResponseCode.NO_SUCH_PARENT_GROUP.getCode());
			responseModel.setMessage(ResponseCode.NO_SUCH_PARENT_GROUP.getMessage());
		}else if(res == -3){
			responseModel.setResponseCode(ResponseCode.PARENT_LEVEL_ILLEGAL.getCode());
			responseModel.setMessage(ResponseCode.PARENT_LEVEL_ILLEGAL.getMessage());
		}else if(res == -6){
			responseModel.setResponseCode(ResponseCode.DUPLICATED_GROUP.getCode());
			responseModel.setMessage(ResponseCode.DUPLICATED_GROUP.getMessage());
		}else if(res == -5){
			responseModel.setResponseCode(ResponseCode.DUPLICATED_GROUP_EN.getCode());
			responseModel.setMessage(ResponseCode.DUPLICATED_GROUP_EN.getMessage());
		}


		return responseModel;
	}


	/**
	 * 暂时不允许group的删除，因为删除后对文章影响较大
	 * 作为个人博客使用时候，会实现有一个默认分类，默认分类不允许被删除，不然的话，删除后就会有文章不属于分类里
	 * 作为中间件网站时，会事先有分类，用户post文章时，只需要将文章正确分类即可
	 * 用户想增加分类，则可以申请表单有管理员进行操作
	 */
	/**
	@RequestMapping(value = "/group", method = RequestMethod.DELETE)
	@ResponseBody
	@AdminAllowed
	public ResponseModel deleteArticleGroups(@RequestParam(value = "id")String id){
		ResponseModel responseModel = new ResponseModel();

		Long idL = Long.parseLong(id);
		int res = groupService.deleteArticleGroup(idL);

		if(res == -1){
			responseModel.setResponseCode(ResponseCode.GROUP_DELETE_ERROR.getCode());
			responseModel.setMessage(ResponseCode.GROUP_DELETE_ERROR.getMessage());
			return responseModel;
		}

		//需要删除和这个分类相关的文章


		return responseModel;
	}
	**/
}