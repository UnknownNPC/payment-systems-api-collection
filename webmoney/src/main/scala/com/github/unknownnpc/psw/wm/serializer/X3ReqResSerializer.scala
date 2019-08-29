package com.github.unknownnpc.psw.wm.serializer

import com.github.unknownnpc.psw.api.Utils.safeParse
import com.github.unknownnpc.psw.api.{APIParseException, Serializer}
import com.github.unknownnpc.psw.wm.model._
import org.apache.http.client.methods.HttpPost

import scala.xml.{Elem, XML}

private[serializer] class X3ReqResSerializer extends Serializer[X3Request, X3Response, HttpPost, String] with ReqResSerializerLike  {

  private val urlTarget: String = "https://w3s.webmoney.ru/asp/XMLOperations.asp"

  override def toReq(obj: X3Request): Either[APIParseException, HttpPost] = {

    def operation(op: X3RequestOperation): Elem = {
      scala.xml.XML.loadString(s"<${op.name}>${op.value}</${op.name}>")
    }

    def operations(operations: List[X3RequestOperation]): Elem = {
      <getoperations>
        {operations.map(operation)}
      </getoperations>
    }

    def xmlReq(obj: X3Request): Elem = {
      <w3s.request>
        <reqn>{obj.requestN}</reqn>
        <wmid>{obj.wmid}</wmid>
        <sign>{obj.signature}</sign>
        {operations(obj.operations)}
      </w3s.request>
    }

    safeParse {
      formPostReq(xmlReq(obj).toString(), urlTarget)
    }
  }

  override def fromRes(out: String): Either[APIParseException, X3Response] = {
    safeParse {
      val unPrettyOut = out.replaceAll(">\\s+<", "><")
      val responseXml = XML.loadString(unPrettyOut)
      X3Response(
        (responseXml \ "reqn").text.toLong,
        RetVal.values.find(v => v.id.toString == (responseXml \ "retval").text).get,
        (responseXml \ "retdesc").text,
        X3ResponseOperations(
          (responseXml \ "operations" \ "@cnt").text,
          (responseXml \ "operations" \ "operation").map(op => {
            X3OperationInfo(
              (op \ "@id").text,
              (op \ "@ts").text,
              op.child.map(c => {
                X3ResponseOperationType.values
                  .find(rot => rot.toString == c.label)
                  .getOrElse(X3ResponseOperationType.unknown) -> c.text
              }).toMap.filter(_._1 != X3ResponseOperationType.timelock),
              op.child.exists(n => n.label.eq("timelock"))
            )
          }).toList
        )
      )
    }
  }
}
