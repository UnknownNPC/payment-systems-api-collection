package com.github.unknownnpc.psw.p24.serializer

import com.github.unknownnpc.psw.api.Serializer
import com.github.unknownnpc.psw.p24.model.P24Model.WalletHistory.{WalletHistoryRequest, WalletHistoryResponse}
import org.apache.http.client.methods.HttpPost

private[p24] object P24Serializer {

  implicit val walletHistoryReqResSerializer: Serializer[WalletHistoryRequest, WalletHistoryResponse, HttpPost, String] = new WalletHistoryReqResSerializer

}
