package es.kazzpa.selattserver.services;

import es.kazzpa.selattserver.models.AppUser;
import es.kazzpa.selattserver.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AppUser register(AppUser user) throws Exception {
        String username = user.getUsername().trim();
        String email = user.getEmail().trim();
        String password = user.getPassword().trim();
        user.setUsername(username);
        user.setEmail(email);
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || username.contains(password) || password.contains(username)) {
            throw new Exception("Algunos campos son invalidos.");
        }
        AppUser userAlreadyRegistered = appUserRepository.findByUsername(username);
        //Some check before saving
        if (userAlreadyRegistered != null) {
            throw new Exception("Ya esta registrado en el sistema, pruebe a logearse");
        }
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(email);

        if (!mat.matches()) {
            throw new Exception("El email es invalido");
        }
        user.setPassword(passwordEncoder.encode(password));
        appUserRepository.save(user);
        return user;
    }
}
