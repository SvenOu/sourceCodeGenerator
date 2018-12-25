package com.sql.code.generator.commom.bean;

import java.io.Serializable;

public class Response<D, E> implements Serializable {
	private static final long serialVersionUID = -5568849565674522428L;
	protected boolean success = false;
	protected D data;
	protected String errorCode;
	protected E errors;
	protected String message;

	public Response() {
	}

	public Response(boolean success, D data, String errorCode, E errors,
			String message) {
		this.success = success;
		this.data = data;
		this.errorCode = errorCode;
		this.errors = errors;
		this.message = message;
	}

	public boolean isSuccess() {
		return this.success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public D getData() {
		return this.data;
	}

	public void setData(D data) {
		this.data = data;
	}

	public String getErrorCode() {
		return this.errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public E getErrors() {
		return this.errors;
	}

	public void setErrors(E errors) {
		this.errors = errors;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}