package org.fall.mvc.config;

import org.fall.constant.CrowdConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration// 表示当前类为配置类
@EnableWebSecurity// 启用Web安全
@EnableGlobalMethodSecurity(prePostEnabled = true)// 开启基于方法的安全认证机制，也就是说在controller层启用注解机制的安全确认
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {

        // 1. 开启在内存中进行身份验证（开发时测试的时候暂用）
//        builder
//                .inMemoryAuthentication()        // 开启在内存中进行身份验证（开发时暂用）
//                .withUser("zy0710")        // 设置用户名
//                .password("123456")             // 设置密码
//                .roles("ADMIN");                 // 设置权限
//
       // 2. 使用userDetailsService，即配置的数据库验证登录
        builder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);      // 使用BCryptPasswordEncoder进行带盐值的密码加密
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        String[] permitUrls = {"/index.jsp","/bootstrap/**",
                "/crowd/**","/css/**","/fonts/**","/img/**",
                "/jquery/**","/layer/**","/script/**","/ztree/**",
                "/admin/to/login/page.html"};
        security
                .authorizeRequests()        // 表示对请求进行授权
                .antMatchers(permitUrls)    // 传入的ant风格的url
                .permitAll()                // 允许上面的所有请求，不需要认证

                .anyRequest()               // 其他未设置的全部请求
                .authenticated()            // 表示需要认证

                .and()
                .csrf()         // 设置csrf
                .disable()      // 关闭csrf

                .formLogin()                                    // 开启表单登录功能
                .loginPage("/admin/to/login/page.html")            // 指定登陆页面
                .usernameParameter("loginAcct")                // 设置表单中对应用户名的标签的name属性名
                .passwordParameter("userPswd")                 // 设置表单中对应密码的标签的name属性名
                .loginProcessingUrl("/security/do/login.html")  // 设置登录请求的提交地址
                .defaultSuccessUrl("/admin/to/main/page.html")     // 设置登陆成功后前往的地址
                .and()
                .logout()                                       // 开启退出登录功能
                .logoutUrl("/security/do/logout.html")          // 设置退出登录的url
                .logoutSuccessUrl("/admin/to/login/page.html")    // 设置退出成功后前往的页面

                .and()
                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                        httpServletRequest.setAttribute("exception", new Exception("抱歉，您没有权限访问该资源！"));
                        httpServletRequest.getRequestDispatcher("/WEB-INF/system-error.jsp").forward(httpServletRequest,httpServletResponse);
                    }
                })
        ;

    }
}
