package com.Astralis.backend.management.configuration;

import com.Astralis.backend.management.filter.AuthenticationFilter;
import com.Astralis.backend.management.filter.AuthorizationFilter;
import com.Astralis.backend.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static String ALLOWED_ORIGIN_PATH = "http://localhost:4200";
    private boolean corsEnabled = true;

    @Autowired
    private TokenProperties tokenProperties;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserService personService;
    @Autowired
    private LogoutSuccess logoutSuccess;

    //same as Controller annotation
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");  // TODO: lock down before deploying
        config.addAllowedHeader("*");
        config.addExposedHeader(HttpHeaders.AUTHORIZATION);
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        applyCors(http)
                .antMatcher(tokenProperties.getLoginPath())
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedResponse())
                .and()
                .logout()
                .logoutUrl(tokenProperties.getLogoutPath())
                .logoutSuccessHandler(logoutSuccess)
                .and()
                .addFilter(new AuthenticationFilter(authenticationManagerBean(), tokenProperties))
                .addFilterAfter(new AuthorizationFilter(tokenProperties), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, tokenProperties.getLoginPath()).permitAll()
                .antMatchers(HttpMethod.POST, tokenProperties.getRegistrationPath()).permitAll()
                .antMatchers("/**").permitAll(); // TODO: replacing with authentication required
                //.antMatchers("/**").authenticated();
    }

    private HttpSecurity applyCors(HttpSecurity http) throws Exception {
        if (corsEnabled) {
            return http.cors().and();
        } else {
            return http;
        }
    }

    private AuthenticationEntryPoint unauthorizedResponse() {
        return (req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
