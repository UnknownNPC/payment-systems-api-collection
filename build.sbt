name := "payment-systems-wrapper"
version := "0.1"
scalaVersion := "2.13.0"
organization in ThisBuild := "com.github.unknownnpc"

lazy val global = project
  .in(file("."))
  .settings(settings)
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    api,
    privat24,
    qiwi
  )

lazy val api = project
  .settings(
    name := "api",
    settings,
    libraryDependencies ++= commonDependencies
  )
  .disablePlugins(AssemblyPlugin)

lazy val privat24 = project
  .settings(
    name := "privat24",
    settings,
    assemblySettings,
    libraryDependencies ++= commonDependencies ++ Seq()
  )
  .dependsOn(
    api
  )

lazy val qiwi = project
  .settings(
    name := "qiwi",
    settings,
    assemblySettings,
    libraryDependencies ++= commonDependencies ++ Seq()
  )
  .dependsOn(
    api
  )

lazy val dependencies =
  new {
    val logbackV = "1.2.3"
    val scalaLoggingV = "3.9.2"
    val slf4jV = "1.7.25"
    val typesafeConfigV = "1.3.4"
    val scalatestV = "3.0.8"
    val scalacheckV = "1.14.0"

    val logback = "ch.qos.logback" % "logback-classic" % logbackV
    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingV
    val typesafeConfig = "com.typesafe" % "config" % typesafeConfigV
    val scalatest = "org.scalatest" %% "scalatest" % scalatestV
    val scalacheck = "org.scalacheck" %% "scalacheck" % scalacheckV
  }

lazy val commonDependencies = Seq(
  dependencies.logback,
  dependencies.scalaLogging,
  dependencies.typesafeConfig,
  dependencies.scalatest % "test",
  dependencies.scalacheck % "test"
)

lazy val settings =
  commonSettings ++
    wartremoverSettings

lazy val compilerOptions = Seq(
  "-unchecked",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-deprecation",
  "-encoding",
  "utf8"
)

lazy val commonSettings = Seq(
  scalacOptions ++= compilerOptions,
  resolvers ++= Seq(
    Resolver.mavenLocal,
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  )
)

lazy val wartremoverSettings = Seq(
  wartremoverWarnings in (Compile, compile) ++= Warts
    .allBut(Wart.Throw, Wart.DefaultArguments)
)

lazy val assemblySettings = Seq(
  assemblyJarName in assembly := name.value + ".jar",
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case "application.conf"            => MergeStrategy.concat
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  }
)
