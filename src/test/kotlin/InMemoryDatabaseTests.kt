import models.BankAccount
import database.InMemoryDatabase
import exceptions.NotFoundException
import models.AccountId
import org.example.utils.ResultHelper.Companion.expectFailure
import org.example.utils.ResultHelper.Companion.expectSuccess
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class InMemoryDatabaseTests {

    private lateinit var database: InMemoryDatabase

    @BeforeEach
    fun setup() {
        database = InMemoryDatabase()
    }

    @Test
    fun `addAccount stores account correctly`() {
        val accountId = AccountId()
        val account = BankAccount(accountId, BigDecimal("1000"))

        database.addAccount(account).expectSuccess()

        val retrievedAccount = database.getAccount(accountId).expectSuccess()
        assertNotNull(retrievedAccount)
        assertEquals(accountId, retrievedAccount.accountId)
        assertEquals(BigDecimal("1000"), retrievedAccount.getBalance())
    }

    @Test
    fun `addAccount with existing account throws exception`() {
        val accountId = AccountId()
        val account = BankAccount(accountId, BigDecimal("1000"))
        database.addAccount(account).expectSuccess()

        val exception = database.addAccount(account).expectFailure()

        assertEquals("Account with account ID $accountId already exists.", exception.message)
    }

    @Test
    fun `getAccount with invalid accountNumber throws exception`() {
        val invalidAccountNumber = AccountId()

        val exception = database.getAccount(invalidAccountNumber).expectFailure()

        assertEquals("Account with account ID $invalidAccountNumber does not exist.", exception.message)
    }
}
