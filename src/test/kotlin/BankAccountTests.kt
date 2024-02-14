import exceptions.InvalidAmountException
import exceptions.InvalidBalanceException
import models.AccountId
import models.BankAccount
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class BankAccountTests {

    @Test
    fun `BankAccount creation with initial deposit`() {
        val accountId = AccountId.generate()
        val account = BankAccount(accountId, BigDecimal("1000"))

        assertEquals(accountId, account.accountId)
        assertEquals(BigDecimal("1000"), account.getBalance())
    }

    @Test
    fun `deposit positive amount increases balance`() {
        val account = BankAccount(AccountId.generate(), BigDecimal("500"))

        account.deposit(BigDecimal("500"))

        assertEquals(BigDecimal("1000"), account.getBalance())
    }

    @Test
    fun `withdraw positive amount decreases balance`() {
        val account = BankAccount(AccountId.generate(), BigDecimal("1000"))

        account.deposit(BigDecimal("2000"))
        account.withdraw(BigDecimal("1000"))

        assertEquals(BigDecimal("2000"), account.getBalance())
    }

    @Test
    fun `withdraw more than balance throws exception`() {
        val account = BankAccount(AccountId.generate(), BigDecimal("500"))

        val exception = assertThrows(InvalidBalanceException::class.java) {
            account.withdraw(BigDecimal("600"))
        }
        assertEquals("Insufficient funds.", exception.message)
    }

    @Test
    fun `deposit negative amount throws exception`() {
        val account = BankAccount(AccountId.generate(), BigDecimal("1000"))

        val exception = assertThrows(InvalidAmountException::class.java) {
            account.deposit(BigDecimal("-100"))
        }
        assertEquals("Deposit amount must be positive.", exception.message)
    }
}
