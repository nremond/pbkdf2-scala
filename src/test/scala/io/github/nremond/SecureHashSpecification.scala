package io.github.nremond

import io.github.nremond.SecureHash._
import org.scalacheck.Prop.{ BooleanOperators, forAll }
import org.scalacheck.{ Gen, Properties }

object SecureHashSpecification extends Properties("SecureHash") {

  val iterationsGen = Gen.chooseNum(10000, 30000)
  val dkLengthGen = Gen.oneOf(16, 32, 64)

  property("createHash and validatePassword should always round trip") =
    forAll(Gen.alphaStr, iterationsGen, dkLengthGen) {
      (a: String, iterations: Int, dkl: Int) =>
        !a.isEmpty ==> validatePassword(a, createHash(a, iterations, dkl))
    }
}
