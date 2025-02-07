package dev.gabryel.Service;

import dev.gabryel.Entity.Player;
import dev.gabryel.Entity.PokerTable;
import dev.gabryel.Entity.User;
import dev.gabryel.Repository.PlayerRepository;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ValidationException;

@ApplicationScoped
public class PlayerService {

    @Inject
    PlayerRepository playerRepository;

    public void validatePlayer(Long tableId, @Nullable User user, @Nullable PokerTable table, Long userId) {

        Player existingPlayer = playerRepository.findExistingPlayer(tableId, userId);

        if (existingPlayer != null) throw new ValidationException("Usuário já está na mesa");

        if (table == null || user == null) throw new ValidationException("Mesa ou Usuário não encontrado");

        if (table.players.size() >= 8) throw new ValidationException("Mesa já está cheia");

    }

    public void validateWinner(PokerTable table) {
        if (table.players.size() < 3) {
            throw new ValidationException("Mesa precisa ter no minimo 3 jogadores para que haja um ganhador");
        }
    }
}
