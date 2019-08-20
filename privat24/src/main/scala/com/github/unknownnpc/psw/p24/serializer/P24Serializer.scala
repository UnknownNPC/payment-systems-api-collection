package com.github.unknownnpc.psw.p24.serializer

import com.github.unknownnpc.psw.api.Serializer
import com.github.unknownnpc.psw.p24.model.{CardBalanceResponse, Request, WalletHistoryResponse}
import org.apache.http.client.methods.HttpPost

private[p24] object P24Serializer {

  implicit val walletHistoryReqResSerializer: Serializer[Request, WalletHistoryResponse, HttpPost, String] = new WalletHistoryReqResSerializer
  implicit val cardBalanceReqResSerializer: Serializer[Request, CardBalanceResponse, HttpPost, String] = new CardBalanceReqResSerializer

}
