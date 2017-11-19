package com.android.markwhisperers.marketwhispererstradingjournal.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sihlemabaleka on 11/15/17.
 */

public class Trade {

    String tradeImage, tradePosition, tradePrice, tradeStopLoss, tradeTakeProfit, tradePair, tradePipsToStopLoss, tradePipsToTakeProfit, riskRewardRatio;

    public Trade() {
    }

    public Trade(String tradeImage, String tradePosition, String tradePrice, String tradeStopLoss, String tradeTakeProfit, String tradePair, String tradePipsToStopLoss, String tradePipsToTakeProfit, String riskRewardRatio) {
        this.tradeImage = tradeImage;
        this.tradePosition = tradePosition;
        this.tradePrice = tradePrice;
        this.tradeStopLoss = tradeStopLoss;
        this.tradeTakeProfit = tradeTakeProfit;
        this.tradePair = tradePair;
        this.tradePipsToStopLoss = tradePipsToStopLoss;
        this.tradePipsToTakeProfit = tradePipsToTakeProfit;
        this.riskRewardRatio = riskRewardRatio;
    }

    public String getTradePair() {
        return tradePair;
    }

    public void setTradePair(String tradePair) {
        this.tradePair = tradePair;
    }

    public String getTradeImage() {
        return tradeImage;
    }

    public void setTradeImage(String tradeImage) {
        this.tradeImage = tradeImage;
    }

    public String getTradePosition() {
        return tradePosition;
    }

    public void setTradePosition(String tradePosition) {
        this.tradePosition = tradePosition;
    }

    public String getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(String tradePrice) {
        this.tradePrice = tradePrice;
    }

    public String getTradeStopLoss() {
        return tradeStopLoss;
    }

    public void setTradeStopLoss(String tradeStopLoss) {
        this.tradeStopLoss = tradeStopLoss;
    }

    public String getTradeTakeProfit() {
        return tradeTakeProfit;
    }

    public void setTradeTakeProfit(String tradeTakeProfit) {
        this.tradeTakeProfit = tradeTakeProfit;
    }

    public String getTradePipsToStopLoss() {
        return tradePipsToStopLoss;
    }

    public void setTradePipsToStopLoss(String tradePipsToStopLoss) {
        this.tradePipsToStopLoss = tradePipsToStopLoss;
    }

    public String getTradePipsToTakeProfit() {
        return tradePipsToTakeProfit;
    }

    public void setTradePipsToTakeProfit(String tradePipsToTakeProfit) {
        this.tradePipsToTakeProfit = tradePipsToTakeProfit;
    }

    public String getRiskRewardRatio() {
        return riskRewardRatio;
    }

    public void setRiskRewardRatio(String riskRewardRatio) {
        this.riskRewardRatio = riskRewardRatio;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("image", tradeImage);
        result.put("position", tradePosition);
        result.put("price", tradePrice);
        result.put("stop_loss", tradeStopLoss);
        result.put("take_profit", tradeTakeProfit);
        result.put("pair", tradePair);
        result.put("pip_to_sl", tradePipsToStopLoss);
        result.put("pips_to_tp", tradePipsToTakeProfit);
        result.put("risk_reward", riskRewardRatio);
        return result;
    }

}
