import database.InMemoryDatabase
import models.AccountId
import services.AccountService
import utils.ResultHelper.Companion.expectSuccess
import java.math.BigDecimal

fun main() {
    val database = InMemoryDatabase()
    val accountService = AccountService(database)

    val account1 = accountService.createAccount(BigDecimal("1000")).expectSuccess()
    val account2 = accountService.createAccount(BigDecimal("500")).expectSuccess()
    val accountId1 = account1.accountId
    val accountId2 = account2.accountId

    accountService.deposit(accountId1, BigDecimal("500"))
    accountService.withdraw(accountId1, BigDecimal("200"))
    accountService.transfer(accountId1, accountId2, BigDecimal("300"))

    println("Account $accountId1 Balance: ${accountService.checkBalance(accountId1).getOrNull()}")
    println("Account $accountId2 Balance: ${accountService.checkBalance(accountId2).getOrNull()}")
}
