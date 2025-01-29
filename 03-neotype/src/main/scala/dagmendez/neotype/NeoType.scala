package dagmendez.neotype

import scala.annotation.tailrec

import neotype.*

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
      val validChars = Set('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z')
      if validChars.contains(input) then true
      else s"$input is not a valid control letter"

  val controlDigit: Map[Int, Char] = Map(
    0  -> 'T',
    1  -> 'R',
    2  -> 'W',
    3  -> 'A',
    4  -> 'G',
    5  -> 'M',
    6  -> 'Y',
    7  -> 'F',
    8  -> 'P',
    9  -> 'D',
    10 -> 'X',
    11 -> 'B',
    12 -> 'N',
    13 -> 'J',
    14 -> 'Z',
    15 -> 'S',
    16 -> 'Q',
    17 -> 'V',
    18 -> 'H',
    19 -> 'L',
    20 -> 'C',
    21 -> 'K',
    22 -> 'E'
  )

  class DNI private (number: ValidNumber, letter: ValidLetter):

    override def toString: String =
      String
        .format("%08d", number.unwrap)
        .concat("-")
        .concat(letter.unwrap.toString)

  object DNI:
    def parse(possibleDNI: String): Either[String, DNI] =
      val withoutDash = possibleDNI.replace("-", "")
      if withoutDash.length == 9 then
        val (number, letter) = withoutDash.splitAt(8)
        val possibleNumber   = ValidNumber.make(number.toLong)
        val possibleLetter   = ValidLetter.make(letter.toUpperCase.head)
        for
          number <- possibleNumber
          letter <- possibleLetter
          dni <- Either.cond(
            letter.unwrap == controlDigit(number.unwrap.toInt % 23),
            DNI(number, letter),
            "Control letter does not match the number"
          )
        yield dni
      else Left("Valid DNI must contain 8 digits and one control character.")

  @main
  def validate(): Unit =

    println("""
        | *---------------*
        | | DNI validator |
        | *---------------*
        |
        | Introduce any DNI. For example:
        | - 12345678-Z
        | - 12345678-z
        | - 12345678Z
        | - 12345678z
        |
        | write QUIT, quit, Q or q to exit the program
        |
        |""".stripMargin)
    @tailrec
    def loop(): Unit =
      val userInput   = scala.io.StdIn.readLine("Enter a DNI: ")
      val exitCommand = Set("QUIT", "Q", "quit", "q")
      if exitCommand.contains(userInput) then ()
      else {
        println(DNI.parse(userInput))
        loop()
      }
    loop()
