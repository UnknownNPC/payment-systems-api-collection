package com.github.unknownnpc.psw.wm

import java.util.{Date, Optional}

import com.github.unknownnpc.psw.api.{APIException, InvalidParam}
import com.github.unknownnpc.psw.wm.action.{X3Action, X9Action}
import com.github.unknownnpc.psw.wm.client.InsecureHttpClient
import com.github.unknownnpc.psw.wm.model._
import com.github.unknownnpc.psw.wm.serializer.WebMoneySerializer._
import com.github.unknownnpc.psw.wm.signer.WmSigner
import org.apache.http.impl.client.CloseableHttpClient

class WebMoneyAPI(signer: WmSigner, wmid: String,
                  x3Action: X3Action,
                  x9Action: X9Action) {

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
                   tranidOpt: Option[Long] = None, wminvidOpt: Option[Long] = None, orderidOpt: Option[Long] = None): Either[APIException, X3Response] = {
    val requestN = Utils.wmReqnGen.toString
    signer.sign(wmid + requestN) match {
      case Left(e) =>
        Left(InvalidParam(cause = e))
      case Right(signature) =>
        val operationOpts = List(
          Some(X3RequestOperation("purse", wmid)),
          Some(X3RequestOperation("datestart", Utils.WMDateFormatter.format(dateStart))),
          Some(X3RequestOperation("datefinish", Utils.WMDateFormatter.format(dateFinish))),
          wmtranidOpt.map(wmtranid => X3RequestOperation("wmtranid", wmtranid.toString)),
          tranidOpt.map(tranid => X3RequestOperation("tranid", tranid.toString)),
          wminvidOpt.map(wminvid => X3RequestOperation("wminvid", wminvid.toString)),
          orderidOpt.map(orderid => X3RequestOperation("orderid", orderid.toString))
        )

        val x3Request = X3Request(wmid, signature, requestN, operationOpts.flatten)
        x3Action.run(x3Request)
    }
  }

  def runX3CommandJava(wmid: String, dateStart: Date, dateFinish: Date,
                       wmtranidOpt: Optional[java.lang.Long],
                       tranidOpt: Optional[java.lang.Long],
                       wminvidOpt: Optional[java.lang.Long],
                       orderidOpt: Optional[java.lang.Long]): Either[APIException, X3Response] = {
    runX3Command(wmid, dateStart, dateFinish, Option(wmtranidOpt.orElse(null)),
      Option(tranidOpt.orElse(null)), Option(wminvidOpt.orElse(null)),
      Option(orderidOpt.orElse(null))
    )
  }

  /**
    * Selects wallets balance, interface X9:
    * https://wiki.webmoney.ru/projects/webmoney/wiki/%D0%98%D0%BD%D1%82%D0%B5%D1%80%D1%84%D0%B5%D0%B9%D1%81_X9
    *
    * @param wmid the target WMID
    * @return the payload or error
    */
  def runX9Command(wmid: String): Either[APIException, X9Response] = {
    val requestN = Utils.wmReqnGen.toString
    signer.sign(wmid + requestN) match {
      case Left(e) =>
        Left(InvalidParam(cause = e))
      case Right(signature) =>
        x9Action.run(
          X9Request(wmid, signature, requestN)
        )
    }
  }

  def runX9CommandJava(wmid: String): Either[APIException, X9Response] = {
    runX9Command(wmid)
  }

}

object WebMoneyAPI {

  def apply(wmid: String, password: String, kwmBytes: Array[Byte],
            httpClient: CloseableHttpClient = InsecureHttpClient.getInstance()): WebMoneyAPI = {

    val wmSigner = WmSigner(wmid, password, kwmBytes)
    val x3Action = X3Action(httpClient)
    val x9Action = X9Action(httpClient)

    new WebMoneyAPI(wmSigner, wmid, x3Action, x9Action)
  }

  def getInstance(wmid: String, password: String, kwmBytes: Array[Byte]) =
    apply(wmid, password, kwmBytes)

  def getInstance(wmid: String, password: String, kwmBytes: Array[Byte], httpClient: CloseableHttpClient) =
    apply(wmid, password, kwmBytes, httpClient)

}
