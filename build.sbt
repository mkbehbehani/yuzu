lazy val commonSettings = Seq(
  version := "0.1.0",
  scalaVersion := "2.11.8"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "yuzu",
    mainClass in assembly := Some("yuzu.Main")
  )

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor_2.11" % "2.4.0",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "com.sksamuel.elastic4s" %% "elastic4s-core" % "2.1.0",
  "com.typesafe.akka" % "akka-stream-experimental_2.11" % "1.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "me.tongfei" % "progressbar_2.11" % "0.3.2",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)

assemblyMergeStrategy in assembly := {
  case PathList("reference.conf") => MergeStrategy.concat
  case PathList(ps @ _*) if ps.last contains "BaseDateTime" => MergeStrategy.first
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
