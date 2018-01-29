package com.test.services.model.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sequence")
public class Sequence {

	@Id
	private String _id;

	private int currentGenId;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public int getCurrentGenId() {
		return currentGenId;
	}

	public void setCurrentGenId(int currentGenId) {
		this.currentGenId = currentGenId;
	}

}