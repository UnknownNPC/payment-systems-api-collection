package com.github.unknownnpc.psw.wm.serializer

import com.github.unknownnpc.psw.api.Serializer
import com.github.unknownnpc.psw.wm.model.{X3Request, X3Response, X9Request, X9Response}
import org.apache.http.client.methods.HttpPost

private[wm] object WebMoneySerializer {

  implicit val x3ReqResSerializer: Serializer[X3Request, X3Response, HttpPost, String] = new X3ReqResSerializer
  implicit val x9ReqResSerializer: Serializer[X9Request, X9Response, HttpPost, String] = new X9ReqResSerializer

}
