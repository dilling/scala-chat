val scala3Version = "3.3.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala-chat",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
    libraryDependencies += "dev.zio" %% "zio" % "2.0.15",
    libraryDependencies += "dev.maxmelnyk" %% "openai-scala" % "0.3.0",
    libraryDependencies += "com.softwaremill.sttp.client3" %% "zio" % "3.8.16",
    libraryDependencies += "dev.zio" %% "zio-interop-cats" % "22.0.0.0",
    libraryDependencies += "dev.zio" %% "zio-config" % "3.0.7",
    libraryDependencies += "dev.zio" %% "zio-http" % "3.0.0-RC2"
  )
