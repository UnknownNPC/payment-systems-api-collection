package com.github.unknownnpc.psw.p24

import java.text.SimpleDateFormat
import java.util.Date

import com.github.unknownnpc.psw.api.APIException
import com.github.unknownnpc.psw.p24.action.RetrieveTransferHistoryAction
import com.github.unknownnpc.psw.p24.model.P24Model.Merchant
import com.github.unknownnpc.psw.p24.model.P24Model.WalletHistory._
import com.github.unknownnpc.psw.p24.serializer.P24Serializer.walletHistoryReqResSerializer
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
    * @return the response payload or error
    */
  def retrieveTransferHistory(cardNum: String, from: Date, to: Date, waitVal: Long = 15): Either[APIException, WalletHistoryResponse] = {

    val request = WalletHistoryRequest(
      merchPass,
      Merchant(merchId, None),
      WalletHistoryRequestData(
        waitField = waitVal,
        payment =
          WalletHistoryRequestDataPayment(
            props = List(
              WalletHistoryRequestDataProp(WalletRequestHistoryFromDate, p24ReqDateFormatter.format(from)),
              WalletHistoryRequestDataProp(WalletRequestHistoryToDate, p24ReqDateFormatter.format(to)),
              WalletHistoryRequestDataProp(WalletRequestHistoryCardName, cardNum)
            )
          )
      )
    )

    RetrieveTransferHistoryAction(httpClient).run(request)
  }

}

object P24API {

  def apply(merchId: Long, merchPass: String,
            httpClient: CloseableHttpClient = HttpClients.createDefault()): P24API = new P24API(merchId, merchPass, httpClient)

  def getInstance(merchId: Long, merchPass: String) = apply(merchId, merchPass)

}
