/**
 * Copyright 2012 Nicolas Rémond
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

package com.github.nremond

import javax.crypto
import java.math.BigInteger

object PBKDF2 {

  def apply(password: String, salt: String, iterations: Int, dkLength: Int): String =
    new PBKDF2(password, salt, iterations, dkLength).calculate
}

class PBKDF2(val password: String, val salt: String, val iterations: Int, val dkLength: Int) {

  val mac = crypto.Mac.getInstance("HmacSHA1")
  val saltBuff = salt.getBytes("UTF8")
  mac.init(new crypto.spec.SecretKeySpec(password.getBytes("UTF8"), "RAW"))

  def bytesFromInt(i: Int) = Array((i >>> 24).toByte, (i >>> 16).toByte, (i >>> 8).toByte, i.toByte)

  def xor(buff1: Array[Byte], buff2: Array[Byte]): Array[Byte] =
    buff1.zip(buff2) map { case (b1, b2) => (b1 ^ b2).toByte }

  // pseudo-random function defined in the spec
  def prf(buff: Array[Byte]) = mac.doFinal(buff)

  // this is a translation of the helper function "F" defined in the spec
  def calculateBlock(blockNum: Int): Array[Byte] = {
    // u_1
    val u_1 = prf(saltBuff ++ bytesFromInt(blockNum))

    // u_2 through u_c : calculate u_n and xor it with the previous value
    def u_n(u: Array[Byte]): Stream[Array[Byte]] = u #:: u_n(prf(u))
    u_n(u_1).take(iterations).reduceLeft(xor(_, _))
  }

  // the bit that actually does the calculating
  def calculate = {
    // how many blocks we'll need to calculate (the last may be truncated)
    val blocksNeeded = (dkLength.toFloat / 20).ceil.toInt

    (1 to blocksNeeded).map(calculateBlock(_).map("%02x" format _)).flatten.mkString.substring(0, dkLength * 2)
  }

}
