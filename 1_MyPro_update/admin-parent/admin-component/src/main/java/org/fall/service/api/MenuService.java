package org.fall.service.api;

import crowd.entity.Menu;
import crowd.entity.MenuExample;
import org.fall.mapper.MenuMapper;

import java.util.List;

/**
 * @author zhaoy
 * @date 2022/8/8 - 16:19
 */
public interface MenuService{
    public List<Menu> getAll();

    public void saveMenu(Menu menu);

    void editMenu(Menu menu);

    void removeMenuById(Integer id);
}
