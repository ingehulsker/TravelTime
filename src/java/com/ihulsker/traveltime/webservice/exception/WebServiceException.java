package com.ihulsker.traveltime.webservice.exception;

/**
 *
 * @author Inge
 */
public class WebServiceException extends Exception
{
    public WebServiceException(Exception x)
    {
        super("WebserviceException", x);
    }
    
    public WebServiceException(String message)
    {
        super(message);
    }
}
