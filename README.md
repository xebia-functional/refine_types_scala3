# Opaque Types and Inline

What are the possibilities of Scala 3 features `opaque type` and `inline` for type refinement?

This repository covers 4 levels of complexity:
- [Basic](#basic)
- [Standard](#standard)
- [Advanced](#advanced)
- [Scala Magic](#scala-magic)

The use case is based on a very simplistic model for a financial institution.

```scala 3
final case class AccountHolder(firstName: String, middleName: Option[String], lastName: String, secondLastName: Option[String])

final case class Account(accountHolder: AccountHolder, iban: String, balance: Double)
```

How can we model _better_ this domain? The primitive types do not help us much. Let's dive into the Scala 3 type system.

## Basic

[Source Code](./01-basic/src/main/scala/dagmendez/basic)

The basic way will be to declare just some `type aliases` for the underlying types. It works the same in Scala 2. For example:
```scala 3
  type Name    = String
  type IBAN    = String // International Bank Account Number
  type Balance = Double
```

With these type aliases we could redefine our basic model to:

```scala 3
final case class AccountHolder(firstName: Name, middleName: Option[Name], lastName: Name, secondLastName: Option[Name])

final case class Account(accountHolder: AccountHolder, iban: IBAN, balance: Balance)
```

Doesn't it look better? Now we can create an instance like this:

```scala 3
val firstName: Name  = "John"
val middleName: Name = "Stuart"
val lastName: Name   = "Mill"
val iban: IBAN       = "GB33BUKB20201555555555"
val balance: Balance = -10.0

val holder = AccountHolder(firstName, Some(middleName), lastName, None)

val account = Account(holder, iban, balance)
```

So, what are the benefits of using `type aliases`? Well, our code is more readable, and we can grasp faster what is going on.
But that is about it. We can still use the underlying types' API. We have just gain some readability.

Additional information in Alvin Alexander's [blog](https://alvinalexander.com/scala/scala-type-aliases-syntax-examples/).

## Standard

[Source Code](./02-standard/src/main/scala/dagmendez/standard)

Scala 3 includes a new way of declaring types that is _cheaper_ in terms of overhead. 
Just add the soft keyword `opaque` in front of type. Now, the compiler only sees the opaque type during compilation.
Thus, it does not know which is the underlying type until it compiles the code.
This prevents us from accessing the API of the underlying primitive type and pushes us into creating our own API.

```scala 3
opaque type Name    = String
opaque type IBAN    = String
opaque type Balance = Double
```

But, since the compiler does not see the underlying type... how do you create values of the `opaque type`? 
With an apply method in the companion object.

```scala 3
object Name:
  def apply(name: String): Name = name

object IBAN:
  def apply(iban: String): IBAN = iban

object Balance:
  def apply(balance: Double): Balance = balance
```

For those unaware of the `opaque types`, this code could make them think that we are using there `case class`(es) as wrappers of other types.

```scala 3
val firstName: Name  = Name("John")
val middleName: Name = Name("Stuart")
val lastName: Name   = Name("Mill")
val iban: IBAN       = IBAN("GB33BUKB20201555555555")
val balance: Balance = Balance(123.45)

val holder: AccountHolder = AccountHolder(firstName, Some(middleName), lastName, None)

val account: Account = Account(holder, iban, balance)
```

But we are not using `case class`(es). 
Once the code is compiled, those opaque types will be represented as their underlying type.
There is no need for the creation of an instance of a class wrapper. 
That is the main benefit of this approach in Scala 3.

Additional information in the [Scala 3 Documentation](https://docs.scala-lang.org/scala3/reference/other-new-features/opaques.html), and [Alvin Alexander's blog](https://alvinalexander.com/scala/scala-3-opaque-types-how-to/).

## Advanced

[Source Code](./03-advanced/src/main/scala/dagmendez/advanced)

So now, what happens in real applications? 
Often times we will work with values that are unknown at runtime. Hence, we want certain kind of validation.
We can achieve this with a new method in the companion object called `from` (it can be found also by `safe` in some codebases):

```scala 3
final case class InvalidName(message: String) extends RuntimeException(message) with NoStackTrace

opaque type Name = String

object Name:
  
  def from(fn: String): Either[InvalidName, Name] =
  // Here we can access the underlying type API because it is evaluated during runtime.
    if fn.isBlank | (fn.trim.length < fn.length)
    then Left(InvalidName(s"First name is invalid with value <$fn>."))
    else Right(fn)
```

What about those values that we know during compilation time? 
Is there a way that the compiler could tell us that the values fail the validation?
Yes, there is a way in Scala 3. 
We will combine the soft keyword `inline` and the tools present in the package `scala.compiletime`.

```scala 3
inline def apply(name: String): Name =
  inline if name == ""
  then error(codeOf(name) + " is invalid.")
  else name
```
Explanation: `inline` replaces the right hand side where the left hand side is called. 
The `inline if` will evaluate the condition during compile time. If true, will rewrite the `apply` as:
```scala 3
inline def apply(name: String) = error(codeOf(name) + " is invalid.")
```
So if we try to write something like this:
```scala 3
val firstName: Name  = Name("")
```
It will replace the right hand side of the def apply (because is also inlined) during compilation time to:
```scala 3
val firstName: Name  = error(codeOf("") + " is invalid.")
```
And we will get a compiler error:
```shell
[error] -- Error: /opaque_types_and_inline/03-advanced/src/main/scala/dagmendez/advanced/Main.scala:12:39 
[error] 12 |    val firstName: Name  = Name("")
[error]    |                                   ^^^^^^^^
[error]    |                                   "" is invalid.
[error] one error found
[error] (advanced / Compile / compileIncremental) Compilation failed
```

So now that we are using the two methods `apply` and `from`, we can validate known and unknown values during compilation and runtime.
But... the validation on the `apply` method was different from the one in the `from` method. Why?

> An if-then-else expression whose condition is a constant expression can be simplified to the selected branch.
> Prefixing an if-then-else expression with inline enforces that the condition has to be a constant expression, and thus guarantees that the conditional will always simplify.

The methods used in the `from` method are evaluated at runtime, so they cannot be reduced to a constant expression.
If we try to compile the same validation in the `apply` method, the compiler won't allow us.

Full documentation on inlining at [Scala 3 reference for metaprogramming](https://docs.scala-lang.org/scala3/reference/metaprogramming/inline.html#).

## Scala Magic

[Source Code](04-scala-magic/src/main/scala/dagmendez/magic)

How to implement refined types that are robust and maintainable? 
Well, first, the validation algorithm has to be robust and should be _the same_ for the `apply` and `from` methods.
Second, the error messages should be as similar as possible so the errors during runtime can be easily identified.

So let's go and check one by one or refined types.

### Balance

The bank decides that for the given accounts that our service will handle, there is a maximum and minimum amount of money allowed.
These limits are -1,000.00€ and 1,000,000.00€.
In this specific case, the same validation could be used in the `apply` method since the expression in the `if` can be reduced
to `true` or `false` during compilation time.
We will declare an inline def that will take as parameter the balance and return a boolean.
For this to work we need a boolean expression that can be evaluated at compile time.
```scala 3
private inline def validation(balance: Double): Boolean = balance >= -1000.0 && balance <= 1000000.0
```
So we have the same validation. Now, do we have the same error message? Yes!
For it to work, we have to inline the error message, so it can be reduced to a single string during compilation time.
```scala 3
private inline val errorMessage = " is invalid. Balance should be equal or greater than -1,000.00 and equal or smaller than 1,000,000.00"
```
In the `apply` we used `codeOf()`, `error` and `+`:
- `codeOf(x)` returns the value of the parameter `x`
- `error(x)` prints the x string into the console as a compilation error message
- `+` concatenates the value of the parameter `x` and the rest of the error messages (that has to be inlined to work!)
```scala 3
error(codeOf(balance) + errorMessage)
```
In the `from` method we just return the concatenation of the parameter and the error message wrapped into a specific error case class:
```scala 3
Left(InvalidBalance(balance + errorMessage))
```
The complete implementation would look like this:
```scala 3
object Balance:

  private inline def validation(balance: Double): Boolean = balance >= -1000.0 && balance <= 1000000.0
  private inline val errorMessage = " is invalid. Balance should be equal or greater than -1,000.00 and equal or smaller than 1,000,000.00"

  inline def apply(balance: Double): Balance =
    inline if validation(balance)
    then balance
    else error(codeOf(balance) + errorMessage)

  def from(balance: Double): Either[InvalidBalance, Balance] =
    if validation(balance)
    then Right(balance)
    else Left(InvalidBalance(balance + errorMessage))
```

### IBAN - International Bank Account Number

[More info on IBAN](https://www.iban.com/structure)

For the IBAN field, we will use the Spanish rule:
- IBAN always starts with the country code "ES"
- IBAN has a total length of 26 characters:
  - 2 letters (country code)
  - followed by 24 digits

We know that the `apply` method we cannot use `substring` or `length` since they are evaluated at runtime.
How can we do it? Here, Scala 3 has a very handy package that will help us a lot: `scala.compiletime.ops`:
- [API docs](https://www.scala-lang.org/api/3.2.1/scala/compiletime/ops.html#)
- [Reference documentation](https://docs.scala-lang.org/scala3/reference/metaprogramming/compiletime-ops.html)

```scala 3
inline def apply(iban: String): IBAN =
  inline if constValue[
    Substring[iban.type, 0, 2] == "ES" &&
    Length[iban.type] == 26 &&
    Matches[Substring[iban.type, 2, 25], "^\\d*$"]
  ]
  then iban
  else error(codeOf(iban) + errorMessage)

def from(iban: String): Either[InvalidIBAN, IBAN] =
  if 
    iban.substring(0, 2) == "ES" && 
    iban.length == 26 &&
    iban.substring(2, 25).matches("^\\d*$")
  then Right(iban)
  else Left(InvalidIBAN(iban + errorMessage))
```

The _real magic_ of inlining and the compile time API starts to show:
- `constValue[T]`: returns the value of the type `T`. So, `T` in this case has to be of type `Boolean`.
- `Substring[String, Int, Int]`: returns the value of the substring as a type `String`. 
Here we use `iban.type` because we are working with types, but this call does not return `String` but the value itself as a [_literal type_](https://docs.scala-lang.org/sips/42.type.html).
```scala 3
val iban: ES012345678901234567890123 = "ES012345678901234567890123"
val condition: Boolean = Substring[iban.type, 0, 2] == "ES"
val condition: Boolean = Substring[ES012345678901234567890123, 0, 2] == "ES"
val condition: Boolean = ES == "ES"
val condition: Boolean = "ES" == "ES" //ES is converted to its value
val condition: Boolean = true
```
- `Length[String]`: returns the length of the string as an `Int`
```scala 3
val iban: ES012345678901234567890123 = "ES012345678901234567890123"
val condition: Boolean = Length[iban.type] == 26
val condition: Boolean = Length[ES012345678901234567890123] == 26
val condition: Boolean = 26 == 26 //Type 26 is converted to its value
val condition: Boolean = true
```

### Name

Our final refined type will be `Name`. 
In Spain is very usual for people to have multiple first names and, at the same time, people do not categorized any of these names as middle name. 
Thus, our refinement has to be flexible while keeping some rules. So let's say that we want:
- Names start with upper case followed by lower case
- No empty spaces before or after the name
- Name can contain multiple valid names separated by one white space

To do this validation we can use a regular expression following the Java standards. 

```scala 3
object Name:

  /**
   * Explanation:
   *
   * `^` :Asserts the start of the string. [A-Z]: Matches an uppercase letter at the beginning of the string.
   * [a-zA-Z]*: Matches zero or more letters (uppercase or lowercase) after the first letter.
   * (?:\s[A-Z][a-zA-Z]*)*: Allows for zero or more occurrences of a space followed by an uppercase letter and zero or more lowercase/uppercase letters. $: Asserts the end of the string.
   */
  private inline val validation   = """^[A-Z][a-zA-Z]*(?:\s[A-Z][a-zA-Z]*)*$"""
  private inline val errorMessage = " is invalid. It must: \n - be trimmed.\n - start with upper case.\n - follow upper case with lower case."

  inline def apply(fn: String): Name =
    inline if constValue[Matches[fn.type, validation.type]]
    then fn
    else error(codeOf(fn) + errorMessage)

  def from(fn: String): Either[InvalidName, Name] =
    if validation.r.matches(fn)
    then Right(fn)
    else Left(InvalidName(fn + errorMessage))
```

With this approach, we have a common error message and validation logic expressed in an elegant way in just a few lines of code.

## Conclusion

Leveraging the power of `opaque types`, `inline` and the compile time API, 
we can define refined types in Scala 3 that are precise and elegant.
There is no need to use any other library than the language itself. 
