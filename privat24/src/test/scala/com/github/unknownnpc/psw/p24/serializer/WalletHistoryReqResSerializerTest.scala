package com.github.unknownnpc.psw.p24.serializer

import com.github.unknownnpc.psw.p24.model.P24Model
import com.github.unknownnpc.psw.p24.model.P24Model.Merchant
import org.apache.http.util.EntityUtils
import org.scalatest.{FunSpec, Matchers}

class WalletHistoryReqResSerializerTest extends FunSpec with Matchers {

  it("serialize to request") {

    val waitField = 1
    val testFieldVal = 2
    val merchantIdVal = 3
    val operVal = "oper"
    val paymentId = "id"
    val cardPropName = "card"
    val cardPropVal = "cardNum"
    val fromPropName = "sd"
    val fromPropVal = "01.12.1992"
    val toPropName = "sd"
    val toPropVal = "01.12.1992"
    val request = P24Model.Request(
      "password",
      Merchant(merchantIdVal, Some("signature")),
      P24Model.RequestData(
        operVal,
        waitField,
        testFieldVal,
        P24Model.RequestDataPayment(
          paymentId,
          List(
            P24Model.RequestDataProp(cardPropName, cardPropVal),
            P24Model.RequestDataProp(fromPropName, fromPropVal),
            P24Model.RequestDataProp(toPropName, toPropVal)
          )
        )
      )
    )

    val requestSample = P24Serializer.walletHistoryReqResSerializer.toReq(request)

    requestSample should not be null
    requestSample.getAllHeaders.toList shouldBe List()
    EntityUtils.toString(requestSample.getEntity) shouldBe "<request version=\"1.0\"><xml version=\"1.0\" encoding=\"UTF-8\">" +
      "<merchant><id>3</id><signature>4868fb6901c599cea3598b9d4abf76987e9d1438</signature></merchant><data><oper>oper</oper>" +
      "<wait>1</wait><test>2</test><payment id=\"id\"><prop name=\"card\" value=\"cardNum\"></prop><prop name=\"sd\" " +
      "value=\"01.12.1992\"></prop><prop name=\"sd\" value=\"01.12.1992\"></prop></payment></data></xml></request>"
  }

  it("serialize from response") {

    val idVal = 1
    val signatureVal = "553995c5ccc8c81815b58cf6374f68f00a28bbd7"
    val operVal = "cmt"
    val statusVal = "excellent"
    val creditVal = "0.0"
    val debetVal = "0.3"
    val cardVal = "5168742060221193"
    val appCodeVal = "591969"
    val tranDateVal = "2013-09-02"
    val amountVal = "0.10 UAH"
    val cardAmountVal = "-0.10 UAH"
    val restVal = "0.95 UAH"
    val terminalVal = "Пополнение мобильного +380139917053 через «Приват24»"
    val descriptionVal = "desc"

    val responseSample =
      s"""<?xml version="1.0" encoding="UTF-8"?>
         |            <response version="1.0">
         |                <merchant>
         |                    <id>$idVal</id>
         |                    <signature>$signatureVal</signature>
         |                </merchant>
         |                <data>
         |                    <oper>$operVal</oper>
         |                    <info>
         |                        <statements status="$statusVal" credit="$creditVal" debet="$debetVal"  >
         |                            <statement card="$cardVal" appcode="$appCodeVal" trandate="$tranDateVal" amount="$amountVal" cardamount="$cardAmountVal" rest="$restVal" terminal="$terminalVal" description="$descriptionVal" />
         |                            <statement card="5168742060221193" appcode="991794" trandate="2013-09-02" amount="0.10 UAH" cardamount="-0.10 UAH" rest="1.05 UAH" terminal="Пополнение мобильного +380139917035 через «Приват24»" description="" />
         |                            <statement card="5168742060221193" appcode="801111" trandate="2013-09-02" amount="0.10 UAH" cardamount="-0.10 UAH" rest="1.15 UAH" terminal="Пополнение мобильного +380139910008 через «Приват24»" description="" />
         |                        </statements>
         |                    </info>
         |                </data>
         |            </response>
      """.stripMargin

    val result = P24Serializer.walletHistoryReqResSerializer.fromRes(responseSample)

    result should not be null
    result.merchant.id shouldBe idVal
    result.merchant.signature shouldBe Some(signatureVal)
    result.data.oper shouldBe operVal
    result.data.info.status shouldBe statusVal
    result.data.info.credit shouldBe creditVal
    result.data.info.debet shouldBe debetVal
    result.data.info.statements.head.card shouldBe cardVal
    result.data.info.statements.head.appcode shouldBe appCodeVal
    result.data.info.statements.head.trandate should not be null
    result.data.info.statements.head.amount shouldBe amountVal
    result.data.info.statements.head.cardamount shouldBe cardAmountVal
    result.data.info.statements.head.rest shouldBe restVal
    result.data.info.statements.head.terminal shouldBe terminalVal
    result.data.info.statements.head.description shouldBe descriptionVal
    result.data.info.statements should have size 3
  }

}
