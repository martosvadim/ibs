package edu.ibs.core.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: EgoshinME
 * Date: 20.12.12
 * Time: 5:40
 */
public final class ServletUtils {
	/**
	 * Конструктор
	 */
	private ServletUtils() {
	}

	/**
	 * Запрос
	 */
	private static ThreadLocal<HttpServletRequest> servletRequest =
			new ThreadLocal<HttpServletRequest>();

	/**
	 * Ответ
	 */
	private static ThreadLocal<HttpServletResponse> servletResponse =
			new ThreadLocal<HttpServletResponse>();

	/**
	 * Return the request which invokes the service. Valid only if used in the
	 * dispatching thread.
	 *
	 * @return the servlet request
	 */
	public static HttpServletRequest getRequest() {
		return servletRequest.get();
	}

	/**
	 * Return the response which accompanies the request. Valid only if used in
	 * the dispatching thread.
	 *
	 * @return the servlet response
	 */
	public static HttpServletResponse getResponse() {
		return servletResponse.get();
	}

	/**
	 * Assign the current servlet request to a thread local variable.
	 * Valid only if used inside the invoking thread scope.
	 *
	 * @param request request
	 */
	public static void setRequest(final HttpServletRequest request) {
		servletRequest.set(request);
	}

	/**
	 * Assign the current servlet response to a thread local variable.
	 * Valid only if used inside the invoking thread scope.
	 *
	 * @param response response
	 */
	public static void setResponse(final HttpServletResponse response) {
		servletResponse.set(response);
	}

}
