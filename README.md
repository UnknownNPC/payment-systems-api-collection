[![Build Status](https://travis-ci.org/UnknownNPC/payment-systems-wrapper.svg?branch=master)](https://travis-ci.org/UnknownNPC/payment-systems-wrapper)

payment-systems-wrapper
=====================

## Overview
Multi-module sbt project with API for integration with banks/money systems (Qiwi, WM, P24).
Table below contains some more details about current implementation status.

PS: project structure is not perfect, so I am planning to enhance it someday. =_=

## Content
 
#### Install  
Requires Java 1.8+ and sbt 1.2.8
```
sbt clean compile
```

#### Usage in pom.xml
```
<dependencies>
...
</dependencies>
```


#### API details
| Payment system  | Command/Tool | URL | Class |
| ------------- | ------------- |------|-------|
| Privat24  | Select card history | [P24 API docs](https://api.privatbank.ua/#p24/orders) | p24.P24API#retrieveTransferHistory|
| Qiwi  | Payments list  |[QIWI API docs](https://developer.qiwi.com/ru/qiwi-wallet-personal/#payments_list)| qiwi.QiwiAPI#retrieveTransferHistory |
| WebMoney  | Payments list, interface X3  |[WM API docs](https://wiki.webmoney.ru/projects/webmoney/wiki/%D0%98%D0%BD%D1%82%D0%B5%D1%80%D1%84%D0%B5%D0%B9%D1%81_X3)| wm.WebMoneyAPI#runX3Command |
| WebMoney  | WM Signer  |[WM Signer docs](https://wiki.wmtransfer.com/projects/webmoney/wiki/WMSigner)| wm.signer.WMSigner |
