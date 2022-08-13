package org.fall.mvc.interceptor;

import crowd.entity.Admin;
import org.fall.constant.CrowdConstant;
import org.fall.exception.AccessForbiddenException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 创建一个拦截器，拦截除了登录页面、登录请求、登出操作的其他请求，只有能从session域中得到admin对象时，才可以放行
 * @author zhaoy
 * @date 2022/7/10 - 19:29
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 通过request对象获取Session对象
        HttpSession session = request.getSession();
        // 2. 尝试从Session域中获取Admin对象
        Admin admin = (Admin) session.getAttribute(CrowdConstant.LOGIN_ADMIN_NAME);
        // 3. 判断amin对象是否为空
        if (admin == null){
            // 4. 抛出异常
            throw new AccessForbiddenException(CrowdConstant.MESSAGE_ACCESS_FORBIDDEN);
        }
        // 5. 如果Admin对象不为null,则返回true放行
        return true;
    }
}
