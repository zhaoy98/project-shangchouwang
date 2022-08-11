package org.fall.mvc.handler;

import com.github.pagehelper.PageInfo;
import crowd.entity.Admin;
import org.fall.constant.CrowdConstant;
import org.fall.service.api.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * @author zhaoy
 * @date 2022/7/10 - 15:51
 */
@Controller
public class AdminHandler {

    @Autowired
    private AdminService adminService;

    /**
     * 注意：我们以前自己写的登录 handler 方法以后就不使用了。
     * 使用 SpringSecurity 之后，登录请求由 SpringSecurity 处理。
     * @param loginAcct
     * @param userPswd
     * @param httpSession
     * @return
     */
    @RequestMapping("/admin/do/login.html")  // security/do/login.html
    public String doLogin(@RequestParam("loginAcct") String loginAcct,
                          @RequestParam("userPswd") String userPswd,
                          HttpSession httpSession){
        // 调用Service方法执行登录
        // 这个方法如果能够返回admin对象，说明登陆成功
        // ，如果账号密码不正确则会抛出异常
        Admin admin = adminService.getAdminByLoginAcct(loginAcct, userPswd);
        // 将登陆成功的返回的admin对象存入Session域中
        httpSession.setAttribute(CrowdConstant.LOGIN_ADMIN_NAME, admin);
        // 为了避免跳转到后台主页面再刷新浏览器导致重复提交登录表单，重定向到目标页面。

        return "redirect:/admin/to/main/page.html";
    }


    @RequestMapping("/admin/do/logout.html")
    public String doLogout(HttpSession session){
        // 强制Session失效
        session.invalidate();
        return "redirect:/admin/to/login/page.html";
    }

    /**
     * // 注意：页面上有可能不提供关键词，要进行适配
     * @param keyword // 在@RequestParam注解中设置defaultValue属性为空字符串表示浏览器不提 供关键词时，keyword 变量赋值为空字符串
     * @param pageNum // 浏览器未提供 pageNum 时，默认前往第一页
     * @param pageSize // 浏览器未提供 pageSize 时，默认每页显示 5 条记录
     * @param modelMap
     * @return
     */
    @PreAuthorize("hasAnyRole('443开会之王', '443写代码的')")
    @RequestMapping("/admin/page/page.html")
    public String getAdminPage(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                               ModelMap modelMap){
        // 查询得到分页数据
        PageInfo<Admin> pageInfo = adminService.getAdminPage(keyword, pageNum, pageSize);
        // 将分页数据存入模型
        modelMap.addAttribute(CrowdConstant.NAME_PAGE_INFO, pageInfo);
        return "admin-page";
    }

    @PreAuthorize("hasAuthority('user:delete')")
    @RequestMapping("/admin/page/remove/{adminId}/{pageNum}/{keyword}.html")
    public String removeAdmin(
            // 从前端获取管理员的ID
            @PathVariable("adminId") Integer adminId,
            // 从前端获取当前页的页码与关键字(为了删除后跳转的页面仍然是刚才的页面，优化体验)
            @PathVariable("pageNum") Integer pageNum,
            @PathVariable("keyword") String keyword
    ){
        // 调用Service层方法，从数据库根据id删除管理员
        adminService.removeById(adminId);
        // 删除完成后，重定向（减少数据库操作）返回信息页
        return "redirect:/admin/page/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }

    @Autowired
    PasswordEncoder passwordEncoder;
    @RequestMapping("/admin/page/doSave.html")
    public String addAdmin(Admin admin){
        // 保存 Admin 时使用 SpringSecurity 加密方式
        String encodedPSWD = passwordEncoder.encode(admin.getUserPswd());
        admin.setUserPswd(encodedPSWD);
        // 调用service层存储admin对象的方法
        adminService.saveAdmin(admin);

        // 重定向会原本的页面，且为了能在添加管理员后看到管理员，设置pageNum为整型的最大值（通过修正到最后一页）
        return "redirect:/admin/page/page.html?pageNum="+Integer.MAX_VALUE;
    }

    @RequestMapping("/admin/page/update/{adminId}/{pageNum}/{keyword}.html")
    public String toUpdatePage(
            @PathVariable("adminId") Integer adminId,
            @PathVariable("pageNum") Integer pageNum,
            @PathVariable("keyword") String keyword,
            ModelMap modelMap){
        // 调用Service方法，通过id查询admin对象
        Admin admin = adminService.queryAdmin(adminId);

        // 将admin对象、页码、关键字存入modelMap，传到前端页面
        modelMap.addAttribute("admin", admin);
        modelMap.addAttribute("pageNum", pageNum);
        modelMap.addAttribute("keyword", keyword);

        // 跳转到更新页面WEB-INF/admin-update.jsp
        return "admin-update";
    }

    @RequestMapping("/admin/page/doUpdate.html")
    public String updateAdmin(Admin admin, @RequestParam("pageNum") Integer pageNum, @RequestParam("keyword") String keyword){
        adminService.updateAdmin(admin);
        // 正常执行则进入原先的管理员信息页面
        return "redirect:/admin/page/page.html?pageNum="+pageNum + "&keyword="+keyword;
    }


}
