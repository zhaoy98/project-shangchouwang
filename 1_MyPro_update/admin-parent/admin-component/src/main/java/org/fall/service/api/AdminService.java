package org.fall.service.api;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import crowd.entity.Admin;

import java.util.List;

/**
 * @author Zhaoy
 * @creat 2022-03-25-21:52
 */

public interface AdminService {

    int addAdmin(Admin admin);

    List<Admin> getAll();

    void saveAdmin(Admin admin);

    Admin queryAdmin(Integer id);

    Admin getAdminByUsername(String adminLoginAcct, String adminPassword);

    PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize);

    void removeById(Integer adminId);

    void updateAdmin(Admin admin);

    void saveAdminRoleRelationship(Integer adminId, List<Integer> roleList);

    Admin getAdminByLoginAcct(String loginAcct, String userPswd);

    PageInfo<Admin> getAdminPage(String keyword, Integer pageNum, Integer pageSize);

    Admin queryAdminByLoginAcct(String loginAcct);
}
