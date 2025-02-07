package dev.gabryel.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.*;

@Entity
@Table(name = "users")
public class User extends PanacheEntity {

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, message = "O nome deve ter pelo menos 3 caracteres")
    public String name;

    @NotBlank(message = "O CPF é obrigatório")
    public String cpf;

    @NotBlank(message = "O telefone é obrigatório")
    public String phone;

    @NotBlank(message = "A senha é obrigatória")
    public String password;

    @NotBlank(message = "O username é obrigatório")
    public String username;

}
