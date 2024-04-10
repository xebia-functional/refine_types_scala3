package dagmendez.workshop.b

import scala.compiletime.*
import scala.compiletime.ops.any.*
import scala.compiletime.ops.string.*

/**
 * IBAN: We will refine an IBAN number for an Spanish account. Requirements:
 *   - Length of 26
 *   - First two characters are "ES"
 *   - Remaining characters are digits
 */
object ApplyAndFrom:

  opaque type IBAN = String

  object IBAN:

    /**
     * The error messages have to be inlined so they can be used in the apply method.
     */
    inline val invalidLengthErrorMessage      = " is invalid. Length has to be 26."
    inline val invalidCountryCodeErrorMessage = " is invalid. Country code must be <ES>."
    inline val invalidDigitsErrorMessage      = " is invalid. Only numbers are accepted after the country code."

    def either(iban: String): Either[String, IBAN] =
      if iban.length != 26
      then Left(iban + invalidLengthErrorMessage)
      else if iban.substring(0, 2) != "ES"
      then Left(iban + invalidCountryCodeErrorMessage)
      else if !iban.substring(2, 25).matches("^\\d*$")
      then Left(iban + invalidDigitsErrorMessage)
      else Right(iban)

    inline def apply(iban: String): IBAN = ???

    // Solution for apply
    /*
      inline if constValue[Length[iban.type] != 26]
      then error(codeOf(iban) + invalidLengthErrorMessage)
      else if constValue[Substring[iban.type , 0, 2] != "ES"]
      then error(codeOf(iban) + invalidCountryCodeErrorMessage)
      else if !constValue[Matches[Substring[iban.type , 2, 25], "^\\d*$"]]
      then error(codeOf(iban) + invalidDigitsErrorMessage)
      else iban
     */

  end IBAN

  @main def run(): Unit =
    Seq(
      IBAN.either("ES123456789012345678901234"), // Valid IBAN
      IBAN.either("ES1234567890123456789"),      // InvalidLength
      IBAN.either("PT123456789012345678901234"), // InvalidCountry
      IBAN.either("ES1234567890123456789012ES")  // InvalidNumber
      // IBAN("ES123456789012345678901234"),
      // IBAN("ES1234567890123456789"),
      // IBAN("PT123456789012345678901234"),
      // IBAN("ES1234567890123456789012ES"),
    ).foreach(println)

  // Task 1
  /**
   * Can we unify the validation values?
   */

  // Task 2
  /**
   * Can we customize the error messages?
   */

end ApplyAndFrom
