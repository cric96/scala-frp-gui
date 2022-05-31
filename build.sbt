ThisBuild / scalaVersion := "3.1.2"

name := "scala-frp-gui"

lazy val core = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .withoutSuffixFor(JVMPlatform)
  .settings(
    libraryDependencies ++= Seq(
      "io.monix" %%% "monix" % "3.4.0",
      "dev.optics" %%% "monocle-core" % "3.1.0",
      "dev.optics" %%% "monocle-macro" % "3.1.0"
    )
  )

lazy val swing = project.dependsOn(core.jvm)

lazy val js = project
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(core.js)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "2.2.0"
  )
