import javax.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class AppWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${app.username}")
    private String username;

    @Value("${app.password}")
    private String password;

    @Override
    protected void configure(HttpSecurity http) {
        http
            .authorizeRequests()
                .anyRequest()
                    .authenticated()
            .and()
                .cors()
            .and()
                .csrf()
                    .disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth
            .inMemoryAuthentication()
            .withUser(username)
            .password(password)
            .roles("ROLE_ADMIN");
    }
}