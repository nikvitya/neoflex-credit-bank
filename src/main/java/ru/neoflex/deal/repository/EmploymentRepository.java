package ru.neoflex.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.neoflex.deal.model.Employment;

import java.util.UUID;

public interface EmploymentRepository extends JpaRepository<Employment, UUID> {
}
