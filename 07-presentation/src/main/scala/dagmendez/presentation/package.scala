package dagmendez

import scala.util.control.NoStackTrace

package object presentation:
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
   * Takes the number, casts it to String and prepends as many zeroes (0) as needed until the length is 8.
   * @param number
   *   input number
   * @tparam N
   *   any type that is a sub-type of Int or an Int
   * @return
   *   a String of length 8
   */
  def addLeadingZeroes[N <: Int](number: N): String = String.format("%08d", number.asInstanceOf[Int])

  case class FormatError(reason: String) extends Exception(reason) with NoStackTrace
