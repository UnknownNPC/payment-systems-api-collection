package com.github.unknownnpc.psw.advcash.model

import java.util.Date

sealed trait Request

case class AuthInfo(apiName: String, authenticationToken: String, apiEmail: String) extends Request

case class TransactionHistoryRequest(auth: AuthInfo,
                                     from: Int,
                                     count: Int,
                                     sortOrder: Sort.Value,
                                     startTimeFrom: Date,
                                     startTimeTo: Date,
                                     transactionName: TransactionName.Value,
                                     transactionStatus: TransactionStatus.Value,
                                     walletId: Option[String]) extends Request

