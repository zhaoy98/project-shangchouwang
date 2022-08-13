package org.fall.handler;

import org.fall.api.MySQLRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PortalHandler {

    @Autowired
    MySQLRemoteService mySQLRemoteService;

    // 首页
    @RequestMapping("/")
    public String showPortalPage(ModelMap modelMap){

        return "portal";
    }

}
