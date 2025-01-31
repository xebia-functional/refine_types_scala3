package dagmendez.neotype

import scala.annotation.tailrec

import neotype._

/**
 * This is an example of how to implement a basic validator for Spanish IDs, for nationals and foreign residents. Find the information on how the IDs
 * are validated - in Spanish -
 * [[https://www.interior.gob.es/opencms/es/servicios-al-ciudadano/tramites-y-gestiones/dni/calculo-del-digito-de-control-del-nif-nie/ here]]
 */
object NeoType:

  type ValidNumber = ValidNumber.Type
  object ValidNumber extends Newtype[Long]:
    override inline def validate(input: Long): Boolean | String =
      input match
        case negative if negative <= 0L => s"$input is not positive. PositiveNumber must be positive"
        case short if short > 99999999L => s"$input is too long. Maximum amount of numbers is 8"
        case _                          => true

  type ValidControlLetter = ValidControlLetter.Type
  object ValidControlLetter extends Newtype[Char]:
    override inline def validate(input: Char): Boolean | String = controlLetter.values.toSeq.contains(input)

  object ValidID extends Newtype[(ValidNumber, ValidControlLetter)]:
    override def validate(input: (ValidNumber, ValidControlLetter)): Boolean | String =
      val number = input._1.unwrap // Extracts the underlying Long
      val letter = input._2.unwrap // Extracts the underlying Char
      letter == controlLetter(number.toInt % 23) // Validates the control char against the number

  /**
   * The NIE initial letters are defined as an Enum to leverage the method "ordinal". The letter should be replaced by number like this:
   *   - x -> 0
   *   - y -> 1
   *   - z -> 2
   *
   * Hence, do not alter the order of the enumeration.
   */
  enum NieLetter:
    case X, Y, Z

  class NIE(number: ValidNumber, letter: ValidControlLetter):
    override def toString: String =
      val nieLetter     = NieLetter.fromOrdinal(number.unwrap.toString.head.toString.toInt)
      val nieNumber     = number.unwrap.toString.tail
      val controlLetter = letter.unwrap
      s"$nieLetter-$nieNumber-$controlLetter"

  class DNI(number: ValidNumber, letter: ValidControlLetter):
    override def toString: String = s"${number.unwrap}-${letter.unwrap}"

  object ID:
    def parse(input: String): Either[String, DNI | NIE] =

      // Allows for introducing DNI with or without dash as input.
      val withoutDash = input.replace("-", "")

      // Inputs with length smaller or bigger than 9 are invalid by definition (once the dash is removed).
      if withoutDash.length != 9 then Left("Valid ID must be 9 characters long by counting only the numbers and letters.")

      // If the input is equal to 9 characters, it could be valid.
      else
        // Splits the numbers from the tailing character.
        val (number, letter) = withoutDash.splitAt(8)

        // We check if the ID is a NIE or a DNI
        val maybeNie: Option[NieLetter] =
          if number.head.isDigit then None
          else Some(NieLetter.valueOf(number.head.toString.toUpperCase)) // Allows for the character to be both upper and lower case.

        // We build the number. If it is a NIE, we must transform the first letter into a digit
        val completeNumber =
          if maybeNie.isEmpty then number.toLong
          else s"${maybeNie.get.ordinal}${number.tail}".toLong

        // Chains the computations of validations in a for comprehension.
        for
          number <- ValidNumber.make(completeNumber)
          letter <- ValidControlLetter.make(letter.toUpperCase.head) // Allows for the character to be both upper and lower case.
          id     <- ValidID.make(number, letter)                     // Validates if the control character matches the ID number
        yield
          if maybeNie.isEmpty
          then DNI(id.unwrap._1, id.unwrap._2)
          else NIE(id.unwrap._1, id.unwrap._2)

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
        ID.parse(userInput).foreach(id => println(s"\n$id is valid.\n"))
        loop()
      }
    loop()
