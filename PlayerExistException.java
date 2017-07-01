/**
 * Created by 45377 on 2017/5/8.
 */
public class PlayerExistException extends Exception
{
    public PlayerExistException()
    {
        super("The player already exists.");
    }
    public PlayerExistException(String message)
    {
        super(message);
    }
}
