import scala.util.control.NoStackTrace

object error:
  /*
  * We add two custom error classes to handle invalid values 
  */

  case class InvalidName(message: String) extends RuntimeException(message) with NoStackTrace

  case class InvalidIBAN(message: String) extends RuntimeException(message) with NoStackTrace
  
  case class InvalidBalance(message: String) extends RuntimeException(message) with NoStackTrace
