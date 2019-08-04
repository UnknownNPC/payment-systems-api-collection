[![Build Status](https://travis-ci.org/UnknownNPC/payment-systems-wrapper.svg?branch=master)](https://travis-ci.org/UnknownNPC/payment-systems-wrapper)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.unknownnpc.psw/privat24_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.unknownnpc.psw/privat24_2.12)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.unknownnpc.psw/webmoney_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.unknownnpc.psw/webmoney_2.12)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.unknownnpc.psw/qiwi_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.unknownnpc.psw/qiwi_2.12)

payment-systems-wrapper
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
</dependencies>
```
#### Usage in java code
```
P24API p24Api = P24API.getInstance(1, "merchPass"));
QiwiAPI qiwiApi = QiwiAPI.getInstance("token");
// Requires KWM backup key(!)
WebMoneyAPI webMoneyApi = WebMoneyAPI.getInstance("wimd", "password", "/kwmPath/test.kwm");        
```

#### API details
| Payment system  | Action/Tool | URL | Class | Check on live data|
| ------------- | ------------- |------|-------|------|
| Privat24  | Retrieve card history | [P24 API docs](https://api.privatbank.ua/#p24/orders) | p24.P24API#retrieveTransferHistory| _Yes_ |
| Privat24  | Retrieve card balance | [P24 API docs](https://api.privatbank.ua/#p24/balance) | p24.P24API#retrieveCardBalance| _Yes_ |
| Qiwi  | Retrieve payments |[QIWI API docs](https://developer.qiwi.com/ru/qiwi-wallet-personal/#payments_list)| qiwi.QiwiAPI#retrieveTransferHistory | _Partially_ |
| Qiwi  | Retrieve account balance |[QIWI API docs](https://developer.qiwi.com/ru/qiwi-wallet-personal/#balances_list)| qiwi.QiwiAPI#retrieveAccountBalance | _Yes_ |
| WebMoney  | Retrieve payments, interface X3  |[WM API docs](https://wiki.webmoney.ru/projects/webmoney/wiki/%D0%98%D0%BD%D1%82%D0%B5%D1%80%D1%84%D0%B5%D0%B9%D1%81_X3)| wm.WebMoneyAPI#runX3Command | _Partially_ |
| WebMoney  | WM Signer  |[WM Signer docs](https://wiki.wmtransfer.com/projects/webmoney/wiki/WMSigner)| wm.signer.WMSigner | _Yes_ |
