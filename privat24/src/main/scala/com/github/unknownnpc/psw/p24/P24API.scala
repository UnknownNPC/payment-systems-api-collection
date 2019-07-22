package com.github.unknownnpc.psw.p24

import java.text.SimpleDateFormat
import java.util.Date

import com.github.unknownnpc.psw.p24.action.RetrieveTransferHistoryAction
import com.github.unknownnpc.psw.p24.model.P24Model.WalletHistory._
import com.github.unknownnpc.psw.p24.model.P24Model.{Merchant, P24Credential}
import com.github.unknownnpc.psw.p24.serializer.P24Serializer.walletHistoryReqResSerializer
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}

private[p24] class P24API(credentials: P24Credential, httpClient: CloseableHttpClient) {

  private val p24ReqDateFormatter = new SimpleDateFormat(WalletRequestHistoryDateFormat)

  def retrieveTransferHistory(cardNum: String, from: Date, to: Date): WalletHistoryResponse = {

    val request = WalletHistoryRequest(
      credentials.pass,
      Merchant(credentials.id, None),
      WalletHistoryRequestData(payment =
        WalletHistoryRequestDataPayment(
          props = List(
            WalletHistoryRequestDataProp(WalletRequestHistoryCardName, cardNum),
            WalletHistoryRequestDataProp(WalletRequestHistoryFromDate, p24ReqDateFormatter.format(from)),
            WalletHistoryRequestDataProp(WalletRequestHistoryToDate, p24ReqDateFormatter.format(to))
          )
        )
      )
    )

    RetrieveTransferHistoryAction(httpClient).run(request)
  }

}

object P24API {

  def apply(credentials: P24Credential,
            httpClient: CloseableHttpClient = HttpClients.createDefault()): P24API = new P24API(credentials, httpClient)

}
