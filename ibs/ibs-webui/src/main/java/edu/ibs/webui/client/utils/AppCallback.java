package edu.ibs.webui.client.utils;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.smartgwt.client.util.SC;

/**
 * User: EgoshinME
 * Date: 04.01.13
 * Time: 6:14
 */
public abstract class AppCallback<T> implements AsyncCallback<T> {

	/**
	 * Код ошибки таймаута
	 */
	private static final int SC_TIMEOUT = 12029;
	/**
	 * Код серверной ошибки
	 */
	private static final int SC_500 = 500;

	@Override
	public void onFailure(Throwable caught) {
		if (caught instanceof StatusCodeException && ((StatusCodeException) caught).getStatusCode() == SC_TIMEOUT) {
			/**
			 * Таймаут запроса на сервер
			 */
			SC.warn("Соединение с сервером потеряно...<br/> Обновите страницу");
			return;
		}
		if (caught instanceof StatusCodeException && (((StatusCodeException) caught).getStatusCode() == SC_500
				|| ((StatusCodeException) caught).getStatusCode() == 0)) {
			SC.warn("Произошло обновление приложения на сервере...<br/> Обновите страницу");
		}
		SC.warn(caught.getLocalizedMessage());
	}
}
