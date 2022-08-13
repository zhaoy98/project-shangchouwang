package org.fall.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import crowd.entity.Admin;
import crowd.entity.AdminExample;
import org.apache.ibatis.annotations.Param;
import org.fall.constant.CrowdConstant;
import org.fall.exception.LoginAcctAlreadyInUseException;
import org.fall.exception.LoginAcctAlreadyInUseForUpdateException;
import org.fall.exception.LoginFailedException;
import org.fall.mapper.AdminMapper;
import org.fall.service.api.AdminService;
import org.fall.util.CrowdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Zhaoy
 * @creat 2022-03-25-21:52
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public int addAdmin(Admin admin) {
        return adminMapper.insert(admin);
    }

    @Override
    public List<Admin> getAll() {
        return adminMapper.selectByExample(new AdminExample());
    }

    /**
     * 注意：我们以前自己写的登录 handler 方法以后就不使用了。
     * 使用 SpringSecurity 之后，登录请求由 SpringSecurity 处理。
     * @param loginAcct
     * @param userPswd
     * @return
     */
    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {
        // 1. 根据登录账号查询Admin对象
        // 1.1 创建AdminExample对象
        AdminExample adminExample = new AdminExample();
        // 1.2 创建Criteria对象
        AdminExample.Criteria criteria = adminExample.createCriteria();
        // 1.3 在Criteria对象中封装查询条件
        criteria.andLoginAcctEqualTo(loginAcct);
        // 1.4 调用AdminMapper的方法执行查询
        List<Admin> list = adminMapper.selectByExample(adminExample);

        // 2. 判断Admin的对象是否为Null
        if (list == null || list.size() == 0){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        if (list.size() > 1) {
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }

        Admin admin = list.get(0);

        // 3. 如果Admin的对象为null则抛出异常
        if (admin == null){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        // 4. 如果Admin对象部位null则将数据库密码从Admin对象中取出来
        String userPswdDB = admin.getUserPswd();

        // 5. 将表单提交的明文密码进行加密
        String userPswdForm = CrowdUtil.md5(userPswd);

        // 6. 对密码进行比较
        if (!Objects.equals(userPswdDB, userPswdForm)){
            // 7. 密码不一样则抛出异常
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        // 8. 如果一致返回Admin对象
        return admin;
    }

    @Override
    public PageInfo<Admin> getAdminPage(String keyword, Integer pageNum, Integer pageSize) {
        // 1. 开启分页功能
        PageHelper.startPage(pageNum, pageSize);
        // 2. 查询Admin数据
        List<Admin> adminList = adminMapper.selectAdminListByKeyword(keyword);
        // 辅助代码，打印adminList的全类名
        Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
        logger.debug("adminList的全类名是："+adminList.getClass().getName());
        // 3. 为了方便页面使用将adminList封装为PageInfo
        PageInfo<Admin> pageInfo = new PageInfo<>(adminList);
        return pageInfo;
    }

    @Override
    public Admin queryAdminByLoginAcct(String loginAcct) {
        Admin admin = adminMapper.selectByLoginAcct(loginAcct);
        return admin;
    }

    @Override
    public void saveAdmin(Admin admin) {
        // 生成当前的系统的时间
        Date date = new Date();
        // 格式化时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String creatTime= simpleDateFormat.format(date);
        // 设置管理员创建时间
        admin.setCreateTime(creatTime);

        /*
        注意：我们以前自己写的登录 handler 方法以后就不使用了。
        使用 SpringSecurity 之后，登录请求由 SpringSecurity 处理。
        因此不需要自己加密的这一步了
        // 得到前端输入的密码，加密后放回原本的admin对象
        String userPswd = admin.getUserPswd();
        String md5pswd = CrowdUtil.md5(userPswd);
        admin.setUserPswd(md5pswd);
         */

        // 执行插入操作
        try {
            adminMapper.insert(admin);
        } catch (Exception e) {
            e.printStackTrace();
            // 这里出现异常的话一般就是DuplicateKeyException（因为插入的loginAcct已存在而触发）
            if (e instanceof DuplicateKeyException){
                // 如果确实是DuplicateKeyException，此时抛出一个自定义的异常
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
            }
        }

    }

    @Override
    public Admin queryAdmin(Integer id) {
        Admin admin = adminMapper.selectByPrimaryKey(id);
        return admin;
    }

    @Override
    public Admin getAdminByUsername(String adminLoginAcct, String adminPassword) {
        return null;
    }

    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public void removeById(Integer adminId) {
        adminMapper.deleteByPrimaryKey(adminId);
    }

    @Override
    public void updateAdmin(Admin admin) {
        try {
            adminMapper.updateByPrimaryKeySelective(admin);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInUseForUpdateException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
            }
        }
    }

    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleList) {
        // 先清除旧的对应inner_admin_role表中对应admin_id的数据
        adminMapper.clearOldRelationship(adminId);
        // 如果roleIdList非空，则将该list保存到数据库表中，且admin_id=adminId
        if (roleList != null && roleList.size()>0){
            adminMapper.saveAdminRoleRelationship(adminId,roleList);
        }
        // roleIdList为空，则清空后不做操作
    }
}
