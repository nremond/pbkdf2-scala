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

package io.github.nremond.legacy

import scala.io.Codec.UTF8
import io.github.nremond._

import java.security.SecureRandom

/**
 *  This is the legacy API.
 *
 * @param iterations the number of encryption iterations. Default to 20000
 * @param dkLength derived-key length, default to 32
 * @param cryptoAlgo HMAC+SHA256 is the default as HMAC+SHA1 is now considered weak
 */
case class SecureHash(iterations: Int = 20000, dkLength: Int = 32, cryptoAlgo: String = "HmacSHA256") {

  /**
   * Creates a hashed password using [[PBKDF2]]
   *
   * this function output a string in the following format:
   *
   * salt:key
   *
   *  - salt : hex encoded salt
   *
   *  - key : hex encoded derived key
   *
   *  Example :
   *
   *  a9c654289407047fd197516196e14b97bdabfa4bc934d0e9:f2f458b2502ca7595a4c964b14f146bd9c49174fa41b625227602bf4aaffbf5e
   *
   * @param password the password to hash
   */
  def createHash(password: String): String = {
    val random = new SecureRandom
    val salt = new Array[Byte](24) //192 bits
    random.nextBytes(salt)

    val hash = PBKDF2(password.getBytes(UTF8.charSet), salt, iterations, dkLength, cryptoAlgo)

    raw"${toHex(salt)}:${toHex(hash)}"
  }

  /**
   * Validate a password against a password hash
   *
   * this function will first try to validate with the *new* format as generated
   *  by [[io.github.nremond.SecureHash.createHash]]. if it fails, it will fall back to the *old* format,
   *  making this function very useful when transitioning form the *old* to the *new* format.
   *
   * @param password the password to validate
   * @param hashedPassword the password hash.
   * @return true is the password is valid
   */
  def validatePassword(password: String, hashedPassword: String): Boolean =
    //Try new format first and then fall back to legacy
    if (io.github.nremond.SecureHash.validatePassword(password, hashedPassword))
      true
    else
      legacyValidatePassword(password, hashedPassword)

  private def legacyValidatePassword(password: String, hashedPassword: String): Boolean = {
    val params = hashedPassword.split(":")
    assert(params.size == 2)
    val salt = fromHex(params(0))
    val hash = PBKDF2(password.getBytes(UTF8.charSet), salt, iterations, dkLength, cryptoAlgo)
    params(1) == toHex(hash)
  }
}
