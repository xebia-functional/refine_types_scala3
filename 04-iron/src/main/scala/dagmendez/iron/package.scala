package dagmendez

import scala.util.control.NoStackTrace

package object iron:
  val controlDigit: Map[Int, String] = Map(
    0  -> "T",
    1  -> "R",
    2  -> "W",
    3  -> "A",
    4  -> "G",
    5  -> "M",
    6  -> "Y",
    7  -> "F",
    8  -> "P",
    9  -> "D",
    10 -> "X",
    11 -> "B",
    12 -> "N",
    13 -> "J",
    14 -> "Z",
    15 -> "S",
    16 -> "Q",
    17 -> "V",
    18 -> "H",
    19 -> "L",
    20 -> "C",
    21 -> "K",
    22 -> "E"
  )

  /**
   * Takes the number, casts it to String and prepends as many zeroes (0) as needed until the length is 8. Then appends a dash (-) and the letter.
   * @param number
   *   DNI number of any type that is a sub type of Int.
   * @param letter
   *   DNI letter of any type that is a sub type of String.
   * @tparam N
   *   Any sub type of Int
   * @tparam S
   *   Any subtype of String
   * @return
   *   a human readable DNI
   */
  def prettyDNI[N <: Int, S <: String](number: N, letter: S): String =
    String
      .format("%08d", number.asInstanceOf[Int])
      .concat("-")
      .concat(letter)

  case class FormatError(reason: String) extends Exception(reason) with NoStackTrace
