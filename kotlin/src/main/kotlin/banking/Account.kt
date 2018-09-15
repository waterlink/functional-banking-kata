package banking

import arrow.core.Either
import arrow.core.Option
import arrow.core.Try

open class Account(private val bankService: BankService,
                   private val id: String) {

    open fun deposit(cents: Int): Either<DepositError, AccountStatus> {
        val transaction = Transaction(accountId = id, cents = cents)

        val toEither: Either<Throwable, AccountStatus> = Try { bankService.makeTransaction(transaction) }
                .toEither()
        val mapLeft: Either<Account.DepositError, AccountStatus> = toEither
                .mapLeft { exception ->
                    when (exception) {
                        is BankService.ConnectionException -> DepositError.CONNECTION_ERROR
                        else -> TODO("should not be reached yet")
                    }
                }
        return mapLeft

//        val accountStatus = bankService.makeTransaction(transaction)
//        return Either.right(accountStatus)
    }

    // exhaustive

    enum class DepositError {
        CONNECTION_ERROR,
        ACCOUNT_NOT_FOUND,
        ACCOUNT_BLOCKED
    }

    open fun withdraw(cents: Int): Either<WithdrawError, AccountStatus> {
        TODO("not implemented yet")
    }

    enum class WithdrawError {
        CONNECTION_ERROR,
        ACCOUNT_NOT_FOUND,
        ACCOUNT_BLOCKED,
        NOT_ENOUGH_FUNDS
    }

    // Either<L, R> would have worked a bit better here
    // but we want to learn Option<A> as well, so… :)
    open fun createStatement(): Option<BankStatement> {
        TODO("not implemented yet")
    }

}