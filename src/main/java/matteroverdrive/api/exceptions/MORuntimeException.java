package matteroverdrive.api.exceptions;

/**
 * Created by Simeon on 1/18/2016.
 */
public class MORuntimeException extends RuntimeException
{
    public MORuntimeException() {}
    public MORuntimeException(String message) {super(message);}
    public MORuntimeException(String message, Throwable cause) {super(message, cause);}
    public MORuntimeException(Throwable cause) {super(cause);}
    public MORuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {super(message, cause, enableSuppression, writableStackTrace);}
}
