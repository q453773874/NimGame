/**
 * Created by 45377 on 2017/5/8.
 */
public class CommandLessException extends Exception
{
    CommandLessException()
    {
        super("Incorrect number of arguments supplied to command.");
    }
    public CommandLessException(String message)
    {
        super(message);
    }

}
