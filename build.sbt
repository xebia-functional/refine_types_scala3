
ThisBuild / organization := "dagmendez"

ThisBuild / scalaVersion := "3.3.5"

ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

ThisBuild / scalacOptions ++= Seq("-Wunused:all")


lazy val `opaque-types-and-inline`: Project =
  project
    .in(file("."))
    .aggregate(
      `language-feature`,
      workshop,
      spanishIDs
    )

lazy val `language-feature`: Project =
  project
    .in(file("01-language-feature"))
    .settings(commonSettings)
    .settings(name := "language-feature")

lazy val `workshop`: Project = {
  project
    .in(file("02-workshop"))
    .settings(commonSettings)
    .settings(
      name := "workshop",
      libraryDependencies ++= Seq(
      )
    )
}

lazy val spanishIDs: Project = {
  project
    .in(file("spanishIDs"))
    .settings(commonSettings)
    .settings(
      name := "spanishIDs",
      libraryDependencies ++= Seq(
        "io.github.iltotore" %% "iron" % "2.6.0",
        "io.github.kitlangton" %% "neotype" % "0.3.11"
      )
    )
}

lazy val commonSettings = commonScalacOptions ++ Seq(resolvers += "confluent" at "https://packages.confluent.io/maven/")

lazy val commonScalacOptions = Seq(
  Compile / console / scalacOptions --= Seq(
    "-Xfatal-warnings",
    "-Wunused:_"
  ),
  Test / console / scalacOptions := (Compile / console / scalacOptions).value
)
