package services

import database.InMemoryDatabase
import models.AccountId
import models.BankAccount
import org.example.utils.ResultHelper.Companion.failure
import org.example.utils.ResultHelper.Companion.success
import java.math.BigDecimal

class AccountService(private val database: InMemoryDatabase) {

    fun createAccount(accountId: AccountId, initialDeposit: BigDecimal): Result<BankAccount> {
        val account = BankAccount(accountId, initialDeposit)
        database.addAccount(account).onFailure { return failure(it) }
        return success(account)
    }

    fun deposit(accountId: AccountId, amount: BigDecimal): Result<Unit> {
        database.getAccount(accountId).getOrElse { return failure(it) }.deposit(amount)
        return success(Unit)
    }

    fun withdraw(accountId: AccountId, amount: BigDecimal): Result<Unit> {
        database.getAccount(accountId).getOrElse { return failure(it) }.withdraw(amount)
        return success(Unit)
    }

    fun transfer(fromAccountId: AccountId, toAccountId: AccountId, amount: BigDecimal): Result<Unit> {
        val fromAccount = database.getAccount(fromAccountId).getOrElse { return failure(it) }
        val toAccount = database.getAccount(toAccountId).getOrElse { return failure(it) }

        fromAccount.withdraw(amount).onFailure { return failure(it) }
        toAccount.deposit(amount).onFailure { return failure(it) }

        return success(Unit)
    }

    fun checkBalance(accountId: AccountId): Result<BigDecimal> =
        database.getAccount(accountId).map { it.getBalance() }.onFailure { return failure(it) }
}
