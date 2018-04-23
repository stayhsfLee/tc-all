package com.thenorthw.tc.web.controller.comment;

import com.thenorthw.blog.common.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by theNorthW on $date.
 * blog: thenorthw.com
 *
 * @autuor : theNorthW
 */
@Controller
@RequestMapping(value = "/web/v1")
public class CommentController {
    @Autowired
    HttpServletRequest httpServletRequest;


    @RequestMapping(value = "comment", method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel postComment(){
        ResponseModel responseModel = new ResponseModel();


        return responseModel;
    }

}
