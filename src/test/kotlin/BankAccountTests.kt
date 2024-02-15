import models.BankAccount
import utils.ResultHelper.Companion.expectFailure
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class BankAccountTests {

    private lateinit var account: BankAccount

    @BeforeEach
    fun setup() {
        account = BankAccount()
    }

    @Test
    fun `BankAccount creation with initial deposit`() {
        assertEquals(BigDecimal.ZERO, account.getBalance())
    }

    @Test
    fun `deposit positive amount increases balance`() {
        account.deposit(BigDecimal("500"))
        account.deposit(BigDecimal("300"))
        account.deposit(BigDecimal("200"))

        assertEquals(BigDecimal("1000"), account.getBalance())
    }

    @Test
    fun `withdraw positive amount decreases balance`() {
        account.deposit(BigDecimal("2000"))
        account.withdraw(BigDecimal("1000"))

        assertEquals(BigDecimal("1000"), account.getBalance())
    }

    @Test
    fun `withdraw more than balance fails`() {
        val exception = account.withdraw(BigDecimal("600")).expectFailure()

        assertEquals("Insufficient funds.", exception.message)
    }

    @Test
    fun `deposit negative amount fails`() {
        val exception = account.deposit(BigDecimal("-100")).expectFailure()

        assertEquals("Deposit amount must be positive.", exception.message)
    }
}
