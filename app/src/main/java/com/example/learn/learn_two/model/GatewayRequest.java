package com.example.learn.learn_two.model;

import com.example.learn.learn_two.utils.AESUtils;

import java.io.Serializable;

/**
 * 请求网关基类
 * @author liyaohua
 * */
public class GatewayRequest implements Serializable {
    private String sn;
    private String terminalType;
    private String carModelId;
    private String sign;
    private long timestamp;
    private Object data;

    public GatewayRequest(String sn, String carModelId){
        this.sn = sn;
        this.carModelId = carModelId;
        this.timestamp = System.currentTimeMillis();
        this.sign = signData();
        this.terminalType="CAR";
        this.data = null;
    }

    private String signData() {
        return AESUtils.encrypt(this.timestamp+"");
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getCarModelId() {
        return carModelId;
    }

    public void setCarModelId(String carModelId) {
        this.carModelId = carModelId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
