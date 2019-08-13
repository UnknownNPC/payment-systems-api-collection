package com.github.unknownnpc.psw.wm.serializer

import com.github.unknownnpc.psw.api.Serializer
import com.github.unknownnpc.psw.wm.model.Model.{X3, X9}
import org.apache.http.client.methods.HttpPost

private[wm] object WebMoneySerializer {

  implicit val x3ReqResSerializer: Serializer[X3.Request, X3.Response, HttpPost, String] = new X3ReqResSerializer
  implicit val x9ReqResSerializer: Serializer[X9.Request, X9.Response, HttpPost, String] = new X9ReqResSerializer

}
