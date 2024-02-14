package org.example.services

import database.InMemoryDatabase
import models.BankAccount

class BankService(private val database: InMemoryDatabase) {
    fun getAccounts(): Result<List<BankAccount>> = database.getAccounts()
}
