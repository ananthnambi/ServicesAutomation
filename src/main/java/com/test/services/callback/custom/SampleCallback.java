package com.test.services.callback.custom;

import org.springframework.stereotype.Component;

import com.test.services.callback.Callback;

@Component
public class SampleCallback extends Callback {

	private static final String SERVICE_NAME = "Sample";

	public SampleCallback() {

		super(SERVICE_NAME);

	}

	public boolean isPass() {
		return true;
	}

}