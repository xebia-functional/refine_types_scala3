import scala.util.control.NoStackTrace

object error:
  /*
  * We add two custom error classes to handle invalid values 
  */

  transparent case class InvalidName(message: String) extends RuntimeException(message) with NoStackTrace

  transparent case class InvalidIBAN(message: String) extends RuntimeException(message) with NoStackTrace
  
  transparent case class InvalidBalance(message: String) extends RuntimeException(message) with NoStackTrace
