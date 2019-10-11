package com.github.unknownnpc.psw.advcash.serializer

import com.github.unknownnpc.psw.advcash.model.{AuthInfo, BalancesResponse, TransactionHistoryRequest, TransactionHistoryResponse}
import com.github.unknownnpc.psw.api.Serializer
import org.apache.http.client.methods.HttpPost

private[advcash] object AdvCashSerializer {

  implicit val balancesReqResSerializer: Serializer[AuthInfo, BalancesResponse, HttpPost, String] =
    new BalancesReqResSerializer
  implicit val transactionHistoryReqResSerializer: Serializer[TransactionHistoryRequest, TransactionHistoryResponse,
    HttpPost, String] = new TransactionHistoryReqResSerializer

}
