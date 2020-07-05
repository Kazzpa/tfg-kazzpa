package es.kazzpa.selattserver.security;

import io.jsonwebtoken.*;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Bean
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 1. get the authentication header. Tokens are supposed to be passed in the authentication header
        String header = request.getHeader(SecurityConfig.TOKEN_HEADER);

        // 2. validate the header and check the prefix
        if (header == null || !header.startsWith(SecurityConfig.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);        // If not valid, go to the next filter.
            return;
        }

        try {    // exceptions might be thrown in creating the claims if for example the token is expired
            UsernamePasswordAuthenticationToken auth = getAuthentication(request);

            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            // In case of failure. Make sure it's clear; so guarantee user won't be authenticated
            SecurityContextHolder.clearContext();
        }

        // go to the next filter in the filter chain
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) throws Exception {
        String token = request.getHeader(SecurityConfig.TOKEN_HEADER);
        if (token != null && !token.trim().isEmpty() && token.startsWith(SecurityConfig.TOKEN_PREFIX)) {
            try {
                byte[] signingKey = SecurityConfig.JWT_SECRET.getBytes();

                Jws<Claims> parsedToken = Jwts.parser()
                        .setSigningKey(signingKey)
                        .parseClaimsJws(token.replace("Bearer ", ""));

                String username = parsedToken
                        .getBody()
                        .getSubject();

                List<SimpleGrantedAuthority> authorities = ((List<?>) parsedToken.getBody()
                        .get("rol")).stream()
                        .map(authority -> new SimpleGrantedAuthority((String) authority))
                        .collect(Collectors.toList());

                if (!username.trim().isEmpty()) {
                    return new UsernamePasswordAuthenticationToken(username, null, authorities);
                }
            } catch (ExpiredJwtException exception) {
                throw new Exception("Request to parse expired JWT : " + token + " failed : " + exception.getMessage());
            } catch (UnsupportedJwtException exception) {
                throw new Exception("Request to parse unsupported JWT : " + token + " failed : " + exception.getMessage());
            } catch (MalformedJwtException exception) {
                throw new Exception("Request to parse invalid JWT : " + token + " failed : " + exception.getMessage());
            } catch (SignatureException exception) {
                throw new Exception("Request to parse JWT with invalid signature : " + token + " failed : " + exception.getMessage());
            } catch (Exception exception) {
                throw new Exception("Request to parse empty or null JWT : " + token + " failed : " + exception.getMessage());
            }
        }

        return null;
    }
}
