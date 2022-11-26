package beertag.config;

import beertag.security.CustomAuthenticationProvider;
import beertag.security.CustomWebAuthenticationDetailsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.sql.DataSource;

@Configuration
@Order(2)
@EnableWebSecurity
@ComponentScan(basePackages = "beertag.security")
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final DataSource dataSource;
    private final CustomWebAuthenticationDetailsSource detailsSource;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(DataSource dataSource,
                          CustomWebAuthenticationDetailsSource detailsSource,
                          AuthenticationFailureHandler authenticationFailureHandler,
                          @Qualifier("userDetailsService") UserDetailsService userDetailsService) {

        this.dataSource = dataSource;
        this.detailsSource = detailsSource;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public UserDetailsManager userDetailsManager() {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
        jdbcUserDetailsManager.setDataSource(dataSource);
        return jdbcUserDetailsManager;
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        final CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        //authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

//    @Bean
//    public PasswordEncoder encoder() {
//        return new BCryptPasswordEncoder(11);
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication()
//                .dataSource(dataSource);
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        csrf().disable();
        //  http.formLogin().defaultSuccessUrl("/", true);
      //  http.csrf().disable().authorizeRequests()
        http.authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/assets/img/**", "/brewery", "/swagger-ui.html").permitAll()
                .antMatchers("/", "/beers**", "/register", "/register/confirm", "/register-confirmation").permitAll()
                .antMatchers("/rate/**", "/beers/create-beer").hasRole("USER")
                .antMatchers("/list/**").hasRole("USER")
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/api/users/").hasRole("USER")
                .antMatchers("/upload-dir/").hasRole("USER")
                .antMatchers("/files", "/files/**").hasRole("USER")
                .antMatchers("/api/users/**").authenticated()
                .antMatchers("/api/beers/").hasRole("USER")
                .antMatchers("/api/beers/**").authenticated()
                .antMatchers("/api/beers").hasRole("USER")
                .antMatchers("/api/breweries/").hasRole("USER")
                .antMatchers("/api/breweries/**").authenticated()
                .antMatchers("/api/users/register").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin().authenticationDetailsSource(detailsSource)
                .failureHandler(authenticationFailureHandler)
                .failureUrl("/login/error")
                .loginPage("/login")
                .loginProcessingUrl("/authenticate")
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied");

    }


}
