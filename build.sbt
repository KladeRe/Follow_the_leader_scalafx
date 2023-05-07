// Name of the project
name := "ScalaFX Follow-the-leader"

// Project version
version := "19.0.0-R30"

// Version of Scala used by the project
scalaVersion := "3.2.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % Test
// Add dependency on ScalaFX library
libraryDependencies += "org.scalafx" % "scalafx_3" % "19.0.0-R30"

libraryDependencies +=
  "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4"
libraryDependencies += "com.github.plokhotnyuk.rtree2d" %% "rtree2d-core" % "0.11.12"
val circeVersion = "0.14.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

scalacOptions ++= Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature")

mainClass := Some("GUI.Scala")

lazy val osName = System.getProperty("os.name") match {
case n if n.startsWith("Linux") => "linux"
case n if n.startsWith("Mac") => "mac"
case n if n.startsWith("Windows") => "win"
case _ => throw new Exception("Unknown platform!")
}

lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
libraryDependencies ++= javaFXModules.map( m =>
"org.openjfx" % s"javafx-$m" % "14.0.1" classifier osName
)

// Fork a new JVM for 'run' and 'test:run', to avoid JavaFX double initialization problems
fork := true