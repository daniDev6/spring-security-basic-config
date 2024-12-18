package com.danidev.secutyweb.app_security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                //.csrf(c->c.disable()) no desahabilitar si se trabaja con formularios
                .authorizeHttpRequests(aut->{
                            aut.requestMatchers("/v1/index2").permitAll();
                            aut.anyRequest().authenticated();
                        }
                )
                .formLogin(f->f.successHandler(successHandler()))//redirige despues de iniciar sesion
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sesion->{

                    sesion.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);//crea una sesion si no existe
                    //sesion.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);//crea una sesion solo si es necesario
                    //sesion.sessionCreationPolicy(SessionCreationPolicy.NEVER);//no crea ninguna sesion, pero si ya hay una usa esa
                    //sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS);//sin estado
                    sesion.invalidSessionUrl("/login");
                    sesion.maximumSessions(1).expiredUrl("/login");

                    sesion.sessionConcurrency(s->{
                        s.sessionRegistry(sessionRegistry());
                    });
                    sesion.sessionFixation(sessionFixationConfigurer -> {
                        sessionFixationConfigurer.migrateSession();//crea otro id session de ser necesario
                        sessionFixationConfigurer.newSession();//crea nueva sesion
                        sessionFixationConfigurer.none();//ante ataques de fijacion spring no hace nada
                    });
                })
                //.httpBasic()

                .build();
    }
    public AuthenticationSuccessHandler successHandler(){
        return (((request, response, authentication) -> {
            response.sendRedirect("/v1/sesion");
        }));
    }
    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }
}
