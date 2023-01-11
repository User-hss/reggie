package com.hss.reggie.interceptor;

import com.hss.reggie.common.BaseContext;
import com.hss.reggie.common.JacksonObjectMapper;
import com.hss.reggie.common.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long loginId = (Long) request.getSession().getAttribute("employee");//员工
        if (loginId != null) {
            //将当前员工用户id存入线程
            BaseContext.setId(loginId);
            return true;
        }
        Long userId = (Long) request.getSession().getAttribute("user");
        if (userId != null) {
            BaseContext.setId(userId);
            return true;
        }
        log.warn("---拦截非法访问---");
        JacksonObjectMapper mapper = new JacksonObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(R.error("NOTLOGIN")));
        return false;
    }
}
