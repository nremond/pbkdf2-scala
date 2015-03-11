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
import java.util.Base64
import javax.crypto.spec.PBEKeySpec
import javax.crypto.SecretKeyFactory
import scala.io.Codec.UTF8
import scala.util.Try

case class SecureHash(iterations: Int = 20000, dkLength: Int = 32, cryptoAlgo: String = "HmacSHA256") {

  def createHash(password: String): String = {
    val random = new SecureRandom
    val salt = new Array[Byte](24) //192 bits
    random.nextBytes(salt)

    val hash = PBKDF2(password.getBytes(UTF8.charSet), salt, iterations, dkLength, cryptoAlgo)

    raw"${toHex(salt)}:${toHex(hash)}"
  }

  def validatePassword(password: String, hashedPassword: String): Boolean = {
    val params = hashedPassword.split(":")
    assert(params.size == 2)

    val salt = fromHex(params(0))
    val hash = PBKDF2(password.getBytes(UTF8.charSet), salt, iterations, dkLength, cryptoAlgo)
    params(1) == toHex(hash)
  }

}

object SecureHash {
  def encodeOutput(salt: Array[Byte], hash: Array[Byte], iterations: Int, algo: String): String = {
    val s = Base64.getEncoder.encodeToString(salt)
    val h = Base64.getEncoder.encodeToString(hash)
    val params = algo + toHex(ByteBuffer.allocate(4).putInt(iterations).array())
    s"s0$$$params$$$s$$$h"
  }

  type Decoded = (String, String, Int, Array[Byte], Array[Byte])
  val rx = "s0\\$([0-9a-fA-F]{8})([^\\$]+)\\$([^\\$]*)\\$([^\\$]*)".r
  val b64decoder = Base64.getDecoder.decode(_: String)
  def decode(s: String): Option[Decoded] = Try {
    s match {
      case rx(i, a, s, h) => Some(("s0", a, ByteBuffer.wrap(fromHex(i)).getInt, b64decoder(s), b64decoder(h)))
      case _              => None
    }
  }.toOption.getOrElse(None)
}
