package com.github.unknownnpc.psw.qiwi.serializer

import com.github.unknownnpc.psw.api.APIParseException
import com.github.unknownnpc.psw.qiwi.model.AccountBalanceRequest
import org.scalatest.{FunSpec, Matchers}

class AccountBalanceReqResSerializerTest extends FunSpec with Matchers {

  it("should serialize to request correctly when all params exist") {
    val request = AccountBalanceRequest("token", "personId")
    val requestSample = QiwiSerializer.accountBalanceReqResSerializer.toReq(request).right.get
    requestSample should not be null
    requestSample.getAllHeaders.toList should have size 2
    requestSample.getURI.toString shouldBe "https://edge.qiwi.com/funding-sources/v2/persons/personId/accounts"
  }

  it("should serialize to request with valid headers") {
    val token = "token123"
    val request = AccountBalanceRequest(token, "wallet")
    val requestSample = QiwiSerializer.accountBalanceReqResSerializer.toReq(request).right.get
    requestSample.getAllHeaders.head.getName shouldBe "Authorization"
    requestSample.getAllHeaders.head.getValue shouldBe s"Bearer $token"
    requestSample.getAllHeaders.lift(1).get.getName shouldBe "Accept"
    requestSample.getAllHeaders.lift(1).get.getValue shouldBe "application/json"
  }

  it("serialize from response") {
    val responseStr =
      """{
        |  "accounts": [
        |    {
        |      "alias": "qw_wallet_rub",
        |      "fsAlias": "qb_wallet",
        |      "bankAlias": "QIWI",
        |      "title": "WALLET",
        |      "type": {
        |        "id": "WALLET",
        |        "title": "Visa QIWI Wallet"
        |      },
        |      "hasBalance": true,
        |      "balance": {
        |        "amount": 8.74,
        |        "currency": 643
        |      },
        |      "currency": 643,
        |      "defaultAccount": true
        |    }
        |  ]
        |}""".stripMargin

    val response = QiwiSerializer.accountBalanceReqResSerializer.fromRes(responseStr).right.get
    response.accounts.head.alias shouldBe "qw_wallet_rub"
    response.accounts.head.fsAlias shouldBe "qb_wallet"
    response.accounts.head.bankAlias shouldBe "QIWI"
    response.accounts.head.title shouldBe "WALLET"
    response.accounts.head.`type`.id shouldBe "WALLET"
    response.accounts.head.`type`.title shouldBe "Visa QIWI Wallet"
    response.accounts.head.hasBalance shouldBe true
    response.accounts.head.balance.amount shouldBe BigDecimal(8.74)
    response.accounts.head.balance.currency shouldBe 643
    response.accounts.head.currency shouldBe 643
    response.accounts.head.defaultAccount shouldBe true
  }

  it("should return error when response is invalid") {
    val result = QiwiSerializer.accountBalanceReqResSerializer.fromRes("asdasda").left.get
    result shouldBe a[APIParseException]
  }

}
