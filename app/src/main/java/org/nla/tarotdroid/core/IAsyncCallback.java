/**
 * 
 */
package org.nla.tarotdroid.core;

/**
 * Callback for AsyncTask.
 */
public interface IAsyncCallback<T> {
	
	/**
	 * Executes the callback.
	 * @param object
	 * @param exception
	 */
	void execute(T object, Exception exception);
}
