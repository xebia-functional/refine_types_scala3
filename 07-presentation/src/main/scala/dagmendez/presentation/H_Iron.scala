package dagmendez.presentation

import io.github.iltotore.iron.{:|, RefinedTypeOps}
import io.github.iltotore.iron.constraint.string.Match
import io.github.iltotore.iron.constraint.any.DescribedAs
import io.github.iltotore.iron.RefinedTypeOps
import io.github.iltotore.iron.autoRefine

object H_Iron:

  type ValidDniNumber =  Match["[0-9]{8}"] DescribedAs "El número ha de contener 8 caracteres numéricos"
  opaque type DniNumber = String :| ValidDniNumber
  object DniNumber extends RefinedTypeOps[String, ValidDniNumber, DniNumber]

  type ValidDniControlLetter = Match["[ABCDEFGHJKLMNPQRSTWXYZ]{1}"] DescribedAs "Letra de control no válida"
  opaque type DniControlLetter = String :| ValidDniControlLetter
  object DniControlLetter extends RefinedTypeOps[String, ValidDniControlLetter, DniControlLetter]

  case class DNI(numero: DniNumber, letra: DniControlLetter):
    def value: String = s"${numero.toString}-${letra.toString}"


  @main def ironRun: Unit =
    Vector(
      // Descomentar para ver las trazas de Iron en tiempo de compilación
      //DNI(DniNumber("12345"), DniControlLetter("O")),
      DNI(DniNumber("12345678"), DniControlLetter("D"))
    ).map(_.value).foreach(println)