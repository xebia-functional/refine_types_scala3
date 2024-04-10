package dagmendez.workshop

import scala.util.control.NoStackTrace
import scala.compiletime.constValue
import scala.compiletime.ops.any.ToString
import scala.compiletime.ops.string.Length
import scala.compiletime.ops.any.==
import scala.compiletime.error
object OTwithCompileError extends App:

  val controlChars = Set('T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E')

  case class FormatError(reason: String) extends Exception(reason) with NoStackTrace

  opaque type DniNumber = Int

  object DniNumber:
    inline def apply(number: Int): DniNumber =
      inline if constValue[Length[ToString[number.type]] == 8] then number
      else error("El número ha de contener 8 cifras")

    def wrap(number: Int): DniNumber = number
    def parse(number: Int): Either[FormatError, DniNumber] =
      Either.cond(
        number.toString.length == 8,
        DniNumber.wrap(number),
        FormatError("El número ha de contener 8 cifras")
      )

  extension (dniNumber: DniNumber)
    inline def value: Int = dniNumber

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
    invalidDNI.map(_.value),
    DNI(DniNumber.apply(12345678), DniControlChar('d')).value
  ).foreach(println)