package dagmendez.presentation

object C_ValueClasses:

  case class DniNumber(number: String)       extends AnyVal
  case class DniControlChar(character: Char) extends AnyVal

  case class DNI(numero: DniNumber, letra: DniControlChar):
    def value: String = s"${numero.number}-${letra.character}"

  val dnis = Vector[DNI](
    DNI(DniNumber("12345678"), DniControlChar('A')),
    DNI(DniNumber("0"), DniControlChar('f')),
    DNI(DniNumber("Hola"), DniControlChar('0'))
  )

  @main def printDNI(): Unit =
    // Python-ish way if writing lambda functions, but with a line break
    dnis
      .map: dni =>
        dni.value
      .foreach: dniValue =>
        println(dniValue)
