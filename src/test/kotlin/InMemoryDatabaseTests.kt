import models.BankAccount
import database.InMemoryDatabase
import exceptions.NotFoundException
import models.AccountId
import utils.ResultHelper.Companion.expectFailure
import utils.ResultHelper.Companion.expectSuccess
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
    fun `addAccount stores account correctly and reflects initial deposit`() {
        val depositAmount = BigDecimal("1000")

        val account = database.addAccount().expectSuccess()
        account.deposit(depositAmount).expectSuccess()

        val retrievedAccount = database.getAccount(account.accountId).expectSuccess()
        assertEquals(depositAmount, retrievedAccount.getBalance())
    }

    @Test
    fun `getAccount with invalid accountId fails`() {
        val invalidAccountNumber = AccountId()

        val exception = database.getAccount(invalidAccountNumber).expectFailure()

        assertEquals("Account with account ID $invalidAccountNumber does not exist.", exception.message)
    }
}
