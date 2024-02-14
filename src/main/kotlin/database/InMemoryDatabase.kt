package database

import exceptions.AlreadyExistsException
import exceptions.NotFoundException
import models.AccountId
import models.BankAccount
import org.example.utils.ResultHelper.Companion.failure
import org.example.utils.ResultHelper.Companion.success

class InMemoryDatabase {
    private val accounts: MutableMap<AccountId, BankAccount> = mutableMapOf()

    fun addAccount(account: BankAccount): Result<Unit> {
        if (accounts.containsKey(account.accountId))
            return failure(AlreadyExistsException("Account with account ID ${account.accountId} already exists."))

        accounts[account.accountId] = account
        return success(Unit)
    }

    fun getAccount(accountId: AccountId): Result<BankAccount> {
        if (!accounts.containsKey(accountId))
            return failure(NotFoundException("Account with account ID $accountId does not exist."))

        return success(accounts.getValue(accountId))
    }

    fun getAccounts(): Result<List<BankAccount>> = success(accounts.values.toList())
}
