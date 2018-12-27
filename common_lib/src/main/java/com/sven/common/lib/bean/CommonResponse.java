package com.sven.common.lib.bean;

import java.io.Serializable;
import java.util.Map;

public class CommonResponse extends Response<Object, Map<String, Object>>
		implements Serializable {
	private static final long serialVersionUID = -4546350488380014155L;
	public static CommonResponse SIMPLE_SUCCESS = new CommonResponse(true,
			null, null, null, null);

	public static CommonResponse SIMPLE_FAILURE = new CommonResponse(false,
			null, null, null, null);

	public CommonResponse() {
	}

	public static CommonResponse success(String message) {
		return new CommonResponse(true, null, null, null, message);
	}

	public static CommonResponse success(Object data) {
		return new CommonResponse(true, data, null, null, null);
	}

	public static CommonResponse success(Object data, String message) {
		return new CommonResponse(true, data, null, null, message);
	}

	public static CommonResponse failure(String errorCode) {
		return new CommonResponse(false, null, errorCode, null, null);
	}

	public static CommonResponse failure(String errorCode,
			Map<String, Object> errors) {
		return new CommonResponse(false, null, errorCode, errors, null);
	}

	public static CommonResponse failure(String errorCode,
			Map<String, Object> errors, String message) {
		return new CommonResponse(false, null, errorCode, errors, message);
	}

	public CommonResponse(boolean success, Object data, String errorCode,
			Map<String, Object> errors, String message) {
		super(success, data, errorCode, errors, message);
	}
}
