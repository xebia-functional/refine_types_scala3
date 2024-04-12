package dagmendez.workshop

object ValueClasses extends App:

  case class DniNumber(number: Int) extends AnyVal
  case class DniControlChar(character: Char) extends AnyVal

  case class DNI(numero: DniNumber, letra: DniControlChar):
    def value: String = s"${numero.number}-${letra.character}"


  List[Any](
    //DNI(12345678, 'A').value,
    //DNI(0, 'f').value,
    DNI(DniNumber(12345678), DniControlChar('A')).value,
    DNI(DniNumber(0), DniControlChar('f')).value

  ).foreach(println)
