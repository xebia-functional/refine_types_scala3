package dagmendez.presentation

object B_ValueClasses extends App:

  case class DniNumber(number: String) extends AnyVal
  case class DniControlChar(character: Char) extends AnyVal

  case class DNI(numero: DniNumber, letra: DniControlChar):
    def value: String = s"${numero.number}-${letra.character}"


  List[Any](
    DNI(DniNumber("12345678"), DniControlChar('A')).value,
    DNI(DniNumber("0"), DniControlChar('f')).value,
    DNI(DniNumber("Hola"), DniControlChar('0'))
  ).foreach(println)
