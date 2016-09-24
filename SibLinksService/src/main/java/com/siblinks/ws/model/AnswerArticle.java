package com.siblinks.ws.model;

public class AnswerArticle {
	
	private String aId;
	private String arId;
	private String authorId;
	private String content;
	private String limit;
	private String pageno;
	
	public String getaId() {
		return aId;
	}
	
	public void setaId(String aId) {
		this.aId = aId;
	}

	public String getArId() {
		return arId;
	}

	public void setArId(String arId) {
		this.arId = arId;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getPageno() {
		return pageno;
	}

	public void setPageno(String pageno) {
		this.pageno = pageno;
	}
}
