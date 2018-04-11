package com.hongwei.demo.controller;

import com.hongwei.demo.service.IRedisRedPackageService;
import com.hongwei.demo.service.IUserRedPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private IUserRedPackageService userRedPackageService;

    @Autowired
    private IRedisRedPackageService redisRedPackageService;

    @RequestMapping(value = {"/", "/index"})
    public String index(){
        return "test";
    }

    @RequestMapping("/grab")
    @ResponseBody
    public Map<String,Object> grab(Integer pid, Integer customerId){
//        boolean result = userRedPackageService.grabRedPackage(pid, customerId);
//        boolean result = userRedPackageService.grabRedPackageOptimistic(pid, customerId);
        boolean result = redisRedPackageService.grab(pid, customerId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("success", result);
        map.put("message", result ? "抢红包成功" : "抢红包失败");
        return map;
    }
}
