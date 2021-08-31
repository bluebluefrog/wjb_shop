package com.wjb.shop.controller.management;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wjb.shop.entity.Shop;
import com.wjb.shop.exception.BussinessException;
import com.wjb.shop.service.ShopService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/management/shop")
public class MShopController {
    @Resource
    private ShopService shopService;

    @GetMapping("/index.html")
    public ModelAndView showShop() {
        return new ModelAndView("/management/shop");
    }

    @PostMapping("/upload")
    @ResponseBody
    public Map upload(@RequestParam("img") MultipartFile file, HttpServletRequest request) throws IOException {

        String uploadFilePath = request.getServletContext().getResource("/").getPath() + "/upload/";
        String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        file.transferTo(new File(uploadFilePath + fileName + suffix));
        Map result = new HashMap();
        result.put("errno", 0);
        result.put("data", new String[]{"/upload/" + fileName + suffix});
        return result;
    }

    @PostMapping("/create")
    @ResponseBody
    public Map createShop(Shop shop) {
        Map result = new HashMap();
        try {
            shop.setEvaluationQuantity(0);
            shop.setEvaluationScore(0f);
            Document doc = Jsoup.parse(shop.getDescription());
            Element img = doc.select("img").first();
            String cover = img.attr("src");
            shop.setCover(cover);
            shopService.createShop(shop);
            result.put("code", "0");
            result.put("msg", "success");
        } catch (BussinessException e) {
            e.printStackTrace();
            result.put("code", e.getCode());
            result.put("msg", e.getMessage());
        }
        return result;
    }

    @GetMapping("/list")
    @ResponseBody
    public Map shopList(Integer page, Integer limit) {
        if (page == null) {
            page = 1;
        }
        if (limit == 0) {
            limit = 10;
        }
        IPage<Shop> pageObject = shopService.paging(null, null, page, limit);
        Map result = new HashMap();
        result.put("code", "0");
        result.put("msg", "success");
        result.put("data", pageObject.getRecords());//当前页面记录数
        result.put("count", pageObject.getTotal());//未分页总记录数
        return result;
    }

    @GetMapping("/id/{id}")
    @ResponseBody
    public Map selectById(@PathVariable("id") Long shopId) {
        Shop shop = shopService.selectById(shopId);
        Map result = new HashMap();
        result.put("code", "0");
        result.put("msg", "success");
        result.put("data", shop);
        return result;
    }

    @PostMapping("/update")
    @ResponseBody
    public Map update(Shop shop) {
        Map result = new HashMap();
        try {
            Shop rawShop = shopService.selectById(shop.getShopId());
            rawShop.setShopName(shop.getShopName());
            rawShop.setSubTitle(shop.getSubTitle());
            rawShop.setOwner(shop.getOwner());
            rawShop.setCategoryId(shop.getCategoryId());
            rawShop.setDescription(shop.getDescription());
            Document doc = Jsoup.parse(shop.getDescription());
            String cover = doc.select("img").first().attr("src");
            rawShop.setCover(cover);
            shopService.updateShop(rawShop);
            result.put("code", "0");
            result.put("msg", "success");
        } catch (BussinessException e) {
            e.printStackTrace();
            result.put("code", e.getCode());
            result.put("msg", e.getMessage());
        }
        return result;
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    public Map deleteShop(@PathVariable("id") Long shopId) {
        Map result = new HashMap();
        try {
            shopService.deleteShop(shopId);
            result.put("code", "0");
            result.put("msg", "success");
        } catch (BussinessException e) {
            e.printStackTrace();
            result.put("code", e.getCode());
            result.put("msg", e.getMessage());
        }
        return result;
    }
}
