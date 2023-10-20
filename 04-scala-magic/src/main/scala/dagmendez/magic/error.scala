package dagmendez.magic

import scala.util.control.NoStackTrace

object error:

  final case class InvalidName(message: String) extends RuntimeException(message) with NoStackTrace

  final case class InvalidIBAN(message: String) extends RuntimeException(message) with NoStackTrace

  final case class InvalidBalance(message: String) extends RuntimeException(message) with NoStackTrace
