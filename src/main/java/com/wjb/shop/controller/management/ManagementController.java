package com.wjb.shop.controller.management;

import com.wjb.shop.entity.Member;
import com.wjb.shop.entity.User;
import com.wjb.shop.exception.BussinessException;
import com.wjb.shop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/management")
public class ManagementController {

    @GetMapping("/index.html")
    public ModelAndView showIndex() {
        ModelAndView mav = new ModelAndView("/management/index");
        return mav;
    }
}
