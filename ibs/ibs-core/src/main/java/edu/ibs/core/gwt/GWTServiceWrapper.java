package edu.ibs.core.gwt;

import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import edu.ibs.common.exceptions.IbsServiceException;
import org.gwtwidgets.server.spring.GWTRPCServiceExporter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * User: Максим
 * Date: 16.12.12
 * Time: 20:16
 */
public final class GWTServiceWrapper extends GWTRPCServiceExporter {
    /**
     * Set the service service. RPC requests are decoded and the corresponding method of the service object is invoked.
     *
     * @param service сервис
     */
    public void setService(final Object service) {
        this.service = service;
    }

    /**
     * Обработка запроса
     *
     *
     * @param request запрос
     * @param response ответ
     * @return
     */
    public void handleRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * Врапер для вызова метода сервиса
     *
     * @param service сервис
     * @param targetMethod метод
     * @param targetParameters параметры
     * @param rpcRequest идентификатор запроса
     * @return результат
     */
    protected String invokeMethodOnService(final Object service, final Method targetMethod,
            final Object[] targetParameters, final RPCRequest rpcRequest) throws Exception {
        Object result;
        try {
            result = targetMethod.invoke(service, targetParameters);
        } catch (Exception e) {
            IbsServiceException serviceEception;
            if (e instanceof InvocationTargetException) {
                if (e.getCause() instanceof IbsServiceException) {
                    serviceEception = (IbsServiceException) e.getCause();
                } else {
                    serviceEception = new IbsServiceException(e.getCause());
                }
            } else if (e instanceof IbsServiceException) {
                serviceEception = (IbsServiceException) e;
            } else {
                serviceEception = new IbsServiceException(e);
            }
            /**
             * Сохраняем стектрейс
             */
            if (serviceEception.getStackTrace() != null && serviceEception.getStackTrace().length > 0) {
                StringWriter stringWriter = new StringWriter();
                serviceEception.printStackTrace(new PrintWriter(stringWriter) {
                    @Override
                    public void println(final String x) {
                        String aspectsPackage = "dp.server.utils.aspect";
                        if ((x.indexOf("com.qulix") != -1 || x.indexOf("$Proxy") != -1)
                                && x.indexOf(aspectsPackage) == -1) {
                            super.println(x);
                        }
                    }
                });
                stringWriter.close();
                String html = stringWriter.getBuffer().toString();
                html = html.replaceAll("\\.", ". ");
                html = html.replaceAll("\n", "<br/>");
                serviceEception.setHtmlStackTrace(html);
            }
            return RPC.encodeResponseForFailure(targetMethod, serviceEception);
        }
        return RPC.encodeResponseForSuccess(rpcRequest.getMethod(), result, rpcRequest.getSerializationPolicy());
    }

}
