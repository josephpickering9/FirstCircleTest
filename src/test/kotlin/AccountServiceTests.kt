import database.InMemoryDatabase
import exceptions.NotFoundException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import models.AccountId
import models.BankAccount
import utils.ResultHelper.Companion.expectFailure
import utils.ResultHelper.Companion.expectSuccess
import utils.ResultHelper.Companion.failure
import utils.ResultHelper.Companion.success
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
        accountService = AccountService(database)
    }

    @Test
    fun `createAccount and check balance`() {
        val accountId = setupAccount(BigDecimal("1000")).accountId

        val balance = accountService.checkBalance(accountId).expectSuccess()

        assertEquals(BigDecimal("1000"), balance)
    }

    @Test
    fun `deposit increases account balance`() {
        val account = setupAccount(BigDecimal("1000"))
        val accountId = account.accountId

        accountService.deposit(accountId, BigDecimal("500")).expectSuccess()

        val balance = accountService.checkBalance(accountId).expectSuccess()
        assertEquals(BigDecimal("1500"), balance)
    }

    @Test
    fun `deposit with invalid account fails`() {
        val accountId = setupInvalidAccount()

        val exception = accountService.deposit(accountId, BigDecimal("100")).expectFailure()

        assertEquals("Account with account ID $accountId does not exist.", exception.message)
    }

    @Test
    fun `withdraw decreases account balance`() {
        val accountId = setupAccount(BigDecimal("1000")).accountId

        accountService.withdraw(accountId, BigDecimal("500")).expectSuccess()

        val balance = accountService.checkBalance(accountId).expectSuccess()
        assertEquals(BigDecimal("500"), balance)
    }

    @Test
    fun `withdraw from invalid account fails`() {
        val accountId = setupInvalidAccount()

        val exception = accountService.withdraw(accountId, BigDecimal("100")).expectFailure()

        assertEquals("Account with account ID $accountId does not exist.", exception.message)
    }

    @Test
    fun `transfer moves funds between accounts`() {
        val fromAccountId = setupAccount(BigDecimal("1000")).accountId
        val toAccountId = setupAccount(BigDecimal("500")).accountId

        accountService.transfer(fromAccountId, toAccountId, BigDecimal("500")).expectSuccess()

        val fromBalance = accountService.checkBalance(fromAccountId).expectSuccess()
        assertEquals(BigDecimal("500"), fromBalance)

        val toBalance = accountService.checkBalance(toAccountId).expectSuccess()
        assertEquals(BigDecimal("1000"), toBalance)
    }

    @Test
    fun `transfer with the same account fails`() {
        val accountId = setupAccount(BigDecimal("1000")).accountId

        val exception = accountService.transfer(accountId, accountId, BigDecimal("100")).expectFailure()

        assertEquals("Cannot transfer to the same account.", exception.message)
    }

    @Test
    fun `transfer with invalid from account fails`() {
        val fromAccountId = setupInvalidAccount()
        val toAccountId = setupAccount(BigDecimal("1000")).accountId

        val exception = accountService.transfer(fromAccountId, toAccountId, BigDecimal("100")).expectFailure()

        assertEquals("Account with account ID $fromAccountId does not exist.", exception.message)
    }

    @Test
    fun `transfer with invalid to account fails`() {
        val fromAccountId = setupAccount(BigDecimal("1000")).accountId
        val toAccountId = setupInvalidAccount()

        val exception = accountService.transfer(fromAccountId, toAccountId, BigDecimal("100")).expectFailure()

        assertEquals("Account with account ID $toAccountId does not exist.", exception.message)
    }

    @Test
    fun `transfer with failed withdrawal ensures balances are kept the same`() {
        val fromAccount = setupMockAccount(BigDecimal("50"))
        val toAccount = setupAccount(BigDecimal("500"))
        every { fromAccount.withdraw(any()) } returns failure(NotFoundException("Insufficient funds."))

        val exception = accountService.transfer(fromAccount.accountId, toAccount.accountId, BigDecimal("100")).expectFailure()

        assertEquals(BigDecimal("50"), fromAccount.getBalance())
        assertEquals(BigDecimal("500"), toAccount.getBalance())
        assertEquals("Insufficient funds.", exception.message)
    }

    @Test
    fun `transfer with failed deposit ensures balances are kept the same`() {
        val fromAccount = setupAccount(BigDecimal("500"))
        val toAccount = setupMockAccount(BigDecimal("500"))
        every { toAccount.deposit(any()) } returns failure(NotFoundException("Insufficient funds."))

        val exception = accountService.transfer(fromAccount.accountId, toAccount.accountId, BigDecimal("100")).expectFailure()

        assertEquals(BigDecimal("500"), fromAccount.getBalance())
        assertEquals(BigDecimal("500"), toAccount.getBalance())
        assertEquals("Insufficient funds.", exception.message)
    }

    private fun setupAccount(initialDeposit: BigDecimal): BankAccount {
        val accountId = AccountId()
        every { database.addAccount(any()) } returns success(BankAccount())

        val createdAccount = accountService.createAccount(initialDeposit).expectSuccess()
        createdAccount.accountId = accountId
        every { database.getAccount(accountId) } returns success(createdAccount)
        return createdAccount
    }

    private fun setupInvalidAccount(): AccountId {
        val accountId = AccountId()
        every { database.getAccount(accountId) } returns failure(NotFoundException("Account with account ID $accountId does not exist."))
        return accountId
    }

    private fun setupMockAccount(initialDeposit: BigDecimal? = null): BankAccount {
        val accountId = AccountId()
        val mockAccount = mockk<BankAccount>()
        every { mockAccount.accountId } returns accountId
        every { database.addAccount(match { it.accountId == accountId }) } returns success(mockAccount)
        every { database.getAccount(accountId) } returns success(mockAccount)
        every { mockAccount.deposit(any()) } returns success(Unit)
        every { mockAccount.withdraw(any()) } returns success(Unit)
        if (initialDeposit != null) every { mockAccount.getBalance() } returns initialDeposit
        return mockAccount
    }
}
