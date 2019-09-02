package com.github.unknownnpc.psw.p24

import java.util.Date

import com.github.unknownnpc.psw.p24.action.{RetrieveCardBalanceAction, RetrieveTransferHistoryAction}
import com.github.unknownnpc.psw.p24.model.WalletHistoryResponse
import org.mockito.ArgumentMatchersSugar.any
import org.mockito.Mockito.when
import org.scalatest.{FunSpec, Matchers}
import org.scalatestplus.mockito.MockitoSugar

class P24APITest extends FunSpec with Matchers with MockitoSugar {

  private val retrieveTransferHistoryAction: RetrieveTransferHistoryAction = mock[RetrieveTransferHistoryAction]
  private val retrieveCardBalanceAction: RetrieveCardBalanceAction = mock[RetrieveCardBalanceAction]
  private val p24API = new P24API(1, "pass", retrieveTransferHistoryAction, retrieveCardBalanceAction)

  describe("retrieveTransferHistory") {

    it("should run action") {
      val currentDate = new Date()
      val walletHistoryResponseMock: WalletHistoryResponse = mock[WalletHistoryResponse]
      when(retrieveTransferHistoryAction.run(any)(any)).thenReturn(Right(walletHistoryResponseMock))
      val response = p24API.retrieveTransferHistory("1", currentDate, currentDate)
      response.right.get shouldBe walletHistoryResponseMock
    }

  }

}
