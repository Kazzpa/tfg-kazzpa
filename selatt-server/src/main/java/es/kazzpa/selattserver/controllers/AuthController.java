package es.kazzpa.selattserver.controllers;

import es.kazzpa.selattserver.models.AppUser;
import es.kazzpa.selattserver.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
public class AuthController {
    @Autowired
    public AuthService authService;

    @PostMapping("/register")
    public AppUser register(@RequestBody AppUser user) throws Exception {
        return authService.register(user);
    }
}
