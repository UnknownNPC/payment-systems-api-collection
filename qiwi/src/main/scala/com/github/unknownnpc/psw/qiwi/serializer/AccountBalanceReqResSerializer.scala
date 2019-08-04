package com.github.unknownnpc.psw.qiwi.serializer

import com.github.unknownnpc.psw.api.Serializer
import com.github.unknownnpc.psw.qiwi.model.QiwiModel.AccountBalance.{Request, Response}
import org.apache.http.client.methods.HttpGet

private[serializer] class AccountBalanceReqResSerializer extends Serializer[Request, Response, HttpGet, String] {

  private val urlTarget: String = "https://edge.qiwi.com/funding-sources/v2/persons/%s/accounts"

  override def toReq(req: Request): HttpGet = {
    val fullRequestUrl = String.format(urlTarget, req.wallet)
    val httpGet = new HttpGet(fullRequestUrl)
    httpGet.setHeader("Authorization", "Bearer " + req.apiToken)
    httpGet.setHeader("Accept", "application/json")

    httpGet
  }

  override def fromRes(out: String): Response = {
    import org.json4s._
    import org.json4s.native.Serialization
    import org.json4s.native.Serialization.read
    implicit val formats = Serialization.formats(NoTypeHints)

    read[Response](out)
  }

}
