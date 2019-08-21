package com.github.unknownnpc.psw.wm.signer

import org.scalatest.{FunSpec, Matchers}

class WmSignerTest extends FunSpec with Matchers {

  private val wmid = "405002833238"
  private val keyFilePath = "/test.kwm"
  private val keyPassword = "FvGqPdAy8reVWw789"
  private val testString = "TEST"
  private val testSignature = "642c2f71aafe930bcd238d925833414ccba29f2d408f3b77f1d7100111269865a100c6550258420734e96b4c11153ed597af9a28a066ffece8b50b0c5ffa15fe068f"

  it("should sign string correctly") {
    val signerWithoutRandom = new WmSigner(wmid, keyPassword, getFileBytes(keyFilePath), nextRandBytes = _ => ())
    signerWithoutRandom.sign(testString) shouldBe Right(testSignature)

    val signerWithRandom = new WmSigner(wmid, keyPassword, getFileBytes(keyFilePath))
    val signatureWithRand = signerWithRandom.sign(testString)
    signatureWithRand.right.get should have length 132
  }

  def getFileBytes(resource: String): Array[Byte] = {
    import java.nio.file.{Files, Paths}
    Files.readAllBytes(Paths.get(getClass.getResource(resource).toURI))
  }

}
