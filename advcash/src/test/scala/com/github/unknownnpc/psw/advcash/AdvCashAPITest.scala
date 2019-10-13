package com.github.unknownnpc.psw.advcash

import java.time.ZonedDateTime

import org.scalatest.{FunSpec, Matchers}
import org.scalatestplus.mockito.MockitoSugar

class AdvCashAPITest extends FunSpec with Matchers with MockitoSugar {

  private val advCashAPI = AdvCashAPI("1", "2", "3")

  describe("getAuthenticationToken") {

    it("should generate valid auth token") {
      val time = ZonedDateTime.parse("2011-12-03T10:15:30+00:00")
      advCashAPI.getAuthenticationToken(time) shouldBe "b6de69cfeb250e1a66acbbe1a536e5b6107f7d02a1012b91b1d227ca6a85fa3e"
    }

    it("should generate token with valid length") {
      val token = advCashAPI.getAuthenticationToken()
      token should not be null
      token should have length 64
    }

  }

}
