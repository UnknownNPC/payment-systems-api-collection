package com.github.unknownnpc.psw.api.executor

import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.util.EntityUtils


trait RestHttpExecutor[REQ <: HttpUriRequest] extends Executor[REQ, String] {

  def httpClient: CloseableHttpClient

  override def execute(req: REQ): String = {
    val httpResponse = httpClient.execute(req)
    EntityUtils.toString(httpResponse.getEntity)
  }

}
