package dagmendez.advanced

import scala.util.control.NoStackTrace

import dagmendez.advanced.domain.*
import dagmendez.advanced.error.*
import dagmendez.advanced.opaqueTypes.*

object Main:

  private object HappyApply:
    private val firstName: Name  = Name("John")
    private val middleName: Name = Name("Stuart")
    private val lastName: Name   = Name("Mill")
    private val iban: IBAN       = IBAN("GB33BUKB20201555555555")
    private val balance: Balance = Balance(-300)

    private val account: Account =
      Account(
        AccountHolder(
          firstName,
          Some(middleName),
          lastName,
          secondLastName = None
        ),
        iban,
        balance
      )

    def print(): Unit = println(account)

  private object UnhappyApply:
    private val firstName: Name = Name("John") // Comment this one an uncomment next line
    // private val firstName: FirstName = FirstName("") // Uncomment and won't compile
    private val middleName: Name = Name("Stuart")
    private val lastName: Name   = Name("Mill")
    private val iban: IBAN       = IBAN("GB33BUKB20201555555555")
    private val balance: Balance = Balance(-1000)

    private val account: Account =
      Account(
        AccountHolder(
          firstName,
          Some(middleName),
          lastName,
          secondLastName = None
        ),
        iban,
        balance
      )

    def print(): Unit = println(account)

  private object HappyFrom:
    private val firstName: Either[InvalidName, Name]     = Name.from("John")
    private val middleName: Either[InvalidName, Name]    = Name.from("Stuart")
    private val lastName: Either[InvalidName, Name]      = Name.from("Mill")
    private val iban: Either[InvalidIBAN, IBAN]          = IBAN.from("GB33BUKB20201555555555")
    private val balance: Either[InvalidBalance, Balance] = Balance.from(0)

    private val account: Either[RuntimeException with NoStackTrace, Account] =
      for
        fn <- firstName
        mn <- middleName
        ln <- lastName
        ib <- iban
        bl <- balance
      yield Account(AccountHolder(fn, Some(mn), ln, secondLastName = None), ib, bl)

    assert(account.isRight)
    def print(): Unit = println(account)

  private object UnhappyFrom:

    // Play with any field that would crash the validation and return Left
    private val firstName: Either[InvalidName, Name]     = Name.from("John")
    private val middleName: Either[InvalidName, Name]    = Name.from("Stuart ") // This returns Left.
    private val lastName: Either[InvalidName, Name]      = Name.from("Mill")
    private val iban: Either[InvalidIBAN, IBAN]          = IBAN.from("GB33BUKB20201555555555")
    private val balance: Either[InvalidBalance, Balance] = Balance.from(-5000)  // This returns Left.

    private val account: Either[RuntimeException with NoStackTrace, Account] =
      for
        fn <- firstName
        mn <- middleName
        ln <- lastName
        ib <- iban
        bl <- balance
      yield Account(AccountHolder(fn, Some(mn), ln, secondLastName = None), ib, bl)

    assert(account.isLeft)
    def print(): Unit = println(account)

  @main def run(): Unit = {
    HappyApply.print()   // Compiles
    UnhappyApply.print() // Won't compile
    HappyFrom.print()    // Right
    UnhappyFrom.print()  // Left
  }
