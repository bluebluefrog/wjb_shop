package com.wjb.shop.controller;

import com.wjb.shop.entity.Evaluation;
import com.wjb.shop.entity.Member;
import com.wjb.shop.exception.BussinessException;
import com.wjb.shop.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MemberController {

    @Resource
    private MemberService memberService;

    @GetMapping("/register.html")
    public ModelAndView showRegister() {
        return new ModelAndView("/register");
    }

    @GetMapping("/login.html")
    public ModelAndView showLogin() {
        return new ModelAndView("/login");
    }

    @PostMapping("/registe")
    @ResponseBody
    public Map register(String vc, String username, String password, String nickname, HttpServletRequest request){
        //获取正确验证码
        String verifyCode = (String)request.getSession().getAttribute("kaptchaVerifyCode");
        //验证码进行比对
        Map result = new HashMap();
        if (vc == null || verifyCode == null || !vc.equals(verifyCode)) {
            result.put("code", "VC01");
            result.put("msg", "验证码错误");
        }else{
            try{
                memberService.createMember(username, password, nickname);
                result.put("code", "0");
                result.put("msg", "success");
            } catch(BussinessException e){
                result.put("code", e.getCode());
                result.put("msg", e.getMsg());
            }
        }
        return result;
    }

    @PostMapping("/check_login")
    @ResponseBody
    public Map memberLogin(String vc, String username, String password, String nickname, HttpSession session){
        //获取正确验证码
        String verifyCode = (String)session.getAttribute("kaptchaVerifyCode");
        //验证码进行比对
        Map result = new HashMap();
        if (vc == null || verifyCode == null || !vc.equals(verifyCode)) {
            result.put("code", "VC01");
            result.put("msg", "验证码错误");
        }else{
            try {
                Member member = memberService.memberLogin(username, password);
                session.setAttribute("loginMember", member);
                result.put("code", "0");
                result.put("msg", "success");
            } catch (BussinessException e) {
                result.put("code", e.getCode());
                result.put("msg", e.getMsg());
            }
        }
        return result;
    }

    @PostMapping("/update_read_state")
    @ResponseBody
    public Map updateReadState(Long memberId, Long shopId, Integer readState) {
        Map result = new HashMap();
        try {
            memberService.updateMemberReadState(memberId, shopId, readState);
            result.put("code", "0");
            result.put("msg", "success");
        } catch (BussinessException e) {
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
        }
        return result;
    }
    @PostMapping("/evaluate")
    @ResponseBody
    public Map evaluate(Long memberId, Long shopId, Integer score, String content) {
        Map result = new HashMap();
        try {
            memberService.evaluate(memberId, shopId,score,content);
            result.put("code", "0");
            result.put("msg", "success");
        } catch (BussinessException e) {
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
        }
        return result;
    }

    @PostMapping("/enjoy")
    @ResponseBody
    public Map enjoy(Long evaluationId) {
        Map result = new HashMap();
        try {
            Evaluation enjoy = memberService.enjoy(evaluationId);
            result.put("code", "0");
            result.put("msg", "success");
            result.put("evaluation", enjoy);
        } catch (BussinessException e) {
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
        }
        return result;
    }
}
