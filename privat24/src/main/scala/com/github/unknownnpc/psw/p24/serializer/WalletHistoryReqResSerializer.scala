package com.github.unknownnpc.psw.p24.serializer

import java.security.MessageDigest
import java.text.SimpleDateFormat

import com.github.unknownnpc.psw.api.Serializer
import com.github.unknownnpc.psw.p24.model.P24Model.Merchant
import com.github.unknownnpc.psw.p24.model.P24Model.WalletHistory._
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.{ContentType, StringEntity}

import scala.xml.{Elem, XML}

private[serializer] class WalletHistoryReqResSerializer extends Serializer[WalletHistoryRequest, WalletHistoryResponse, HttpPost, String] {

  private val urlTarget: String = "https://api.privatbank.ua/p24api/balance"

  private def encodeMessage(message: String, hashType: String): String = {
    val messageDigest = MessageDigest.getInstance(hashType)
    messageDigest.update(message.getBytes())
    messageDigest.digest().map("%02X".format(_)).mkString
  }

  private def formRequest(merchantXml: Elem, dataXml: Elem): Elem = {
    <request version="1.0">
      {merchantXml}{dataXml}
    </request>
  }

  private def merchantToXml(merchant: Merchant, dataXml: Elem, password: String): Elem = {
    <merchant>
      <id>
        {merchant.id}
      </id>
      <signature>
        {encodeMessage(encodeMessage(dataXml.toString() + password, "MD5"), "SHA1")}
      </signature>
    </merchant>
  }

  private def walletHistoryRequestDataToXml(walletHistoryRequestData: WalletHistoryRequestData): Elem = {

    def walletHistoryRequestDataPropToXml(walletHistoryRequestDataProp: WalletHistoryRequestDataProp): Elem = {
        <prop name={walletHistoryRequestDataProp.name} value={walletHistoryRequestDataProp.value}/>
    }

    <data>
      <oper>
        {walletHistoryRequestData.oper}
      </oper>
      <wait>
        {walletHistoryRequestData.waitField}
      </wait>
      <test>
        {walletHistoryRequestData.test}
      </test>
      <payment id={walletHistoryRequestData.payment.idAttr}>
        {walletHistoryRequestData.payment.props.map(walletHistoryRequestDataPropToXml)}
      </payment>
    </data>
  }

  /**
    * Form request related to next p24 doc:
    * https://api.privatbank.ua/#p24/orders
    *
    * @param walletHistoryReq the request entity.
    * @return the p24 request on the xml format.
    */
  override def toReq(walletHistoryReq: WalletHistoryRequest): HttpPost = {

    def formPostReq(payload: String): HttpPost = {
      val httpPost = new HttpPost(urlTarget)
      httpPost.setEntity(new StringEntity(payload, ContentType.APPLICATION_XML))
      httpPost
    }

    val header = "<xml version=\"1.0\" encoding=\"UTF-8\">"
    val dataXml = walletHistoryRequestDataToXml(walletHistoryReq.data)
    val merchantXml = merchantToXml(walletHistoryReq.merchant, dataXml, walletHistoryReq.merchantPassword)

    formPostReq(
      header + formRequest(merchantXml, dataXml).toString()
    )
  }

  val p24StatementDateFormatter = new SimpleDateFormat("yyyy-MM-dd")

  override def fromRes(out: String): WalletHistoryResponse = {
    val unPrettyOut = out.replaceAll(">\\s+<", "><")
    val responseXml = XML.loadString(unPrettyOut)

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
              p24StatementDateFormatter.parse((statementXml \ "@trandate").text),
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
