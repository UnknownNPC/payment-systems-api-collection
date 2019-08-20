package com.github.unknownnpc.psw.p24.serializer

import com.github.unknownnpc.psw.api.Serializer
import com.github.unknownnpc.psw.p24.model._
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.{ContentType, StringEntity}

import scala.xml.XML

private[serializer] class CardBalanceReqResSerializer extends Serializer[Request, CardBalanceResponse, HttpPost, String] with P24SerializerLike {

  private val urlTarget: String = "https://api.privatbank.ua/p24api/balance"

  override def toReq(obj: Request): HttpPost = {

    def formHttpPostReq(payload: String): HttpPost = {
      val httpPost = new HttpPost(urlTarget)
      httpPost.setEntity(new StringEntity(payload, ContentType.APPLICATION_XML))
      httpPost
    }

    formHttpPostReq(
      formRequestXmlStr(obj)
    )
  }

  override def fromRes(out: String): CardBalanceResponse = {
    val responseXml = XML.loadString(unPrettyOut(out))

    CardBalanceResponse(
      Merchant(
        (responseXml \ "merchant" \ "id").text.toLong,
        Option((responseXml \ "merchant" \ "signature").text)
      ),
      CardBalanceResponseData(
        (responseXml \ "data" \ "oper").text,
        CardBalanceResponseCardBalance(
          BigDecimal((responseXml \ "data" \ "info" \ "cardbalance" \ "av_balance").text),
          p24ResponseDateTimeFormatter.parse(
            (responseXml \ "data" \ "info" \ "cardbalance" \ "bal_date").text
          ),
          (responseXml \ "data" \ "info" \ "cardbalance" \ "bal_dyn").text,
          BigDecimal((responseXml \ "data" \ "info" \ "cardbalance" \ "balance").text),
          BigDecimal((responseXml \ "data" \ "info" \ "cardbalance" \ "fin_limit").text),
          BigDecimal((responseXml \ "data" \ "info" \ "cardbalance" \ "trade_limit").text)
        ),
        CardBalanceResponseCard(
          (responseXml \ "data" \ "info" \ "cardbalance" \ "card" \ "account").text,
          (responseXml \ "data" \ "info" \ "cardbalance" \ "card" \ "card_number").text,
          (responseXml \ "data" \ "info" \ "cardbalance" \ "card" \ "acc_name").text,
          (responseXml \ "data" \ "info" \ "cardbalance" \ "card" \ "acc_type").text,
          (responseXml \ "data" \ "info" \ "cardbalance" \ "card" \ "currency").text,
          (responseXml \ "data" \ "info" \ "cardbalance" \ "card" \ "card_type").text,
          (responseXml \ "data" \ "info" \ "cardbalance" \ "card" \ "main_card_number").text,
          (responseXml \ "data" \ "info" \ "cardbalance" \ "card" \ "card_stat").text,
          (responseXml \ "data" \ "info" \ "cardbalance" \ "card" \ "src").text
        )
      )
    )
  }

}
