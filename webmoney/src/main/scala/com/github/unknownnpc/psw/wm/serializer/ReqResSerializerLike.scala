package com.github.unknownnpc.psw.wm.serializer

import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.{ContentType, StringEntity}

private[serializer] trait ReqResSerializerLike {

  def formPostReq(payload: String, urlTarget: String): HttpPost = {
    val httpPost = new HttpPost(urlTarget)
    httpPost.setEntity(new StringEntity(payload, ContentType.APPLICATION_XML))
    httpPost
  }

}
