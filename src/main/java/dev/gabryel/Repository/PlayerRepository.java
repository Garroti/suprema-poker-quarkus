package dev.gabryel.Repository;

import dev.gabryel.Entity.Player;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlayerRepository implements PanacheRepository<Player> {

    public Player findExistingPlayer(Long tableId, Long userId) {
        return Player.find("pokerTable.id = ?1 and user.id = ?2", tableId, userId).firstResult();
    }

}
