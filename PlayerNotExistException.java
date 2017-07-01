/**
 * Created by 45377 on 2017/5/8.
 */
public class PlayerNotExistException extends Exception
{
    public PlayerNotExistException()
    {
        super("The player does not exist.");
    }

    public PlayerNotExistException(String message)
    {
        super(message);
    }
}
