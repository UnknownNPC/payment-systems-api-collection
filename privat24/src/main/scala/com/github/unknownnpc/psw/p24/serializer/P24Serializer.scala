package com.github.unknownnpc.psw.p24.serializer

import com.github.unknownnpc.psw.api.Serializer
import com.github.unknownnpc.psw.p24.model.P24Model
import com.github.unknownnpc.psw.p24.model.P24Model.CardBalance
import com.github.unknownnpc.psw.p24.model.P24Model.WalletHistory.WalletHistoryResponse
import org.apache.http.client.methods.HttpPost

private[p24] object P24Serializer {

  implicit val walletHistoryReqResSerializer: Serializer[P24Model.Request, WalletHistoryResponse, HttpPost, String] = new WalletHistoryReqResSerializer
  implicit val cardBalanceReqResSerializer: Serializer[P24Model.Request, CardBalance.Response, HttpPost, String] = new CardBalanceReqResSerializer

}
