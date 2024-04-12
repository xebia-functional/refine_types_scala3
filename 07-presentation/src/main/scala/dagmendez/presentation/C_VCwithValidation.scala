package dagmendez.presentation

object C_VCwithValidation extends App:
  
  val controlChars = Set('T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E')

  case class DniNumber(number: String):
    require(number.length == 8, "El número ha de contener 8 cifras")
    require(number.forall(_.isDigit), "Los primeros 8 caracteres han de ser números")

  case class DniControlChar(character: Char):
    require(controlChars.contains(character), "La letra de control es incorrecta")

  case class DNI(numero: DniNumber, letra: DniControlChar):
    def value: String = s"${numero.number}-${letra.character}"


  List[Any](
    // Insuficientes números
    DNI(DniNumber("0"), DniControlChar('A')).value,
    // Letras en los números
    DNI(DniNumber("123BCD78"), DniControlChar('A')).value,
    // Letra de control inválida
    DNI(DniNumber("12345678"), DniControlChar('Y')).value,
    // Todas las validaciones son correctas
    DNI(DniNumber("12345678"), DniControlChar('A')).value,
  ).foreach(println)
