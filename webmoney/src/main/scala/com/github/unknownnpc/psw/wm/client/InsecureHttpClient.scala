package com.github.unknownnpc.psw.wm.client

import org.apache.http.conn.ssl.{SSLConnectionSocketFactory, TrustSelfSignedStrategy}
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
import org.apache.http.ssl.SSLContextBuilder

/**
  * SSL-free HTTP client.
  */
object InsecureHttpClient {

  def getInstance(): CloseableHttpClient = {
    val builder = new SSLContextBuilder()
    builder.loadTrustMaterial(null, new TrustSelfSignedStrategy())
    val sslsf = new SSLConnectionSocketFactory(builder.build())
    HttpClients.custom().setSSLSocketFactory(sslsf).build()
  }

}
