package es.kazzpa.selattserver.services;

import es.kazzpa.selattserver.models.AppUser;

public interface AuthService {
    AppUser register(AppUser user) throws Exception;
}
