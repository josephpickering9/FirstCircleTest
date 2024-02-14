package database

import exceptions.AlreadyExistsException
import exceptions.NotFoundException
import models.AccountId
import models.BankAccount

class InMemoryDatabase {
    private val accounts: MutableMap<AccountId, BankAccount> = mutableMapOf()

    fun addAccount(account: BankAccount): Result<Unit> {
        if (accounts.containsKey(account.accountId))
            Result.failure<AlreadyExistsException>(AlreadyExistsException("Account with account ID ${account.accountId} already exists."))

        accounts[account.accountId] = account
        return Result.success(Unit)
    }

    fun getAccount(accountId: AccountId): Result<BankAccount> {
        if (!accounts.containsKey(accountId))
            Result.failure<NotFoundException>(NotFoundException("Account with account ID $accountId does not exist."))

        return Result.success(accounts.getValue(accountId))
    }
}
