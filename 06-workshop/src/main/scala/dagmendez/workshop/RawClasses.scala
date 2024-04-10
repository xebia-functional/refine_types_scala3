package dagmendez.workshop

object RawClasses extends App:

  case class DNI(numero: Int, letra: Char):
    def value: String = s"$numero-$letra"

  List[Any](
    DNI(12345678, 'A').value,
    DNI(0, 'f').value
  ).foreach(println)