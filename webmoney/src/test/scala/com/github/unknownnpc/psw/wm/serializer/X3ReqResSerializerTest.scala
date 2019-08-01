package com.github.unknownnpc.psw.wm.serializer

import java.util.Date

import com.github.unknownnpc.psw.wm.Utils.WMDateFormatter
import com.github.unknownnpc.psw.wm.model.Model.X3
import com.github.unknownnpc.psw.wm.model.Model.X3.{RequestOperation, ResponseOperationType, RetVal}
import org.apache.http.util.EntityUtils
import org.scalatest.{FunSpec, Matchers}

class X3ReqResSerializerTest extends FunSpec with Matchers {

  it("serialize to request") {
    val testDate = new Date(0)
    val request = X3.Request("wmid", "sign", List(
      RequestOperation("purse", "walletId"),
      RequestOperation("datestart", WMDateFormatter.format(testDate)),
      RequestOperation("datefinish", WMDateFormatter.format(testDate)),
      RequestOperation("wmtranid", "wmtranid"),
      RequestOperation("tranid", "tranid"),
      RequestOperation("wminvid", "wminvid"),
      RequestOperation("orderid", "orderid")
    ))
    val requestSample = WebMoneySerializer.x3ReqResSerializer.toReq(request)
    requestSample should not be null
    requestSample.getAllHeaders.toList shouldBe List()
    val requestBody = EntityUtils.toString(requestSample.getEntity)
    requestBody should include(
      """<w3s.request>
        |        <reqn>""".stripMargin)
    requestBody should include(
      """    </reqn>
        |        <wmid>
        |          wmid
        |        </wmid>
        |        <sign>
        |          sign
        |        </sign><getoperations>
        |        <purse>walletId</purse><datestart>19700101 03:00:00</datestart><datefinish>19700101 03:00:00</datefinish><wmtranid>wmtranid</wmtranid><tranid>tranid</tranid><wminvid>wminvid</wminvid><orderid>orderid</orderid>
        |      </getoperations>
        |      </w3s.request>""".stripMargin)
  }

  it("serialize from response") {
    val rawResponse =
      """<w3s.response>
        |    <reqn>1</reqn>
        |    <retval>0</retval>
        |    <retdesc>dsa</retdesc>
        |    <operations cnt="n">
        |        <operation id="n1" ts="n2">
        |            <pursesrc>1</pursesrc>
        |            <pursedest>2</pursedest>
        |            <amount>3</amount>
        |            <comiss>4</comiss>
        |            <opertype>5</opertype>
        |            <wminvid>6</wminvid>
        |            <orderid>7</orderid>
        |            <tranid>8</tranid>
        |            <period>9</period>
        |            <desc>10</desc>
        |            <datecrt>11</datecrt>
        |            <dateupd>12</dateupd>
        |            <corrwm>13</corrwm>
        |            <rest>14</rest>
        |            <timelock/>
        |        </operation>
        |    </operations>
        |</w3s.response>
      """.stripMargin
    val result = WebMoneySerializer.x3ReqResSerializer.fromRes(rawResponse)
    result should not be null
    result.reqn shouldBe 1
    result.retval shouldBe RetVal.OK
    result.retdesc shouldBe "dsa"
    result.operations.details should have length 1
    result.operations.details.head.id shouldBe "n1"
    result.operations.details.head.ts shouldBe "n2"
    result.operations.details.head.isTimeLock shouldBe true
    result.operations.details.head.props should have size 14
    result.operations.details.head.props(ResponseOperationType.pursesrc) shouldBe "1"
    result.operations.details.head.props(ResponseOperationType.pursedest) shouldBe "2"
    result.operations.details.head.props(ResponseOperationType.amount) shouldBe "3"
    result.operations.details.head.props(ResponseOperationType.comiss) shouldBe "4"
    result.operations.details.head.props(ResponseOperationType.opertype) shouldBe "5"
    result.operations.details.head.props(ResponseOperationType.wminvid) shouldBe "6"
    result.operations.details.head.props(ResponseOperationType.orderid) shouldBe "7"
    result.operations.details.head.props(ResponseOperationType.tranid) shouldBe "8"
    result.operations.details.head.props(ResponseOperationType.period) shouldBe "9"
    result.operations.details.head.props(ResponseOperationType.desc) shouldBe "10"
    result.operations.details.head.props(ResponseOperationType.datecrt) shouldBe "11"
    result.operations.details.head.props(ResponseOperationType.dateupd) shouldBe "12"
    result.operations.details.head.props(ResponseOperationType.corrwm) shouldBe "13"
    result.operations.details.head.props(ResponseOperationType.rest) shouldBe "14"
  }

}