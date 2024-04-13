package dagmendez.presentation

import scala.util.control.NoStackTrace

object E_VCwithErrorHandling:

  val controlChars = Set('T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E')

  case class FormatError(reason: String) extends Exception(reason) with NoStackTrace

  case class DniNumber(number: String)
  object DniNumber:
    def parse(number: String): Either[FormatError, DniNumber] =
      Either.cond(
        number.length == 8 && number.forall(_.isDigit),
        DniNumber(number),
        FormatError("El número ha de contener 8 carácteres numéricos")
      )

  case class DniControlChar(character: Char)
  object DniControlChar:
    def parse(character: Char): Either[FormatError, DniControlChar] =
      Either.cond(
        controlChars.contains(character),
        DniControlChar(character),
        FormatError("La letra de control es incorrecta")
      )

  case class DNI(numero: DniNumber, letra: DniControlChar):
    def value: String = s"${numero.number}-${letra.character}"

  val validDNI =
    for
      dniNumber <- DniNumber.parse("12345678")
      dniChar   <- DniControlChar.parse('A')
    yield DNI(dniNumber, dniChar)

  val invalidDNI =
    for
      dniNumber <- DniNumber.parse("0")
      dniChar   <- DniControlChar.parse('f')
    yield DNI(dniNumber, dniChar)

  @main def printEitherDNIs(): Unit =
    Vector(validDNI, invalidDNI)
      .map: either =>
        either.map: dni =>
          dni.value
      .foreach(println)

    // Equivalente
    Vector(validDNI, invalidDNI).map(_.map(_.value)).foreach(println)
