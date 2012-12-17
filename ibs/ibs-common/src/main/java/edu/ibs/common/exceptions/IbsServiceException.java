package edu.ibs.common.exceptions;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * User: Максим
 * Date: 16.12.12
 * Time: 20:21
 */
public class IbsServiceException extends Exception implements Serializable, IsSerializable {

    /**
     * Stack trace for html.
     */
    private String htmlStackTrace;

    public IbsServiceException() {
        super();
    }

    public IbsServiceException(Throwable cause) {
        super(cause);
    }

    public IbsServiceException(String message) {
        super(message);
    }

    public IbsServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getHtmlStackTrace() {
        return htmlStackTrace;
    }

    public void setHtmlStackTrace(String htmlStackTrace) {
        this.htmlStackTrace = htmlStackTrace;
    }

}

