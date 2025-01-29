package dagmendez.c

import scala.compiletime.codeOf
import scala.compiletime.error
import scala.util.control.NoStackTrace

object Advanced:

  /*
   * 1. Opaque Tyoes
   */

  opaque type Name    = String
  opaque type IBAN    = String // International Bank Account Number
  opaque type Balance = Int

  /*
   * Validations on the apply method are restricted to those that can be evaluated at compile time.
   * Most of the API of the basic types (String, Int, etc.) are of no use here.
   * Notice that the validations are different in the apply method and the from method.
   * Both validations should be equal and defined in a single places.
   * `scala.compiletime.codeOf` returns the value of the parameter passed into the inlined method apply
   * `scala.compiletime.error` generates a custom compiler error.
   * */

  object Name:

    inline def apply(name: String): Name =
      inline if name == ""
      then error(codeOf(name) + " is invalid.")
      else name

    def either(fn: String): Either[InvalidName, Name] =
      // Here we can access the underlying type API because it is evaluated during runtime.
      if fn.isBlank | (fn.trim.length < fn.length)
      then Left(InvalidName(s"First name is invalid with value <$fn>."))
      else Right(fn)

  object IBAN:

    inline def apply(iban: String): IBAN =
      inline if iban == ""
      then error(codeOf(iban) + " in invalid.")
      else iban

    def either(iban: String): Either[InvalidIBAN, IBAN] =
      if iban.isBlank | iban.contains(" ")
      then Left(InvalidIBAN(s"First name is invalid with value <$iban>."))
      else Right(iban)

  object Balance:

    inline def apply(balance: Int): Balance =
      inline if balance > 1000000 | balance < -1000
      then error(codeOf(balance) + " in invalid.")
      else balance

    def either(balance: Int): Either[InvalidBalance, Balance] =
      if balance > 1000000 | balance < -1000
      then Left(InvalidBalance(s"First name is invalid with value <$balance>."))
      else Right(balance)

  /*
   * 2. Domain
   */

  // The account holder is the person who signs the contract for said account with the bank
  final case class AccountHolder(firstName: Name, middleName: Option[Name], lastName: Name, secondLastName: Option[Name])
  final case class Account(accountHolder: AccountHolder, iban: IBAN, balance: Balance)

  /*
   * 3. Errors
   */

  // We add two custom error classes to handle invalid values
  final case class InvalidName(message: String)    extends RuntimeException(message) with NoStackTrace
  final case class InvalidIBAN(message: String)    extends RuntimeException(message) with NoStackTrace
  final case class InvalidBalance(message: String) extends RuntimeException(message) with NoStackTrace

  @main def run(): Unit =

    object HappyApply:
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

    object UnhappyApply:
      private val firstName: Name = Name("John") // Comment this one an uncomment next line
      // private val firstName: Name = Name("") // Uncomment and won't compile
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

    object HappyFrom:
      private val firstName: Either[InvalidName, Name]     = Name.either("John")
      private val middleName: Either[InvalidName, Name]    = Name.either("Stuart")
      private val lastName: Either[InvalidName, Name]      = Name.either("Mill")
      private val iban: Either[InvalidIBAN, IBAN]          = IBAN.either("GB33BUKB20201555555555")
      private val balance: Either[InvalidBalance, Balance] = Balance.either(0)

      private val account: Either[RuntimeException & NoStackTrace, Account] =
        for
          fn <- firstName
          mn <- middleName
          ln <- lastName
          ib <- iban
          bl <- balance
        yield Account(AccountHolder(fn, Some(mn), ln, secondLastName = None), ib, bl)

      assert(account.isRight)

      def print(): Unit = println(account)

    object UnhappyFrom:

      // Play with any field that would crash the validation and return Left
      private val firstName: Either[InvalidName, Name]     = Name.either("John")
      private val middleName: Either[InvalidName, Name]    = Name.either("Stuart ") // This returns Left.
      private val lastName: Either[InvalidName, Name]      = Name.either("Mill")
      private val iban: Either[InvalidIBAN, IBAN]          = IBAN.either("GB33BUKB20201555555555")
      private val balance: Either[InvalidBalance, Balance] = Balance.either(-5000)  // This returns Left.

      private val account: Either[RuntimeException & NoStackTrace, Account] =
        for
          fn <- firstName
          mn <- middleName
          ln <- lastName
          ib <- iban
          bl <- balance
        yield Account(AccountHolder(fn, Some(mn), ln, secondLastName = None), ib, bl)

      assert(account.isLeft)

      def print(): Unit = println(account)

    HappyApply.print()   // Compiles
    UnhappyApply.print() // Won't compile
    HappyFrom.print()    // Right
    UnhappyFrom.print()  // Left
