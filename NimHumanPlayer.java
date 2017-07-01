import java.util.Scanner;

/**
 * Created by 45377 on 2017/5/8.
 */
public class NimHumanPlayer extends NimPlayer
{


    NimHumanPlayer()
    {
        super();
        super.setType("humanPlayer");
    }

    @Override
    // ask user to input the remove number
    public int removeStone()
    {
       // Nimsys.keyboard = new Scanner(System.in);
        for(;;)
        {
            try
            {
                return Integer.parseInt(Nimsys.keyboard.nextLine());
            }
            catch (NumberFormatException e)
            {
                System.out.println(e.getMessage());
            }
            break;
        }
        // make program happy
        return 0;
    }


    //advanced move
    public String advancedMove(boolean[] available, String lastMove)
    {
        // the implementation of the victory
        // guaranteed strategy designed by you
        String move = Nimsys.keyboard.nextLine();
        return move;
    }

}
