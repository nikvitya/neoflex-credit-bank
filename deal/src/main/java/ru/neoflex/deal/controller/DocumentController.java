package ru.neoflex.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.deal.service.DocumentService;

@Tag(name = "Сделка: документы", description = "API по оформлению и отправке документов по кредиту.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/deal/document/{statementId}")
public class DocumentController {
    private final DocumentService documentService;

    @Operation(summary = "Запрос на отправку документов.")
    @PostMapping("/send")
    public void sendDocuments(@PathVariable @Parameter(required = true) String statementId) {
        documentService.sendDocuments(statementId);
    }

    @Operation(summary = "Запрос на подписание документов.")
    @PostMapping("/sign")
    public void signDocuments(@PathVariable @Parameter(required = true) String statementId) {
        documentService.signDocuments(statementId);
    }

    @Operation(summary = "Подписание документов.")
    @PostMapping("/code")
    public void verifySesCode(@PathVariable @Parameter(required = true) String statementId,
                              @RequestBody String code) {
        documentService.verifySesCode(statementId, code);
    }
}
