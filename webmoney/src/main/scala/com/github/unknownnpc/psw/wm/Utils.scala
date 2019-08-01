package com.github.unknownnpc.psw.wm

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
    System.nanoTime().toString.takeRight(15).toLong
  }

}
