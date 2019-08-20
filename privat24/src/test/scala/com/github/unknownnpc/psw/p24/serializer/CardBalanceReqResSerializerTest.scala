package com.github.unknownnpc.psw.p24.serializer

import com.github.unknownnpc.psw.p24.model._
import org.apache.http.util.EntityUtils
import org.scalatest.{FunSpec, Matchers}

class CardBalanceReqResSerializerTest extends FunSpec with Matchers {

  it("serialize to request") {
    val request = Request(
      "password",
      Merchant(1111, Some("signature")),
      RequestData(
        "oper",
        12,
        13,
        RequestDataPayment(
          "id2",
          List(
            RequestDataProp("cardnum", "111"),
            RequestDataProp("country", "UA")
          )
        )
      )
    )

    val requestSample = P24Serializer.cardBalanceReqResSerializer.toReq(request)

    requestSample should not be null
    requestSample.getAllHeaders.toList shouldBe List()
    EntityUtils.toString(requestSample.getEntity) shouldBe "<request version=\"1.0\"><xml version=\"1.0\" encoding=\"UTF-8\">" +
      "<merchant><id>1111</id><signature>7b66ce8e2a6f3faffa1163cbbe7b5962a50ffda0</signature></merchant><data><oper>oper</oper>" +
      "<wait>12</wait><test>13</test><payment id=\"id2\"><prop name=\"cardnum\" value=\"111\"></prop>" +
      "<prop name=\"country\" value=\"UA\"></prop></payment></data></xml></request>"
  }

  it("serialize from response") {
    val responseSample =
      s"""<?xml version="1.0" encoding="UTF-8"?>
            <response version="1.0">
                <merchant>
                    <id>75482</id>
                    <signature>bff932d0e97877619965283ed0d147c87a78b6c1</signature>
                </merchant>
                <data>
                    <oper>cmt</oper>
                    <info>
                        <cardbalance>
                        <card>
                        <account>5168742060221193</account>
                        <card_number>5168742060221193</card_number>
                        <acc_name>Карта для Выплат Gold</acc_name>
                        <acc_type>CC</acc_type>
                        <currency>UAH</currency>
                        <card_type>Карта для Выплат Gold</card_type>
                        <main_card_number>5168742060221193</main_card_number>
                        <card_stat>NORM</card_stat>
                        <src>M</src>
                        </card>
                        <av_balance>0.95</av_balance>
                        <bal_date>11.09.13 15:56</bal_date>
                        <bal_dyn>E</bal_dyn>
                        <balance>0.95</balance>
                        <fin_limit>0.00</fin_limit>
                        <trade_limit>0.00</trade_limit>
                        </cardbalance>
                    </info>
                </data>
            </response>""".stripMargin

    val result = P24Serializer.cardBalanceReqResSerializer.fromRes(responseSample)

    result should not be null
    result.merchant.id shouldBe 75482
    result.merchant.signature shouldBe Some("bff932d0e97877619965283ed0d147c87a78b6c1")
    result.data.oper shouldBe "cmt"
    result.data.cardBalance.finLimit shouldBe BigDecimal(0.00)
    result.data.cardBalance.balDyn shouldBe "E"
    result.data.cardBalance.balDate should not be null
    result.data.cardBalance.balance shouldBe BigDecimal(0.95)
    result.data.cardBalance.avBalance shouldBe BigDecimal(0.95)
    result.data.cardBalance.tradeLimit shouldBe BigDecimal(0.00)
    result.data.card.mainCardNumber shouldBe "5168742060221193"
    result.data.card.currency shouldBe "UAH"
    result.data.card.cardStat shouldBe "NORM"
    result.data.card.cardNumber shouldBe "5168742060221193"
    result.data.card.account shouldBe "5168742060221193"
    result.data.card.accType shouldBe "CC"
    result.data.card.cardType shouldBe "Карта для Выплат Gold"
    result.data.card.accName shouldBe "Карта для Выплат Gold"
    result.data.card.src shouldBe "M"
  }

}
