package com.zxshare.gmall.service;

public interface OrderService {

    String getTradeCode(String menBerId);

    Boolean checkTradeCode(String menBerId,String TradeCode);
}
