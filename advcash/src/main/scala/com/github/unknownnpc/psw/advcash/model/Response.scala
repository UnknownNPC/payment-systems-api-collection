package com.github.unknownnpc.psw.advcash.model

import java.util.Date

sealed trait Response

case class BalancesResponse(wallets: Seq[WalletBalance]) extends Response
case class WalletBalance(id: String, amount: BigDecimal)

case class TransactionHistoryResponse(transactions: Seq[TransactionInfo]) extends Response

case class TransactionInfo(id: String, comment: String, startTime: Date,
                           status: TransactionStatus.Value, transactionName: TransactionName.Value,
                           sci: Boolean, walletSrcId: String, walletDestId: String,
                           senderEmail: String, receiverEmail: String, amount: BigDecimal, currency: Currency.Value,
                           fullCommission: BigDecimal, direction: Direction.Value, orderId: Option[String])
