package dev.gabryel.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.*;

@Entity
@Table(name = "poker_table")
public class PokerTable extends PanacheEntity {

    public PokerTable() {
    }

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, message = "O nome deve ter pelo menos 3 caracteres")
    public String name;

    @OneToMany(mappedBy = "pokerTable", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Player> players;

}
