package com.github.unknownnpc.psw.p24

import java.util.Date

import org.apache.http.client.methods.{CloseableHttpResponse, HttpPost}
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.mockito.ArgumentMatchersSugar.any
import org.mockito.Mockito.when
import org.scalatest.{FunSpec, Matchers}
import org.scalatestplus.mockito.MockitoSugar

/**
  * Should be enhanced in unit-test way
  */
class P24APITest extends FunSpec with Matchers with MockitoSugar {

  private val client: CloseableHttpClient = mock[CloseableHttpClient]
  private val p24API = P24API(1, "pass", client)

  describe("retrieveTransferHistory") {

    val responseSample =
      """<?xml version="1.0" encoding="UTF-8"?>
        |            <response version="1.0">
        |                <merchant>
        |                    <id>75482</id>
        |                    <signature>553995c5ccc8c81815b58cf6374f68f00a28bbd7</signature>
        |                </merchant>
        |                <data>
        |                    <oper>cmt</oper>
        |                    <info>
        |                        <statements status="excellent" credit="0.0" debet="0.3"  >
        |                            <statement card="5168742060221193" appcode="591969" trandate="2013-09-02" amount="0.10 UAH" cardamount="-0.10 UAH" rest="0.95 UAH" terminal="Пополнение мобильного +380139917053 через «Приват24»" description="" />
        |                            <statement card="5168742060221193" appcode="991794" trandate="2013-09-02" amount="0.10 UAH" cardamount="-0.10 UAH" rest="1.05 UAH" terminal="Пополнение мобильного +380139917035 через «Приват24»" description="" />
        |                            <statement card="5168742060221193" appcode="801111" trandate="2013-09-02" amount="0.10 UAH" cardamount="-0.10 UAH" rest="1.15 UAH" terminal="Пополнение мобильного +380139910008 через «Приват24»" description="" />
        |                        </statements>
        |                    </info>
        |                </data>
        |            </response>""".stripMargin

    it("should run action") {
      val rawResponseMock = mock[CloseableHttpResponse]
      val currentDate = new Date()
      when(rawResponseMock.getEntity).thenReturn(new StringEntity(responseSample))
      when(client.execute(any[HttpPost])).thenReturn(rawResponseMock)
      val response = p24API.retrieveTransferHistory("1", currentDate, currentDate)

      response should not be null
      response.right.get.merchant.id shouldBe 75482L
    }

  }

}
