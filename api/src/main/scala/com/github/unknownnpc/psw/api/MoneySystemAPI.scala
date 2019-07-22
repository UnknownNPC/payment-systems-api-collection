package com.github.unknownnpc.psw.api

import com.github.unknownnpc.psw.api.model.{Request, Response}

trait MoneySystemAPI {

  def retrieveTransferHistory(request: Request): Either[APIException, Response]

}
