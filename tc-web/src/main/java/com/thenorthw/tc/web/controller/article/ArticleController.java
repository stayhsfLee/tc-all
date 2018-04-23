package com.thenorthw.tc.web.controller.article;

import com.thenorthw.blog.common.ResponseCode;
import com.thenorthw.blog.common.ResponseModel;
import com.thenorthw.blog.common.annotation.LoginNeed;
import com.thenorthw.blog.common.constants.BlogConstant;
import com.thenorthw.blog.common.model.article.Article;
import com.thenorthw.blog.common.model.article.ArticleContent;
import com.thenorthw.blog.common.model.group.Group;
import com.thenorthw.blog.common.model.group.RArticleGroup;
import com.thenorthw.blog.common.utils.JwtUtil;
import com.thenorthw.blog.face.dto.ArticleDto;
import com.thenorthw.blog.face.form.Page;
import com.thenorthw.blog.face.form.article.ArticlePostForm;
import com.thenorthw.blog.face.form.article.ArticleUpdateForm;
import com.thenorthw.blog.face.form.article.GroupChangePostForm;
import com.thenorthw.blog.web.service.article.ArticleService;
import com.thenorthw.blog.web.service.group.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * @autuor theNorthW
 * @date 17/09/2017.
 * blog: thenorthw.com
 */
@RequestMapping(value = "/web/v1")
@Controller
public class ArticleController {
	@Autowired
	HttpServletRequest httpServletRequest;
	@Autowired
	ArticleService articleService;
	@Autowired
	GroupService groupService;

	/**
	 * 来获取最近发表的article 10篇
	 * @return
	 */
	@RequestMapping(value = "/article/recent", method = RequestMethod.GET)
	@ResponseBody
	public ResponseModel getRecentArticles(){
		ResponseModel responseModel = new ResponseModel();

		List<ArticleDto> dtos = articleService.getRecentArticles();

		responseModel.setData(dtos);

		return responseModel;
	}

	/**
	 * 来获取所有发表过的articles，不过返回的只是不包含content的元信息
	 * 前端可以画一条时间线
	 * @return
	 */
	@RequestMapping(value = "/article/all", method = RequestMethod.GET)
	@ResponseBody
	public ResponseModel getAllArticles(@Valid Page page, BindingResult bindingResult){
		ResponseModel responseModel = new ResponseModel();

		List<ArticleDto> as = articleService.getAllArticles(Integer.valueOf(page.getPageNumber())-1,Integer.valueOf(page.getPageSize()));

		Map<String,Object> data = new HashMap<String, Object>();
		data.put("pageSize",page.getPageSize());
		data.put("pageNumber",page.getPageNumber());
		data.put("total",articleService.getTotalArticleNumber());
		data.put("articles",as);

		responseModel.setData(data);

		return responseModel;
	}


	@RequestMapping(value = "/article/group/{groupName}",method = RequestMethod.GET)
	@ResponseBody
	public ResponseModel getArticleInGroup(@PathVariable String groupName){
		ResponseModel responseModel = new ResponseModel();

		//首先在group表中找到groupId，然后到关系表中找到group下的文章
		Group group = groupService.getGroupByEn(groupName);
		if(group != null) {
			List<RArticleGroup> rs = groupService.getArticlesInGroup(group.getId());

			List<Long> ids = new ArrayList<Long>();

			for(RArticleGroup r : rs){
				ids.add(r.getArticleId());
			}

			List<Article> l1 = articleService.getArticlesByIds(ids);
			List<ArticleDto> l2 = new ArrayList<ArticleDto>();

			for(Article a : l1){
				ArticleDto t = new ArticleDto(a,null);
				t.setGroup(group.getId());
				l2.add(t);
			}

			responseModel.setData(l2);
		}else {
			responseModel.setResponseCode(ResponseCode.BAD_REQUEST.getCode());
			responseModel.setMessage(ResponseCode.BAD_REQUEST.getMessage());
		}

		return responseModel;
	}

