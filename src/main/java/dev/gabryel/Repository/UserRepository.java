package dev.gabryel.Repository;

import dev.gabryel.Entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public User findByUsername(String username) {
        return find("username", username).firstResult();
    }

    public User findByCPF(String cpf) {
        return find("cpf", cpf).firstResult();
    }

}

