package dev.gabryel.Repository;

import dev.gabryel.Entity.PokerTable;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PokerTableRepository implements PanacheRepository<PokerTable> {
}
