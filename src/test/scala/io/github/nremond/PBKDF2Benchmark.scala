package io.github.nremond

import org.scalameter.api._

object PBKDF2Benchmark extends PerformanceTest.Quickbenchmark {

  performance of "PBKDF2" in {

    val iterations = Gen.enumeration("iterations")(5000, 20000, 50000)

    measure method "encryption with SHA-256" in {
      using(iterations) in {
        PBKDF2("PleaseLetMeIn", "SodiumChloride", _)
      }
    }
  }
}