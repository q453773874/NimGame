/**
 * Created by 45377 on 2017/5/8.
 */
class InvalidMoveException extends Exception
{
    private int displayNumber;

    InvalidMoveException()
    {
        super("InvalidMoveException");
    }

    InvalidMoveException(String message)
    {
        super(message);
    }
    // overload

    InvalidMoveException(int moveNumber)
    {
        super("InvalidMoveException");
        displayNumber = moveNumber;
    }

    int getDisplayNumber(){
        return displayNumber;
    }
}
