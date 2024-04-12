package dagmendez.presentation

import scala.util.control.NoStackTrace

object F_OpaqueTypes:

  val controlChars = Set('T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E')

  case class FormatError(reason: String) extends Exception(reason) with NoStackTrace

  opaque type DniNumber = String

  object DniNumber:
    inline def apply(number: String): DniNumber = number
    def parse(number: String): Either[FormatError, DniNumber] =
      Either.cond(
        number.length == 8 && number.forall(_.isDigit),
        DniNumber(number),
        FormatError("El número ha de contener 8 carácteres numéricos")
      )
  extension (dniNumber: DniNumber)
    inline def value: String = dniNumber

  opaque type DniControlChar = Char

  object DniControlChar:
    inline def apply(char: Char): DniControlChar = char
    def parse(character: Char): Either[FormatError, DniControlChar] =
      Either.cond(
        controlChars.contains(character),
        DniControlChar(character),
        FormatError("La letra de control es incorrecta")
      )

  extension (dniChar: DniControlChar)
    inline def value: Char = dniChar

  case class DNI(numero: DniNumber, letra: DniControlChar):
    def value: String = s"${numero.value}-${letra.value}"

  val validDNI =
    for
      dniNumber <- DniNumber.parse("12345678")
      dniChar <- DniControlChar.parse('A')
    yield DNI(dniNumber, dniChar)


  val invalidDNI =
    for
      dniNumber <- DniNumber.parse("0")
      dniChar <- DniControlChar.parse('f')
    yield DNI(dniNumber, dniChar)
  
  @main def run: Unit = 
    Vector(validDNI, invalidDNI)
      .map(_.map(_.value))
      .foreach(println)