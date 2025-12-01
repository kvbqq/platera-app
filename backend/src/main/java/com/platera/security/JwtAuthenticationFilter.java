package com.platera.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.platera.common.TokenConstants.AUTH_HEADER_NAME;
import static com.platera.common.TokenConstants.TOKEN_PREFIX;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader(AUTH_HEADER_NAME);
        String authToken = null;
        String username = null;

        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            authToken = header.substring(TOKEN_PREFIX.length());

            try {
                username = tokenProvider.getUsernameFromToken(authToken);
            } catch (io.jsonwebtoken.ExpiredJwtException e) {
                logger.warn("Token expired", e);
            } catch (Exception e) {
                logger.warn("Cannot extract username from token", e);
            }
        } else {
            logger.debug("Authorization header not found or does not start with Bearer");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (tokenProvider.validateToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication =
                        tokenProvider.getAuthentication(authToken, userDetails);

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                logger.info("Authenticated user " + username + " - setting security context");

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
