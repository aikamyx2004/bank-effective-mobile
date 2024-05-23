package ru.ainur.bank.service.bank;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.ainur.bank.model.QBankAccount;

@Component
@Slf4j
public class UserBalanceScheduler {
    private final JPAQueryFactory queryFactory;

    public UserBalanceScheduler(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void increaseBalances() {
        log.info("Starting balance increase process...");

        QBankAccount bankAccount = QBankAccount.bankAccount;
        queryFactory.update(bankAccount)
                .set(bankAccount.balance, bankAccount.balance.multiply(1.05))
                .where(bankAccount.balance.multiply(1.05).loe(bankAccount.balance.multiply(2.07)))
                .execute();
    }
}
