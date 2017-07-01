/**
 * Created by Yunjie Jia on 2017/5/8.
 */
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Nimsys
{
    private static final int MAX_NUMBER_OF_PLAYER = 100;
    private static final int MAX_NUMBER_OF_DISPLAY = 10;
    private static final int CORRECT_COMMAND_NUMBER_TWO = 2;
    private static final int CORRECT_COMMAND_NUMBER_FOUR = 4;
    private static final int CORRECT_COMMAND_NUMBER_FIVE = 5;
    static Scanner keyboard;
    private NimPlayer[] nimPlayer = new NimPlayer[MAX_NUMBER_OF_PLAYER];
    private int playerNumber = 0;
    private PrintWriter printWriter =null;


    private void displayMenu()
    {
        try{
                keyboard = new Scanner(new FileInputStream("players.dat"));
                String[] readLine;
                while(keyboard.hasNextLine())
                {
                    readLine = keyboard.nextLine().split(",");
                    String[] playedGameNumber = readLine[3].split(" ");
                    String[] wonGameNumber = readLine[4].split(" ");
                    if (readLine[5].equals("humanPlayer"))
                    {
                        nimPlayer[playerNumber] = new NimHumanPlayer();
                    }
                    else if (readLine[5].equals("AIPlayer"))
                    {
                        nimPlayer[playerNumber] = new NimAIPlayer();
                    }
                    nimPlayer[playerNumber].setPlayerName(readLine[0],readLine[2],readLine[1]);
                    nimPlayer[playerNumber].setNumberOfPlayed(Integer.parseInt(playedGameNumber[0]));
                    nimPlayer[playerNumber].setNumberOfWon(Integer.parseInt(wonGameNumber[0]));
                    playerNumber += 1;
                }
            // just catch this Exception, ignore it
        }
        catch (IOException e)
        {
        }
        System.out.println("Welcome to Nim");
        keyboard = new Scanner(System.in);
        for(;;)
        {
            String commands;
            System.out.println("");
            System.out.print("$");
            commands = keyboard.nextLine();
            commandsExecute(commands);
        }
    }
    // divide the input command into 4 parts
    private String[] divideTheInput(String commands)
    {
        // delete the first space
        commands = commands.trim();
        // get the main word of command from a long command
        return commands.split("[ ,]");
    }

    private void commandLessCheck(int correctLength,int currentLength)
            throws CommandLessException
    {
        if (correctLength > currentLength)
        {
            throw new CommandLessException();
        }
    }

    // used for execute each commands
    private void commandsExecute(String commands)
    {
        try
        {
            // first command
            String firstCommand = divideTheInput(commands)[0];
            // add player command
            if (firstCommand.equals("addplayer"))
            {
                addPlayer(commands,"human");
            }
            // add ai player
            else if (firstCommand.equals("addaiplayer"))
            {
                addPlayer(commands,"ai");
            }
            // remove player
            else if (firstCommand.equals("removeplayer"))
            {
                boolean whetherRemoveAll = false;
                int removeCommandLength = divideTheInput(commands).length;
                whetherRemoveAll = (removeCommandLength < CORRECT_COMMAND_NUMBER_TWO);
                removePlayer(commands,whetherRemoveAll);
            }
            // edit player info
            else if(firstCommand.equals("editplayer"))
            {
                editPlayer(commands);
            }
            // reset
            else if (firstCommand.equals("resetstats"))
            {
                resetStats(commands);
            }
            // display player info
            else if (firstCommand.equals("displayplayer"))
            {
                displayPlayer(commands);
            }
            else if (firstCommand.equals("rankings"))
            {
                rankings(commands);
            }
            // start game
            else if (firstCommand.equals("startgame"))
            {
                startGame(commands);
            }
            else if(firstCommand.equals("startadvancedgame"))
            {
                startadvancedGame(commands);
            }
            // exit the game
            else if (firstCommand.equals("exit"))
            {
                System.out.println();
                // write into the file
                fileInput();
                printWriter.close();
                keyboard.close();
                System.exit(0);
            }
            else
            {
                throw new CommandFormatException(divideTheInput(commands)[0]);
            }
        }
        catch (CommandFormatException e)
        {
            System.out.printf("\'%s\' is not a valid command.\n",e.getInputMessage());
        }

    }// end conmmad

    // find the user through user name
    private int searchIndexOfUser(String userName,NimPlayer[] nimPlayer)
            throws PlayerNotExistException
    {
        for (int i = 0;i < playerNumber; i++)
        {
            if (userName.equals(nimPlayer[i].getUserName()) )
            {
                return i;
            }
        }
        // if not find player
        throw new PlayerNotExistException();
    }

    private void checkUserExist(NimPlayer[] nimPlayer)
            throws PlayerExistException
    {
        if (playerNumber > 1)
        {
            // compare the current player user name with previous
            for (int i = 0; i <= playerNumber - 2; i++){
                if (nimPlayer[playerNumber - 1].equals(nimPlayer[i]))
                {
                    // delete the player added just now
                    nimPlayer[playerNumber - 1] = null;
                    playerNumber -= 1;
                    throw new PlayerExistException();
                }
            }
        }
    }

    private void fileInput()
    {
        try
        {
            printWriter = new PrintWriter(new FileOutputStream("players.dat",true));
        }
        catch (FileNotFoundException e)
        {
            System.out.print(e.getMessage());
        }
        // write player info
        NimPlayer[] playerSortedByAlpha = Arrays.copyOf(nimPlayer,playerNumber);
        playerSortedByAlpha = sortByAlpha(playerSortedByAlpha);
        for (int i = 0; i < playerNumber; i++){
            printWriter.println(playerSortedByAlpha[i].toString(playerSortedByAlpha[i].getType()));
        }
    }
    // add player
    // has a file input here
    private void addPlayer(String commands,String mode)
    {
        try
        {
            // check whether the command is less or not
            commandLessCheck(CORRECT_COMMAND_NUMBER_FOUR,divideTheInput(commands).length);
            String userName = divideTheInput(commands)[1];
            String familyName = divideTheInput(commands)[2];
            String givenName = divideTheInput(commands)[3];
            if (playerNumber <= MAX_NUMBER_OF_PLAYER)
            {
                if (mode.equals("human"))
                {
                    nimPlayer[playerNumber] = new NimHumanPlayer();
                }
                else if(mode.equals("ai"))
                {
                    nimPlayer[playerNumber] = new NimAIPlayer();
                }
                nimPlayer[playerNumber].setPlayerName(userName,familyName,givenName);
                playerNumber += 1;
                // judge whether user name is repeat
                checkUserExist(nimPlayer);
                // just support 100 players
            }
            else
            {
                throw new PlayerNumberOutOfBoundsException();
            }

        }
        catch (CommandLessException | PlayerExistException | PlayerNumberOutOfBoundsException e)
        {
            System.out.println(e.getMessage());
        }
    }
    // edit player
    private void editPlayer(String commands)
    {
        try
        {
            commandLessCheck(CORRECT_COMMAND_NUMBER_FOUR,divideTheInput(commands).length);
            String searchUsername = divideTheInput(commands)[1];
            String newFamilyName = "";
            String newGivenName = "";
            int index = searchIndexOfUser(searchUsername,nimPlayer);
            String currentUsername = nimPlayer[index].getUserName();
            newFamilyName = divideTheInput(commands)[2];
            newGivenName = divideTheInput(commands)[3];
            nimPlayer[index].setPlayerName(currentUsername,newFamilyName,newGivenName);
        }
        catch (CommandLessException | PlayerNotExistException e)
        {
            System.out.println(e.getMessage());
        }

    }
    // remove player
    private void removePlayer(String commands,boolean whetherRemoveAll)
    {
        if (! whetherRemoveAll) {
            String userNameForSearch = divideTheInput(commands)[1];
            try{
                int index = searchIndexOfUser(userNameForSearch,nimPlayer);
                if (playerNumber > 1) {
                    // move the final player to current location and make the final one become null
                    nimPlayer[index] = nimPlayer[playerNumber - 1];
                    nimPlayer[playerNumber - 1] = null;
                } else {
                    nimPlayer[index] = null;
                }
                playerNumber -= 1;
            }catch (PlayerNotExistException e){
                System.out.println(e.getMessage());
            }
            // the command just a remove
        }else{
            String choice = "";
            System.out.println("Are you sure you want to remove all players? (y/n)");
            choice = keyboard.nextLine();
            if (choice.equalsIgnoreCase("Y")){
                for (int i = 0; i < playerNumber; i++){
                    nimPlayer[i] = null;
                    playerNumber = 0;
                }
            }
        }
    }
    // reset stats
    private void resetStats(String commands)
    {
        try
        {
            commandLessCheck(CORRECT_COMMAND_NUMBER_TWO,divideTheInput(commands).length);
            String resetPlayer = divideTheInput(commands)[1];
            if (playerNumber > 0)
            {
                // just reset the specify player
                int index = searchIndexOfUser(resetPlayer,nimPlayer);
                nimPlayer[index].resetStats();
            }
            else
            {
                System.out.println("No player exist can be reset.");
            }
        }
        catch (CommandLessException e)
        {
            String choice = "";
            // reset all player
            System.out.println("Are you sure you want to reset all player statistics? (y/n)");
            choice = keyboard.nextLine();
            if (choice.equalsIgnoreCase("y"))
            {
                if (playerNumber > 0)
                {
                    for (int i = 0; i < playerNumber; i++)
                    {
                        nimPlayer[i].resetStats();
                    }
                }
            }
        }
        catch (PlayerNotExistException e)
        {
            System.out.println(e.getMessage());
        }
    }
    // display the info of player
    private void displayPlayer(String commands)
    {
        NimPlayer[] playerSortedByAlpha = Arrays.copyOf(nimPlayer,playerNumber);
        try
        {
            commandLessCheck(CORRECT_COMMAND_NUMBER_TWO,divideTheInput(commands).length);
            String disPlayPlayer = divideTheInput(commands)[1];
            playerSortedByAlpha = sortByAlpha(playerSortedByAlpha);
            int index = searchIndexOfUser(disPlayPlayer,playerSortedByAlpha);
            System.out.println(playerSortedByAlpha[index].toString());
            System.out.println(playerSortedByAlpha[index].getType());
        }
        catch (CommandLessException e)
        {
            for (int i = 0; i < playerNumber; i++)
            {
                System.out.println(playerSortedByAlpha[i].toString());
            }
        }
        catch (PlayerNotExistException e)
        {
            System.out.println(e.getMessage());
        }
    }
    // displayASC order
    private void displayASCSort(int displayNumber)
    {
        NimPlayer[] sortedAsc;
        sortedAsc = Arrays.copyOf(nimPlayer, playerNumber);
        // sort them in Alpha order first
        sortedAsc = sortByAlpha(sortedAsc);
        // then sort them by ranking order
        sortedAsc = insertionSortAsc(sortedAsc);
        for (int i = 0; i < displayNumber; i++)
        {
            String rankPercentage = sortedAsc[i].calculatePercentage();
            String numberOfGamePlayed = sortedAsc[i].displayNumberOfPlayed();
            String playerFullName = sortedAsc[i].getPlayerName();
            System.out.printf
                    ("%-5s%s %-9s%s %s\n",rankPercentage,"|",numberOfGamePlayed,"|",playerFullName);
        }
    }
    // display DESC order
    private void displayDESCSort(int displayNumber)
    {
        NimPlayer[] sortedDesc;
        sortedDesc = Arrays.copyOf(nimPlayer, playerNumber);
        // sort them in Alpha order first
        sortedDesc = sortByAlpha(sortedDesc);
        // then sort them by ranking order
        sortedDesc = insertionSortDesc(sortedDesc);
        for (int i = 0; i < displayNumber; i++) {
            String rankPercentage = sortedDesc[i].calculatePercentage();
            String numberOfGamePlayed = sortedDesc[i].displayNumberOfPlayed();
            String playerFullName = sortedDesc[i].getPlayerName();
            System.out.printf
                    ("%-5s%s %-9s%s %s\n",rankPercentage,"|",numberOfGamePlayed,"|",playerFullName);
        }
    }
    // display the rank
    private void rankings(String commands)
                throws CommandFormatException
    {
        // the max display number is 10
        int displayNumber = playerNumber > MAX_NUMBER_OF_DISPLAY ? MAX_NUMBER_OF_DISPLAY : playerNumber;
        try
        {
            commandLessCheck(CORRECT_COMMAND_NUMBER_TWO,divideTheInput(commands).length);
            String sortType = divideTheInput(commands)[1];
            // has desc or asc
            if (sortType.equals("asc"))
            {
                displayASCSort(displayNumber);
            }
            else if (sortType.equals("desc"))
            {
                displayDESCSort(displayNumber);
            }
        }
        // this Exception just judge whether has asc command
        catch (CommandLessException e)
        {
            displayDESCSort(displayNumber);
        }

    }
    // ranking in Asc order
    private NimPlayer[] insertionSortAsc(NimPlayer[] A)
    {
        for (int i = 0; i < A.length; i++)
        {
            double v = A[i].getRanking();
            NimPlayer record = A[i];
            int j = i - 1;
            while (j >= 0 && A[j].getRanking() > v)
            {
                A[j + 1] = A[j];
                j = j - 1;
            }
            A[j + 1] = record;
        }
        return A;
    }
    // ranking in Desc order
    private NimPlayer[] insertionSortDesc(NimPlayer[] A)
    {
        for (int i = 0; i < A.length; i++)
        {
            double v = A[i].getRanking();
            NimPlayer record = A[i];
            int j = i - 1;
            while (j >= 0 && A[j].getRanking() < v)
            {
                A[j + 1] = A[j];
                j = j - 1;
            }
            A[j + 1] = record;
        }
        return A;
    }
    // sort the player name as alpha order
    private NimPlayer[] sortByAlpha(NimPlayer[] A)
    {
        for (int i = 0;i < A.length; i++)
        {
            String v = A[i].getUserName();
            NimPlayer record = A[i];
            int j = i - 1;
            while (j >= 0 && A[j].getUserName().compareToIgnoreCase(v) > 0)
            {
                A[j + 1] = A[j];
                j = j - 1;
            }
            A[j + 1] = record;
        }
        return A;
    }

    private void startGame(String commands)
    {
        NimGame nimGame = new NimGame();
        int initialStone = 0;
        int upperBound = 0;
        try
        {
            commandLessCheck(CORRECT_COMMAND_NUMBER_FIVE,divideTheInput(commands).length);
            String playerOne = divideTheInput(commands)[3];
            String playerTwo = divideTheInput(commands)[4];
            initialStone = Integer.parseInt(divideTheInput(commands)[1]);
            upperBound = Integer.parseInt(divideTheInput(commands)[2]);
            int indexPlayerOne = searchIndexOfUser(playerOne,nimPlayer);
            int indexOfPlayerTwo = searchIndexOfUser(playerTwo,nimPlayer);
            // judge the player not the same player
            if (indexPlayerOne != indexOfPlayerTwo)
            {
                nimGame.gameStart(initialStone,upperBound,nimPlayer[indexPlayerOne],nimPlayer[indexOfPlayerTwo]);
                // start game with same player
            }
            else if (indexPlayerOne == indexOfPlayerTwo)
            {
                System.out.println("You cannot play with yourself.");
            }
        }
        catch (CommandLessException e)
        {
            System.out.println(e.getMessage());
        }
        catch (PlayerNotExistException e)
        {
            System.out.println("One of the players does not exist.");
        }

    }

    private void startadvancedGame(String commands)
    {
        AdvanceNimGame nimGame = new AdvanceNimGame();
        int initialStone = 0;
        try
        {
            // check the command lenght is correct
            commandLessCheck(CORRECT_COMMAND_NUMBER_FOUR,divideTheInput(commands).length);
            String playerOne = divideTheInput(commands)[2];
            String playerTwo = divideTheInput(commands)[3];
            initialStone = Integer.parseInt(divideTheInput(commands)[1]);
            int indexPlayerOne = searchIndexOfUser(playerOne,nimPlayer);
            int indexOfPlayerTwo = searchIndexOfUser(playerTwo,nimPlayer);
            // judge the player not the same player
            if (indexPlayerOne != indexOfPlayerTwo)
            {
                nimGame.gameStart(initialStone,nimPlayer[indexPlayerOne],nimPlayer[indexOfPlayerTwo]);
                // start game with same player
            }
            else if (indexPlayerOne == indexOfPlayerTwo)
            {
                System.out.println("You cannot play with yourself.");
            }
        }
        catch (CommandLessException e)
        {
            System.out.println(e.getMessage());
        }
        catch (PlayerNotExistException e)
        {
            System.out.println("One of the players does not exist.");
        }
    }
    // main
    public static void main(String[] args)
    {
        Nimsys nimsys = new Nimsys();
        nimsys.displayMenu();
    }
}
