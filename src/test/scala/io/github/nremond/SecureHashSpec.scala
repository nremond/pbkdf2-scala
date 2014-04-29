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

import org.scalatest._

import scala.io.Codec.UTF8

class SecureHashSpec extends FlatSpec with Matchers with Inspectors {

  val secureHash = SecureHash()

  val passwords = Vector("password", ":-( or :-)", "2¢", """H"qvVL5.y629_BA;1%:f/[OGo/B]x*UR2X:OUO3C/UKus$q.%$q@xmkJk&<_k+|
""")

  it should "be able to hash passwords and verify them" in {
    val hashedPasswords = passwords.map(secureHash.createHash)

    forAll(passwords.zip(hashedPasswords)) {
      case (pwd: String, hashedPwd: String) =>
        secureHash.validatePassword(pwd, hashedPwd) should be(true)
    }
  }

  it should "only validate correct password" in {
    val password = "secret_password"
    val incorrectHash = "dead:beef"
    secureHash.validatePassword(password, incorrectHash) should be(false)
  }

}