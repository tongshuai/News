package com.example.feicui.news2.entity;

/**
 * 有关注册和登录的entity
 * 
 * @author qinyq
 * 
 */
public class Register {

	String result; 
	String token;  //用户令牌，用于验证用户。每次请求都传递给服务器。具有时效期。
	String explain;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getExplain() {
		return explain;
	}

	public void setLexplain(String explain) {
		this.explain = explain;
	}
}
