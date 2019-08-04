package com.github.unknownnpc.psw.wm

import java.text.SimpleDateFormat
import java.util.TimeZone

object Utils {

  def bytes2UShort(bytes: Array[Byte]): Int = {
    val b1 = 0x000000FF & bytes(0)
    val b2 = 0x000000FF & bytes(1)
    b2 << 8 | b1
  }

  def bytes2UInt(bytes: Array[Byte]): Int = {
    val b1 = 0x000000FF & bytes(0)
    val b2 = 0x000000FF & bytes(1)
    val b3 = 0x000000FF & bytes(2)
    val b4 = 0x000000FF & bytes(3)
    b4 << 24 | b3 << 16 | b2 << 8 | b1
  }

  def md4Hash(raw: Array[Byte]): Array[Byte] = {

    import sun.security.provider.MD4

    val md4Instance = MD4.getInstance()
    md4Instance.update(raw)
    md4Instance.digest()
  }

  def wmReqnGen: Long = {
    val currentNanoTime = System.nanoTime().toString
    (currentNanoTime.length until 15).foldRight(currentNanoTime){
      case (a, b) => b.concat("0")
    }.toLong
  }

  val WMDateFormatter: SimpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss") {
    setTimeZone(
      TimeZone.getTimeZone("GMT+3")
    )
  }

}
