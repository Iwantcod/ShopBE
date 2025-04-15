package com.example.shopPJT.config;

import com.example.shopPJT.filter.JwtFilter;
import com.example.shopPJT.filter.LoginFilter;
import com.example.shopPJT.oauth2.service.CustomOAuth2UserService;
import com.example.shopPJT.user.service.UserService;
import com.example.shopPJT.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
public class SecurityConfig {
    @Value("${app.client-url}")
    private String clientUrl;
    private final CustomOAuth2UserService customOAuth2UserService;
    @Autowired
    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public LoginFilter loginFilter(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil) {
        LoginFilter loginFilter = new LoginFilter(authenticationManager, jwtUtil, userService);
        loginFilter.setFilterProcessesUrl("/api/auth/login");
        loginFilter.setAuthenticationManager(authenticationManager); // loginFilter의 상위 부모 필터의 필드를 초기화하는 상위 클래스의 메소드
        return loginFilter;
    }

    @Bean
    public JwtFilter jwtFilter(JwtUtil jwtUtil) {
        return new JwtFilter(jwtUtil);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, LoginFilter loginFilter, JwtFilter jwtFilter, AuthenticationSuccessHandler authenticationSuccessHandler, AuthenticationFailureHandler authenticationFailureHandler) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors((corsCustom) -> corsCustom.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        // 허용할 Origin 설정
                        // Collections.singletonList() == 단 하나의 요소만 가지는 불변(final) 리스트 생성
                        config.setAllowedOrigins(Collections.singletonList(clientUrl));

                        // 허용할 HTTP 메서드 설정 ("*": 모든 HTTP 메소드)
                        config.setAllowedMethods(Collections.singletonList("*"));

                        // 요청에 자격 증명(Credentials, 예: 쿠키, HTTP 인증 등)을 포함하도록 허용
                        // 이 설정이 true일 경우, `Access-Control-Allow-Credentials` 헤더가 true
                        config.setAllowCredentials(true);

                        // 허용할 요청 헤더 설정
                        config.setAllowedHeaders(Collections.singletonList("*"));

                        // CORS 응답 캐시 시간(초)을 설정 클라이언트가 얼마나 오랫동안 이 CORS 정책을 캐시할지를 설정
                        // 3600초(1시간)
                        config.setMaxAge(3600L);

                        // CORS 응답에서 노출될 헤더를 설정. 클라이언트에서 `Access`, 'Refresh' 헤더에 접근할 수 있도록 허용
                        // List를 인자로 받는다.
                        config.setExposedHeaders(List.of("Access", "Refresh"));
                        return config;
                    }
                }))
                .exceptionHandling(ex -> ex // 인증 실패 시 401 메시지 반환
                        .authenticationEntryPoint((req, res, authEx) ->
                                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                )
                .authorizeHttpRequests((auth) ->
                        auth.requestMatchers("/api/auth/**", "/oauth2/**","/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated())
                .formLogin(AbstractHttpConfigurer::disable)
                .oauth2Login((oauth2) -> oauth2.loginPage("/auth/login")
                        .redirectionEndpoint(redirection -> redirection.baseUri("/oauth2/code/*"))
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(authenticationSuccessHandler)
                        .failureHandler(authenticationFailureHandler)
                )
                .logout(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, LoginFilter.class)
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
