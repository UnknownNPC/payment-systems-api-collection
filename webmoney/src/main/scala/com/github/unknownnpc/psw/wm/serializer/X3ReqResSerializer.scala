package com.github.unknownnpc.psw.wm.serializer

import com.github.unknownnpc.psw.api.Serializer
import com.github.unknownnpc.psw.wm.model.Model.X3._
import com.github.unknownnpc.psw.wm.model.Model.{RetVal, X3}
import org.apache.http.client.methods.HttpPost

import scala.xml.{Elem, XML}

private[serializer] class X3ReqResSerializer extends Serializer[X3.Request, X3.Response, HttpPost, String] with ReqResSerializerLike  {

  private val urlTarget: String = "https://w3s.webmoney.ru/asp/XMLOperations.asp"

  override def toReq(obj: X3.Request): HttpPost = {

    def operation(op: X3.RequestOperation): Elem = {
      scala.xml.XML.loadString(s"<${op.name}>${op.value}</${op.name}>")
    }

    def operations(operations: List[X3.RequestOperation]): Elem = {
      <getoperations>
        {operations.map(operation)}
      </getoperations>
    }

    def xmlReq(obj: X3.Request): Elem = {
      <w3s.request>
        <reqn>{obj.requestN}</reqn>
        <wmid>{obj.wmid}</wmid>
        <sign>{obj.signature}</sign>
        {operations(obj.operations)}
      </w3s.request>
    }

    formPostReq(xmlReq(obj).toString(), urlTarget)
  }

  override def fromRes(out: String): X3.Response = {
    val unPrettyOut = out.replaceAll(">\\s+<", "><")
    val responseXml = XML.loadString(unPrettyOut)
    X3.Response(
      (responseXml \ "reqn").text.toLong,
      RetVal.values.find(v => v.id.toString == (responseXml \ "retval").text).get,
      (responseXml \ "retdesc").text,
      ResponseOperations(
        (responseXml \ "operations" \ "@cnt").text,
        (responseXml \ "operations" \ "operation").map(op => {
          OperationInfo(
            (op \ "@id").text,
            (op \ "@ts").text,
            op.child.map(c => {
              ResponseOperationType.values
                .find(rot => rot.toString == c.label)
                .getOrElse(ResponseOperationType.unknown) -> c.text
            }).toMap.filter(_._1 != ResponseOperationType.timelock),
            op.child.exists(n => n.label.eq("timelock"))
          )
        }).toList
      )
    )
  }
}
