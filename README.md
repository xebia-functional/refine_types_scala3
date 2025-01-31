# Refined types in Scala 3

They way types are refined in Scala 3 is more performant than Scala 2. 
Thanks to `opaque types` and new `metaprogramming` techniques, refined types in Scala 3 can be evaluated at compile time.
In situation where we do not know the value at compile time, `Either` wrappers around `error` or  `types` come handy to handle the validation process.

This repository contains various modules:
- Language Feature: contains explanations in detail of how the Scala 3 metaprogramming techniques work to achieve "free" refined types.
- Workshop: contains snippets of code that show the different trade-offs of multiple approaches to refining types
- NeoType: Scala 3 only library that allows to refine types without having to work with the Type System
- Iron: Scala 3 only library that allows to refine types and has a robust type system framework

## Language Feature

This module contains multiple examples and explanations of the internal of Scala 3 while defining "free" refined types.
Check the [README.md](01-language-feature/README.md) of this module for a step bt step explanation.

You can check the blogpost [Crafting Concise Constructors with Opaque Types in Scala 3](https://xebia.com/blog/crafting-concise-constructors-opaque-types-scala-3/) were the code snippets were used as a source material.

## Workshop

Contains code snippets that guide the audience towards the mechanics behind opaque types in Scala 3 and how to use them.

# Dedicated Libraries

These 2 libraries work exclusively in Scala 3.
Both leverage the metaprogramming capabilities of Scala 3 in combination with a very strong a versatile type system.
If you would like to know the intuition behind how it works, check the blogpost [Generic Refinement Types in Scala 3](https://xebia.com/blog/generic-refinement-types-in-scala-3/).

## NeoType

This module implements a validation for Spanish IDs.

Neotype is a friendly newtype library for Scala 3.
Allows you to define refinements using basic assertions in plain Scala.

### Features

- Compile-time Checked Values
- Write validations as **plain, old Scala expressions**
- Helpful compilation errors
- No runtime allocations (Thanks to `inline` and `opaque type`)
- Integrates with other libraries (e.g. `zio-json`, `circe`, `tapir`, etc.)

More information in the [library repository](https://github.com/kitlangton/neotype).

## Iron

This module implements a validation for Spanish IDs.
The multiple implementations show the trade-offs of multiple approaches to refine types in a incremental way.

Iron is a lightweight library for refined types in Scala 3.

It enables attaching constraints/assertions (at the type level) to types, to enforce properties and forbid invalid values.

### Features

- Catch bugs. In the spirit of static typing, use more specific types to avoid invalid values.
- Compile-time and runtime. Evaluate constraints at compile time, or explicitly check them at runtime (e.g. for a form).
- Seamless. Iron types are subtypes of their unrefined versions, meaning you can easily add or remove them.
- No black magic. Use Scala 3's powerful inline, types and restricted macros for consistent behaviour and rules. No unexpected behaviour.
- Extendable. Easily create your own constraints or integrations using classic typeclasses.

More information in the [library repository](https://github.com/Iltotore/iron).