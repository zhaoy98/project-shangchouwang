package org.fall.mvc.handler;

import crowd.entity.Menu;
import org.fall.service.api.MenuService;
import org.fall.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhaoy
 * @date 2022/8/8 - 16:20
 */

@RestController
public class MenuHandler {
    @Autowired
    MenuService menuService;

    @RequestMapping("/menu/do/get.json")
    public ResultEntity<Menu> getWholeTree(){
        // 通过service层方法得到全部Menu对象的List
        List<Menu> menuList = menuService.getAll();
        // 声明一个Menu对象root，用于存放找到的根节点
        Menu root = null;
        // 使用map表示每一个菜单与id的对应关系
        Map<Integer,Menu> menuMap = new HashMap<>();
        for (Menu menu:
             menuList) {
            menuMap.put(menu.getId(), menu);
        }
        // 再次遍历，查找根节点、组装父子节点
        for (Menu menu :
                menuList) {
            Integer pid = menu.getPid();
            if (pid == null){
                // 如果pid是null，判定为根节点
                // 如果是根节点，那肯定没有父节点，不必往下执行了
                root = menu;
                continue;
            }
            // 通过pid找到父节点，并为其加入子节点
            Menu father = menuMap.get(pid);
            father.getChildren().add(menu);
        }
        return ResultEntity.successWithData(root);
    }

    @RequestMapping("/menu/save.json")
    public ResultEntity<String> saveMenu(Menu menu){
        menuService.saveMenu(menu);
        return ResultEntity.successWithoutData();
    }

    @RequestMapping("/menu/edit.json")
    public ResultEntity<String> editMenu(Menu menu){
        menuService.editMenu(menu);
        return ResultEntity.successWithoutData();
    }

    @RequestMapping("/menu/remove.json")
    public ResultEntity<String> removeMenu(Integer id){
        menuService.removeMenuById(id);
        return ResultEntity.successWithoutData();
    }
}
