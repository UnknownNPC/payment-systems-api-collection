package com.github.unknownnpc.psw.qiwi.serializer

import java.util.Date

import com.github.unknownnpc.psw.qiwi.model.QiwiModel.WalletHistory.{ReqSources, ReqTransferType, Request, ResStatus}
import org.scalatest.{FunSpec, Matchers}

class WalletHistoryReqResSerializerTest extends FunSpec with Matchers {

  it("should serialize to request correctly when all params exist") {
    val fromDate = new Date(10000)
    val toDate = new Date(20000)
    val request = Request(
      "token", "wallet", 11, Some(ReqTransferType.IN), sources = List(ReqSources.QW_EUR, ReqSources.CARD),
      Some(fromDate, toDate), Some(toDate, 2L)
    )

    val requestSample = QiwiSerializer.walletHistoryReqResSerializer.toReq(request)

    requestSample should not be null
    requestSample.getAllHeaders.toList should have size 2
    requestSample.getURI.toString shouldBe "https://edge.qiwi.com/payment-history/v2/persons/wallet/payments?rows=11?" +
      "operation=IN&sources=2&sources=3&startDate=1970-01-01T01:00:10+01:00&endDate=1970-01-01T01:00:20+01:00&" +
      "nextTxnDate=1970-01-01T01:00:20+01:00&nextTxnId=2"
  }

  it("should serialize to request correctly when all params default") {
    val request = Request("token", "wallet", 11)
    val requestSample = QiwiSerializer.walletHistoryReqResSerializer.toReq(request)
    requestSample should not be null
    requestSample.getAllHeaders.toList should have size 2
    requestSample.getURI.toString shouldBe "https://edge.qiwi.com/payment-history/v2/persons/wallet/payments?rows=11?"
  }

  it("serialize from response") {

    val responseStr =
      """{"data":
        |	[
        |		{
        |		"txnId":9309,
        |		"personId":79112223344,
        |		"date":"2017-01-21T11:41:07+03:00",
        |		"errorCode":0,
        |		"error":null,
        |		"status":"SUCCESS",
        |		"type":"OUT",
        |		"statusText":"Успешно",
        |		"trmTxnId":"1489826461807",
        |		"account":"0003***",
        |		"sum":{
        |				"amount":70,
        |				"currency":"RUB"
        |				},
        |		"commission":{
        |				"amount":0,
        |				"currency":"RUB"
        |				},
        |		"total":{
        |				"amount":70,
        |				"currency":"RUB"
        |				},
        |		"provider":{
        |                       "id":26476,
        |                       "shortName":"Yandex.Money",
        |                       "longName":"ООО НКО \"Яндекс.Деньги\"",
        |                       "logoUrl":"https://static.qiwi.com/img/providers/logoBig/26476_l.png",
        |                       "description":"Яндекс.Деньги",
        |                       "keys":"***",
        |                       "siteUrl":null
        |                      },
        |		"comment":null,
        |		"currencyRate":1,
        |		"extras":null,
        |		"chequeReady":true,
        |		"bankDocumentAvailable":false,
        |		"bankDocumentReady":false,
        |   "repeatPaymentEnabled":false
        |		}
        |	],
        |	"nextTxnId":9001,
        |	"nextTxnDate":"2017-01-31T15:24:10+03:00"
        |}
      """.stripMargin

    val response = QiwiSerializer.walletHistoryReqResSerializer.fromRes(responseStr)

    response.nextTxnId shouldBe 9001
    response.nextTxnDate should not be null
    response.data.head.txnId shouldBe 9309L
    response.data.head.personId shouldBe 79112223344L
    response.data.head.date should not be null
    response.data.head.errorCode shouldBe 0
    response.data.head.error should be(null)
    response.data.head.status shouldBe ResStatus.SUCCESS
    response.data.head.statusText shouldBe "Успешно"
    response.data.head.trmTxnId shouldBe "1489826461807"
    response.data.head.account shouldBe "0003***"
    response.data.head.sum.amount.toString() shouldBe "70"
    response.data.head.sum.currency shouldBe "RUB"
    response.data.head.total.amount.toString() shouldBe "70"
    response.data.head.total.currency shouldBe "RUB"
    response.data.head.provider.id shouldBe 26476
    response.data.head.provider.shortName shouldBe "Yandex.Money"
    response.data.head.provider.longName shouldBe "ООО НКО \"Яндекс.Деньги\""
    response.data.head.provider.logoUrl shouldBe "https://static.qiwi.com/img/providers/logoBig/26476_l.png"
    response.data.head.provider.description shouldBe "Яндекс.Деньги"
    response.data.head.provider.keys shouldBe "***"
    response.data.head.provider.siteUrl should be(null)
    response.data.head.currencyRate.toString() shouldBe "1"
    response.data.head.chequeReady shouldBe Some(true)
    response.data.head.bankDocumentAvailable shouldBe Some(false)
    response.data.head.bankDocumentReady shouldBe Some(false)
    response.data.head.repeatPaymentEnabled shouldBe Some(false)
    response.data.head.favoritePaymentEnabled shouldBe None
    response.data.head.regularPaymentEnabled shouldBe None
  }

}
