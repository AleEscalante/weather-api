package org.consiti.prueba.weather.filter;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

@WebFilter(urlPatterns = {"/*"}, dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD})
@Slf4j
@Component
public class RequestThrottleFilter implements Filter {

    private int MAX_REQUESTS = 3;

    private int DURATION = 1;

    private LoadingCache<String, Integer> requestCountsPerEmail;

    public RequestThrottleFilter() {
        super();
        log.info("MAX REQUESTS" + MAX_REQUESTS + ", DURATION " + DURATION);
        requestCountsPerEmail = Caffeine.newBuilder().
                expireAfterWrite(DURATION, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String requestURI = httpServletRequest.getRequestURI();
        if (requestURI.startsWith("/swagger") ||
                requestURI.startsWith("/v2/api-docs") ||
                requestURI.startsWith("/swagger-resources") ||
                requestURI.startsWith("/configuration/ui") ||
                requestURI.startsWith("/configuration/security") ||
                requestURI.startsWith("/swagger-ui.html") ||
                requestURI.startsWith("/webjars") ||
                requestURI.startsWith("/v3/api-docs") ||
                requestURI.startsWith("/swagger-ui") ||
                requestURI.startsWith("/auth/sign-up")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String email = getEmail(httpServletRequest);
        try {
            if (isMaximumRequestsPerMinuteExceeded(email)) {
                log.warn("LIMITE SOBREPASADO");
                httpServletResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                httpServletResponse.getWriter().write("TOO MANY REQUESTS");
                return;
            }
        } catch (NullPointerException e) {
            filterChain.doFilter(servletRequest, servletResponse);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isMaximumRequestsPerMinuteExceeded(String email) {
        Integer requests = requestCountsPerEmail.get(email);

        if (requests == null) {
            requests = 0;
        }

        requests++;
        log.info("REQUESTS: " + requests);
        if (requests > MAX_REQUESTS) {
            return true;
        } else {
            requestCountsPerEmail.put(email, requests);
            return false;
        }
    }

    public String getEmail(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String email = null;
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.replace("Bearer ", "");
            JWT jwt = null;
            JWTClaimsSet claims = null;
            try {
                jwt = JWTParser.parse(token);
                claims = jwt.getJWTClaimsSet();
                email = claims.getSubject();
            } catch (ParseException ignored) {
            }
        }
        return email;
    }

    @Override
    public void destroy() {

    }
}
