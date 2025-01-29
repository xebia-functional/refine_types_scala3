package dagmendez.workshop.c

import scala.compiletime.codeOf
import scala.compiletime.constValue
import scala.compiletime.error
import scala.compiletime.ops.any.!=
import scala.compiletime.ops.string.Length
import scala.compiletime.ops.string.Matches
import scala.compiletime.ops.string.Substring

/**
 * Each requirement will have its own error type.
 */
object CustomErrors:

  opaque type IBAN = String

  object IBAN:

    // Task 1 is solved! ^_^

    inline val lengthValidation          = 26
    inline val invalidLengthErrorMessage = " is invalid. Length has to be 26."

    inline val countryCodeValidation          = "ES"
    inline val invalidCountryCodeErrorMessage = " is invalid. Country code must be <ES>."

    inline val digitValidation           = "^\\d*$"
    inline val invalidDigitsErrorMessage = " is invalid. Only numbers are accepted after the country code."

    inline def apply(iban: String): IBAN =
      inline if constValue[Length[iban.type] != lengthValidation.type]
      then error(codeOf(iban) + invalidLengthErrorMessage)
      else if constValue[Substring[iban.type, 0, 2] != countryCodeValidation.type]
      then error(codeOf(iban) + invalidCountryCodeErrorMessage)
      else if !constValue[Matches[Substring[iban.type, 2, 25], digitValidation.type]]
      then error(codeOf(iban) + invalidDigitsErrorMessage)
      else iban

    // Task 2

    // Solution task 2 - custom error
    /*
    enum IBANerror(val message: String):
      case InvalidLength(val value: String) extends IBANerror(value + invalidLengthErrorMessage)
      case InvalidCountry(val value: String) extends IBANerror(value + invalidCountryCodeErrorMessage)
      case InvalidDigit(val value: String) extends IBANerror(value + invalidDigitsErrorMessage)
     */

    def either[Error](iban: String): Either[Error, IBAN] =
      if iban.length != lengthValidation
      then Left(???)
      else if iban.substring(0, 2) != countryCodeValidation
      then Left(???)
      else if !iban.substring(2, 25).matches(digitValidation)
      then Left(???)
      else Right(iban)

    // Solution task 2 - def from
    /*
    def either(iban: String): Either[IBANerror, IBAN] =
      if iban.length != lengthValidation
      then Left(IBANerror.InvalidLength(iban))
      else if iban.substring(0, 2) != countryCodeValidation
      then Left(IBANerror.InvalidCountry(iban))
      else if !iban.substring(2, 25).matches(digitValidation)
      then Left(IBANerror.InvalidDigit(iban))
      else Right(iban)
     */

  end IBAN

  @main def run(): Unit =
    Seq(
      // IBAN.either("ES123456789012345678901234"), // Valid IBAN
      // IBAN.either("ES1234567890123456789"),      // InvalidLength
      // IBAN.either("ES1234567890123456789") match
      //  case Left(value) => value.message
      //  case Right(value) => value
      // ,      // InvalidLength error message
      // IBAN.either("PT123456789012345678901234"), // InvalidCountry
      // IBAN.either("PT123456789012345678901234") match
      //  case Left(value) => value.message
      //  case Right(value) => value
      // , // InvalidCountry error message
      // IBAN.either("ES1234567890123456789012ES"),  // InvalidNumber
      // IBAN.either("ES1234567890123456789012ES") match
      //  case Left(value) => value.message
      //  case Right(value) => value
      // ,  // InvalidNumber error message
      IBAN("ES123456789012345678901234") // ,
      // IBAN("ES1234567890123456789"),
      // IBAN("PT123456789012345678901234"),
      // IBAN("ES1234567890123456789012ES"),
    ).foreach(println)

  // Task 1
  /**
   * Can we replace the if/else/then with a match?
   */

  // Task 2
  /**
   * Can we write our own API for safe constructors?
   */

end CustomErrors
