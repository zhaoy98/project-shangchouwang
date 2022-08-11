package org.fall.mvc.handler;

import crowd.entity.Auth;
import crowd.entity.Role;
import org.fall.service.api.AdminService;
import org.fall.service.api.AuthService;
import org.fall.service.api.RoleService;
import org.fall.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author zhaoy
 * @date 2022/8/8 - 21:27
 */
@Controller
public class AssignHandler {

    @Autowired
    RoleService roleService;
    @Autowired
    AdminService adminService;
    @Autowired
    AuthService authService;

    @RequestMapping("/assign/to/page.html")
    public String toAssignPage(
            @RequestParam("adminId") Integer adminId,
            ModelMap modelMap){

        // 得到对应当前adminId未被分配的角色（Role）List
        List<Role> UnAssignedRoleList = roleService.queryUnAssignedRoleList(adminId);

        // 得到对应当前adminId已被分配的角色（Role）List
        List<Role> AssignedRoleList = roleService.queryAssignedRoleList(adminId);

        // 将已选择的、未选择的放入modelMap
        modelMap.addAttribute("UnAssignedRoleList",UnAssignedRoleList);
        modelMap.addAttribute("AssignedRoleList",AssignedRoleList);

        // 请求转发到assign-role.jsp
        return "assign-role";
    }

    @RequestMapping("/assign/do/assign.html")
    public String saveAdminRoleRelationship(
            @RequestParam("adminId") Integer adminId,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("keyword") String keyword,
            // 允许roleIdList为空（因为可能已分配的被清空）
            @RequestParam(value = "roleIdList", required = false) List<Integer> roleIdList
    ){
        adminService.saveAdminRoleRelationship(adminId, roleIdList);
        //重定向（减少数据库操作）返回信息页
        return "redirect:/admin/page/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }

    @ResponseBody
    @RequestMapping("/assign/get/tree.json")
    public ResultEntity<List<Auth>> getAuthTree(){
        List<Auth> authList = authService.queryAuthList();

        return ResultEntity.successWithData(authList);
    }

    // 获得被勾选的auth信息
    @ResponseBody
    @RequestMapping("/assign/get/checked/auth/id.json")
    public ResultEntity<List<Integer>> getAuthByRoleId(Integer roleId){
        List<Integer> authIdList = authService.getAuthByRoleId(roleId);
        return ResultEntity.successWithData(authIdList);
    }

    @ResponseBody
    @RequestMapping("/assign/do/save/role/auth/relationship.json")
    public ResultEntity<String> saveRoleAuthRelationship(
            // 用一个map接收前端发来的数据
            @RequestBody Map<String,List<Integer>> map ) {
        // 保存更改后的Role与Auth关系
        authService.saveRoleAuthRelationship(map);

        return ResultEntity.successWithoutData();
    }
}
