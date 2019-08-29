package com.github.unknownnpc.psw.qiwi.serializer

import com.github.unknownnpc.psw.api.Utils.safeParse
import com.github.unknownnpc.psw.api.{APIParseException, Serializer}
import com.github.unknownnpc.psw.qiwi.model.{AccountBalanceRequest, AccountBalanceResponse}
import org.apache.http.client.methods.HttpGet

private[serializer] class AccountBalanceReqResSerializer extends Serializer[AccountBalanceRequest, AccountBalanceResponse, HttpGet, String] {

  private val urlTarget: String = "https://edge.qiwi.com/funding-sources/v2/persons/%s/accounts"

  override def toReq(req: AccountBalanceRequest): Either[APIParseException, HttpGet] = {
    safeParse {
      val fullRequestUrl = String.format(urlTarget, req.wallet)
      val httpGet = new HttpGet(fullRequestUrl)
      httpGet.setHeader("Authorization", "Bearer " + req.apiToken)
      httpGet.setHeader("Accept", "application/json")

      httpGet
    }
  }

  override def fromRes(out: String): Either[APIParseException, AccountBalanceResponse] = {
    import org.json4s._
    import org.json4s.native.Serialization
    import org.json4s.native.Serialization.read
    implicit val formats = Serialization.formats(NoTypeHints)
    safeParse {
      read[AccountBalanceResponse](out)
    }
  }

}
