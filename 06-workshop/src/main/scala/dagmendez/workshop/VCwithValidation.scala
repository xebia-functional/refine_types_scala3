package dagmendez.workshop

object VCwithValidation extends App:
  
  val controlChars = Set('T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E')

  case class DniNumber(number: Int):
    require(number.toString.length == 8, "El número ha de contener 8 cifras")

  case class DniControlChar(character: Char):
    require(controlChars.contains(character), "La letra de control es incorrecta")

  case class DNI(numero: DniNumber, letra: DniControlChar):
    def value: String = s"${numero.number}-${letra.character}"


  List[Any](
    DNI(DniNumber(12345678), DniControlChar('A')).value,
    DNI(DniNumber(0), DniControlChar('f')).value

  ).foreach(println)
