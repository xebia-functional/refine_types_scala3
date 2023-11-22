package dagmendez.workshop.d

import scala.compiletime.*
import scala.compiletime.ops.any.*
import scala.compiletime.ops.string.*
import scala.util.{Failure, Success, Try}

object FromApi:

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

    enum IBANerror(val message: String):
      case InvalidLength(val value: String)  extends IBANerror(value + invalidLengthErrorMessage)
      case InvalidCountry(val value: String) extends IBANerror(value + invalidCountryCodeErrorMessage)
      case InvalidNumber(val value: String)  extends IBANerror(value + invalidDigitsErrorMessage)

    def from(iban: String): IBAN | IBANerror =
      if iban.length != lengthValidation
      then IBANerror.InvalidLength(iban)
      else if iban.substring(0, 2) != countryCodeValidation
      then IBANerror.InvalidCountry(iban)
      else if !iban.substring(2, 25).matches(digitValidation)
      then IBANerror.InvalidNumber(iban)
      else iban

    /**
     * Implement a `from` API:
     *   - def fromE: Either[IBANerror, IBAN]
     *   - def fromOpt: Option[IBAN]
     * -def fromT: Try[IBAN]
     */

    def fromE(iban: String): Either[IBANerror, IBAN] = ???

    def fromOpt(iban: String): Option[IBAN] = ???

    def fromT(iban: String): Try[IBAN] = ???

  end IBAN

  @main def run(): Unit =
    Seq(
      "\n def from()",
      IBAN.from("ES123456789012345678901234"), // IBAN
      IBAN.from("ES1234567890123456789"),      // InvalidLength
      IBAN.from("PT123456789012345678901234"), // InvalidCountry
      IBAN.from("ES1234567890123456789012ES"), // InvalidNumber
      "\n def fromE()",
      IBAN.fromE("ES123456789012345678901234"), // Right(IBAN)
      IBAN.fromE("ES1234567890123456789"),      // Left(InvalidLength)
      IBAN.fromE("PT123456789012345678901234"), // Left(InvalidCountry)
      IBAN.fromE("ES1234567890123456789012ES"), // Left(InvalidNumber)
      "\n def fromOpt()",
      IBAN.fromOpt("ES123456789012345678901234"), // Some(IBAN)
      IBAN.fromOpt("ES1234567890123456789"),      // None
      IBAN.fromOpt("PT123456789012345678901234"), // None
      IBAN.fromOpt("ES1234567890123456789012ES"), // None
      "\n def fromT()",
      IBAN.fromT("ES123456789012345678901234"), // Success
      IBAN.fromT("ES1234567890123456789"),      // Failure
      IBAN.fromT("PT123456789012345678901234"), // Failure
      IBAN.fromT("ES1234567890123456789012ES")  // Failure
    ).foreach(println)
