package com.electricitysystem.jwt;


import com.electricitysystem.repository.AccountRepository;
import com.electricitysystem.service.impl.AccountDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired(required = false)
    private JwtUtility jwtUtility;
    @Autowired
    private AccountDetailServiceImpl accountDetailService;
    @Autowired(required = false)
    private AccountRepository accountRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtility.validateJwtToken(jwt)) {
                String username = jwtUtility.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = accountDetailService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    public boolean checkUsernameFromToken(String user, String token) {
        String a="";
        if (token.startsWith("Bearer ")) {
            a=token.substring(7, token.length());
            if (a != null) {
                String us = jwtUtility.getUserNameFromJwtToken(a);
                if (accountRepository.getAccountEntityByUsername(us).getRole()==0) {
                    return true;
                }
                else
                    return us.equals(user);
            }
        }
        return false;
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }

        return null;
    }
}