package com.example.Jewelry.filter;


import com.example.Jewelry.Utility.JwtUtils;
import com.example.Jewelry.config.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/user/login") ||
                path.startsWith("/api/user/register") ||
                path.startsWith("/api/user/confirm") ||
                path.startsWith("/api/user/resend-confirmation") ||
                path.startsWith("/oauth2/**");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("[JwtAuthFilter] Request URI: " + request.getRequestURI());

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                username = jwtUtils.extractUsername(token);  // Có thể throw ExpiredJwtException
                System.out.println("[JwtAuthFilter] Found token for user: " + username);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtUtils.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("[JwtAuthFilter] Authenticated user: " + username);
                }
            }

            filterChain.doFilter(request, response);

        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            System.out.println("[JwtAuthFilter] Token expired: " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token expired");
            filterChain.doFilter(request, response);  // Cho phép tiếp tục
            return;
        } catch (Exception e) {
            System.out.println("[JwtAuthFilter] Token error: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token");
        }
    }

//    Phương thức debug !!!

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        System.out.println("[JwtAuthFilter] Request URI: " + request.getRequestURI());
//
//        String authHeader = request.getHeader("Authorization");
//        String token = null;
//        String username = null;
//
//        try {
//            if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                token = authHeader.substring(7);
//                username = jwtUtils.extractUsername(token);  // Có thể throw ExpiredJwtException
//                System.out.println("[JwtAuthFilter] Found token for user: " + username);
//            }
//
//            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                if (jwtUtils.validateToken(token, userDetails)) {
//                    UsernamePasswordAuthenticationToken authToken =
//                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                    System.out.println("[JwtAuthFilter] Authenticated user: " + username);
//                }
//            }
//
//        } catch (Exception e) {
//            System.out.println("[JwtAuthFilter] Token error: " + e.getMessage());
//        }
//
//        filterChain.doFilter(request, response);
//    }

}

