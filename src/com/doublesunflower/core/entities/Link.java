package com.doublesunflower.core.entities;

public class Link {
	private Long id;
	private Long fromEntityId;
	private Long toEntityId;
	private String fromEntityType;
	private String toEntityType;
	private String comments;
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Long getFromEntityId() {
		return fromEntityId;
	}
	public void setFromEntityId(Long fromEntityId) {
		this.fromEntityId = fromEntityId;
	}
	public String getFromEntityType() {
		return fromEntityType;
	}
	public void setFromEntityType(String fromEntityType) {
		this.fromEntityType = fromEntityType;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getToEntityId() {
		return toEntityId;
	}
	public void setToEntityId(Long toEntityId) {
		this.toEntityId = toEntityId;
	}
	public String getToEntityType() {
		return toEntityType;
	}
	public void setToEntityType(String toEntityType) {
		this.toEntityType = toEntityType;
	}

}
