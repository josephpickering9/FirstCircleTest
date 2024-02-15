package services

import database.InMemoryDatabase
import models.AccountId
import models.BankAccount
import exceptions.SameAccountException
import utils.ResultHelper.Companion.failure
import utils.ResultHelper.Companion.success
import java.math.BigDecimal

class AccountService(private val database: InMemoryDatabase) {

    fun createAccount(initialDeposit: BigDecimal? = null): Result<BankAccount> {
        val account = BankAccount()
        database.addAccount(account).onFailure { return failure(it) }

        if (initialDeposit != null) account.deposit(initialDeposit)

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
        if (fromAccountId == toAccountId) return failure(SameAccountException("Cannot transfer to the same account."))

        val fromAccount = database.getAccount(fromAccountId).getOrElse { return failure(it) }
        val toAccount = database.getAccount(toAccountId).getOrElse { return failure(it) }

        fromAccount.withdraw(amount).onFailure { return failure(it) }
        toAccount.deposit(amount).onFailure { fromAccount.deposit(amount); return failure(it) }

        return success(Unit)
    }

    fun checkBalance(accountId: AccountId): Result<BigDecimal> =
        database.getAccount(accountId).map { it.getBalance() }.onFailure { return failure(it) }
}
