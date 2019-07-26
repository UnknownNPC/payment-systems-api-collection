package com.github.unknownnpc.psw.qiwi.serializer

import java.text.SimpleDateFormat
import java.util.Date

import com.github.unknownnpc.psw.api.Serializer
import com.github.unknownnpc.psw.qiwi.model.QiwiModel.WalletHistory.{Request, ResStatus, Response}
import org.apache.http.client.methods.HttpGet
import org.json4s.CustomSerializer
import org.json4s.JsonAST.JString

private[serializer] class WalletHistoryReqResSerializer extends Serializer[Request, Response, HttpGet, String] {

  private val urlTarget: String = "https://edge.qiwi.com/payment-history/v2/persons/%s/payments?rows=%s?"

  private val ReqDateFormat = "yyyy-MM-dd'T'HH:mm:ssXXX"
  private val OperationParam = "operation"
  private val SourcesParam = "sources"
  private val StartDateParam = "startDate"
  private val EndDateParam = "endDate"
  private val NextTxnDateParam = "nextTxnDate"
  private val NextTxnIdParam = "nextTxnId"
  private val qiwiReqDateFormatter = new SimpleDateFormat(ReqDateFormat)

  override def toReq(req: Request): HttpGet = {

    def queryParam(name: String, value: String): String = s"$name=$value"

    val paramsList = for {
      operation <- req.operation.map(t => queryParam(OperationParam, t.toString))
      sources <- Some(req.sources).map(sources => sources.map(s => queryParam(SourcesParam, s.id.toString)))
      startEndDates <- req.startEndDates.map(se => {
        val startStr = qiwiReqDateFormatter.format(se._1)
        val endStr = qiwiReqDateFormatter.format(se._2)
        List(queryParam(StartDateParam, startStr), queryParam(EndDateParam, endStr))
      })
      nextPage <- req.nextPage.map(np => {
        val nextTxnDateStr = qiwiReqDateFormatter.format(np._1)
        List(queryParam(NextTxnDateParam, nextTxnDateStr), queryParam(NextTxnIdParam, np._2.toString))
      })
    } yield List(operation) ++ sources ++ startEndDates ++ nextPage

    val paramsStr = paramsList.map(_.mkString("&")).getOrElse("")
    val fullRequestUrl = String.format(urlTarget, req.wallet, req.rows.toString) + paramsStr
    val httpGet = new HttpGet(fullRequestUrl)
    httpGet.setHeader("Authorization", "Basic " + req.apiToken)
    httpGet.setHeader("Accept", "application/json")

    httpGet
  }

  override def fromRes(out: String): Response = {
    import org.json4s._
    import org.json4s.native.Serialization
    import org.json4s.native.Serialization.read
    implicit val formats = Serialization.formats(NoTypeHints) + CustomDateSerializer + ResStatusSerializer

    read[Response](out)
  }

  object CustomDateSerializer extends CustomSerializer[Date](_ => ( {
    case JString(x) => qiwiReqDateFormatter.parse(x)
  }, {
    case x: Date => JString(qiwiReqDateFormatter.format(x))
  }))

  object ResStatusSerializer extends CustomSerializer[ResStatus.Value](_ => ( {
    case JString(x) => ResStatus.values.find(_.toString == x).getOrElse(ResStatus.ERROR)
  }, {
    case x: ResStatus.Value => JString(x.toString)
  }))

}
