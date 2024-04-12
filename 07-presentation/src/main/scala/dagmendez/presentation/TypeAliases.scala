package dagmendez.workshop

object TypeAliases extends App:

  type DniNumber = Int
  type DniControlChar = Char

  case class DNI(numero: DniNumber, letra: DniControlChar):
    def value: String = s"$numero-$letra"


  List[Any](
    DNI(12345678, 'A').value,
    DNI(0, 'f').value
  ).foreach(println)
