package models

import enums.TransactionType
import exceptions.InvalidAmountException
import exceptions.InvalidBalanceException
import utils.ResultHelper.Companion.failure
import utils.ResultHelper.Companion.success
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.util.UUID

class BankAccount {
    private val transactions: MutableList<Transaction> = mutableListOf()
    lateinit var accountId: AccountId

    fun deposit(amount: BigDecimal): Result<Unit> {
        if (amount <= ZERO) return failure(InvalidAmountException("Deposit amount must be positive."))

        addTransaction(TransactionType.DEPOSIT, amount)
        return success(Unit)
    }

    fun withdraw(amount: BigDecimal): Result<Unit> {
        if (amount <= ZERO) return failure(InvalidAmountException("Withdrawal amount must be positive."))
        if (getBalance() < amount) return failure(InvalidBalanceException("Insufficient funds."))

        addTransaction(TransactionType.WITHDRAWAL, amount)
        return success(Unit)
    }

    fun getBalance(): BigDecimal = if (transactions.any()) transactions.sumOf { it.effectiveAmount } else ZERO

    private fun addTransaction(type: TransactionType, amount: BigDecimal) =
        transactions.add(Transaction(type, amount))
}

data class AccountId(val value: UUID = UUID.randomUUID())
