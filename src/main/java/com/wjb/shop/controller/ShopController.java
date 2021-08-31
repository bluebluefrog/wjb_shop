package com.wjb.shop.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wjb.shop.entity.*;
import com.wjb.shop.mapper.CategoryMapper;
import com.wjb.shop.service.CategoryService;
import com.wjb.shop.service.EvaluationService;
import com.wjb.shop.service.MemberService;
import com.wjb.shop.service.ShopService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ShopController {

    @Resource
    private CategoryService categoryService;

    @Resource
    private ShopService shopService;

    @Resource
    private EvaluationService evaluationService;

    @Resource
    private MemberService memberService;

    @GetMapping("/")
    public ModelAndView showIndex() {
        ModelAndView mav = new ModelAndView("/index");
        List<Category> list = categoryService.selectAll();
        mav.addObject("categoryList", list);
        return mav;
    }

    @GetMapping("/shops")
    @ResponseBody
    public IPage<Shop> selectShop(Long categoryId, String order, Integer p) {
        if (p == null) {
            p = 1;
        }
        IPage<Shop> pageObject = shopService.paging(categoryId, order, p, 10);
        return pageObject;
    }

    @GetMapping("/shop/{id}")
    public ModelAndView showDetail(@PathVariable("id") Long id, HttpSession session) {
        Shop shop = shopService.selectById(id);
        List<Evaluation> evaluations = evaluationService.selectByShopId(id);
        Member member = (Member)session.getAttribute("loginMember");
        ModelAndView mav = new ModelAndView("/detail");
        if (member != null) {
            MemberReadState memberReadState = memberService.selectMemberReadState(member.getMemberId(), id);
            mav.addObject("memberReadState", memberReadState);
        }
        mav.addObject("shop", shop);
        mav.addObject("evaluationList", evaluations);
        return mav;
    }
}
