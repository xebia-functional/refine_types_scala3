package dagmendez.iron

object A_RawClasses:

  class DNI(number: Int, letter: String):
    override def toString: String = s"$number-$letter"

  val valid                   = DNI(1, "R")
  val negativeNumber          = DNI(-1, "R")
  val tooLongNumber           = DNI(1234567890, "R")
  val invalidControlLetterDNI = DNI(1, "Ñ")

  def main(args: Array[String]): Unit =
    println("== Valid DNIs ==")
    println(valid)
    println("== Invalid DNIs ==")
    println(negativeNumber)
    println(tooLongNumber)
    println(invalidControlLetterDNI)
