package com.apiletsshopecom.payloads.response;

import java.util.List;

public class GetProductsResponse {

	private List<Product> data;
	private int count;
	private String message;

	public List<Product> getData() {
		return data;
	}

	public void setData(List<Product> data) {
		this.data = data;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "GetProductsResponse [data=" + data + ", count=" + count + ", message=" + message + "]";
	}

}
