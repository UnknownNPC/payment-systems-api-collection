package com.github.unknownnpc.psw.api.executor

import com.typesafe.scalalogging.StrictLogging
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.util.EntityUtils


trait RestHttpExecutor[REQ <: HttpUriRequest] extends Executor[REQ, String] with StrictLogging {

  def httpClient: CloseableHttpClient

  override def execute(req: REQ): String = {
    logger.debug(s"Sending next request: [$req]")
    val httpResponse = httpClient.execute(req)
    val rawResponse = EntityUtils.toString(httpResponse.getEntity)
    logger.debug(s"Retrieved next response: [$rawResponse]")
    rawResponse
  }

}
