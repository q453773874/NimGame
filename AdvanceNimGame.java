/**
 * Created by 45377 on 2017/5/10.
 */
public class AdvanceNimGame
{

    private int stoneNumber = 0;
    private String[] stone;



     private void displayGameMenu(NimPlayer playerOne, NimPlayer playerTwo)
    {
        System.out.println();
        System.out.println("Initial stone count: " + stoneNumber);
        displayStone(stoneNumber,stoneNumber,"display");
        System.out.println("Player 1: " + playerOne.getPlayerName());
        System.out.println("Player 2: " + playerTwo.getPlayerName());
        System.out.println();
    }


     private void setStoneNumber(int stoneNumber)
    {
        this.stoneNumber = stoneNumber;
        stone = new String[stoneNumber];
        for (int i = 0; i < stoneNumber; i++)
        {
            stone[i] = "*";
        }

    }

    // display the star for each stone number
    private void displayStone(int initialStoneNumber,int stoneLeft,String mode)
    {
        StringBuilder starDisplay = new StringBuilder();
        for (int i = 0; i < initialStoneNumber; i++)
        {
            starDisplay.append(" <" + (i + 1) + "," + stone[i]+ ">");
        }
        if (mode.equals("display"))
        {
            System.out.printf("Stones display:%s\n",starDisplay);
        }
        else if (mode.equals("stoneLeft"))
        {
            System.out.printf("%d stones left:%s\n",stoneLeft,starDisplay);
        }

    }


    private void checkInvalidMove(int stoneStartPosition, int stoneRemoveNumber, int stoneNumber)
            throws InvalidMoveException
    {
        if (stoneStartPosition + stoneRemoveNumber - 1<= stoneNumber)
        {
            for (int i = stoneStartPosition; i <= stoneStartPosition + stoneRemoveNumber - 1; i++)
            {
                if (stone[i - 1].equals("x") )
                {
                    throw new InvalidMoveException("Invalid Move.");
                }
            }
        }
        else
            throw new InvalidMoveException("Invalid Move.");

    }

    private boolean checkAvailableStone(boolean[] available)
    {
        for (int i = 0; i < available.length; i++)
        {
            if (available[i])
                return true;
        }
        return false;
    }

    private void startToRemove(NimPlayer playerOne, NimPlayer playerTwo)
    {
        NimPlayer player;
        player = playerOne;
        int numberPlayedForPlayerOne = playerOne.getNumberOfPlayed();
        int numberPlayedForPlayerTwo = playerTwo.getNumberOfPlayed();
        String[] commandToRemoveSplit;
        String commandInput = "";
        int stoneStartPosition;
        int stoneRemoveNumber;
        int totalRemove = 0;
        String lastMove = "";

        boolean[] available = new boolean[stoneNumber];
        // initialize the available array
        for (int i = 0; i < stoneNumber; i++)
        {
            available[i] = true;
        }
        while (checkAvailableStone(available))
        {
            try
            {
                displayStone(stoneNumber,stoneNumber - totalRemove,"stoneLeft");
                System.out.printf("%s's turn - which to remove?\n",player.getFamilyName());
                if (player instanceof NimHumanPlayer)
                {

                    commandInput = player.advancedMove(null,null);
                    lastMove = commandInput;
                }
                else if (player instanceof NimAIPlayer)
                {
                    commandInput = player.advancedMove(available,lastMove);
                }

                commandToRemoveSplit = commandInput.split(" ");
                stoneStartPosition = Integer.parseInt(commandToRemoveSplit[0]);
                stoneRemoveNumber = Integer.parseInt(commandToRemoveSplit[1]);
                checkInvalidMove(stoneStartPosition,stoneRemoveNumber,stoneNumber);
                System.out.println();
                if (stoneRemoveNumber == 1)
                {
                    stone[stoneStartPosition - 1] = "x";
                    available[stoneStartPosition - 1] = false;
                    totalRemove += 1;
                }
                else if (stoneRemoveNumber == 2)
                {
                    stone[stoneStartPosition - 1] = "x";
                    stone[stoneStartPosition] = "x";
                    available[stoneStartPosition - 1] = false;
                    available[stoneStartPosition] = false;
                    totalRemove += 2;
                }
                else
                {
                    throw new InvalidMoveException();
                }

                // change player
                player = (player == playerOne) ? playerTwo : playerOne;
            }
            catch (Exception e)
            {
                System.out.println("\nInvalid move.\n");
            }
        }
        // game played number plus 1
        player = (player == playerOne) ? playerTwo : playerOne;
        numberPlayedForPlayerOne += 1;
        numberPlayedForPlayerTwo += 1;
        playerOne.setNumberOfPlayed(numberPlayedForPlayerOne);
        playerTwo.setNumberOfPlayed(numberPlayedForPlayerTwo);
        displayResult(player);
    }
//
    private void displayResult(NimPlayer player)
    {
        int wonNumberOfWinner = player.getNumberOfWon();
        // game won number plus 1
        wonNumberOfWinner += 1;
        System.out.println("Game Over");
        System.out.println(player.getPlayerName() + " wins!");
        player.setNumberOfWon(wonNumberOfWinner);
    }

    void gameStart(int initialStone,NimPlayer playerOne, NimPlayer playerTwo)
    {
        setStoneNumber(initialStone);
        displayGameMenu(playerOne,playerTwo);
        startToRemove(playerOne,playerTwo);
    }

}


