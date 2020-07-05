package es.kazzpa.selattserver.repositories;

import es.kazzpa.selattserver.models.AppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends CrudRepository<AppUser, String> {
    AppUser findByUsername(String username);
}
