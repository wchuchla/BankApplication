package com.luxoft.bankapp.model.enums;

import java.io.Serializable;

public enum Gender implements Serializable {
	MALE("Mr"), FEMALE("Ms");
	private final String prefix;

	private Gender(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}
}