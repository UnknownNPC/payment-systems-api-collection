package com.github.unknownnpc.psw.wm.serializer

import com.github.unknownnpc.psw.api.Utils.safeParse
import com.github.unknownnpc.psw.api.{APIParseException, Serializer}
import com.github.unknownnpc.psw.wm.model._
import org.apache.http.client.methods.HttpPost

import scala.xml.{Elem, XML}

private[serializer] class X9ReqResSerializer extends Serializer[X9Request, X9Response, HttpPost, String] with ReqResSerializerLike {

  private val urlTarget: String = "https://w3s.webmoney.ru/asp/XMLPurses.asp"

  override def toReq(obj: X9Request): Either[APIParseException, HttpPost] = {

    def xmlReq(obj: X9Request): Elem = {
      <w3s.request>
        <reqn>{obj.requestN}</reqn>
        <wmid>{obj.wmid}</wmid>
        <sign>{obj.signature}</sign>
        <getpurses>
          <wmid>{obj.wmid}</wmid>
        </getpurses>
      </w3s.request>
    }

    safeParse {
      formPostReq(xmlReq(obj).toString(), urlTarget)
    }
  }

  override def fromRes(out: String): Either[APIParseException, X9Response] = {
    safeParse {
      val unPrettyOut = out.replaceAll(">\\s+<", "><")
      val responseXml = XML.loadString(unPrettyOut)
      X9Response(
        (responseXml \ "reqn").text.toLong,
        RetVal.values.find(v => v.id.toString == (responseXml \ "retval").text).get,
        (responseXml \ "retdesc").text,
        X9ResponsePurses(
          (responseXml \ "purses" \ "@cnt").text,
          (responseXml \ "purses" \ "purse").map(op => {
            X9ResponsePurse(
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

}
