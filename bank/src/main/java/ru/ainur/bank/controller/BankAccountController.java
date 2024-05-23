package ru.ainur.bank.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ainur.bank.dto.request.MoneyTransferRequest;
import ru.ainur.bank.service.bank.BankAccountService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/accounts")
@Tag(name = "Перевод денег")
public class BankAccountController {

    private BankAccountService bankAccountService;

    @PostMapping("/transfer")
    public ResponseEntity<Void> transferMoney(@RequestBody MoneyTransferRequest request) {
        bankAccountService.transferMoney(request);
        return ResponseEntity.ok().build();
    }

}