[![Build Status](https://travis-ci.org/UnknownNPC/payment-systems-api-collection.svg?branch=master)](https://travis-ci.org/UnknownNPC/payment-systems-api-collection)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.unknownnpc.psw/api_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.unknownnpc.psw/api_2.12)

Payment Systems API Collection
=====================

## Overview
Multi-module sbt project with API for integration with banks/money systems (Qiwi, WM, P24).
Table below contains some more details about current implementation status.

_PS: project structure & code smell in some places. It happens because of luck of time. Sorry for that :3_

## Content
 
#### Install  
Requires Java 1.8 and sbt 1.2.8
```
sbt clean compile
```

#### Usage in pom.xml
```
<dependencies>
    <!-- QIWI API -->
    <dependency>
        <groupId>com.github.unknownnpc.psw</groupId>
        <artifactId>qiwi_2.12</artifactId>
        <version>X.X.X</version>
    </dependency>
    <!-- WebMoney API -->
    <dependency>
        <groupId>com.github.unknownnpc.psw</groupId>
        <artifactId>webmoney_2.12</artifactId>
        <version>X.X.X</version>
    </dependency>
    <!-- Privat24 API -->
    <dependency>
        <groupId>com.github.unknownnpc.psw</groupId>
        <artifactId>privat24_2.12</artifactId>
        <version>X.X.X</version>
    </dependency>
    <!-- AdvCash API -->
    <dependency>
        <groupId>com.github.unknownnpc.psw</groupId>
        <artifactId>advcash_2.12</artifactId>
        <version>X.X.X</version>
    </dependency>
</dependencies>
```
#### Usage in java code
```
P24API p24Api = P24API.getInstance(merchId, merchPass));
QiwiAPI qiwiApi = QiwiAPI.getInstance(qiwiApiToken);
// Requires KWM backup key(!)
WebMoneyAPI webMoneyApi = WebMoneyAPI.getInstance("wimd", "kwm_pass", kwmBytesArr);   
AdvCashAPI advCashAPI = AdvCashAPI.getInstance(api_name, api_password, api_email)     
```

#### API details
| Payment system  | Action/Tool | URL | Class | Check on live data|
| ------------- | ------------- |------|-------|------|
| Privat24  | Retrieve card history | [P24 API docs](https://api.privatbank.ua/#p24/orders) | p24.P24API#retrieveTransferHistory| _Yes_ |
| Privat24  | Retrieve card balance | [P24 API docs](https://api.privatbank.ua/#p24/balance) | p24.P24API#retrieveCardBalance| _Yes_ |
| Qiwi  | Retrieve payments |[QIWI API docs](https://developer.qiwi.com/ru/qiwi-wallet-personal/#payments_list)| qiwi.QiwiAPI#retrieveTransferHistory | _Partially_ |
| Qiwi  | Retrieve account balance |[QIWI API docs](https://developer.qiwi.com/ru/qiwi-wallet-personal/#balances_list)| qiwi.QiwiAPI#retrieveAccountBalance | _Yes_ |
| WebMoney  | Retrieve payments, interface X3  |[WM API docs](https://wiki.webmoney.ru/projects/webmoney/wiki/%D0%98%D0%BD%D1%82%D0%B5%D1%80%D1%84%D0%B5%D0%B9%D1%81_X3)| wm.WebMoneyAPI#runX3Command | _Partially_ |
| WebMoney  | Wallets balance, interface X9  |[WM API docs](https://wiki.webmoney.ru/projects/webmoney/wiki/%D0%98%D0%BD%D1%82%D0%B5%D1%80%D1%84%D0%B5%D0%B9%D1%81_X9)| wm.WebMoneyAPI#runX9Command | _Partially_ |
| WebMoney  | WM Signer  |[WM Signer docs](https://wiki.wmtransfer.com/projects/webmoney/wiki/WMSigner)| wm.signer.WMSigner | _Yes_ |
| AdvCash  | Retrieve cards balance | [AdsCash API v1.9](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=2&cad=rja&uact=8&ved=2ahUKEwikuuqs5pnlAhXGR5oKHaUXBp8QFjABegQIBBAC&url=https%3A%2F%2Fadvcash.com%2Ffiles%2Fdocuments%2Fadvcash.merchantapi-1.9_en.pdf&usg=AOvVaw1Xyjhrk61SUA-Rd4vl0_63) | advcash.AdvCashAPI#retrieveBalancePerWallets | _Yes_ |
| AdvCash | Retrieve transactions | [AdsCash API v1.9](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=2&cad=rja&uact=8&ved=2ahUKEwikuuqs5pnlAhXGR5oKHaUXBp8QFjABegQIBBAC&url=https%3A%2F%2Fadvcash.com%2Ffiles%2Fdocuments%2Fadvcash.merchantapi-1.9_en.pdf&usg=AOvVaw1Xyjhrk61SUA-Rd4vl0_63) | advcash.AdvCashAPI#retrieveTransactionsHistory | _Partially_ |
