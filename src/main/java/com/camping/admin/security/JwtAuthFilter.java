package com.camping.admin.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        if (isExcluded(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        } else {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("AUTH_TOKEN".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }

        if (token == null) {
            if (wantsHtml(request)) {
                response.sendRedirect("/login");
            } else {
                unauthorized(response, "Missing or invalid token");
            }
            return;
        }
        if (!jwtService.isTokenValid(token)) {
            if (wantsHtml(request)) {
                response.sendRedirect("/login");
            } else {
                unauthorized(response, "Invalid or expired token");
            }
            return;
        }

        // 현재 로그인 사용자명을 요청 속성으로 전달하여 뷰에서 사용 가능하도록 한다
        request.setAttribute("currentUsername", jwtService.getUsername(token));

        filterChain.doFilter(request, response);
    }

    private boolean isExcluded(String path) {
        return pathMatcher.match("/auth/login", path) ||
               pathMatcher.match("/login", path) ||
               pathMatcher.match("/css/**", path) ||
               pathMatcher.match("/js/**", path) ||
               pathMatcher.match("/images/**", path) ||
               pathMatcher.match("/webjars/**", path) ||
               pathMatcher.match("/favicon.ico", path) ||
               pathMatcher.match("/h2-console/**", path) ||
               path.equals("/");
    }

    private boolean wantsHtml(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        return accept != null && accept.contains("text/html");
    }

    private void unauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String body = "{\"error\":\"" + message + "\"}";
        response.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
    }
}


