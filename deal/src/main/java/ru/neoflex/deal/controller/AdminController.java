package ru.neoflex.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.deal.dto.StatementDtoFull;
import ru.neoflex.deal.dto.StatementDtoShort;
import ru.neoflex.deal.enums.ApplicationStatus;
import ru.neoflex.deal.service.AdminService;

import java.util.List;

import static ru.neoflex.deal.enums.ChangeType.MANUAL;

@Tag(name = "Сделка: админ", description = "API по сохранению нового статуса заявки и получению заявки по идентификтору.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/deal/admin/statement")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "Сохранение статуса заявки.")
    @PutMapping("/{statementId}/status")
    public void saveStatementStatus(@PathVariable @Parameter(required = true) String statementId,
                                    @RequestParam @Parameter(required = true) ApplicationStatus status) {
        adminService.saveStatementStatus(statementId, status, MANUAL);
    }


    @Operation(summary = "Получение заявки по идентификатору.")
    @GetMapping("/{statementId}")
    public StatementDtoFull findStatementById(@PathVariable @Parameter(required = true) String statementId) {
        return adminService.findStatementById(statementId);
    }


    @Operation(summary = "Получение всех заявок.")
    @GetMapping()
    public List<StatementDtoShort> findAllStatements() {
        return adminService.findAllStatements();
    }
}
