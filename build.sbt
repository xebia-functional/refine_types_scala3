ThisBuild / organization := "dagmendez"

ThisBuild / scalaVersion := "3.3.1"

ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

lazy val `opaque-types-and-inline`: Project =
  project
    .in(file("."))
    .aggregate(
      naive,
      standard,
      advanced,
      professional
    )

lazy val naive: Project =
  project
    .in(file("01-naive"))
    .settings(commonScalacOptions)
    .settings(name := "naive")

lazy val standard: Project =
  project
    .in(file("02-standard"))
    .settings(commonScalacOptions)
    .settings(name := "standard")

lazy val advanced: Project =
  project
    .in(file("03-advanced"))
    .settings(commonScalacOptions)
    .settings(name := "advanced")

lazy val professional: Project =
  project
    .in(file("04-professional"))
    .settings(commonScalacOptions)
    .settings(
      name := "professional",
      libraryDependencies ++= Seq(
      )
    )

lazy val commonScalacOptions = Seq(
  Compile / console / scalacOptions --= Seq(
    "-Wunused:_",
    "-Xfatal-warnings"
  ),
  Test / console / scalacOptions := (Compile / console / scalacOptions).value
)
