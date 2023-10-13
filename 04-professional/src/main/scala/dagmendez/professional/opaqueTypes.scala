package dagmendez.professional

import scala.compiletime.ops.boolean.&&
import scala.compiletime.ops.double.{<, >}
import scala.compiletime.ops.string.Matches
import scala.compiletime.{codeOf, constValue, error}

import dagmendez.professional.error.*

object opaqueTypes:

  opaque type Name    = String
  opaque type IBAN    = String
  opaque type Balance = Double

  /*
   * We explore the power of inlining and the complementary methods of the package `scala.compiletime.ops`
   *
   * */

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

    def from(fn: String): Either[InvalidName, Name] =
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
    private inline val validation   = """^ES\d{24}$"""
    private inline val errorMessage = " is invalid. It must: \n - start with the country code ES.\n - follow the country code with 24 numbers."

    inline def apply(iban: String): IBAN =
      inline if constValue[Matches[iban.type, validation.type]]
      then iban
      else error(codeOf(iban) + errorMessage)

    def from(iban: String): Either[InvalidIBAN, IBAN] =
      if validation.r.matches(iban)
      then Right(iban)
      else Left(InvalidIBAN(iban + errorMessage))

  object Balance:

    private inline val errorMessage = " is invalid. Balance should be greater than -1,000.00 and smaller than 1,000,000.00"

    inline def apply(balance: Double): Balance =
      inline if constValue[balance.type > -1000.0 && balance.type < 1000000.0]
      then balance
      else error(codeOf(balance) + errorMessage)

    def from(balance: Double): Either[InvalidBalance, Balance] =
      if balance > -1000.0 && balance < 1000000.0
      then Right(balance)
      else Left(InvalidBalance(balance + errorMessage))
