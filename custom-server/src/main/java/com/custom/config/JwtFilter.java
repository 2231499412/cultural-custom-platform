package com.custom.config;

import com.custom.common.Result;
import com.custom.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        // OPTIONS 预检请求直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 公开接口直接放行
        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 提取 Authorization 头
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                if (!jwtUtil.isTokenExpired(token)) {
                    request.setAttribute("userId", jwtUtil.getUserId(token));
                    request.setAttribute("username", jwtUtil.getUsername(token));
                    request.setAttribute("role", jwtUtil.getRole(token));
                    filterChain.doFilter(request, response);
                    return;
                }
            } catch (Exception ignored) {
            }
        }

        // 未认证，返回 401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(Result.error(401, "未登录或登录已过期")));
    }

    private boolean isPublicPath(String path) {
        if (path.startsWith("/auth/")) return true;
        if (path.startsWith("/template/")) return true;
        if (path.startsWith("/material/list")) return true;
        if (path.startsWith("/file/upload")) return true;
        if (path.startsWith("/swagger") || path.startsWith("/v3/api-docs")) return true;
        return false;
    }
}
