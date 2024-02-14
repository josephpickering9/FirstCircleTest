import models.AccountId
import models.BankAccount
import org.example.utils.ResultHelper.Companion.expectFailure
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class BankAccountTests {

    @Test
    fun `BankAccount creation with initial deposit`() {
        val accountId = AccountId()
        val account = BankAccount(accountId, BigDecimal("1000"))

        assertEquals(accountId, account.accountId)
        assertEquals(BigDecimal("1000"), account.getBalance())
    }

    @Test
    fun `deposit positive amount increases balance`() {
        val account = BankAccount(AccountId(), BigDecimal("500"))

        account.deposit(BigDecimal("500"))

        assertEquals(BigDecimal("1000"), account.getBalance())
    }

    @Test
    fun `withdraw positive amount decreases balance`() {
        val account = BankAccount(AccountId(), BigDecimal("1000"))

        account.deposit(BigDecimal("2000"))
        account.withdraw(BigDecimal("1000"))

        assertEquals(BigDecimal("2000"), account.getBalance())
    }

    @Test
    fun `withdraw more than balance throws exception`() {
        val account = BankAccount(AccountId(), BigDecimal("500"))

        val exception = account.withdraw(BigDecimal("600")).expectFailure()

        assertEquals("Insufficient funds.", exception.message)
    }

    @Test
    fun `deposit negative amount throws exception`() {
        val account = BankAccount(AccountId(), BigDecimal("1000"))

        val exception = account.deposit(BigDecimal("-100")).expectFailure()

        assertEquals("Deposit amount must be positive.", exception.message)
    }
}
