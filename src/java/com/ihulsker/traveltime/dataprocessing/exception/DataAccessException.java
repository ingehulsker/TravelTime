package com.ihulsker.traveltime.dataprocessing.exception;

/**
 *
 * @author Inge
 */
public class DataAccessException extends Exception
{
    public DataAccessException(Exception x)
    {
        super("DataBaseException", x);
    }
}
