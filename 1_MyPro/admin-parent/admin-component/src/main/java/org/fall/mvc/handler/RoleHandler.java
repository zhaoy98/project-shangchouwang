package org.fall.mvc.handler;

import com.github.pagehelper.PageInfo;
import crowd.entity.Role;
import org.fall.service.api.RoleService;
import org.fall.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhaoy
 * @date 2022/8/8 - 20:44
 */

@RestController
public class RoleHandler {
    @Autowired
    RoleService roleService;

    @RequestMapping("/role/page/page.json")
    public ResultEntity<PageInfo<Role>> getPageInfo(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "keyword", defaultValue = "") String keyword ) {
        // 从Service层得到pageInfo
        PageInfo<Role> pageInfo = roleService.getPageInfo(pageNum, pageSize, keyword);

        // 返回ResultEntity，Data就是得到的pageInfo
        return ResultEntity.successWithData(pageInfo);
    }

    @RequestMapping("/role/do/save.json")
    public ResultEntity<String> saveRole(Role role){
        roleService.saveRole(role);
        return ResultEntity.successWithoutData();
    }

    @RequestMapping("/role/do/update.json")
    public ResultEntity<String> updateRole(Role role){
        roleService.updateRole(role);
        return ResultEntity.successWithoutData();
    }

    @RequestMapping("/role/do/remove.json")
    public ResultEntity<String> removeRole(@RequestBody List<Integer> roleIdArray){

        // service层方法通过id的List删除角色
        roleService.removeById(roleIdArray);

        return ResultEntity.successWithoutData();

    }
}
