name := "pbkdf2-scala"

organization := "io.github.nremond"                                        

version := "0.2"                                                       

scalaVersion := "2.10.1"   

/// ScalaTest
resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
    "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"
)   

/// ScalaMeter
resolvers += "Sonatype OSS Snapshots" at
  "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies += "com.github.axel22" %% "scalameter" % "0.3" % "test"

testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework")

