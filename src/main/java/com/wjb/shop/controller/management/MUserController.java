package com.wjb.shop.controller.management;

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
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/management/user")
public class MUserController {

    @Resource
    UserService userService;

    @GetMapping("/userlogin.html")
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView("/management/userlogin");
        return mav;
    }

    @PostMapping("/check_login")
    @ResponseBody
    public Map checkLogin(String username, String password, HttpSession session){
        Map result = new HashMap();
        try {
            User user = userService.checkLogin(username, password);
            session.setAttribute("loginUser", user);
            result.put("code", "0");
            result.put("msg", "success");
            result.put("redirect_url","/management/index.html");
        } catch (BussinessException e) {
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
        }
        return result;
    }

    @GetMapping("/logout")
    @ResponseBody
    public ModelAndView logout(HttpSession session){
        session.invalidate();
        return new ModelAndView("/management/userlogin");
    }
}
