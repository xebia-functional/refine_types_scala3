package dagmendez.neotype

import scala.annotation.tailrec

import neotype._

object NeoType:

  type PositiveNumber = PositiveNumber.type
  object PositiveNumber extends Newtype[Long]:
    override inline def validate(input: Long): Boolean | String =
      if input >= 0L then true
      else s"$input is not positive, the number must be positive"

  type NotTooLongNumber = NotTooLongNumber.type
  object NotTooLongNumber extends Newtype[Long]:
    override inline def validate(input: Long): Boolean | String =
      if input <= 99999999L then true
      else s"$input is too long, the maximum amount of numbers is 8"

  type ValidNumber = ValidNumber.Type
  // This does not work. It works with Iron
  // object ValidNumber extends Newtype[PositiveNumber & NotTooLongNumber]:
  object ValidNumber extends Newtype[Long]:
    override inline def validate(input: Long): Boolean | String =
      input match
        case negative if negative < 0L  => s"$input is not positive. PositiveNumber must be positive"
        case short if short > 99999999L => s"$input is too long. Maximum amount of numbers is 8"
        case _                          => true

  type ValidLetter = ValidLetter.Type
  object ValidLetter extends Newtype[Char]:
    override inline def validate(input: Char): Boolean | String =
      val validChars = Set('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z', 'e',
                           'n', 't', 'f', 'a', 'm', 'v', 'p', 'r', 'w', 'k', 's', 'x', 'j', 'y', 'q', 'b', 'g', 'l', 'c', 'h', 'z', 'd')
      if validChars.contains(input) then true
      else s"$input is not a valid control letter"

  val controlDigit: Map[Char, Int] = Map(
    't' -> 0,
    'T' -> 0,
    'r' -> 1,
    'R' -> 1,
    'w' -> 2,
    'W' -> 2,
    'a' -> 3,
    'A' -> 3,
    'g' -> 4,
    'G' -> 4,
    'm' -> 5,
    'M' -> 5,
    'y' -> 6,
    'Y' -> 6,
    'f' -> 7,
    'F' -> 7,
    'p' -> 8,
    'P' -> 8,
    'd' -> 9,
    'D' -> 9,
    'x' -> 10,
    'X' -> 10,
    'b' -> 11,
    'B' -> 11,
    'n' -> 12,
    'N' -> 12,
    'j' -> 13,
    'J' -> 13,
    'z' -> 14,
    'Z' -> 14,
    's' -> 15,
    'S' -> 15,
    'q' -> 16,
    'Q' -> 16,
    'v' -> 17,
    'V' -> 17,
    'h' -> 18,
    'H' -> 18,
    'l' -> 19,
    'L' -> 19,
    'c' -> 20,
    'C' -> 20,
    'k' -> 21,
    'K' -> 21,
    'e' -> 22,
    'E' -> 22
  )

  class DNI private (number: ValidNumber, letter: ValidLetter):

    override def toString: String =
      String
        .format("%08d", number.unwrap)
        .concat("-")
        .concat(letter.unwrap.toString)
  object DNI:
    def parse(possibleDNI: String): Either[String, DNI] =
      val number         = possibleDNI.dropRight(1).toLong
      val letter         = possibleDNI.last
      val possibleNumber = ValidNumber.make(number)
      val possibleLetter = ValidLetter.make(letter)
      for
        number <- possibleNumber
        letter <- possibleLetter
        dni <- Either.cond(
                 controlDigit(letter.unwrap) == number.unwrap.toInt % 23,
                 DNI(number, letter),
                 "Control letter does not match the number"
               )
      yield dni

  @main
  def validate(): Unit =
    println(controlDigit('q'))
    println("""
        | *---------------*
        | | DNI validator |
        | *---------------*
        |
        | Introduce the DNI number and the letter together.
        | Once you have finished, write "quit" to exit.
        |
        |""".stripMargin)
    @tailrec
    def loop(): Unit =
      val userInput = scala.io.StdIn.readLine("Enter a DNI: ")
      if userInput == "quit" then ()
      else {
        println(DNI.parse(userInput))
        loop()
      }
    loop()
