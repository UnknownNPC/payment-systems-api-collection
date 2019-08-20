package com.github.unknownnpc.psw.wm.model

case class X9Request(wmid: String, signature: String, requestN: String)

case class X9Response(reqn: Long, retval: RetVal.Value, retdesc: String, purses: X9ResponsePurses)

case class X9ResponsePurses(cnt: String, details: List[X9ResponsePurse])

case class X9ResponsePurse(id: String, pursename: String, amount: String, desc: String, outsideopen: String,
                           lastintr: String, lastouttr: String)
