/**
 *  Copyright 2013 Nicolas Rémond
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

import org.scalatest.{ FlatSpec, Matchers }

import scala.io.Codec.UTF8

/**
 * This spec contains test vectors for the Public-Key Cryptography
 * Standards (PKCS) #5 Password-Based Key Derivation Function 2 (PBKDF2)
 * with the Hash-based Message Authentication Code (HMAC) Secure Hash
 * Algorithm (SHA-1) pseudorandom function.
 *
 * See RFC6070 (http://tools.ietf.org/html/rfc6070)
 *
 */
class PBKDF2Spec extends FlatSpec with Matchers {

  def pbkdf2(password: String, salt: String, iterations: Int , dkLength: Int, cryptoAlgo: String ) = 
   toHex(PBKDF2(password.getBytes(UTF8.charSet), salt.getBytes(UTF8.charSet), iterations, dkLength, cryptoAlgo))

  it should "work with the 1st test vector" in {
    pbkdf2("password", "salt", 2, 20, "HmacSHA1") should equal("ea6c014dc72d6f8ccd1ed92ace1d41f0d8de8957")
  }

  it should "work with the 2nd test vector" in {
    pbkdf2("password", "salt", 4096, 20, "HmacSHA1") should equal("4b007901b765489abead49d926f721d065a429c1")
  }

  // It takes too long, I'm commenting it. 
  //  it should " "work with the 3rd test vector"" in {
  //    sha1Pbkdf2.encrypt("password", "salt", 16777216, 20) should equal("eefe3d61cd4da4e4e9945b3d6ba2158c2634e984")
  //  }

  it should "work with the 4th test vector" in {
    pbkdf2("passwordPASSWORDpassword", "saltSALTsaltSALTsaltSALTsaltSALTsalt", 4096, 25, "HmacSHA1") should equal("3d2eec4fe41c849b80c8d83662c0e44a8b291a964cf2f07038")
  }

  it should "work with the 5th test vector" in {
    pbkdf2("pass\u0000word", "sa\u0000lt", 4096, 16, "HmacSHA1") should equal("56fa6aa75548099dcc37d7f03425e0c3")
  }
}