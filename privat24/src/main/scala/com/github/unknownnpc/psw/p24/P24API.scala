package com.github.unknownnpc.psw.p24

import java.text.SimpleDateFormat
import java.util.{Date, Optional}

import com.github.unknownnpc.psw.api.APIException
import com.github.unknownnpc.psw.p24.action.{RetrieveCardBalanceAction, RetrieveTransferHistoryAction}
import com.github.unknownnpc.psw.p24.model._
import com.github.unknownnpc.psw.p24.serializer.P24Serializer._
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}

private[p24] class P24API(merchId: Long, merchPass: String, httpClient: CloseableHttpClient) {

  private val p24ReqDateFormatter = new SimpleDateFormat(WalletRequestHistoryDateFormat)

  /**
    * Requests card num history:
    * https://api.privatbank.ua/#p24/orders
    *
    * @param cardNum the target card num
    * @param from    the from date
    * @param to      the to date
    * @param waitVal the request await timeout
    * @return the response payload or error
    */
  def retrieveTransferHistory(cardNum: String, from: Date, to: Date, waitVal: Long = 15): Either[APIException, WalletHistoryResponse] = {

    val request = Request(
      merchPass,
      Merchant(merchId),
      RequestData(
        waitField = waitVal,
        payment =
          RequestDataPayment(
            props = List(
              RequestDataProp(WalletRequestHistoryFromDate, p24ReqDateFormatter.format(from)),
              RequestDataProp(WalletRequestHistoryToDate, p24ReqDateFormatter.format(to)),
              RequestDataProp(WalletRequestHistoryCardName, cardNum)
            )
          )
      )
    )

    RetrieveTransferHistoryAction(httpClient).run(request)
  }

  def retrieveTransferHistoryJava(cardNum: String, from: Date,
                                  to: Date, waitVal: Optional[java.lang.Long]): Either[APIException, WalletHistoryResponse] = {
    retrieveTransferHistory(cardNum, from, to, waitVal.orElse(15L))
  }

  /**
    * Retrieves card balance
    * https://api.privatbank.ua/#p24/balance
    *
    * @param cardNum the target card num
    * @param waitVal the request await timeout
    * @return the response payload or error
    */
  def retrieveCardBalance(cardNum: String, waitVal: Long = 15): Either[APIException, CardBalanceResponse] = {
    val request = Request(
      merchPass,
      Merchant(merchId),
      RequestData(
        waitField = waitVal,
        payment =
          RequestDataPayment(
            props = List(
              RequestDataProp(CardBalanceCardnum, cardNum),
              RequestDataProp(CardBalanceCountryKey, CardBalanceCountryVal)
            )
          )
      )
    )

    RetrieveCardBalanceAction(httpClient).run(request)
  }

  def retrieveCardBalanceJava(cardNum: String, waitVal: Optional[java.lang.Long]): Either[APIException, CardBalanceResponse] = {
    retrieveCardBalance(cardNum, waitVal.orElse(15L))
  }

}

object P24API {

  def apply(merchId: Long, merchPass: String,
            httpClient: CloseableHttpClient = HttpClients.createDefault()): P24API = new P24API(merchId, merchPass, httpClient)

  def getInstance(merchId: java.lang.Long, merchPass: String) = apply(merchId, merchPass)

}
