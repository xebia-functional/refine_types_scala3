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
  object ValidNumber extends Newtype[String]:
    override inline def validate(input: String): Boolean | String =
      if input.exists(_.isLetter) then "Illegal amount of letters in the ID"
      else
        input.toInt match
          case negative if negative <= 0 => s"$input is not positive. PositiveNumber must be positive"
          case short if short > 99999999 => s"$input is too long. Maximum amount of numbers is 8"
          case _                         => true

  type ValidControlLetter = ValidControlLetter.Type
  object ValidControlLetter extends Newtype[String]:
    override inline def validate(input: String): Boolean | String =
      if controlLetter.values.toSeq.contains(input.toCharArray.head) then true
      else s"$input is not a valid control letter"

  object ValidID extends Newtype[(ValidNumber, ValidControlLetter)]:
    override def validate(input: (ValidNumber, ValidControlLetter)): Boolean | String =
      val number = input._1.unwrap.toInt            // Extracts the underlying Int
      val letter = input._2.unwrap.toCharArray.head // Extracts the underlying Char
      if letter == controlLetter(number % 23) then true // Validates the control letter against the number
      else s"The control letter '$letter' is not matching the number"

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
      // Head (char) is cast to String before casting it to Int to get the proper index.
      // For example - Char: '0'.toInt = 48
      // For example - String: "0".toInt = 0
      val nieLetterAsNumber: Int = number.unwrap.head.toString.toInt
      // Only 0, 1 and 2 are valid ordinals
      val nieLetter: NieLetter  = NieLetter.fromOrdinal(nieLetterAsNumber)
      val nieNumber: String     = number.unwrap.tail
      val controlLetter: String = letter.unwrap
      s"$nieLetter-$nieNumber-$controlLetter"

  class DNI(number: ValidNumber, letter: ValidControlLetter):
    override def toString: String = s"${number.unwrap}-${letter.unwrap}"

  object ID:
    def parse(input: String): Either[String, DNI | NIE] =

      // Allows for introducing an ID with or without dash as input.
      val withoutDash: String = input.replace("-", "")

      // Inputs with length smaller or bigger than 9 are invalid by definition (once the dash is removed).
      if withoutDash.length != 9 then Left("Valid ID must be 9 characters long by counting only the numbers and letters.")

      // If the input is equal to 9 characters, it could be valid.
      else
        // Splits the numbers from the tailing character.
        val (number, letter) = withoutDash.splitAt(8)

        // If the first character is a letter, then the ID is a NIE, otherwise is a DNI
        val isNIE: Boolean = number.head.isLetter

        val completeNumber: String =
          // If the ID is a NIE, then the first letter must be transformed into a number before we continue processing.
          if isNIE then s"${NieLetter.valueOf(number.head.toString.toUpperCase).ordinal}${number.tail}"
          // Otherwise, we just continue with the number
          else number

        // Chains the computations of validations in a for comprehension.
        for
          number <- ValidNumber.make(completeNumber)
          // Allows for the character to be both upper and lower case.
          letter <- ValidControlLetter.make(letter.toUpperCase)
          // Validates if the control character matches the ID number
          id <- ValidID.make(number, letter)
        yield
          if isNIE then NIE(id.unwrap._1, id.unwrap._2)
          else DNI(id.unwrap._1, id.unwrap._2)

  @main
  def validate(): Unit =

    println("""
        | *----------------------*
        | | Spanish ID validator |
        | *----------------------*
        |
        | Introduce any ID. For example:
        | - 12345678-Z
        | - 12345678-z
        | - 12345678Z
        | - 12345678z
        | - Y-2345678-Z
        | - Y2345678Z
        | - y-2345678-z
        | - y2345678z
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
        ID.parse(userInput) match
          case Right(id)   => println(s"$id is a valid ID")
          case Left(error) => println(s"$userInput is not a valid ID. Reason: $error")
        loop()
      }
    loop()
