package com.github.unknownnpc.psw.p24.model

case class Merchant(id: Long, signature: Option[String] = None)

case class Request(merchantPassword: String, merchant: Merchant, data: RequestData)
case class RequestData(oper: String = "cmt", waitField: Long = 0, test: Long = 0, payment: RequestDataPayment)
case class RequestDataPayment(idAttr: String = "", props: List[RequestDataProp])
case class RequestDataProp(name: String, value: String)
