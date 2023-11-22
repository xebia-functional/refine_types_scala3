package dagmendez.workshop.c

import scala.compiletime.*
import scala.compiletime.ops.any.*
import scala.compiletime.ops.string.*

/**
 * Each requirement will have its own error type.
 */
object CustomErrors:

  opaque type IBAN = String

  object IBAN:

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

    /*
    We need to define an ADT for the errors and change the signature of the `from` method
     */

    /*
    enum IBANerror(val message: String):
      case InvalidLength(val value: String) extends IBANerror(value + invalidLengthErrorMessage)
      case InvalidCountry(val value: String) extends IBANerror(value + invalidCountryCodeErrorMessage)
      case InvalidNumber(val value: String) extends IBANerror(value + invalidDigitsErrorMessage)
     */

    def from[Error >: String](iban: String): Either[Error, IBAN] =
      if iban.length != lengthValidation
      then Left(???)
      else if iban.substring(0, 2) != countryCodeValidation
      then Left(???)
      else if !iban.substring(2, 25).matches(digitValidation)
      then Left(???)
      else Right(iban)

  end IBAN

  @main def run(): Unit =
    Seq(
      IBAN.from("ES123456789012345678901234"), // Valid IBAN
      IBAN.from("ES1234567890123456789"),      // InvalidLength
      IBAN.from("PT123456789012345678901234"), // InvalidCountry
      IBAN.from("ES1234567890123456789012ES")  // InvalidNumber
      // IBAN("ES123456789012345678901234"),
      // IBAN("ES1234567890123456789"),
      // IBAN("PT123456789012345678901234"),
      // IBAN("ES1234567890123456789012ES"),
    ).foreach(println)
