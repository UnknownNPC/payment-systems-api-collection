package com.github.unknownnpc.psw.wm.model

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
