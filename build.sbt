ThisBuild / organization := "dagmendez"

ThisBuild / scalaVersion := "3.3.1"

ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

lazy val `opaque-types-and-inline`: Project =
  project
    .in(file("."))
    .aggregate(
      basic,
      standard,
      advanced,
      `scala-magic`,
      workshop
    )

lazy val basic: Project =
  project
    .in(file("01-basic"))
    .settings(commonScalacOptions)
    .settings(name := "basic")

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

lazy val `scala-magic`: Project =
  project
    .in(file("04-scala-magic"))
    .settings(commonScalacOptions)
    .settings(
      name := "scala-magic",
      libraryDependencies ++= Seq(
      )
    )

lazy val `workshop`: Project = {
  project
    .in(file("06-workshop"))
    .settings(commonScalacOptions)
    .settings(
      name := "workshop",
      libraryDependencies ++= Seq(
      )
    )
}

lazy val commonScalacOptions = Seq(
  Compile / console / scalacOptions --= Seq(
    "-Wunused:_",
    "-Xfatal-warnings"
  ),
  Test / console / scalacOptions := (Compile / console / scalacOptions).value
)
