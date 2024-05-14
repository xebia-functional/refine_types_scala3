package dagmendez.presentation

object C_ValueClasses:

  class Number(val value: Int)

  class Letter(val value: String)

  class DNI(number: Number, letter: Letter):
    override def toString: String = prettyDNI(number.value, letter.value)

  def main(args: Array[String]): Unit =
    println("== Valid DNIs ==")
    println(DNI(Number(1), Letter("R")))

    println("== Invalid DNIs ==")
    println(" * Negative Number:")
    println(DNI(Number(-1), Letter("R")))
    println(" * Too long number:")
    println(DNI(Number(1234567890), Letter("R")))
    println(" * Incorrect control letter:")
    println(DNI(Number(1), Letter("Ñ")))

/**
 * Conclusion: Value Classes give us some enforcement of order but not much more.
 */
