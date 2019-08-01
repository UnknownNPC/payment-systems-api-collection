package com.github.unknownnpc.psw.wm

import com.github.unknownnpc.psw.wm.model.KwmKeyType.KwmKeyType
import com.github.unknownnpc.psw.wm.signer.WmSigner

class WebMoneyAPI(signer: WmSigner, kwmType: KwmKeyType) {

}

object WebMoneyAPI {

  def apply(wmid: String, password: String, kwmPath: String, kwmType: KwmKeyType): WebMoneyAPI =
    new WebMoneyAPI(
      WmSigner(wmid, password, kwmPath), kwmType
    )

}
