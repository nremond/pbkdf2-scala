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

import java.nio.{ ByteBuffer, IntBuffer }
import javax.crypto

object PBKDF2 {

  private[this] def bytesFromInt(i: Int) = ByteBuffer.allocate(4).putInt(i).array

  private[this] def xorInPlace(buff: Array[Int], a2: Array[Byte]): Unit = {
    val b2 = ByteBuffer.wrap(a2).asIntBuffer

    val len = buff.array.size
    var i = 0
    while (i < len) {
      buff(i) ^= b2.get(i)
      i += 1
    }
  }

  /**
   * Implements PBKDF2 as defined in RFC 2898, section 5.2
   *
   * HMAC+SHA256 is used as the default pseudo random function.
   *
   * Right now 210000 iterations is the strictly recommended default minimum.
   * https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html#pbkdf2
   * The minimum increases every year, please keep that in mind.
   *
   * @param password the password to encrypt
   * @param salt the NIST recommends salt that is at least 128 bits(16 bytes) long (http://csrc.nist.gov/publications/nistpubs/800-132/nist-sp800-132.pdf)
   * @param iterations the number of encryption iterations
   * @param dkLength derived-key length
   * @param cryptoAlgo HMAC+SHA512 is the default and note that HMAC+SHA1 is now considered weak
   * @return the hashed password
   */
  def apply(password: Array[Byte], salt: Array[Byte], iterations: Int = 210000, dkLength: Int = 32, cryptoAlgo: String = "HmacSHA512"): Array[Byte] = {

    val mac = crypto.Mac.getInstance(cryptoAlgo)
    mac.init(new crypto.spec.SecretKeySpec(password, "RAW"))

      // pseudo-random function defined in the spec
      @inline def prf(buff: Array[Byte]) = mac.doFinal(buff)

      // this is a translation of the helper function "F" defined in the spec
      def calculateBlock(blockNum: Int): Array[Byte] = {
        // u_1
        val u_1 = prf(salt ++ bytesFromInt(blockNum))

        val buff = IntBuffer.allocate(u_1.length / 4).put(ByteBuffer.wrap(u_1).asIntBuffer).array.clone
        var u = u_1
        var iter = 1
        while (iter < iterations) {
          // u_2 through u_c : calculate u_n and xor it with the previous value
          u = prf(u)
          xorInPlace(buff, u)
          iter += 1
        }

        val ret = ByteBuffer.allocate(u_1.length)
        ret.asIntBuffer.put(buff)
        ret.array
      }

    // how many blocks we'll need to calculate (the last may be truncated)
    val blocksNeeded = (dkLength.toFloat / 20).ceil.toInt

    (1 to blocksNeeded).iterator.map(calculateBlock).flatten.take(dkLength).toArray
  }
}