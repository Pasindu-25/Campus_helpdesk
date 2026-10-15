package com.campus.helpdesk.config;

import com.campus.helpdesk.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class FirstLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");

        if (loggedUser != null && loggedUser.isFirstLogin()) {
            String uri = request.getRequestURI();

            if (!uri.startsWith("/user/force-password-change") &&
                    !uri.startsWith("/user/update-first-password") &&
                    !uri.startsWith("/logout")) {

                response.sendRedirect("/user/force-password-change");
                return false;
            }
        }
        return true;
    }
}