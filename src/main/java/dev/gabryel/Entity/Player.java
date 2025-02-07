package dev.gabryel.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "players")
public class Player extends PanacheEntity {

    public Player() {}

    @ManyToOne
    @JoinColumn(name = "tableId", nullable = false)
    @JsonBackReference
    public PokerTable pokerTable;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    public User user;

}
