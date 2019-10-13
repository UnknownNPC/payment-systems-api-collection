package com.github.unknownnpc.psw.advcash.serializer

import com.github.unknownnpc.psw.advcash.model.{BalancesResponse, AuthInfo, WalletBalance}
import com.github.unknownnpc.psw.api.Utils.safeParse
import com.github.unknownnpc.psw.api.{APIParseException, Serializer}
import org.apache.http.client.methods.HttpPost
import com.github.unknownnpc.psw.api.Utils.unPrettyOut

import scala.xml._

private[serializer] class BalancesReqResSerializer extends Serializer[AuthInfo, BalancesResponse, HttpPost, String] with AdvCashSerializerLike {

  override def toReq(req: AuthInfo): Either[APIParseException, HttpPost] = {

    def getReqXml(req: AuthInfo) = {
      <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                        xmlns:wsm="http://wsm.advcash/">
        <soapenv:Header/>
        <soapenv:Body>
          <wsm:getBalances>
            {getArg0Xml(req)}
          </wsm:getBalances>
        </soapenv:Body>
      </soapenv:Envelope>
    }

    safeParse {
      formHttpPostReq(getReqXml(req).toString())
    }
  }

  override def fromRes(out: String): Either[APIParseException, BalancesResponse] = {
    safeParse {
      val responseXml = XML.loadString(unPrettyOut(out))
      BalancesResponse(
        (responseXml \ "Body" \ "getBalancesResponse" \ "return").map(statementXml => {
          WalletBalance(
            (statementXml \ "id").text,
            BigDecimal((statementXml  \ "amount").text)
          )
        }).toList
      )
    }
  }

}
