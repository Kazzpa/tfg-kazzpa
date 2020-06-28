package es.kazzpa.selattserver.repositories;

import es.kazzpa.selattserver.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <Usuario, String> {
}
