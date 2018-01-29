package com.test.services.processor;

/**
 * @author ananthnambi@gmail.com
 *
 *         Contract for processor class.
 *
 */
public interface Processor extends Runnable {

	/**
	 * To process the given input.
	 * 
	 */
	public void process();

}