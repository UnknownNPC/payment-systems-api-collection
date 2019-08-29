package com.github.unknownnpc.psw.wm.serializer

import java.util.Date

import com.github.unknownnpc.psw.api.APIParseException
import com.github.unknownnpc.psw.wm.model.{RetVal, X9Request}
import org.apache.http.util.EntityUtils
import org.scalatest.{FunSpec, Matchers}

class X9ReqResSerializerTest extends FunSpec with Matchers {

  it("serialize to request") {
    val testDate = new Date(0)
    val request = X9Request("wmid", "sign", "reqN")
    val requestSample = WebMoneySerializer.x9ReqResSerializer.toReq(request).right.get
    requestSample should not be null
    requestSample.getAllHeaders.toList shouldBe List()

    val requestBody = EntityUtils.toString(requestSample.getEntity)

    requestBody shouldBe
      """<w3s.request>
        |        <reqn>reqN</reqn>
        |        <wmid>wmid</wmid>
        |        <sign>sign</sign>
        |        <getpurses>
        |          <wmid>wmid</wmid>
        |        </getpurses>
        |      </w3s.request>""".stripMargin
  }

  it("serialize from response") {
    val rawResponse =
      """
        |<w3s.response>
        |    <reqn>1</reqn>
        |    <retval>0</retval>
        |    <retdesc>3</retdesc>
        |    <purses cnt="10">
        |        <purse id="1">
        |            <pursename>1</pursename>
        |            <amount>2</amount>
        |            <desc>3</desc>
        |            <outsideopen>4</outsideopen>
        |            <lastintr>5</lastintr>
        |            <lastouttr>6</lastouttr>
        |        </purse>
        |    </purses>
        |</w3s.response>
      """.stripMargin

    val result = WebMoneySerializer.x9ReqResSerializer.fromRes(rawResponse).right.get

    result should not be null
    result.reqn shouldBe 1
    result.retval shouldBe RetVal.OK
    result.retdesc shouldBe "3"
    result.purses.cnt shouldBe "10"
    result.purses.details.head.pursename shouldBe "1"
    result.purses.details.head.amount shouldBe "2"
    result.purses.details.head.desc shouldBe "3"
    result.purses.details.head.outsideopen shouldBe "4"
    result.purses.details.head.lastintr shouldBe "5"
    result.purses.details.head.lastouttr shouldBe "6"
  }

  it("should return error when response is invalid") {
    val result = WebMoneySerializer.x9ReqResSerializer.fromRes("asdasda").left.get
    result shouldBe a[APIParseException]
  }

}