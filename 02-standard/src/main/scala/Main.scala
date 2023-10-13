import domain.*
import opaqueTypes.*

import scala.language.postfixOps

object Main:

  def main(args: Array[String]): Unit = {

    /* 
    * Now we can build values of opaque types using the apply method as if they were case classes
    * Case classes create a new instance during runtime. 
    * Opaque types apply methods happen during compile time.
    */
    
    val firstName: FirstName = FirstName("John")
    val middleName: FirstName = FirstName("Stuart")
    val lastName: LastName = LastName("Mill")
    val iban: IBAN = IBAN("GB33BUKB20201555555555")
    
    val holder = AccountHolder(
      firstName,
      Some(middleName),
      lastName,
      None
    )

    val account = Account(holder, iban)
    
    println(account)
  }

