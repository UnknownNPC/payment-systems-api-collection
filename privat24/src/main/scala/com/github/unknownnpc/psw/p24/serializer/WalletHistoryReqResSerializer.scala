package com.github.unknownnpc.psw.p24.serializer

import java.security.MessageDigest
import java.text.SimpleDateFormat

import com.github.unknownnpc.psw.api.Serializer
import com.github.unknownnpc.psw.p24.model.P24Model
import com.github.unknownnpc.psw.p24.model.P24Model.Merchant
import com.github.unknownnpc.psw.p24.model.P24Model.WalletHistory._
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.{ContentType, StringEntity}

import scala.xml.XML

/**
  * New lines/spaces sensitive. Do not auto-format.
  */
private[serializer] class WalletHistoryReqResSerializer extends Serializer[P24Model.Request, WalletHistoryResponse, HttpPost, String] with P24SerializerLike {

  private val urlTarget: String = "https://api.privatbank.ua/p24api/rest_fiz"

  /**
    * Form request related to next p24 doc:
    * https://api.privatbank.ua/#p24/orders
    *
    * @param walletHistoryReq the request entity.
    * @return the p24 request on the xml format.
    */
  override def toReq(req: P24Model.Request): HttpPost = {

    def formHttpPostReq(payload: String): HttpPost = {
      val httpPost = new HttpPost(urlTarget)
      httpPost.setEntity(new StringEntity(payload, ContentType.APPLICATION_XML))
      httpPost
    }

    val reqString = formRequestXmlStr(req)

    formHttpPostReq(reqString)
  }

  override def fromRes(out: String): WalletHistoryResponse = {
    val responseXml = XML.loadString(unPrettyOut(out))

    WalletHistoryResponse(
      Merchant(
        (responseXml \ "merchant" \ "id").text.toLong,
        Option((responseXml \ "merchant" \ "signature").text)
      ),
      WalletHistoryResponseData(
        (responseXml \ "data" \ "oper").text,
        WalletHistoryResponseInfo(
          (responseXml \ "data" \ "info" \ "statements" \ "@status").text,
          (responseXml \ "data" \ "info" \ "statements" \ "@credit").text,
          (responseXml \ "data" \ "info" \ "statements" \ "@debet").text,
          (responseXml \ "data" \ "info" \ "statements" \ "statement").map(statementXml => {
            WalletHistoryResponseStatementEntity(
              (statementXml \ "@card").text,
              (statementXml \ "@appcode").text,
              p24ResponseDateFormatter.parse((statementXml \ "@trandate").text),
              (statementXml \ "@amount").text,
              (statementXml \ "@cardamount").text,
              (statementXml \ "@rest").text,
              (statementXml \ "@terminal").text,
              (statementXml \ "@description").text
            )
          }).toList
        )
      )
    )
  }

}
