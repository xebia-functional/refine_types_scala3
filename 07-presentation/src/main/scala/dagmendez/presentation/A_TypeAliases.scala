package dagmendez.presentation

object A_TypeAliases extends App:

  type DniNumber = String
  type DniControlChar = Char

  case class DNI(numero: DniNumber, letra: DniControlChar):
    def value: String = s"$numero-$letra"

  // Valid data
  val validDniNumber: DniNumber = "12345678"
  val validControlChar: Char = 'A'
  val validDNI = DNI(validDniNumber, validControlChar)
  
  // Invalid Data
  val invalidDniNumber: DniNumber = "Hola, ¿qué tal?"
  val invalidControlChar: Char = '3'
  val invalidDNI = DNI(invalidDniNumber, invalidControlChar)
  
  Vector(validDNI, invalidDNI).map(_.value).foreach(println)
