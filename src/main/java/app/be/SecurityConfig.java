package app.be;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final List<String> allowedUrls = Arrays.asList("http://localhost:3000",
            "https://ff4b7f44c306.ngrok.io",
            "https://accounts.google.com",
            "https://ccci-scavengerhunt.herokuapp.com");

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedUrls);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.addAllowedHeader("*");

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.cors();

        httpSecurity.csrf()
                .disable()
                .antMatcher("/**").authorizeRequests()
                .antMatchers("/", "/index.html").permitAll()
                .antMatchers("/", "/time").permitAll()
                .antMatchers("/", "/users").permitAll()
                .antMatchers("/", "/config/getsettings").permitAll()
                .antMatchers("/", "/assets/**").permitAll();
    }
}
