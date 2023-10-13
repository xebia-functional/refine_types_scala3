package dagmendez.advanced

import scala.util.control.NoStackTrace

object error:
  /*
   * We add two custom error classes to handle invalid values
   */

  final case class InvalidName(message: String) extends RuntimeException(message) with NoStackTrace

  final case class InvalidIBAN(message: String) extends RuntimeException(message) with NoStackTrace

  final case class InvalidBalance(message: String) extends RuntimeException(message) with NoStackTrace
