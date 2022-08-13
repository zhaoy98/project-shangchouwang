package org.fall.mvc.config;

import com.google.gson.Gson;
import org.fall.constant.CrowdConstant;
import org.fall.exception.AccessForbiddenException;
import org.fall.exception.LoginAcctAlreadyInUseException;
import org.fall.exception.LoginAcctAlreadyInUseForUpdateException;
import org.fall.exception.LoginFailedException;
import org.fall.util.CrowdUtil;
import org.fall.util.ResultEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/** 基于注解的异常处理类
 * @author Zhaoy
 * @creat 2022-03-29-16:30
 */

@ControllerAdvice  // 表示当前类时一个处理异常的类
public class CrowdExceptionResolver {

    // 更新时，不应将账号改为与其他账号同名
    @ExceptionHandler(value = LoginAcctAlreadyInUseForUpdateException.class)
    public ModelAndView resolverLoginAcctAlreadyInUseForUpdateException(
            LoginAcctAlreadyInUseForUpdateException exception, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        // 此时进入的是system-error.jsp的页面
        String viewName = "system-error";
        return commonResolveException(exception,request,response,viewName);
    }

    @ExceptionHandler(value = LoginAcctAlreadyInUseException.class)
    public ModelAndView resolverLoginAcctAlreadyInUseException(
            LoginAcctAlreadyInUseException exception, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String viewName = "admin-add";
        return commonResolveException(exception,request,response,viewName);
    }

    @ExceptionHandler(value = AccessForbiddenException.class)
    public ModelAndView resolveAccessForbiddenException(AccessForbiddenException exception,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) throws IOException {
        String viewName = "admin-login";
        return commonResolveException(exception, request, response, viewName);
    }

    @ExceptionHandler(value = LoginFailedException.class)
    public ModelAndView resolveLoginFailedException(LoginFailedException exception,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) throws IOException {
        String viewName = "admin-login";
        return commonResolveException(exception, request, response, viewName);
    }

    /**
     * ----此处以解决空指针异常为例-----
     // ExceptionHandler将一个具体的异常类型和一个方法关联起来
     @ExceptionHandler(value = NullPointerException.class)
     public ModelAndView resolveLoginFailedException(
     NullPointerException exception, // 实际捕获到的异常
     HttpServletRequest request, // 当前请求对象
     HttpServletResponse response
     ) throws IOException {

     // 1. 判断当前前请求是否为ajax请求
     boolean judgeResult = CrowdUtil.judgeRequestType(request);
     // 2. 如果时ajax请求
     if (judgeResult){
     // 3. 从当前异常对象中获取异常信息
     String message = exception.getMessage();
     // 4. 创建ResultEntity
     ResultEntity<Object> resultEntity = ResultEntity.failed(message);
     // 5. 创建gson对象
     Gson gson = new Gson();
     // 6. 讲resultEntity转换为JSON字符串
     String json = gson.toJson(resultEntity);
     // 7. 把当前的JSON字符串作为当前请求的响应体数据返回给浏览器
     // 7.1 --回去Writer对象
     PrintWriter writer = response.getWriter();
     // 7.2 --写入数据
     writer.write(json);
     // 8. 返回null，不给SpringMVC提供ModelAndView对象
     // 这样子SpringMVC就知道不需要框架解析视图来提供响应，而是程序员自己提供了响应
     return null;
     }
     // 9. 如果不是ajax请求，则创建ModelAndView对象
     ModelAndView modelAndView = new ModelAndView();
     // 10. 讲Exception对象存入模型
     modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);
     // 11. 设置目标视图名称
     modelAndView.setViewName("system-error");

     // 12. 返回ModelAndView对象
     return modelAndView;
     }
     */

    // @ExceptionHandler 将一个具体的异常类型和方法关联起来
    @ExceptionHandler(value = {ArithmeticException.class, NullPointerException.class})
    public ModelAndView resolveArithmeticException(Exception exception,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) throws IOException {
        return commonResolveException(exception,request,response,"system-error");
    }

    private ModelAndView commonResolveException(
            Exception exception,
            HttpServletRequest request,
            HttpServletResponse response,
            String viewName ) throws IOException {
        // 1.判断当前请求是“普通请求”还是“Ajax 请求”
        boolean judgeResult = CrowdUtil.judgeRequestType(request);
        // 2.如果是 Ajax 请求
         if(judgeResult) {
             // 3.从当前异常对象中获取异常信息
             String message = exception.getMessage();
              // 4.创建 ResultEntity
             ResultEntity<Object> resultEntity = ResultEntity.failed(message);
             // 5.创建 Gson 对象
             Gson gson = new Gson();
             // 6.将 resultEntity 转化为 JSON 字符串
              String json = gson.toJson(resultEntity);
             // 7.把当前 JSON 字符串作为当前请求的响应体数据返回给 浏览器
             // ①获取 Writer 对象
              PrintWriter writer = response.getWriter();
             // ②写入数据
              writer.write(json);
             // 8.返回 null，不给 SpringMVC 提供 ModelAndView 对象
             // 这样 SpringMVC 就知道不需要框架解析视图来提供响应， 而是程序员自己提供了响应
              return null;
         }
         // 9.创建 ModelAndView 对象
        ModelAndView modelAndView = new ModelAndView();
         // 10.将 Exception 对象存入模型
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);
        // 11.设置目标视图名称
        modelAndView.setViewName(viewName);
        // 12.返回 ModelAndView 对象
        return modelAndView;
    }
}
