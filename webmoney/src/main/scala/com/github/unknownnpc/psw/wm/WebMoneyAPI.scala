package com.github.unknownnpc.psw.wm

import java.util.Date

import com.github.unknownnpc.psw.api.{APIException, InvalidParam}
import com.github.unknownnpc.psw.wm.action.X3Action
import com.github.unknownnpc.psw.wm.model.Model.X3
import com.github.unknownnpc.psw.wm.model.Model.X3.RequestOperation
import com.github.unknownnpc.psw.wm.serializer.WebMoneySerializer._
import com.github.unknownnpc.psw.wm.signer.WmSigner
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}

class WebMoneyAPI(signer: WmSigner, wmid: String, httpClient: CloseableHttpClient) {

  /**
    * Select wm wallet history, interface X3:
    * https://wiki.webmoney.ru/projects/webmoney/wiki/%D0%98%D0%BD%D1%82%D0%B5%D1%80%D1%84%D0%B5%D0%B9%D1%81_X3
    *
    * @param walletId the target wallet
    * @param dateStart the date start
    * @param dateFinish the date end
    * @param wmtranidOpt the opt field
    * @param tranidOpt the opt field
    * @param wminvidOpt the opt field
    * @param orderidOpt the opt field
    * @return the payload or error
    */
  def runX3Command(walletId: String, dateStart: Date, dateFinish: Date, wmtranidOpt: Option[Long] = None,
                   tranidOpt: Option[Long] = None, wminvidOpt: Option[Long], orderidOpt: Option[Long]): Either[APIException, X3.Response] = {
    signer.sign(Utils.wmReqnGen.toString + walletId) match {
      case Left(e) =>
        Left(InvalidParam(cause = e))
      case Right(value) =>
        val operationOpts = List(
          Some(RequestOperation("purse", walletId)),
          Some(RequestOperation("datestart", Utils.WMDateFormatter.format(dateStart))),
          Some(RequestOperation("datefinish", Utils.WMDateFormatter.format(dateFinish))),
          wmtranidOpt.map(wmtranid => RequestOperation("wmtranid", wmtranid.toString)),
          tranidOpt.map(tranid => RequestOperation("tranid", tranid.toString)),
          wminvidOpt.map(wminvid => RequestOperation("wminvid", wminvid.toString)),
          orderidOpt.map(orderid => RequestOperation("orderid", orderid.toString))
        )

        val x3Request = X3.Request(wmid, value, operationOpts.flatten)
        X3Action(httpClient).run(x3Request)
    }
  }

}

object WebMoneyAPI {

  def apply(wmid: String, password: String, kwmPath: String,
            httpClient: CloseableHttpClient = HttpClients.createDefault()): WebMoneyAPI = {
    val wmSigner = WmSigner(wmid, password, kwmPath)

    new WebMoneyAPI(wmSigner, wmid, httpClient)
  }

  def getInstance(wmid: String, password: String, kwmPath: String) =
    apply(wmid, password, kwmPath)

}
