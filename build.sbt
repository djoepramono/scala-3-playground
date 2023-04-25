name := "scala-3-playground"

name :="scala-3-playground"
version := "1.0"
scalaVersion := "3.2.2"

// only for Scala version lower than 2.13
//scalacOptions += "-Ypartial-unification"

libraryDependencies += "org.typelevel" %% "cats-core" % "2.9.0"
libraryDependencies += "org.typelevel" %% "cats-free" % "2.9.0"
libraryDependencies += "org.typelevel" %% "cats-effect" % "3.4.9"
