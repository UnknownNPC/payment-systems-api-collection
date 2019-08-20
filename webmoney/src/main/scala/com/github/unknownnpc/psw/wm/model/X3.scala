package com.github.unknownnpc.psw.wm.model

trait X3

case class X3Request(wmid: String, signature: String, requestN: String, operations: List[X3RequestOperation])
case class X3RequestOperation(name: String, value: String)

object X3ResponseOperationType extends Enumeration {
  type ResponseOperationType = Val
  val pursesrc, pursedest, amount, comiss, opertype, wminvid, orderid,
  tranid, period, desc, datecrt, dateupd, corrwm, rest, unknown, timelock = Value
}

case class X3Response(reqn: Long, retval: RetVal.Value, retdesc: String, operations: X3ResponseOperations)
case class X3ResponseOperations(cnt: String, details: List[X3OperationInfo])
case class X3OperationInfo(id: String, ts: String, props: Map[X3ResponseOperationType.Value, String], isTimeLock: Boolean = false)
