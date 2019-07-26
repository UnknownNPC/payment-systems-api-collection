package com.github.unknownnpc.psw.qiwi.serializer

import com.github.unknownnpc.psw.api.Serializer
import com.github.unknownnpc.psw.qiwi.model.QiwiModel.WalletHistory.{Request, Response}
import org.apache.http.client.methods.HttpGet

private[qiwi] object QiwiSerializer {

  implicit val walletHistoryReqResSerializer: Serializer[Request, Response, HttpGet, String] = new WalletHistoryReqResSerializer

}
