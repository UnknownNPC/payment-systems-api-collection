package com.github.unknownnpc.psw.wm

import org.scalatest.{FunSpec, Matchers}

class UtilsTest extends FunSpec with Matchers {

  it("bytes2UShort") {
    Utils.bytes2UShort(Array(0, 1)) shouldBe 256
    Utils.bytes2UShort(Array(1, 0)) shouldBe 1
  }

  it("bytes2UInt") {
    Utils.bytes2UInt(Array(0, 0, 0, 1)) shouldBe 16777216
    Utils.bytes2UInt(Array(1, 0, 0, 0)) shouldBe 1
  }

  it("wmReqnGen values") {
    (0 to 10000).forall(_ => {
      Utils.wmReqnGen <  Utils.wmReqnGen
    }) shouldBe true
  }

  it("wmReqnGen sizes") {
    (0 to 10000).forall(_ => {
      Utils.wmReqnGen.toString.length == 15
    }) shouldBe true
  }

}
