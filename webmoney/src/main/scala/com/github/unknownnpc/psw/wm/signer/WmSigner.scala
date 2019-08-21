package com.github.unknownnpc.psw.wm.signer

import java.math.BigInteger

import com.github.unknownnpc.psw.api.InvalidParam
import com.github.unknownnpc.psw.wm.Utils
import javax.xml.bind.DatatypeConverter

import scala.util.{Random, Try}

private[wm] class WmSigner(wmid: String, kwmPass: String, kwmBytes: Array[Byte], nextRandBytes: Array[Byte] => Unit = Random.nextBytes) {

  private val reservedBytes: Array[Byte] = kwmBytes.slice(0, 2)
  private val crc: Array[Byte] = kwmBytes.slice(4, 20)
  private val lengthBytes: Array[Byte] = kwmBytes.slice(20, 24)
  private val length: Int = Utils.bytes2UInt(lengthBytes)
  private val buffer: Array[Byte] = kwmBytes.slice(24, kwmBytes.length)
  private val secureBuffer: Array[Byte] = getSecureBuffer(buffer)
  private val isValidCrc: Boolean = checkCrc(length, lengthBytes, reservedBytes, secureBuffer, crc)

  def sign(text: String): Either[Exception, String] = {

    if (isValidCrc) {
      Try {
        val exponentModulus = exponentAndModulus(secureBuffer)
        val textMd4 = Utils.md4Hash(text.getBytes)
        val random = Array.fill[Byte](40)(0)
        nextRandBytes(random)
        val baseBuff = Array.fill[Byte](58)(0)
          .patch(2, textMd4, 16)
          .patch(18, random, 40)
        baseBuff(0) = 0x38
        baseBuff(1) = 0x00

        val baseBuffReverse = buffReverse(baseBuff.length, baseBuff)
        val baseAsBigInt = new BigInteger(1, baseBuffReverse)

        val rawSignature = baseAsBigInt.modPow(exponentModulus._1, exponentModulus._2).toByteArray
        val hexSignature: String = DatatypeConverter.printHexBinary(rawSignature).toLowerCase
        val hexSignatureWithoutZeros = fillEmptyHexBytesWithZeros(hexSignature)
        reverseHexBytesOrder(hexSignatureWithoutZeros)
      }.toEither.left.map(e => InvalidParam(cause = e))
    } else {
      Left(InvalidParam(cause = new Exception("Invalid kwm key structure")))
    }
  }

  private def fillEmptyHexBytesWithZeros(hexSignature: String): String = {
    (0 until 132 - hexSignature.length).foldLeft(hexSignature) { case (arr, _) => "00" + arr }
  }

  private def reverseHexBytesOrder(hexSignature: String): String = {
    (0 until Math.ceil(hexSignature.length / 4).toInt).foldLeft("") {
      case (str, i) => hexSignature.substring(i * 4, i * 4 + 4) + str
    }
  }

  private def exponentAndModulus(securedBuffer: Array[Byte]) = {

    val eKeySecBufferSlice = (4, 6)
    val eKeyLengthBuff = Array.fill[Byte](2)(0).patch(0, securedBuffer.slice(eKeySecBufferSlice._1, eKeySecBufferSlice._2), 2)
    val eKeyLength = Utils.bytes2UShort(eKeyLengthBuff)

    val expSecBufferSlice = (6, 6 + eKeyLength)
    val exponent = Array.fill[Byte](eKeyLength)(0).patch(0, securedBuffer.slice(expSecBufferSlice._1, expSecBufferSlice._2), eKeyLength)
    val exponentAsBigInt = new BigInteger(1, buffReverse(eKeyLength, exponent))

    val mKeySecBufferSlice = (6 + eKeyLength, 6 + eKeyLength + 2)
    val mKeyLengthBuff = Array.fill[Byte](2)(0).patch(0, securedBuffer.slice(mKeySecBufferSlice._1, mKeySecBufferSlice._2), 2)
    val mKeyLength = Utils.bytes2UShort(mKeyLengthBuff)

    val modSecBufferSlice = (6 + eKeyLength + 2, 6 + eKeyLength + 2 + mKeyLength)
    val modulus = Array.fill[Byte](mKeyLength)(0).patch(0, securedBuffer.slice(modSecBufferSlice._1, modSecBufferSlice._2), mKeyLength)
    val modulusAsBigInt = new BigInteger(1, buffReverse(mKeyLength, modulus))

    (exponentAsBigInt, modulusAsBigInt)
  }

  private def buffReverse(length: Int, target: Array[Byte]): Array[Byte] = {
    (0 until length).toList.foldLeft(Array[Byte]()) { case (array, index) => array :+ target(length - 1 - index) }
  }

  // Weird staff is living here.
  private def getSecureBuffer(buffer: Array[Byte]): Array[Byte] = {
    val md4hashVal: Array[Byte] = Utils.md4Hash((wmid + kwmPass).getBytes)
    val result = Array[Byte]().patch(0, buffer, 6)
    var x = 0
    for (i <- 6 until buffer.length) yield {
      val b1 = buffer(i)
      val b2 = md4hashVal(x)
      result.update(i, (b1 ^ b2).toByte)
      x += 1
      if (x >= md4hashVal.length) x = 0
    }
    result
  }

  private def checkCrc(crcLength: Int, lengthBytes: Array[Byte], reservedBytes: Array[Byte], securedBuffer: Array[Byte], originCRC: Array[Byte]): Boolean = {
    val crcCheck = Array.fill[Byte](24 + crcLength)(0)
      .patch(0, reservedBytes, 2)
      .patch(20, lengthBytes, 4)
      .patch(24, securedBuffer, crcLength)

    val crcCheckMd4 = Utils.md4Hash(crcCheck)
    crcCheckMd4 sameElements originCRC
  }

}

object WmSigner {

  def apply(wmid: String, kwmPass: String, kwmBytes: Array[Byte]): WmSigner =
    new WmSigner(wmid, kwmPass, kwmBytes)

}
