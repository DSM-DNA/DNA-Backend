package com.dna.backend.DNABackend.security.config;

import com.dna.backend.DNABackend.security.jwt.JwtConfigure;
import com.dna.backend.DNABackend.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() //웹사이트 취약점 공격
                .cors().and()
                .sessionManagement().disable()
                .authorizeRequests()
                    .antMatchers(HttpMethod.POST,"/signup").permitAll()
                    .antMatchers("/email").permitAll()
                    .antMatchers(HttpMethod.POST, "/auth").permitAll()
                    .anyRequest().authenticated().and()
                .apply(new JwtConfigure(jwtTokenProvider));

        http.logout() // 만약 csrf와 사용하는 경우 무조건 post
                .logoutUrl("/logout") //로그아웃 요청 시 사용하는 URL
                .logoutSuccessUrl("/auth"); // 로그아웃 성공시 반환하는 URL

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
