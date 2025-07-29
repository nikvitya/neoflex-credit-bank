package ru.neoflex.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.neoflex.deal.model.Statement;

import java.util.UUID;

public interface StatementRepository extends JpaRepository<Statement, UUID> {
}
