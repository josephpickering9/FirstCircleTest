package org.example.services

import database.InMemoryDatabase

class BankService(private val database: InMemoryDatabase) {
    fun getAccounts() {
        database.getAccounts()
    }
}
