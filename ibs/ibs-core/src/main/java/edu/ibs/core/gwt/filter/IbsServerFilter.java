package edu.ibs.core.gwt.filter;


import edu.ibs.core.utils.ServletUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: EgoshinME
 * Date: 20.12.12
 * Time: 5:39
 */
public class IbsServerFilter implements Filter {
	/**
	 * Фильтрация контента
	 *
	 * @param request     запрос
	 * @param response    ответ
	 * @param filterChain filterChain
	 * @throws java.io.IOException      IOException
	 * @throws javax.servlet.ServletException ServletException
	 */
	public void doFilter(final ServletRequest request,
						 final ServletResponse response,
						 final FilterChain filterChain)
			throws IOException, ServletException {
		try {
			if (request instanceof HttpServletRequest) {
				ServletUtils.setRequest((HttpServletRequest) request);
			}
			if (response instanceof HttpServletResponse) {
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				ServletUtils.setResponse(httpResponse);
			}

			filterChain.doFilter(request, response);
		} finally {
			ServletUtils.setRequest(null);
			ServletUtils.setResponse(null);
		}
	}

	/**
	 * Инициализация фильтра
	 *
	 * @param filterConfig filterConfig
	 * @throws ServletException ServletException
	 */

	public void init(final FilterConfig filterConfig) throws ServletException {
	}

	/**
	 * Удаление
	 */
	public void destroy() {
	}
}
