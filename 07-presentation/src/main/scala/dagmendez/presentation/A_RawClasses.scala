package dagmendez.presentation

object A_RawClasses extends App:
  
  // Closest to traditional Java
  class DummyDNI(dni: String):
    override def toString: String = s"DummyDNI($dni)"
  end DummyDNI
  
  List[DummyDNI](
    new DummyDNI("00000001A"),
    new DummyDNI("ABCDEFGH0")
  ).foreach(println)

  // Basic Vanilla Scala
  case class DNI(numero: String, letra: Char):
    def value: String = s"$numero-$letra"

  List[Any](
    DNI("12345678", 'A').value,
    DNI("0", 'f').value
  ).foreach(println)