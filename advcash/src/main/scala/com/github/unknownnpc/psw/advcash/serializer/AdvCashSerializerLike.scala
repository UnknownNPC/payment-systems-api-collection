package com.github.unknownnpc.psw.advcash.serializer

import java.text.SimpleDateFormat
import java.util.TimeZone

import com.github.unknownnpc.psw.advcash.model.AuthInfo
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.{ContentType, StringEntity}

import scala.xml.Elem

private[serializer] trait AdvCashSerializerLike {

  protected val ReqDateFormat = "yyyy-MM-dd'T'HH:mm:ss"
  protected val ResDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSSX"
  protected val reqDateFormatter = new SimpleDateFormat(ReqDateFormat) {
    setTimeZone(TimeZone.getTimeZone("GMT"))
  }
  protected val resDateFormatter = new SimpleDateFormat(ResDateFormat)
  protected val urlTarget: String = "https://wallet.advcash.com/wsm/merchantWebService?wsdl"

  def getArg0Xml(req: AuthInfo): Elem = {
    <arg0>
      <apiName>{req.apiName}</apiName>
      <authenticationToken>{req.authenticationToken}</authenticationToken>
      <accountEmail>{req.apiEmail}</accountEmail>
    </arg0>
  }

  def formHttpPostReq(payload: String): HttpPost = {
    val httpPost = new HttpPost(urlTarget)
    httpPost.setEntity(new StringEntity(payload, ContentType.APPLICATION_XML))
    httpPost
  }

}
