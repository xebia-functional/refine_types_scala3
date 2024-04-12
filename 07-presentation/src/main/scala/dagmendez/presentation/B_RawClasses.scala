package dagmendez.presentation

object B_RawClasses extends App:
  
  // Closest to traditional Java
  class DummyDNI(dni: String):
    override def toString: String = s"DummyDNI($dni)"
  end DummyDNI
  
  Vector[DummyDNI](
    new DummyDNI("00000001A"), 
    new DummyDNI("ABCDEFGH0")
  ).foreach(dummyDNI => println(dummyDNI))

  // Basic Vanilla Scala
  case class DNI(numero: String, letra: Char):
    def value: String = s"$numero-$letra"

  Vector[DNI](DNI("12345678", 'A'), DNI("0", 'f'))
    .map(dni => dni.value)
    .foreach(dniValue => println(dniValue))