	/**
	 * 针对某个group中的某个article进行group更换
	 * @return
	 */
	@RequestMapping(value = "/article/groupchange",method = RequestMethod.POST)
	@ResponseBody
	public ResponseModel changeArticleGroup(@Valid GroupChangePostForm groupChangePostForm){
		ResponseModel responseModel = new ResponseModel();

		int result = groupService.linkArticleAndGroup(Long.parseLong(groupChangePostForm.getArticleId()),Long.parseLong(groupChangePostForm.getGroupId()));


		if(result == -2){
			responseModel.setResponseCode(ResponseCode.NO_SUCH_GROUP.getCode());
			responseModel.setMessage(ResponseCode.NO_SUCH_GROUP.getMessage());
		}
		return responseModel;
	}

	@RequestMapping(value = "article/{articleId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseModel getArticleById(@PathVariable String articleId){
		ResponseModel responseModel = new ResponseModel();

		if(articleId.matches("^[1-9]\\d*")){
			ArticleDto dto = articleService.getArticleDto(Long.parseLong(articleId));
			if(dto == null){
				responseModel.setResponseCode(ResponseCode.FORBIDDEN.getCode());
				responseModel.setMessage(ResponseCode.FORBIDDEN.getMessage());
				return responseModel;
			}
			responseModel.setData(dto);
		}else {
			responseModel.setResponseCode(ResponseCode.PARAMETER_ERROR.getCode());
			responseModel.setMessage(ResponseCode.PARAMETER_ERROR.getMessage());
		}

		return responseModel;
	}

	@RequestMapping(value = "/article",method = RequestMethod.POST)
	@ResponseBody
	@LoginNeed
	public ResponseModel postNewArticle(@Valid ArticlePostForm articlePostForm, BindingResult bindingResult){
		ResponseModel responseModel = new ResponseModel();

		Article article = new Article();
		article.setName(articlePostForm.getName());
		article.setView(0);
		Date now = new Date();
		article.setGmtCreate(now);
		article.setGmtModified(now);
		article.setGrammar(Integer.parseInt(articlePostForm.getGrammar()));
		article.setCreator(Long.parseLong(JwtUtil.verify(httpServletRequest.getHeader("x-token")).get("u").asString()));

		ArticleContent content = new ArticleContent();
		content.setContent(articlePostForm.getContent());
		content.setLength(articlePostForm.getContent().length());
		content.setGmtCreate(now);
		content.setGmtModified(now);

		ArticleDto articleDto = articleService.postArticle(article,content,Long.parseLong(articlePostForm.getGroup()));

		if(articleDto == null){
			responseModel.setResponseCode(ResponseCode.INTERNAL_SERVER_ERROR.getCode());
			responseModel.setMessage(ResponseCode.INTERNAL_SERVER_ERROR.getMessage());
		}else {
			responseModel.setData(articleDto);
		}

		return responseModel;
	}


	/**
	 * 需要判断更新人和文章编写者是否为同一人
	 * @param articleUpdateForm
	 * @return
	 */
	@RequestMapping(value = "/article/update",method = RequestMethod.POST)
	@ResponseBody
	@LoginNeed
	public ResponseModel updateArticle(@Valid ArticleUpdateForm articleUpdateForm, BindingResult bindingResult){
		ResponseModel responseModel = new ResponseModel();

		Article article = new Article();
		article.setId(Long.parseLong(articleUpdateForm.getId()));
		article.setName(articleUpdateForm.getName());
		Date now = new Date();
		article.setGmtModified(now);
		article.setGrammar(Integer.parseInt(articleUpdateForm.getGrammar()));

		ArticleContent content = new ArticleContent();
		content.setId(article.getId());
		content.setContent(articleUpdateForm.getContent());
		content.setLength(articleUpdateForm.getContent().length());
		content.setGmtModified(now);

		Integer result = articleService.updateArticle(article,content, (Long) httpServletRequest.getSession().getAttribute(BlogConstant.ACCOUNT_ID));

		groupService.linkArticleAndGroup(article.getId(),Long.parseLong(articleUpdateForm.getGroup()));
		if(result.equals(-1)){
			responseModel.setResponseCode(ResponseCode.FORBIDDEN.getCode());
			responseModel.setMessage(ResponseCode.FORBIDDEN.getMessage());
		}

		return responseModel;
	}

	/**
	 * 需要给service传入accountId，
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/article", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseModel deleteArticle(Long id){
		ResponseModel responseModel = new ResponseModel();
		Long accountId = (Long)httpServletRequest.getSession().getAttribute("user_id");

		articleService.deleteArticle(id,accountId);

		return responseModel;
	}
}
