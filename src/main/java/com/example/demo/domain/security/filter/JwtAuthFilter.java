package com.example.demo.domain.security.filter;

import com.example.demo.domain.security.auth.UserInfoUserDetailsService;
import com.example.demo.domain.security.jwt.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserInfoUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException {
        String authHeader =request.getHeader("Authorization");

        log.info("Header "+authHeader);

        String token = null;
        String username = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);

        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("SecurityContext "+SecurityContextHolder.getContext().getAuthentication());
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            log.info(userDetails.toString());
            if (jwtService.validateToken(token, userDetails)) {
                log.info(jwtService.validateToken(token, userDetails).toString());
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                log.info("authToken "+authToken);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info(userDetails.toString());
            }
        }

        filterChain.doFilter(request, response);
    }
    //to insert initial user
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/v1/users") && path.startsWith("/h2-console")|| path.equals("/healthcheck");
    }
}
