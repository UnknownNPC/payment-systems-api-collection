package com.github.unknownnpc.psw.advcash.serializer

import java.util.Date

import com.github.unknownnpc.psw.advcash.model._
import org.apache.http.util.EntityUtils
import org.scalatest.{FunSpec, Matchers}

class TransactionHistoryReqResSerializerTest extends FunSpec with Matchers {

  it("serialize to request") {
    val auth = AuthInfo("1", "2", "3")
    val now = new Date(10000)
    val request = TransactionHistoryRequest(auth, 0, 10, Sort.ASC, now, now, TransactionName.ALL,
      TransactionStatus.COMPLETED, Some("walletId"))
    val response = AdvCashSerializer.transactionHistoryReqResSerializer.toReq(request).right.get
    response should not be null
    response.getAllHeaders.toList shouldBe List()
    EntityUtils.toString(response.getEntity) shouldBe
      """<soapenv:Envelope xmlns:wsm="http://wsm.advcash/" xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
        |        <soapenv:Header/>
        |        <soapenv:Body>
        |          <wsm:history>
        |            <arg0>
        |              <apiName>1</apiName>
        |              <authenticationToken>2</authenticationToken>
        |              <accountEmail>3</accountEmail>
        |            </arg0>
        |            <arg1>
        |              <from>0</from>
        |              <count>10</count>
        |              <sortOrder>ASC</sortOrder>
        |              <startTimeFrom>1970-01-01T00:00:10</startTimeFrom>
        |              <startTimeTo>1970-01-01T00:00:10</startTimeTo>
        |              <transactionName>ALL</transactionName>
        |              <transactionStatus>COMPLETED</transactionStatus>
        |              <walletId>walletId</walletId>
        |            </arg1>
        |          </wsm:history>
        |        </soapenv:Body>
        |      </soapenv:Envelope>""".stripMargin
  }

  it("serialize from response") {

    val responseSample =
      s"""<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
      <ns2:historyResponse xmlns:ns2="http://wsm.advcash/">
         <return>
            <id>1575948b-6ead-426f-8ecf-ee7ffaa3969c</id>
            <comment>Comment</comment>
            <startTime>2014-03-25T10:21:59.901+00:00</startTime>
            <status>COMPLETED</status>
            <transactionName>CURRENCY_EXCHANGE</transactionName>
            <sci>false</sci>
            <walletSrcId>U993960083199</walletSrcId>
            <walletDestId>E060990630681</walletDestId>
            <senderEmail>U993960083199</senderEmail>
            <receiverEmail>E060990630681</receiverEmail>
            <amount>6.97</amount>
            <currency>USD</currency>
            <fullCommission>1.00</fullCommission>
            <direction>OUTGOING</direction>
         </return>
         <return>
            <id>20931ce4-f4c9-4cc5-84f7-f7efb38c939c</id>
            <comment>Comment</comment>
            <startTime>2014-03-25T10:21:59.901+00:00</startTime>
            <status>COMPLETED</status>
            <transactionName>BANK_CARD_TRANSFER</transactionName>
            <sci>false</sci>
            <walletSrcId>U993960083199</walletSrcId>
            <amount>10.00</amount>
            <currency>USD</currency>
            <fullCommission>5.00</fullCommission>
            <direction>OUTGOING</direction>
         </return>
         <return>
            <id>7514204c-d4fe-4617-ac79-241703443946</id>
            <comment>Comment</comment>
            <startTime>2014-03-25T10:21:59.901+00:00</startTime>
            <status>COMPLETED</status>
            <transactionName>ADVCASH_CARD_TRANSFER</transactionName>
            <sci>false</sci>
            <walletSrcId>U993960083199</walletSrcId>
            <amount>10.00</amount>
            <currency>USD</currency>
            <fullCommission>0.99</fullCommission>
            <direction>OUTGOING</direction>
         </return>
      </ns2:historyResponse>
   </soap:Body>
</soap:Envelope>""".stripMargin
    val response = AdvCashSerializer.transactionHistoryReqResSerializer.fromRes(responseSample).right.get
    response.transactions should have length 3
    response.transactions.head.id shouldBe "1575948b-6ead-426f-8ecf-ee7ffaa3969c"
    response.transactions.head.comment shouldBe "Comment"
    response.transactions.head.startTime.toInstant.toString shouldBe "2014-03-25T10:21:59.901Z"
    response.transactions.head.status shouldBe TransactionStatus.COMPLETED
    response.transactions.head.transactionName shouldBe TransactionName.CURRENCY_EXCHANGE
    response.transactions.head.sci shouldBe false
    response.transactions.head.walletSrcId shouldBe "U993960083199"
    response.transactions.head.walletDestId shouldBe "E060990630681"
    response.transactions.head.senderEmail shouldBe "U993960083199"
    response.transactions.head.receiverEmail shouldBe "E060990630681"
    response.transactions.head.amount shouldBe BigDecimal(6.97)
    response.transactions.head.currency shouldBe Currency.USD
    response.transactions.head.fullCommission shouldBe BigDecimal(1.00)
    response.transactions.head.direction shouldBe Direction.OUTGOING
    response.transactions.head.orderId shouldBe None
  }

}
