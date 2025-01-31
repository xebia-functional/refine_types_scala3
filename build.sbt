
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
      iron,
      neotype
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

lazy val iron: Project = {
  project
    .in(file("04-iron"))
    .settings(commonSettings)
    .settings(
      name := "iron",
      libraryDependencies ++= Seq(
        "io.github.iltotore" %% "iron" % "2.6.0"
      )
    )
}

lazy val neotype: Project = {
  project
    .in(file("03-neotype"))
    .settings(commonSettings)
    .settings(
      name := "neotype",
      libraryDependencies ++= Seq(
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
