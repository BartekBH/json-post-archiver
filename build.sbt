ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.6.4"

lazy val root = (project in file("."))
  .settings(name := "JsonPostArchiver")

val http4sVersion          = "0.23.30"
val circeVersion           = "0.14.12"
val catsEffectVersion      = "3.6.1"
val pureconfigVersion      = "0.17.8"
val logbackVersion         = "1.5.18"
val log4catsVersion        = "2.7.0"
val munitCatsEffectVersion = "2.1.0"

libraryDependencies ++= Seq(
  "org.http4s"            %% "http4s-ember-client"    % http4sVersion,
  "org.http4s"            %% "http4s-circe"           % http4sVersion,
  "io.circe"              %% "circe-generic"          % circeVersion,
  "io.circe"              %% "circe-parser"           % circeVersion,
  "org.typelevel"         %% "cats-effect"            % catsEffectVersion,
  "ch.qos.logback"         % "logback-classic"        % logbackVersion,
  "org.typelevel"         %% "log4cats-slf4j"         % log4catsVersion,
  "com.github.pureconfig" %% "pureconfig-core"        % pureconfigVersion,
  "com.github.pureconfig" %% "pureconfig-cats-effect" % pureconfigVersion,
  "org.http4s"            %% "http4s-dsl"             % http4sVersion          % Test,
  "org.typelevel"         %% "munit-cats-effect"      % munitCatsEffectVersion % Test
)

ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision
