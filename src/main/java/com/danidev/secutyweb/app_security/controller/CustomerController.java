package com.danidev.secutyweb.app_security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("v1")
public class CustomerController {
    private SessionRegistry sessionRegistry;

    public CustomerController(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @GetMapping("/index")
    public String index(){
        return "Hello word";
    }
    @GetMapping("/index2")
    public String index2(){
        return "Hello word not Secured!";
    }
    @GetMapping("/sesion")
    public ResponseEntity<?> getDetailSession(){
        String sessionId="";
        User userObject = null;
        List<Object> sessiones = sessionRegistry.getAllPrincipals();
        for(Object session : sessiones){
            if(session instanceof User){
                userObject=(User) session;
            }
           List<SessionInformation>  sessionInformations= sessionRegistry.getAllSessions(session,false);
            for(SessionInformation sessionInformation: sessionInformations){
                sessionId=sessionInformation.getSessionId();
            }
        }
        Map<String,Object> response = new HashMap<>();
        response.put("response: " , "hello word");
        response.put("sessionID",sessionId);
        response.put("SessionUser",userObject);
        return (ResponseEntity<?>) ResponseEntity.ok(response);

    }
}
