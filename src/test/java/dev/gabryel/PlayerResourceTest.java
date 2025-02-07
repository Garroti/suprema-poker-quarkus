package dev.gabryel;
import dev.gabryel.Entity.Player;
import dev.gabryel.Entity.PokerTable;
import dev.gabryel.Entity.User;
import dev.gabryel.Repository.PlayerRepository;
import dev.gabryel.Service.PlayerService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@QuarkusTest
public class PlayerResourceTest {

    @InjectMock
    PlayerRepository playerRepository;

    @Inject
    PlayerService playerService;

    @Test
    public void testValidateWinnerWhenNotEnoughPlayers() {
        PokerTable pokerTable = Mockito.mock(PokerTable.class);

        when(pokerTable.players).thenReturn(new ArrayList<>(Collections.nCopies(2, new Player())));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            playerService.validateWinner(pokerTable);
        });

        assertEquals("Mesa precisa ter no minimo 3 jogadores para que haja um ganhador", exception.getMessage());
    }

    @Test
    public void testValidateWinnerWhenEnoughPlayers() {
        PokerTable pokerTable = Mockito.mock(PokerTable.class);

        when(pokerTable.players).thenReturn(new ArrayList<>(Collections.nCopies(3, new Player())));

        assertDoesNotThrow(() -> {
            playerService.validateWinner(pokerTable);
        });
    }

    @Test
    public void testValidateWhenPlayerAlreadyExists() {
        Long tableId = 1L;
        Long userId = 1L;
        PokerTable pokerTable = Mockito.mock(PokerTable.class);
        User user = Mockito.mock(User.class);
        Player player = new Player();

        when(playerRepository.findExistingPlayer(tableId, userId)).thenReturn(player);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            playerService.validatePlayer(tableId, user, pokerTable, userId);
        });

        assertEquals("Usuário já está na mesa", exception.getMessage());

    }

    @Test
    public void testValidatePlayerWhenTableOrUserIsNull() {
        Long tableId = 1L;
        Long userId = 1L;
        PokerTable pokerTable = Mockito.mock(PokerTable.class);
        User user = Mockito.mock(User.class);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            playerService.validatePlayer(tableId, null, pokerTable, userId); // Usuario nulo
        });

        assertEquals("Mesa ou Usuário não encontrado", exception.getMessage());

        exception = assertThrows(ValidationException.class, () -> {
            playerService.validatePlayer(tableId, user, null, userId); // Mesa nula
        });

        assertEquals("Mesa ou Usuário não encontrado", exception.getMessage());
    }

    @Test
    public void testValidatePlayerWhenTableIsFull() {

        Long tableId = 1L;
        Long userId = 1L;
        PokerTable pokerTable = Mockito.mock(PokerTable.class);
        User user = Mockito.mock(User.class);
        when(pokerTable.players).thenReturn(new ArrayList<>(Collections.nCopies(8, new Player())));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            playerService.validatePlayer(tableId, user, pokerTable, userId);
        });

        assertEquals("Mesa já está cheia", exception.getMessage());
    }

    @Test
    public void testValidatePlayerWhenAllConditionsAreMet() {
        Long tableId = 1L;
        Long userId = 1L;
        PokerTable pokerTable = Mockito.mock(PokerTable.class);
        User user = Mockito.mock(User.class);
        when(playerRepository.find("pokerTable.id = ?1 and user.id = ?2", tableId, userId))
                .thenReturn(null);
        when(pokerTable.players).thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> {
            playerService.validatePlayer(tableId, user, pokerTable, userId);
        });
    }

}

