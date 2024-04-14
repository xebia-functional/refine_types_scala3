package dagmendez.presentation

import io.github.iltotore.iron.constraint.any.DescribedAs
import io.github.iltotore.iron.constraint.string.Match
import io.github.iltotore.iron.{:|, RefinedTypeOps}

object I_Iron:

  type ValidDniNumber   = Match["[0-9]{8}"] DescribedAs "El número ha de contener 8 caracteres numéricos"
  opaque type DniNumber = String :| ValidDniNumber

  object DniNumber extends RefinedTypeOps[String, ValidDniNumber, DniNumber]

  type ValidDniControlLetter   = Match["[ABCDEFGHJKLMNPQRSTWXYZ]{1}"] DescribedAs "Letra de control no válida"
  opaque type DniControlLetter = String :| ValidDniControlLetter

  object DniControlLetter extends RefinedTypeOps[String, ValidDniControlLetter, DniControlLetter]

  case class DNI private (numero: DniNumber, letra: DniControlLetter):
    def value: String = s"${numero.toString}-${letra.toString}"

  object DNI:
   
    def parse(dni: String): Either[String, DNI] =
      dni.split("-") match
        case Array(notDash) =>
          Left(
            s"El DNI: $notDash es incorrecto. Vuelva a escribir su DNI separando el numero de la letra con un guion (-)."
          )
        case Array(numero, letra) =>
          val n = DniNumber.applyUnsafe(numero)
          val l = DniControlLetter.applyUnsafe(letra)
          if l == controlDigit(n.toString.toInt % 23)
          then Right(DNI(n, l))
          else Left(s"El DNI: $numero-$letra es incorrecto. La verificación de la letra de seguridad ha fallado.")

  @main def iron: Unit =
    Vector(
      DNI.parse("01234567=D"),
      DNI.parse("12345678-D"),
      DNI.parse("00000023-T")
    ).map(a => a.map(b => b.value)).foreach(println)
