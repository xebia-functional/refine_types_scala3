package dagmendez.presentation

object A_RawClasses:

  class DNI private (number: String, letter: String):
    require(letter == controlDigit(number.toInt % 23))
    override def toString: String = s"$number-$letter"
  end DNI

  object DNI:
    def fromString(dni: String): DNI =
      val dniValues: Array[String] = dni.split("-")
      val number: String           = dniValues.head
      val letter: String           = dniValues(1)
      DNI(number, letter)
  end DNI

  def main(args: Array[String]): Unit =
    Vector(
      DNI.fromString("ABCDEFGH-0"), // Will crash the program
      DNI.fromString("00000001-R")
    ).foreach(dni => println(dni))
