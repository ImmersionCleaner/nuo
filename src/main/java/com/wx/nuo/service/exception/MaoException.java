package com.wx.nuo.service.exception;

import java.util.Objects;

/**
 * 自定义异常
 */
public class MaoException extends Exception {
    private static final long serialVersionUID = -7886859620680170739L;
    /**
     * 错误码
     */
    private Integer errorCode;

    /**
     * 错误信息
     */
    private String info;

    public MaoException() {}

    public MaoException(Integer errorCode, String info) {
        this.errorCode = errorCode;
        this.info = info;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MaoException that = (MaoException)obj;
        return Objects.equals(errorCode, that.errorCode) && Objects.equals(info, that.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorCode, info);
    }
}
