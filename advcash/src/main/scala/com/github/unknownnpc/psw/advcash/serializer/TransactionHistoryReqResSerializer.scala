package com.github.unknownnpc.psw.advcash.serializer

import com.github.unknownnpc.psw.advcash.model._
import com.github.unknownnpc.psw.api.Utils.{safeParse, unPrettyOut}
import com.github.unknownnpc.psw.api.{APIParseException, Serializer}
import org.apache.http.client.methods.HttpPost

import scala.xml.XML

class TransactionHistoryReqResSerializer extends Serializer[TransactionHistoryRequest, TransactionHistoryResponse, HttpPost, String]
  with AdvCashSerializerLike {

  override def toReq(obj: TransactionHistoryRequest): Either[APIParseException, HttpPost] = {

    def getReqXml(obj: TransactionHistoryRequest) = {
      <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                        xmlns:wsm="http://wsm.advcash/">
        <soapenv:Header/>
        <soapenv:Body>
          <wsm:history>
            <arg0>
              <apiName>{obj.auth.apiName}</apiName>
              <authenticationToken>{obj.auth.authenticationToken}</authenticationToken>
              <accountEmail>{obj.auth.apiEmail}</accountEmail>
            </arg0>
            <arg1>
              <from>{obj.from}</from>
              <count>{obj.count}</count>
              <sortOrder>{obj.sortOrder.toString}</sortOrder>
              <startTimeFrom>{reqDateFormatter.format(obj.startTimeFrom)}</startTimeFrom>
              <startTimeTo>{reqDateFormatter.format(obj.startTimeTo)}</startTimeTo>
              <transactionName>{obj.transactionName.toString}</transactionName>
              <transactionStatus>{obj.transactionStatus.toString}</transactionStatus>
              {obj.walletId.map(w => <walletId>{w}</walletId>).getOrElse("")}
            </arg1>
          </wsm:history>
        </soapenv:Body>
      </soapenv:Envelope>
    }

    val reqXml = getReqXml(obj)
    safeParse {
      formHttpPostReq(reqXml.toString())
    }
  }

  override def fromRes(out: String): Either[APIParseException, TransactionHistoryResponse] = {
    safeParse {
      val responseXml = XML.loadString(unPrettyOut(out))
      TransactionHistoryResponse(
        (responseXml \ "Body" \ "historyResponse" \ "return").map(statementXml => {
          TransactionInfo(
            (statementXml \ "id").text,
            (statementXml \ "comment").text,
            resDateFormatter.parse((statementXml \ "startTime").text),
            TransactionStatus.values.find(_.toString == (statementXml \ "status").text).getOrElse(TransactionStatus.UNKNOWN),
            TransactionName.values.find(_.toString == (statementXml \ "transactionName").text).getOrElse(TransactionName.UNKNOWN),
            (statementXml \ "sci").text.toBoolean,
            (statementXml \ "walletSrcId").text,
            (statementXml \ "walletDestId").text,
            (statementXml \ "senderEmail").text,
            (statementXml \ "receiverEmail").text,
            BigDecimal((statementXml \ "amount").text),
            Currency.values.find(_.toString == (statementXml \ "currency").text).getOrElse(Currency.UNKNOWN),
            BigDecimal((statementXml \ "fullCommission").text),
            Direction.values.find(_.toString == (statementXml \ "direction").text).getOrElse(Direction.UNKNOWN),
            if ((statementXml \ "orderId").text.isEmpty) None else Some((statementXml \ "orderId").text)
          )
        })
      )
    }
  }

}
