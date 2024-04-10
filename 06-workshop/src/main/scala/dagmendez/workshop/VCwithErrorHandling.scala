package dagmendez.workshop

import scala.util.control.NoStackTrace

object VCwithErrorHandling extends App:
  
  val controlChars = Set('T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E')

  case class FormatError(reason: String) extends Exception(reason) with NoStackTrace

  case class DniNumber(number: Int)
  object DniNumber:
    def parse(number: Int): Either[FormatError, DniNumber] =
      Either.cond(
        number.toString.length == 8,
        DniNumber(number),
        FormatError("El número ha de contener 8 cifras")
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
      dniNumber <- DniNumber.parse(12345678)
      dniChar <- DniControlChar.parse('A')
    yield DNI(dniNumber, dniChar)


  val invalidDNI =
    for
      dniNumber <- DniNumber.parse(0)
      dniChar <- DniControlChar.parse('f')
    yield DNI(dniNumber, dniChar)

  List[Any](
    validDNI.map(_.value),
    invalidDNI.map(_.value)
  ).foreach(println)
