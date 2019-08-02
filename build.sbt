name := "payment-systems-wrapper"
scalaVersion := "2.13.0"
organization in ThisBuild := "com.github.unknownnpc.psw"

lazy val global = project
  .in(file("."))
  .settings(settings ++ Seq(
    skip in publish := true
  ))
  .aggregate(
    api,
    privat24,
    qiwi,
    webmoney
  )

lazy val api = project
  .settings(
    name := "api",
    settings,
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.httpClient
    ),
    skip in publish := true
  )

lazy val privat24 = project
  .settings(
    name := "privat24",
    settings,
    mavenPublishSettings,
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.scalaXml
    )
  )
  .dependsOn(
    api
  )

lazy val qiwi = project
  .settings(
    name := "qiwi",
    settings,
    mavenPublishSettings,
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.json4s
    )
  )
  .dependsOn(
    api
  )

lazy val webmoney = project
  .settings(
    name := "webmoney",
    settings,
    mavenPublishSettings,
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.scalaXml
    )
  )
  .dependsOn(
    api
  )

lazy val dependencies =
  new {
    private val logbackV = "1.2.3"
    private val scalaLoggingV = "3.9.2"
    private val slf4jV = "1.7.25"
    private val typesafeConfigV = "1.3.4"
    private val catsV = "2.0.0-M4"
    private val scalatestV = "3.0.8"
    private val scalacheckV = "1.14.0"
    private val httpClientV = "4.5.9"
    private val scalaXmlV = "1.2.0"
    private val mockitoV = "1.5.12"
    private val json4sV = "3.6.7"

    val logback = "ch.qos.logback" % "logback-classic" % logbackV
    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingV
    val typesafeConfig = "com.typesafe" % "config" % typesafeConfigV
    val scalatest = "org.scalatest" %% "scalatest" % scalatestV
    val httpClient = "org.apache.httpcomponents" % "httpclient" % httpClientV
    val scalacheck = "org.scalacheck" %% "scalacheck" % scalacheckV
    val scalaXml = "org.scala-lang.modules" %% "scala-xml" % scalaXmlV
    val cats = "org.typelevel" %% "cats-core" % catsV
    val mockito = "org.mockito" %% "mockito-scala" % mockitoV
    val json4s = "org.json4s" %% "json4s-native" % json4sV
  }

lazy val commonDependencies = Seq(
  dependencies.logback,
  dependencies.scalaLogging,
  dependencies.typesafeConfig,
  dependencies.scalatest % "test",
  dependencies.scalacheck % "test",
  dependencies.mockito % "test"
)

lazy val settings =
  commonSettings

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
    Resolver.sonatypeRepo("releases")
  )
)

import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

lazy val mavenPublishSettings = Seq(
  homepage := Some(url("https://github.com/UnknownNPC/payment-systems-wrapper")),
  licenses := Seq("MIT" -> url("https://github.com/UnknownNPC/payment-systems-wrapper/LICENSE.md")),
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  publishTo in ThisBuild := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/UnknownNPC/payment-systems-wrapper"),
      "scm:git:git@github.com:UnknownNPC/payment-systems-wrapper.git"
    )
  ),
  developers := List(
    Developer("UnknownNPC", "Vitalii Zymukha", "unknownvzzv@gmai.com", url("https://github.com/UnknownNPC"))
  ),
  releaseCrossBuild := true,
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    publishArtifacts,
    setNextVersion,
    commitNextVersion,
    ReleaseStep(action = Command.process("sonatypeReleaseAll", _)),
    pushChanges
  )
)
