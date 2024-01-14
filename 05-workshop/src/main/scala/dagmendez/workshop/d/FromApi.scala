package dagmendez.workshop.d

import scala.compiletime.*
import scala.compiletime.ops.any.*
import scala.compiletime.ops.string.*

object FromApi:

  opaque type IBAN = String

  object IBAN:

    inline val lengthValidation          = 26
    inline val invalidLengthErrorMessage = " is invalid. Length has to be 26."

    inline val countryCodeValidation          = "ES"
    inline val invalidCountryCodeErrorMessage = " is invalid. Country code must be <ES>."

    inline val digitValidation           = "^\\d*$"
    inline val invalidDigitsErrorMessage = " is invalid. Only numbers are accepted after the country code."

    // Task 1 - replace if/then/else with match
    inline def apply(iban: String): IBAN =
      inline if constValue[Length[iban.type] != lengthValidation.type]
      then error(codeOf(iban) + invalidLengthErrorMessage)
      else if constValue[Substring[iban.type, 0, 2] != countryCodeValidation.type]
      then error(codeOf(iban) + invalidCountryCodeErrorMessage)
      else if !constValue[Matches[Substring[iban.type, 2, 25], digitValidation.type]]
      then error(codeOf(iban) + invalidDigitsErrorMessage)
      else iban

    // Solution Task 1 - apply match
    /*
    inline def apply(iban: String): IBAN =
      inline iban match
        case length if constValue[Length[length.type] != lengthValidation.type] => error(codeOf(iban) + invalidLengthErrorMessage)
        case country if constValue[Substring[country.type, 0, 2] != countryCodeValidation.type] => error(codeOf(iban) + invalidCountryCodeErrorMessage)
        case digit if !constValue[Matches[Substring[digit.type, 2, 25], digitValidation.type]] => error(codeOf(iban) + invalidDigitsErrorMessage)
        case _ => iban
     */

    enum IBANerror(val message: String):
      case InvalidLength(value: String)  extends IBANerror(value + invalidLengthErrorMessage)
      case InvalidCountry(value: String) extends IBANerror(value + invalidCountryCodeErrorMessage)
      case InvalidNumber(value: String)  extends IBANerror(value + invalidDigitsErrorMessage)

    def from(iban: String): IBAN | IBANerror = iban match
      case length if iban.length != lengthValidation                => IBANerror.InvalidLength(length)
      case country if iban.substring(0, 2) != countryCodeValidation => IBANerror.InvalidCountry(country)
      case digit if !iban.substring(2, 25).matches(digitValidation) => IBANerror.InvalidNumber(digit)
      case validIban                                                => validIban

    /* Task 2
     * Implement a `from` API:
     *   - def either: Either[String, IBAN] //compatible with various codec libraries
     *   - def option: Option[IBAN]
     */

    def either(iban: String): Either[String, IBAN] = ???

    // Solution fromE
    /*
    def either(iban: String): Either[String, IBAN] = from(iban) match
      case validIban: IBAN => Right(validIban)
      case invalidIban: IBANerror => Left(invalidIban.message)
     */

    def option(iban: String): Option[IBAN] = ???

    // Solution fromOpt
    /*
    def option(iban: String): Option[IBAN] = from(iban) match
      case validIban: IBAN => Some(validIban)
      case _: IBANerror => None
     */

  end IBAN

  @main def run(): Unit =
    Seq(
      "\n def from()",
      IBAN.from("ES123456789012345678901234"), // IBAN
      IBAN.from("ES1234567890123456789"),      // InvalidLength
      IBAN.from("PT123456789012345678901234"), // InvalidCountry
      IBAN.from("ES1234567890123456789012ES")  // InvalidNumber
      // "\n def either()",
      // IBAN.either("ES123456789012345678901234"), // Right(IBAN)
      // IBAN.either("ES1234567890123456789"),      // Left(InvalidLength)
      // IBAN.either("PT123456789012345678901234"), // Left(InvalidCountry)
      // IBAN.either("ES1234567890123456789012ES"), // Left(InvalidNumber)
      // "\n def option()",
      // IBAN.option("ES123456789012345678901234"), // Some(IBAN)
      // IBAN.option("ES1234567890123456789"),      // None
      // IBAN.option("PT123456789012345678901234"), // None
      // IBAN.option("ES1234567890123456789012ES"), // None
    ).foreach(println)
