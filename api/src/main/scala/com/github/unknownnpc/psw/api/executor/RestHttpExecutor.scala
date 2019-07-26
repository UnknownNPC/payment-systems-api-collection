package com.github.unknownnpc.psw.api.executor

import org.apache.http.client.methods.{HttpGet, HttpPost, HttpUriRequest}
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.util.EntityUtils


sealed trait RestHttpExecutor[REQ <: HttpUriRequest] extends Executor[REQ, String] {

  def httpClient: CloseableHttpClient

  override def execute(req: REQ): String = {
    val httpResponse = httpClient.execute(req)
    EntityUtils.toString(httpResponse.getEntity)
  }

}

trait GetRestHttpExecutor extends RestHttpExecutor[HttpGet]

trait PostRestHttpExecutor extends RestHttpExecutor[HttpPost]
