package es.kazzpa.selattserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import es.kazzpa.selattserver.models.JwtResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;

        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(SecurityConfig.AUTH_LOGIN_URL, HttpMethod.POST.toString()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String username = null;
        String password = null;
        //String method = request.getMethod();
        if (request.getMethod().equals(HttpMethod.POST.toString())) {
            try {
                String requestBody = IOUtils.toString(request.getReader());
                if (requestBody.trim().isEmpty()) {
                    throw new Exception("Login data is empty");
                }
                LoginRequest loginRequest = objectMapper.readValue(requestBody, LoginRequest.class);
                username = loginRequest.getUsername();
                password = loginRequest.getPassword();
                System.out.println("attempted auth:" + username+password);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password, Collections.emptyList());
        System.out.println(authenticationManager.authenticate(authenticationToken));
        System.out.println(authenticationToken.isAuthenticated());
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) throws IOException {
        User user = ((User) authentication.getPrincipal());

        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Long now = System.currentTimeMillis();

        byte[] signingKey = SecurityConfig.JWT_SECRET.getBytes();
        String token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", SecurityConfig.TOKEN_TYPE)
                .setIssuer(SecurityConfig.TOKEN_ISSUER)
                .setAudience(SecurityConfig.TOKEN_AUDIENCE)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + SecurityConfig.EXPIRATION_TIME * 1000))  // in milliseconds

                .claim("rol", roles)
                .compact();
        //response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String res = objectMapper.writeValueAsString(new JwtResponse(token));
        out.print(res);
        out.flush();
    }
}
