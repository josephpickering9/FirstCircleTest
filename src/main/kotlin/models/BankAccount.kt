package models

import enums.TransactionType
import exceptions.InvalidAmountException
import exceptions.InvalidBalanceException
import org.example.models.Transaction
import java.math.BigDecimal
import java.util.UUID

class BankAccount(val accountId: AccountId, initialDeposit: BigDecimal) {
    private val transactions: MutableList<Transaction> = mutableListOf()
    val balance: BigDecimal =
        transactions.sumOf { transaction ->
            when (transaction.type) {
                TransactionType.DEPOSIT -> transaction.amount
                TransactionType.WITHDRAWAL -> transaction.amount.negate()
            }
        }

    init {
        if (initialDeposit > BigDecimal.ZERO) {
            transactions.add(Transaction(TransactionType.DEPOSIT, initialDeposit))
        }
    }

    fun deposit(amount: BigDecimal): Result<Unit> {
        if (amount <= BigDecimal.ZERO) throw InvalidAmountException("Deposit amount must be positive.")

        transactions.add(Transaction(TransactionType.DEPOSIT, amount))
        return Result.success(Unit)
    }

    fun withdraw(amount: BigDecimal): Result<Unit> {
        if (amount <= BigDecimal.ZERO) throw InvalidAmountException("Withdrawal amount must be positive.")
        if (this.balance < amount) throw InvalidBalanceException("Insufficient funds.")

        transactions.add(Transaction(TransactionType.WITHDRAWAL, amount))
        return Result.success(Unit)
    }
}

data class AccountId(val value: UUID) {
    companion object {
        fun generate(): AccountId = AccountId(UUID.randomUUID())
    }
}
