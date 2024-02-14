package models

import enums.TransactionType
import exceptions.InvalidAmountException
import exceptions.InvalidBalanceException
import org.example.models.Transaction
import org.example.utils.ResultHelper.Companion.failure
import org.example.utils.ResultHelper.Companion.success
import java.math.BigDecimal
import java.util.UUID

class BankAccount(val accountId: AccountId, initialDeposit: BigDecimal) {
    private val transactions: MutableList<Transaction> = mutableListOf()

    init {
        if (initialDeposit > BigDecimal.ZERO) {
            transactions.add(Transaction(TransactionType.DEPOSIT, initialDeposit))
        }
    }

    fun deposit(amount: BigDecimal): Result<Unit> {
        if (amount <= BigDecimal.ZERO) return failure(InvalidAmountException("Deposit amount must be positive."))

        transactions.add(Transaction(TransactionType.DEPOSIT, amount))
        return success(Unit)
    }

    fun withdraw(amount: BigDecimal): Result<Unit> {
        if (amount <= BigDecimal.ZERO) return failure(InvalidAmountException("Withdrawal amount must be positive."))
        if (this.getBalance() < amount) return failure(InvalidBalanceException("Insufficient funds."))

        transactions.add(Transaction(TransactionType.WITHDRAWAL, amount))
        return success(Unit)
    }

    fun getBalance(): BigDecimal = transactions.sumOf { it.effectiveAmount }
}

data class AccountId(val value: UUID) {
    companion object {
        fun generate(): AccountId = AccountId(UUID.randomUUID())
    }
}
