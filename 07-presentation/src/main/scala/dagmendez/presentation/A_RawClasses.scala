package dagmendez.presentation

object A_RawClasses:

  class DNI(number: String, letter: String):
    require(
      letter == controlDigit(number.toInt % 23),
      "Verify the DNI. Control letter does not match the number."
    )
    override def toString: String = s"$number-$letter"
  end DNI

  def main(args: Array[String]): Unit =
    Vector(
      // requirement failed: Verify the DNI. Control letter does not match the number.
      DNI("00000001", "A"),
      // java.lang.NumberFormatException: For input string: "R"
      DNI("R", "00000001"),
      // Valid DNI
      DNI("00000001", "R")
    ).foreach(dni => println(dni))
