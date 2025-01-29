package dagmendez.d

import scala.compiletime.codeOf
import scala.compiletime.constValue
import scala.compiletime.error
import scala.compiletime.ops.any.==
import scala.compiletime.ops.boolean.&&
import scala.compiletime.ops.string.Length
import scala.compiletime.ops.string.Matches
import scala.compiletime.ops.string.Substring
import scala.util.control.NoStackTrace

object Magic:

  /*
   * 1. Opaque Types
   */

  opaque type Name    = String
  opaque type IBAN    = String
  opaque type Balance = Int

  object Name:

    /**
     * Explanation:
     *
     * `^` :Asserts the start of the string. [A-Z]: Matches an uppercase letter at the beginning of the string. [a-zA-Z]*: Matches zero or more
     * letters (uppercase or lowercase) after the first letter. (?:\s[A-Z][a-zA-Z]*)*: Allows for zero or more occurrences of a space followed by an
     * uppercase letter and zero or more lowercase/uppercase letters. $: Asserts the end of the string.
     */
    private inline val validation   = """^[A-Z][a-zA-Z]*(?:\s[A-Z][a-zA-Z]*)*$"""
    private inline val errorMessage = " is invalid. It must: \n - be trimmed.\n - start with upper case.\n - follow upper case with lower case."

    inline def apply(fn: String): Name =
      inline if constValue[Matches[fn.type, validation.type]]
      then fn
      else error(codeOf(fn) + errorMessage)

    def either(fn: String): Either[InvalidName, Name] =
      if validation.r.matches(fn)
      then Right(fn)
      else Left(InvalidName(fn + errorMessage))

  object IBAN:

    /**
     * This is a naive validation of the IBAN code. We only validate IBAN valid in Spain. In Spain the IBAN has 24 characters, comprising your full
     * account number preceded by 4 other characters. The first two are the country code (ES) followed by two check digits. The full algorithm to
     * validate IBAN codes can be found here:
     *   - [[https://www.tbg5-finance.org/?ibandocs.shtml]].
     */
    private inline val errorMessage = " is invalid. It must: \n - start with the country code ES.\n - follow the country code with 24 numbers."

    inline def apply(iban: String): IBAN =
      inline if constValue[
          Substring[iban.type, 0, 2] == "ES" && Length[iban.type] == 26 && Matches[Substring[iban.type, 2, 25], "^\\d*$"]
        ]
      then iban
      else error(codeOf(iban) + errorMessage)

    def either(iban: String): Either[InvalidIBAN, IBAN] =
      if iban.substring(0, 2) == "ES"
        && iban.length == 26
        && iban.substring(2, 25).matches("^\\d*$")
      then Right(iban)
      else Left(InvalidIBAN(iban + errorMessage))

  object Balance:

    private inline def validation(balance: Int): Boolean = balance >= -1000 && balance <= 1000000

    private inline val errorMessage = " is invalid. Balance should be equal or greater than -1,000 and equal or smaller than 1,000,000"

    inline def apply(balance: Int): Balance =
      inline if validation(balance)
      then balance
      else error(codeOf(balance) + errorMessage)

    def either(balance: Int): Either[InvalidBalance, Balance] =
      if validation(balance)
      then Right(balance)
      else Left(InvalidBalance(balance + errorMessage))

  /*
   * 2. Domain
   */

  final case class AccountHolder(firstName: Name, middleName: Option[Name], lastName: Name, secondLastName: Option[Name])
  final case class Account(accountHolder: AccountHolder, iban: IBAN, balance: Balance)

  /*
   * 3. Errors
   */

  final case class InvalidName(message: String)    extends RuntimeException(message) with NoStackTrace
  final case class InvalidIBAN(message: String)    extends RuntimeException(message) with NoStackTrace
  final case class InvalidBalance(message: String) extends RuntimeException(message) with NoStackTrace

  @main def run(): Unit =

    object HappyApply:
      private val firstName: Name  = Name("John")
      private val middleName: Name = Name("Stuart T")
      private val lastName: Name   = Name("Mill")
      private val iban: IBAN       = IBAN("ES451234567890123456789012")
      private val balance: Balance = Balance(123)

      private val account: Account = Account(
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

    // If you uncomment the following block, you will see that multiple line won't compile
    /*
    object UnhappyApply:
      val firstName: Name = Name("")
      val middleName: Name = Name("Maria")
      val lastName: Name   = Name("Garcia")
      val iban: IBAN = IBAN("ES4512345678901234567890")
      val balance: Balance = Balance(-3000.0)
      val balance: Balance = Balance(-100)
     */

    object HappyFrom:
      private val firstName  = Name.either("Juan")
      private val middleName = Name.either("Manuel")
      private val lastName   = Name.either("Herrero")
      private val iban       = IBAN.either("ES451234567890123456789012")
      private val balance    = Balance.either(15)

      private val account: Either[RuntimeException & NoStackTrace, Account] =
        for
          fn <- firstName
          mn <- middleName
          ln <- lastName
          ib <- iban
          ba <- balance
        yield Account(AccountHolder(fn, Some(mn), ln, secondLastName = None), ib, ba)

      assert(account.isRight)

      def print(): Unit = println(account)

    object UnhappyFrom:
      // Play with any field that would crash the validation and return Left
      private val firstName      = Name.either("Juana")
      private val middleName     = Name.either("Manuela")
      private val lastName       = Name.either("Herrero")
      private val secondLastName = Name.either("Garcia")
      private val iban           = IBAN.either("ES451234567890123456789012")
      private val balance        = Balance.either(5000000) // Left

      private val account: Either[RuntimeException & NoStackTrace, Account] =
        for
          fn  <- firstName
          mn  <- middleName
          ln  <- lastName
          sln <- secondLastName
          ib  <- iban
          ba  <- balance
        yield Account(AccountHolder(fn, Some(mn), ln, Some(sln)), ib, ba)

      assert(account.isLeft)

      def print(): Unit = println(account)
    HappyApply.print()  // Compiles
    HappyFrom.print()   // Right
    UnhappyFrom.print() // Left
