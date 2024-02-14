package org.example

import database.InMemoryDatabase
import models.AccountId
import services.AccountService
import java.math.BigDecimal

fun main() {
    val database = InMemoryDatabase()
    val accountService = AccountService(database)

    val accountId1 = AccountId()
    val accountId2 = AccountId()

    accountService.createAccount(accountId1, BigDecimal("1000"))
    accountService.createAccount(accountId2, BigDecimal("500"))

    accountService.deposit(accountId1, BigDecimal("500"))
    accountService.withdraw(accountId1, BigDecimal("200"))
    accountService.transfer(accountId1, accountId2, BigDecimal("300"))

    println("Account $accountId1 Balance: ${accountService.checkBalance(accountId1)}")
    println("Account $accountId2 Balance: ${accountService.checkBalance(accountId2)}")
}
