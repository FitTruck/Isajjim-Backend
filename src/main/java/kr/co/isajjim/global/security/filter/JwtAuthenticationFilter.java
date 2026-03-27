package kr.co.isajjim.global.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.isajjim.global.security.token.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static kr.co.isajjim.global.common.Constants.BEARER;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 요청 헤더에서 토큰 추출
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Whitelist 접근 또는 401 응답 (http.authorizeHttpRequests에서 처리)
        if (!StringUtils.hasText(header) || !header.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(BEARER.length());

        // Provider에게 토큰 유효성 검사 및 인증 객체 생성 책임을 위임
        Authentication authentication = jwtProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}