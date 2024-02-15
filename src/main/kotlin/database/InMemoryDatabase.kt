package database

import exceptions.NotFoundException
import models.AccountId
import models.BankAccount
import utils.ResultHelper.Companion.failure
import utils.ResultHelper.Companion.success

class InMemoryDatabase {
    private val accounts: MutableMap<AccountId, BankAccount> = mutableMapOf()

    fun addAccount(): Result<BankAccount> {
        val accountId = AccountId()
        val account = BankAccount()
        account.accountId = accountId
        accounts[accountId] = account
        return success(account)
    }

    fun getAccount(accountId: AccountId): Result<BankAccount> =
        accounts[accountId]?.let { success(it) } ?: failure(NotFoundException("Account with account ID $accountId does not exist."))
}
