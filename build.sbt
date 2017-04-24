lazy val buildSettings = Seq(
  organization := "fincherror",
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.11.11"
)

lazy val circeVersion = "0.7.1"
lazy val finchVersion = "0.14.0"
lazy val twitterServerVersion = "1.28.0"

lazy val circeDependencies = Seq(
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-generic-extras" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion
)

lazy val restDependencies = Seq(
  "com.github.finagle" %% "finch-core" % finchVersion,
  "com.github.finagle" %% "finch-circe" % finchVersion,
  "com.twitter" %% "twitter-server" % twitterServerVersion
)

lazy val rest = project
  .settings(moduleName := "rest")
  .settings(buildSettings)
  .settings(libraryDependencies ++= circeDependencies)
  .settings(libraryDependencies ++= restDependencies)
