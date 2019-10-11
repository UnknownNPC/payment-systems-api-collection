package com.github.unknownnpc.psw.advcash.serializer

import com.github.unknownnpc.psw.advcash.model.AuthInfo
import org.apache.http.util.EntityUtils
import org.scalatest.{FunSpec, Matchers}

class BalancesReqResSerializerTest extends FunSpec with Matchers {

  it("serialize to request") {
    val request = AuthInfo("1", "2", "3")
    val response = AdvCashSerializer.balancesReqResSerializer.toReq(request).right.get
    response should not be null
    response.getAllHeaders.toList shouldBe List()
    EntityUtils.toString(response.getEntity) shouldBe
      """<soapenv:Envelope xmlns:wsm="http://wsm.advcash/" xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
        |        <soapenv:Header/>
        |        <soapenv:Body>
        |          <wsm:getBalances>
        |            <arg0>
        |      <apiName>1</apiName>
        |      <authenticationToken>2</authenticationToken>
        |      <accountEmail>3</accountEmail>
        |    </arg0>
        |          </wsm:getBalances>
        |        </soapenv:Body>
        |      </soapenv:Envelope>""".stripMargin
  }

  it("serialize from response") {
    val walletId = "U170277900000"
    val walletAmount = "1.00"
    val responseSample =
      s"""<soap:Envelope
         |  xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
         |  <soap:Body>
         |    <ns2:getBalancesResponse xmlns:ns2="http://wsm.advcash/">
         |      <return>
         |        <amount>$walletAmount</amount>
         |        <id>$walletId</id>
         |      </return>
         |      <return>
         |        <amount>2.00</amount>
         |        <id>E046765100000</id>
         |      </return>
         |      <return>
         |        <amount>0.00</amount>
         |        <id>R104598900000</id>
         |      </return>
         |    </ns2:getBalancesResponse>
         |  </soap:Body>
         |</soap:Envelope>""".stripMargin
    val response = AdvCashSerializer.balancesReqResSerializer.fromRes(responseSample).right.get
    response.wallets should have size 3
    response.wallets.head.amount shouldBe BigDecimal(walletAmount)
    response.wallets.head.id shouldBe walletId
  }

}
