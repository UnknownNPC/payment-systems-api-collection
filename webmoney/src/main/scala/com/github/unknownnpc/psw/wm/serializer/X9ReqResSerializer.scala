package com.github.unknownnpc.psw.wm.serializer

import com.github.unknownnpc.psw.api.Serializer
import com.github.unknownnpc.psw.wm.model.Model.X9.{ResponsePurse, ResponsePurses}
import com.github.unknownnpc.psw.wm.model.Model.{RetVal, X9}
import org.apache.http.client.methods.HttpPost

import scala.xml.{Elem, XML}

private[serializer] class X9ReqResSerializer extends Serializer[X9.Request, X9.Response, HttpPost, String] with ReqResSerializerLike {

  private val urlTarget: String = "https://w3s.webmoney.ru/asp/XMLPurses.asp"

  override def toReq(obj: X9.Request): HttpPost = {

    def xmlReq(obj: X9.Request): Elem = {
      <w3s.request>
        <reqn>{obj.requestN}</reqn>
        <wmid>{obj.wmid}</wmid>
        <sign>{obj.signature}</sign>
        <getpurses>
          <wmid>{obj.wmid}</wmid>
        </getpurses>
      </w3s.request>
    }

    formPostReq(xmlReq(obj).toString(), urlTarget)
  }

  override def fromRes(out: String): X9.Response = {
    val unPrettyOut = out.replaceAll(">\\s+<", "><")
    val responseXml = XML.loadString(unPrettyOut)
    X9.Response(
      (responseXml \ "reqn").text.toLong,
      RetVal.values.find(v => v.id.toString == (responseXml \ "retval").text).get,
      (responseXml \ "retdesc").text,
      ResponsePurses(
        (responseXml \ "purses" \ "@cnt").text,
        (responseXml \ "purses" \ "purse").map(op => {
          ResponsePurse(
            (op \ "@id").text,
            (op \ "pursename").text,
            (op \ "amount").text,
            (op \ "desc").text,
            (op \ "outsideopen").text,
            (op \ "lastintr").text,
            (op \ "lastouttr").text
          )
        }).toList
      )
    )
  }

}
