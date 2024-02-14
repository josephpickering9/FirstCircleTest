import database.InMemoryDatabase
import exceptions.NotFoundException
import io.mockk.every
import io.mockk.mockk
import models.AccountId
import models.BankAccount
import org.example.utils.ResultHelper.Companion.expectFailure
import org.example.utils.ResultHelper.Companion.expectSuccess
import org.example.utils.ResultHelper.Companion.failure
import org.example.utils.ResultHelper.Companion.success
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import services.AccountService
import java.math.BigDecimal

class AccountServiceTests {

    private lateinit var database: InMemoryDatabase
    private lateinit var accountService: AccountService

    @BeforeEach
    fun setup() {
        database = mockk<InMemoryDatabase>()
        every { database.addAccount(any()) } returns success(Unit)

        accountService = AccountService(database)
    }

    @Test
    fun `createAccount and check balance`() {
        val accountId = setupAccount(BigDecimal("1000"))

        val balance = accountService.checkBalance(accountId).expectSuccess()

        assertEquals(BigDecimal("1000"), balance)
    }

    @Test
    fun `deposit increases account balance`() {
        val accountId = setupAccount(BigDecimal("1000"))

        accountService.deposit(accountId, BigDecimal("500")).expectSuccess()

        val balance = accountService.checkBalance(accountId).expectSuccess()
        assertEquals(BigDecimal("1500"), balance)
    }

    @Test
    fun `deposit with invalid account throws exception`() {
        val accountId = setupInvalidAccount()

        val exception = accountService.deposit(accountId, BigDecimal("100")).expectFailure()

        assertEquals("Account with account ID $accountId does not exist.", exception.message)
    }

    @Test
    fun `withdraw decreases account balance`() {
        val accountId = setupAccount(BigDecimal("1000"))

        accountService.withdraw(accountId, BigDecimal("500")).expectSuccess()

        val balance = accountService.checkBalance(accountId).expectSuccess()
        assertEquals(BigDecimal("500"), balance)
    }

    @Test
    fun `withdraw from invalid account throws exception`() {
        val accountId = setupInvalidAccount()

        val exception = accountService.withdraw(accountId, BigDecimal("100")).expectFailure()

        assertEquals("Account with account ID $accountId does not exist.", exception.message)
    }

    @Test
    fun `transfer moves funds between accounts`() {
        val fromAccountId = setupAccount(BigDecimal("1000"))
        val toAccountId = setupAccount(BigDecimal("500"))

        accountService.transfer(fromAccountId, toAccountId, BigDecimal("500")).expectSuccess()

        val fromBalance = accountService.checkBalance(fromAccountId).expectSuccess()
        assertEquals(BigDecimal("500"), fromBalance)

        val toBalance = accountService.checkBalance(toAccountId).expectSuccess()
        assertEquals(BigDecimal("1000"), toBalance)
    }

    @Test
    fun `transfer with invalid from account throws exception`() {
        val fromAccountId = setupInvalidAccount()
        val toAccountId = setupAccount(BigDecimal("1000"))

        val exception = accountService.transfer(fromAccountId, toAccountId, BigDecimal("100")).expectFailure()

        assertEquals("Account with account number $fromAccountId does not exist.", exception.message)
    }

    @Test
    fun `transfer with invalid to account throws exception`() {
        val fromAccountId = setupAccount(BigDecimal("1000"))
        val toAccountId = setupInvalidAccount()

        val exception = accountService.transfer(fromAccountId, toAccountId, BigDecimal("100")).expectFailure()

        assertEquals("Account with account number $toAccountId does not exist.", exception.message)
    }

    private fun setupAccount(initialDeposit: BigDecimal): AccountId {
        val accountId = AccountId()
        accountService.createAccount(accountId, initialDeposit).expectSuccess()
        every { database.getAccount(accountId) } returns success(BankAccount(accountId, initialDeposit))
        return accountId
    }

    private fun setupInvalidAccount(): AccountId {
        val accountId = AccountId()
        every { database.getAccount(accountId) } returns failure(NotFoundException("Account with account number $accountId does not exist."))
        return accountId
    }
}
