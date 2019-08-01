package com.github.unknownnpc.psw.wm.model

object Model {

  object X3 {

    case class Request(wmid: String, sign: String, operations: List[RequestOperation])
    case class RequestOperation(name: String, value: String)

    object RetVal extends Enumeration {
      val OK = Value(0)
      val InvalidFormat = Value(-100)
      val InvalidIP = Value(-110)
      val InvalidWMID = Value(-1)
      val InvalidRequestGetOpPurse = Value(-2)
      val InvalidRequestSign = Value(-3)
      val InvalidRequestReqN = Value(-4)
      val InvalidSign = Value(-5)
      val InvalidRequestGetOpDateStart = Value(-7)
      val InvalidRequestGetOpDateFinish = Value(-8)
      val WMIDNotFound = Value(-9)
      val ReqNInvalidIncrement = Value(102)
      val UnknownWMIDWallet = Value(111)
      val RequestRangeIsTooLarge = Value(1004)
    }

    object ResponseOperationType extends Enumeration {
      type ResponseOperationType = Val
      val pursesrc, pursedest, amount, comiss, opertype, wminvid, orderid,
      tranid, period, desc, datecrt, dateupd, corrwm, rest, unknown, timelock  = Value
    }

    case class Response(reqn: Long, retval: RetVal.Value, retdesc: String, operations: ResponseOperations)
    case class ResponseOperations(cnt: String, details: List[OperationInfo])
    case class OperationInfo(id: String, ts: String, props: Map[ResponseOperationType.Value, String], isTimeLock: Boolean = false)
  }

}
