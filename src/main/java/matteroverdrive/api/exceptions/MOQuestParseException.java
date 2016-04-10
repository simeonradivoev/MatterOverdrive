package matteroverdrive.api.exceptions;

/**
 * Created by Simeon on 1/25/2016.
 */
public class MOQuestParseException extends MORuntimeException
{
	public MOQuestParseException()
	{
	}

	public MOQuestParseException(String message, Object... params)
	{
		super(String.format(message, params));
	}

	public MOQuestParseException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public MOQuestParseException(Throwable cause)
	{
		super(cause);
	}

	public MOQuestParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
