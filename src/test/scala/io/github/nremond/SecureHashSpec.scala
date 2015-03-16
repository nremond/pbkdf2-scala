/**
 *  Copyright 2012-2014 Nicolas Rémond (@nremond)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.github.nremond

import java.nio.ByteBuffer
import java.security.SecureRandom

import org.scalatest._

class SecureHashSpec extends FlatSpec with Matchers with Inspectors {

  "SecureHash.internals" should "encode the output properly" in {
    import SecureHash.internals._

    val salt = ByteBuffer.allocate(3).put(0.toByte).array()
    val out = encode(salt, salt, 22000, "Alg1")

    out should be("$p0$000055f0Alg1$AAAA$AAAA")
  }

  it should "decode the input properly" in {
    import SecureHash.internals._

    val Some(Decoded(version, iterations, algo, salt, hash)) = decode("$p0$000055f0Alg1$AAAA$AAAAAAAA")

    version should be("p0")
    algo should be("Alg1")
    iterations should be(22000)
    val zero = 0.toByte
    salt should be(Array[Byte](zero, zero, zero))
    hash should be(Array[Byte](zero, zero, zero, zero, zero, zero))
  }

  val passwords = Vector("password", ":-( or :-)", "2¢", """H"qvVL5.y629_BA;1%:f/[OGo/B]x*UR2X:OUO3C/UKus$q.%$q@xmkJk&<_k+|
""")

  it should "roundtrip " in {
    import SecureHash.internals._

      def getBytes(i: Int) = {
        val b = new Array[Byte](i)
        new SecureRandom().nextBytes(b)
        b
      }
    val salt = getBytes(32)
    val hash = getBytes(64)
    val output = encode(salt, hash, 100, "test")
    val Some(decoded) = decode(output)
    decoded.salt should be(salt)
    decoded.key should be(hash)
    decoded.iterations should be(100)
    decoded.algo should be("test")
  }

  "SecureHash" should "be able to hash passwords and verify them" in {
    val hashedPasswords = passwords.map(SecureHash.createHash(_))

    forAll(passwords.zip(hashedPasswords)) {
      case (pwd: String, hashedPwd: String) =>
        SecureHash.validatePassword(pwd, hashedPwd) should be(true)
    }
  }

  it should "only validate correct password" in {
    val password = "secret_password"
    val incorrectHash = "dead:beef"
    SecureHash.validatePassword(password, incorrectHash) should be(false)
  }

}