package org.fall.mvc;

import com.alibaba.druid.support.json.JSONUtils;
import crowd.entity.Admin;

import crowd.entity.Boss;
import crowd.entity.Student;
import org.fall.service.api.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @author Zhaoy
 * @creat 2022-03-25-22:41
 */
@Controller
public class TestHandler {
    private Logger logger = LoggerFactory.getLogger(TestHandler.class);
    @Autowired
    AdminService adminService;

    @RequestMapping("/ssm.html")
    public String testSSM(Model model){
        List<Admin> admins = adminService.getAll();
        System.out.println("------------------------");
        System.out.println(admins);
        System.out.println("------------------------");
        model.addAttribute("admins", admins);
        return "target";
    }

    //通过@RequestParam接收数组
    @ResponseBody
    @RequestMapping("/test/one.html")
    public String testAjax01(@RequestParam("array") Object[] objects){
        for (Object o :
                objects) {
            logger.info(o.toString());
        }
        return Arrays.toString(objects);
    }

    //通过@RequestBody接收数组
    @ResponseBody
    @RequestMapping(value = "/test/two.html", method = RequestMethod.POST)
    public String testAjax02(@RequestBody List<Integer> integers, HttpServletRequest request){
        System.out.println(integers.toString());

        // 判断是否是ajax请求
//        boolean isJson = CrowdUtil.judgeRequestType(request);
//        System.out.println(isJson);

        return integers.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/send/compose/object.html")
    public String testAjax03(@RequestBody Student str){
        System.out.println(str);
        return str.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/send/compose/object.json", method = RequestMethod.POST)
    public String testAjax04(@RequestBody Boss str){
        System.out.println(str);
        return str.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/exception_common.html")
    public String testException_common(){
        /**
         * 1. 基于xml的异常映射,解决不了ajax异常映射
         * 2. 基于注解的异常映射
         */
        System.out.println(10 / 0);
//        String a = null;
//        System.out.println(a.length());
        return "target";
    }

    @ResponseBody
    @RequestMapping("/exception_ajax.html")
    public String testException_ajax(@RequestBody Integer[] integers){
        System.out.println(Arrays.toString(integers));
//        System.out.println(10 / 0);
        String a = null;
        System.out.println(a.charAt(2));
        return Arrays.toString(integers);
    }

}
