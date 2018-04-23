package com.thenorthw.tc.common;

/**
 *
 * @author theNorthW
 * @date 06/04/2017
 * blog: thenorthw.com
 *
 */
public class ResponseModel<T> {
    private int responseCode = ResponseCode.OK.getCode();
    private String message = "request successfully return";
    private T data;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
