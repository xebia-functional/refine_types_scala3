package dagmendez.workshop.a

import scala.compiletime.*
import scala.compiletime.ops.int.*

object Presentation:

  opaque type Age = Int

  object Age:

    inline def apply(age: Int): Age =
      inline if constValue[age.type < 0] then error(codeOf(age) + " illegal age. Age should be positive")
      else if constValue[age.type > 150] then error(codeOf(age) + " illegal age. Age should be not greater than 150")
      else age

    def from(age: Int): Either[String, Age] =
      if age < 0 then Left("Person cannot have negative age")
      else if age > 150 then Left("Person is too old (>150)")
      else Right(age)

  @main def run(): Unit =
    Seq(
      Presentation.Age.from(60),
      Presentation.Age.from(-60),
      Presentation.Age.from(160),
      Presentation.Age(60)
      // Presentation.Age(-60),
      // Presentation.Age(160),
    ).foreach(println)

  /**
   * Can we unify the error messages?
   */

end Presentation
