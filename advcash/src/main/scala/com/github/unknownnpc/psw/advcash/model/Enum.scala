package com.github.unknownnpc.psw.advcash.model

sealed trait Enum

object TransactionName extends Enumeration with Enum {
  type TransactionNames = Value
  val ALL, CHECK_DEPOSIT, WIRE_TRANSFER_DEPOSIT, WIRE_TRANSFER_WITHDRAW,
  INNER_SYSTEM, CURRENCY_EXCHANGE, BANK_CARD_TRANSFER, ADVCASH_CARD_TRANSFER, EXTERNAL_SYSTEM_DEPOSIT,
  EXTERNAL_SYSTEM_WITHDRAWAL, REPAYMENT, UNKNOWN = Value
}

object TransactionStatus extends Enumeration with Enum {
  type TransactionStatuses = Value
  val PENDING, PROCESS, COMPLETED, CANCELED, CONFIRMED, UNKNOWN = Value
}

object Sort extends Enumeration with Enum {
  type Sort = Value
  val ASC, DESC = Value
}

object Currency extends Enumeration with Enum {
  type Sort = Value
  val USD, EUR, RUR, GBP, UAH, UNKNOWN = Value
}

object Direction extends Enumeration with Enum {
  type Sort = Value
  val INCOMING, OUTGOING, UNKNOWN = Value
}
