package com.ucm.lib.config;

import com.ucm.lib.config.util.AuthEntryPointJwt;
import com.ucm.lib.config.util.AuthTokenFilter;
import com.ucm.lib.services.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Provides all default security settings. Applications can then define additional rules on top of this.
 * @author Joshua Podhola
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class BaseSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserInfoService userInfoService;

    private final AuthEntryPointJwt unauthorizedHandler;

    /**
     * Constructor-based injection of dependant components. Not intended to be called.
     * @author Joshua Podhola
     * @param userInfoService UserInfoService
     */
    @Autowired
    public BaseSecurityConfig(UserInfoService userInfoService, AuthEntryPointJwt unauthorizedHandler) {
        this.userInfoService = userInfoService;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userInfoService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * For testing purposes, override password hashing to bypass it. Deploy this and I'll deep fry your boots.
     * @author Joshua Podhola
     */
    private static class CustomPasswordEncoder implements PasswordEncoder {
        @Override
        public String encode(CharSequence charSequence) {
            return charSequence.toString();
        }

        @Override
        public boolean matches(CharSequence charSequence, String s) {
            return charSequence.toString().equals(s);
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/api/test/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated();

        http.headers().frameOptions().disable();
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
