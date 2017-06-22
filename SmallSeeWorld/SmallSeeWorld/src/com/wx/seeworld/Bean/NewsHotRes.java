package com.wx.seeworld.Bean;

import java.util.List;

public class NewsHotRes {
	private String error_code;
	private List<String> result;
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public List<String> getResult() {
		return result;
	}
	public void setResult(List<String> result) {
		this.result = result;
	}
}
