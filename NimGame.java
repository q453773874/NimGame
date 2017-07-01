 class NimGame
 {
    private int stoneNumber;
    private int upperBound;
    // constructor
    NimGame()
    {
        stoneNumber = 1;
        upperBound = 1;
    }


    private void displayGameMenu(NimPlayer playerOne, NimPlayer playerTwo)
    {
        System.out.println();
        System.out.println("Initial stone count: " + stoneNumber);
        System.out.println("Maximum stone removal: " + upperBound);
        System.out.println("Player 1: " + playerOne.getPlayerName());
        System.out.println("Player 2: " + playerTwo.getPlayerName());
    }

    private void setUpperBound(int upperBound)
    {
        this.upperBound = upperBound;
    }

    private void setStoneNumber(int stoneNumber)
    {
        this.stoneNumber = stoneNumber;
    }
    // display the star for each stone number
    private void displayStone(int stoneNumber)
    {
        StringBuilder starDisplay = new StringBuilder();
        for (int i = 0; i < stoneNumber; i++)
        {
            starDisplay.append(" *");
        }
        System.out.println();
        System.out.printf("%d stones left:%s\n",stoneNumber,starDisplay);
    }

    private int getInvalidMoveNumber()
    {
        return (stoneNumber > upperBound) ? upperBound : stoneNumber;
    }

    private void checkInvalidMove(int moveNumber)
            throws InvalidMoveException
    {
        int displayNumber;
        if (moveNumber > stoneNumber || moveNumber > upperBound || moveNumber <= 0)
        {
            displayNumber = getInvalidMoveNumber();
            throw new InvalidMoveException(displayNumber);
        }
    }

    private void startToRemove(NimPlayer playerOne, NimPlayer playerTwo)
    {
        NimPlayer player;
        player = playerOne;
        int numberPlayedForPlayerOne = playerOne.getNumberOfPlayed();
        int numberPlayedForPlayerTwo = playerTwo.getNumberOfPlayed();
        int numberToRemove;

        for (;stoneNumber > 0;)
        {
            try
            {
                if (player instanceof NimAIPlayer)
                {
                    ((NimAIPlayer) player).setGameInfo(stoneNumber,getInvalidMoveNumber());
                }
                displayStone(stoneNumber);
                System.out.printf("%s's turn - remove how many?\n",player.getFamilyName());
                numberToRemove = player.removeStone();
                checkInvalidMove(numberToRemove);
                stoneNumber -= numberToRemove;
                // change player
                player = (player == playerOne) ? playerTwo : playerOne;
            }
            catch (InvalidMoveException e)
            {
                System.out.printf
                        ("\nInvalid move. You must remove between 1 and %d stones.\n",e.getDisplayNumber());
            }
        }
        // game played number plus 1
        numberPlayedForPlayerOne += 1;
        numberPlayedForPlayerTwo += 1;
        playerOne.setNumberOfPlayed(numberPlayedForPlayerOne);
        playerTwo.setNumberOfPlayed(numberPlayedForPlayerTwo);
        displayResult(player);
    }

    private void displayResult(NimPlayer player)
    {
        int wonNumberOfWinner = player.getNumberOfWon();
        // game won number plus 1
        wonNumberOfWinner += 1;
        System.out.println();
        System.out.println("Game Over");
        System.out.println(player.getPlayerName() + " wins!");
        player.setNumberOfWon(wonNumberOfWinner);
    }



    void gameStart(int initialStone,int upperBound,NimPlayer playerOne, NimPlayer playerTwo)
    {
        setStoneNumber(initialStone);
        setUpperBound(upperBound);
        if (upperBound <= 0 )
        {
            System.out.println("The upper bound should bigger than 0.");
        }
        else if (stoneNumber < upperBound)
        {
            System.out.println("The stone number should bigger than upperbound.");
        }
        else
        {
            displayGameMenu(playerOne,playerTwo);
            startToRemove(playerOne,playerTwo);
        }
    }

}
