package ru.itis.dis403.lab2_06.controller.gui;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.itis.dis403.lab2_06.dto.AuthRequest;
import ru.itis.dis403.lab2_06.service.JWTService;

@Controller
@RequiredArgsConstructor
public class AppPageController {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    @GetMapping("/app")
    public String appPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            System.out.println(authentication.getPrincipal());
            return "app";
        } else {
            System.out.println("redirect:login");
            return "redirect:login";
        }
    }

    @PostMapping("/app")
    public String app(@RequestBody AuthRequest authRequest, Model model) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.username(),
                        authRequest.password()
                )
        );

        UserDetails user = userDetailsService.loadUserByUsername(authRequest.username());
        String token = jwtService.generateToken(user);
        System.out.println("token " + token);

        model.addAttribute("jwt_token", token);

        return "app";
    }
}

