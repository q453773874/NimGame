/**
 * Created by 45377 on 2017/5/10.
 */
public class PlayerNumberOutOfBoundsException extends Exception
{
    public PlayerNumberOutOfBoundsException()
    {
        super("It only support 100 players.");
    }

    public PlayerNumberOutOfBoundsException(String message)
    {
        super(message);
    }
}
