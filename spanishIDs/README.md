# Spanish IDs validation in Scala 3

**Note**: No regex is used to validate the inputs

## Description of the task

In Spain, both the `DNI` (_Documento Nacional de Identidad_) and the `NIE` (_Número de Identificación de Extranjero_) must meet specific format requirements to be considered valid.
Here are the details for each:

### DNI (National Identity Document)

1. **Format:** Consists of 8 digits followed by a letter (e.g., 12345678A).
2. **Digit Requirements:** The first 8 characters must be numbers from 0 to 9.
3. **Letter Requirement:** The final single letter (the 9th character) is a control character calculated through a specific algorithm based on the 8 digits.

### NIE (Foreigner Identification Number)

1. **Format:** A starting letter followed by 7 digits and ending with a letter (e.g., X1234567L).
    - Possible initial letters: 'X', 'Y', 'Z'.
    - The final letter is also a control character determined using a similar algorithm to that of the DNI.
2. **Digit Requirements:** The 7 characters following the initial letter must be numbers from 0 to 9.

### Validation Example

1. **DNI:** 12345678Z
    - Convert the base number (12345678) using the algorithm to get the letter 'Z'.
2. **NIE:** Y6543210L
    - Convert 'Y' to a numeric equivalent (making it `16543210`), and validate with the same algorithm.

| LETTER | NUMBER |
|--------|--------|
| X      | 0      |
| Y      | 1      |
| Z      | 2      |

### Control Letter Calculation Algorithm

For both documents, the calculation involves a modulo 23 operation on the numeric digits:

1. Divide the base number by 23.
2. The remainder of this division is used to index a specific letter from the following table:

| LETTER | REMAINDER |
|--------|-----------|
| T      | 0         |
| R      | 1         |
| W      | 2         |
| A      | 3         |
| G      | 4         |
| M      | 5         |
| Y      | 6         |
| F      | 7         |
| P      | 8         |
| D      | 9         |
| X      | 10        |
| B      | 11        |
| N      | 12        |
| J      | 13        |
| Z      | 14        |
| S      | 15        |
| Q      | 16        |
| V      | 17        |
| H      | 18        |
| L      | 19        |
| C      | 20        |
| K      | 21        |
| E      | 22        |


#### Calculation Example

If the number is 12345678:

1. `12345678 % 23 = 14`
2. Looking up the remainder '14' in the table gives the letter 'Z'.

## How can we solve it with plain Scala 3?

With Algebraic Data Types:
- Product types: case classes
- Sum types: enums
- Opaque types: Scala 3 only wrapper-free refined types

### Language features

Under the package `language` there are multiple implementation, from the most naive to the most powerful:
1. Raw Classes: Raw classes are the simplest form of classes in Scala. They do not employ any special techniques or features for type safety or wrapping values. They are straightforward and can be used to define data and behavior directly.
2. Type Aliases:  Type aliases allow you to create a new name for an existing type, making the code more readable and intention-revealing. While they add clarity, they do not provide extra type safety.
3. Value Classes: Value classes are used to avoid runtime overhead by creating lightweight wrappers for values. This is useful for creating type-safe abstractions without additional runtime allocations.
4. Value Classes with Validation: Enhanced value classes (with validation) add constraints to the wrapped values, ensuring certain invariants are maintained directly within the class.
5. Opaque types: Opaque types introduce a new way to define new types without runtime overhead. They provide type safety by hiding the underlying representation from the outside scope.
6. Opaque types with compiler errors: This approach extends opaque types with compile-time validation ensuring constraints on the values right when they are defined.

### Libraries

Traditionally, `refined` is the to-go library to refine types in Scala 2. The library is also available in Scala 3.
Nevertheless, this repository has 2 libraries that are Scala 3 only. 

#### NeoType

NeoType is a lightweight library for refined types in Scala 3. 
It builds upon the concept of opaque types introduced in Scala 3 to provide a way to define domain-specific types without runtime overhead. 
NeoType makes it easy to define new types that are distinct from their underlying primitive types but behave like value classes in terms of performance. 
For example, you can define a type-safe UserId type that wraps an Int and is not interchangeable with any other Int values in your code.

The usage of NeoType involves defining domain-specific types using macros or provided validators to ensure that values conform to certain rules at compile-time, thus preventing invalid states from propagating through your system.

#### Iron

Iron is another library for refined types that is exclusive to Scala 3. 
It leverages Scala 3's advanced type system capabilities to enforce constraints on types more declaratively and ergonomically.
Iron uses a combination of opaque types and compile-time validation to ensure that types adhere to specified invariants.

The distinguishing feature of Iron is its ability to integrate constraints directly into the type definitions using compile-time checks, which allows for more expressive and safer APIs. 
This results in clearer error messages and a more declarative approach to specifying type constraints.
