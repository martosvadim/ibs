package edu.ibs.webui.client.utils;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;

/**
 * User: EgoshinME
 * Date: 04.01.13
 * Time: 6:14
 */
public abstract class AppCallback<T> implements AsyncCallback<T> {
	@Override
	public void onFailure(Throwable throwable) {
		SC.warn(throwable.getLocalizedMessage());
	}
}
