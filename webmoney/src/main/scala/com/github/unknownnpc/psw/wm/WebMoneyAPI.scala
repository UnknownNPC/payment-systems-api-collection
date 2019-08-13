package com.github.unknownnpc.psw.wm

import java.util.Date

import com.github.unknownnpc.psw.api.{APIException, InvalidParam}
import com.github.unknownnpc.psw.wm.action.{X3Action, X9Action}
import com.github.unknownnpc.psw.wm.client.InsecureHttpClient
import com.github.unknownnpc.psw.wm.model.Model.X3.RequestOperation
import com.github.unknownnpc.psw.wm.model.Model.{X3, X9}
import com.github.unknownnpc.psw.wm.serializer.WebMoneySerializer._
import com.github.unknownnpc.psw.wm.signer.WmSigner
import org.apache.http.impl.client.CloseableHttpClient

class WebMoneyAPI(signer: WmSigner, wmid: String, httpClient: CloseableHttpClient) {

  /**
    * Select wm wallet history, interface X3:
    * https://wiki.webmoney.ru/projects/webmoney/wiki/%D0%98%D0%BD%D1%82%D0%B5%D1%80%D1%84%D0%B5%D0%B9%D1%81_X3
    *
    * @param wmid        the target WMID
    * @param dateStart   the date start
    * @param dateFinish  the date end
    * @param wmtranidOpt the opt field
    * @param tranidOpt   the opt field
    * @param wminvidOpt  the opt field
    * @param orderidOpt  the opt field
    * @return the payload or error
    */
  def runX3Command(wmid: String, dateStart: Date, dateFinish: Date, wmtranidOpt: Option[Long] = None,
                   tranidOpt: Option[Long] = None, wminvidOpt: Option[Long] = None, orderidOpt: Option[Long] = None): Either[APIException, X3.Response] = {
    val requestN = Utils.wmReqnGen.toString
    signer.sign(wmid + requestN) match {
      case Left(e) =>
        Left(InvalidParam(cause = e))
      case Right(signature) =>
        val operationOpts = List(
          Some(RequestOperation("purse", wmid)),
          Some(RequestOperation("datestart", Utils.WMDateFormatter.format(dateStart))),
          Some(RequestOperation("datefinish", Utils.WMDateFormatter.format(dateFinish))),
          wmtranidOpt.map(wmtranid => RequestOperation("wmtranid", wmtranid.toString)),
          tranidOpt.map(tranid => RequestOperation("tranid", tranid.toString)),
          wminvidOpt.map(wminvid => RequestOperation("wminvid", wminvid.toString)),
          orderidOpt.map(orderid => RequestOperation("orderid", orderid.toString))
        )

        val x3Request = X3.Request(wmid, signature, requestN, operationOpts.flatten)
        X3Action(httpClient).run(x3Request)
    }
  }

  /**
    * Selects wallets balance, interface X9:
    * https://wiki.webmoney.ru/projects/webmoney/wiki/%D0%98%D0%BD%D1%82%D0%B5%D1%80%D1%84%D0%B5%D0%B9%D1%81_X9
    *
    * @param wmid the target WMID
    * @return the payload or error
    */
  def runX9Command(wmid: String): Either[APIException, X9.Response] = {
    val requestN = Utils.wmReqnGen.toString
    signer.sign(wmid + requestN) match {
      case Left(e) =>
        Left(InvalidParam(cause = e))
      case Right(signature) =>
        X9Action(httpClient).run(
          X9.Request(wmid, signature, requestN)
        )
    }
  }

}

object WebMoneyAPI {

  def apply(wmid: String, password: String, kwmPath: String,
            httpClient: CloseableHttpClient = InsecureHttpClient.getInstance()): WebMoneyAPI = {
    val wmSigner = WmSigner(wmid, password, kwmPath)

    new WebMoneyAPI(wmSigner, wmid, httpClient)
  }

  def getInstance(wmid: String, password: String, kwmPath: String) =
    apply(wmid, password, kwmPath)

}
