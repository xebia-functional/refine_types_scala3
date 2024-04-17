package dagmendez.presentation

object A_RawClasses:

  class DNI(number: String, letter: String):
    override def toString: String = s"$number-$letter"

  def main(args: Array[String]): Unit =
    Vector(
      // Valid DNI
      DNI("00000001", "R"),
      // Invalid DNIs
      DNI("00000001", "A"),
      DNI("R", "00000001")
    ).foreach(dni => println(dni))
