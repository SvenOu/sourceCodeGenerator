package com.sven.security.config;

import com.sven.security.utils.NoOpPasswordEncoder;
import com.sven.security.bean.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.sql.DataSource;

@Configuration
@ImportResource(value = {
        "classpath:security/database-context.xml"
})
@PropertySource(value = {
        "classpath:security/application.properties",
        "classpath:security/database-jdbc.properties",
})
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Value("${com.sven.security.loginPageUrl}")
    private String loginPageUrl;

    @Value("${API_security_PREFIX}")
    private String apiSecurityPrefix;

    @Value("${com.sven.security.loginSuccessPageUrl}")
    private String loginSuccessPageUrl;

    @Value("${com.sven.security.loginAuthorizedPageUrls}")
    private String loginAuthorizedPageUrls;

    @Autowired
    public SecurityConfig(@Qualifier("CUserDetailsService") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(loginAuthorizedPageUrls.split(",")).hasAnyRole(RoleEnum.getAllValues())
                .antMatchers("/api/**").hasAnyRole(RoleEnum.getAllValues())
                .and()
                .anonymous()
                .disable()
                .sessionManagement()
                .sessionFixation()
                .none()
                .invalidSessionUrl(loginPageUrl)
                .and()
                .formLogin()
                .loginPage(loginPageUrl)
                .defaultSuccessUrl(loginSuccessPageUrl, true)
                .loginProcessingUrl("/j_spring_security_check")
                .usernameParameter("j_username")
                .passwordParameter("j_password")
                .failureForwardUrl(apiSecurityPrefix + "/loginFail")
                .and()
                .logout()
                .logoutUrl("/j_spring_security_logout")
                .invalidateHttpSession(true)
                .logoutSuccessUrl(loginPageUrl)
                .and()
                .exceptionHandling().accessDeniedPage(loginPageUrl);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

}