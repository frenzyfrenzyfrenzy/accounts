package com.svintsov.accounts.model;

public class GeneralResponse<T> {

    private boolean success;
    private String error;
    private T body;

    public GeneralResponse() {
    }

    public GeneralResponse(boolean success, String error, T body) {
        this.success = success;
        this.error = error;
        this.body = body;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public static <T> GeneralResponse<T> success(T body) {
        return new GeneralResponse<>(true, null, body);
    }

    public static <T> GeneralResponse<T> error(String error) {
        return new GeneralResponse<>(false, error, null);
    }
}
