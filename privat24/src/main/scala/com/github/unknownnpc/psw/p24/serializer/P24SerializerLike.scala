package com.github.unknownnpc.psw.p24.serializer

import java.security.MessageDigest
import java.text.SimpleDateFormat

import com.github.unknownnpc.psw.p24.model.P24Model
import com.github.unknownnpc.psw.p24.model.P24Model.Merchant
import org.apache.commons.codec.binary.Hex

import scala.xml.{Elem, Node}

trait P24SerializerLike {

  val p24ResponseDateFormatter = new SimpleDateFormat("yyyy-MM-dd")

  def unPrettyOut(string: String): String = {
    string.replaceAll(">\\s+<", "><")
  }

  def formRequestXmlStr(req: P24Model.Request): String = {
    val dataXml = requestDataToXml(req.data)
    val merchantXml = merchantToXml(req.merchant, dataXml.child, req.merchantPassword)

    unPrettyOut(
      formRequestXml(merchantXml, dataXml).toString()
    )
  }

  private def encodeMessage(message: String, hashType: String): String = {
    val messageDigest = MessageDigest.getInstance(hashType)
    messageDigest.update(message.getBytes())
    new String(Hex.encodeHex(messageDigest.digest()))
  }

  private def formRequestXml(merchantXml: Elem, dataXml: Elem): Elem = {
    <request version="1.0">
      <xml version="1.0" encoding="UTF-8">
        {merchantXml}{dataXml}
      </xml>
    </request>
  }

  private def merchantToXml(merchant: Merchant, dataProps: Seq[Node], password: String): Elem = {

    val dataPropsStr: String = dataProps.map(d => unPrettyOut(d.toString()))
      .filter(d => !d.trim.isEmpty).mkString("")

    <merchant>
      <id>{merchant.id}</id>
      <signature>{encodeMessage(encodeMessage(dataPropsStr + password, "MD5"), "SHA1")}</signature>
    </merchant>
  }

  private def requestDataToXml(walletHistoryRequestData: P24Model.RequestData): Elem = {

    def walletHistoryRequestDataPropToXml(requestDataProp: P24Model.RequestDataProp): Elem = {
      <prop name={requestDataProp.name} value={requestDataProp.value}></prop>
    }

    <data>
      <oper>{walletHistoryRequestData.oper}</oper>
      <wait>{walletHistoryRequestData.waitField}</wait>
      <test>{walletHistoryRequestData.test}</test>
      <payment id={walletHistoryRequestData.payment.idAttr}>{walletHistoryRequestData.payment.props.map(walletHistoryRequestDataPropToXml)}</payment>
    </data>
  }

}
