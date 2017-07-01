/**
 * Created by 45377 on 2017/5/8.
 */
public class CommandFormatException extends Exception
{
    private String message;
    public CommandFormatException()
    {
        super("CommandFormatException");
    }
    CommandFormatException(String message)
    {
        super(message);
        this.message = message;
    }
    String getInputMessage()
    {
        return message;
    }
}
