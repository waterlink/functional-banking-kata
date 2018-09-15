package banking

import arrow.core.right
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock

class AccountTest {

    private val bankService = mock(BankService::class.java)
    private val account = Account(bankService, id = "MYACCOUNT")

    @Test
    fun `deposit - happy path`() {
        // setup mock for bankService here
        val accountStatus = AccountStatus("MYACCOUNT", balanceInCents = 455001)
        val transaction = Transaction("MYACCOUNT", cents = 23000)
        given(bankService.makeTransaction(transaction)).willReturn(accountStatus)

        // make a call to account
        val result = account.deposit(23000)

        // verify the result is correct
        assertThat(result.isRight(), equalTo(true))
        assertThat(result.toOption().get(), equalTo(accountStatus))
    }

    @Test
    fun `deposit - when no connection to bank`() {
        // setup mock for bankService here
        val transaction = Transaction("MYACCOUNT", cents = 23000)
        val connectionException = BankService.ConnectionException()
        given(bankService.makeTransaction(transaction))
                .willThrow(connectionException)

        // make a call to account
        val result = account.deposit(23000)

        // verify the result is correct
        assertThat(result.isLeft(), equalTo(true))

        result.mapLeft { error ->
            assertThat(error, equalTo(Account.DepositError.CONNECTION_ERROR))
        }
    }


}