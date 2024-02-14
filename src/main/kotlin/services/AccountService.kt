package services

import database.InMemoryDatabase
import models.AccountId
import models.BankAccount
import java.math.BigDecimal

class BankAccountService(private val database: InMemoryDatabase) {

    fun createAccount(accountId: AccountId, initialDeposit: BigDecimal) {
        val account = BankAccount(accountId, initialDeposit)
        database.addAccount(account)
    }

    fun deposit(accountId: AccountId, amount: BigDecimal) =
        database.getAccount(accountId).onSuccess { it.deposit(amount) }

    fun withdraw(accountId: AccountId, amount: BigDecimal) =
        database.getAccount(accountId).onSuccess { it.withdraw(amount) }

    fun transfer(fromAccountId: AccountId, toAccountId: AccountId, amount: BigDecimal) {
        database.getAccount(fromAccountId).onSuccess { it.withdraw(amount) }
        database.getAccount(toAccountId).onSuccess { it.deposit(amount) }
    }

    fun checkBalance(accountId: AccountId): Result<BigDecimal> =
        database.getAccount(accountId).map { it.balance }
}
