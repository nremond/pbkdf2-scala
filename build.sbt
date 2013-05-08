name := "pbkdf2-scala"

version := "0.1"                                                       

organization := "com.github.nremond"                                        

scalaVersion := "2.10.1"   

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
    "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"
)   
