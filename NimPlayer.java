import java.text.NumberFormat;

public abstract class NimPlayer implements Testable
{

    private String userName;
    private String givenName;
    private String familyName;
    private int numberOfPlayed;
    private int numberOfWon;
    private String type;

    // constructor
    NimPlayer()
    {
        this.userName = "";
        this.givenName = "";
        this.familyName = "";
        this.numberOfPlayed = 0;
        this.numberOfWon = 0;
        this.type = "";
    }
    // just set game played number and game won number
    void resetStats()
    {
        this.numberOfPlayed = 0;
        this.numberOfWon = 0;
    }

    void setPlayerName(String userName,String givenName, String familyName)
    {
        this.userName = userName;
        this.givenName = givenName;
        this.familyName = familyName;
    }

    void setNumberOfPlayed(int numberOfPlayed)
    {
        this.numberOfPlayed = numberOfPlayed;
    }

    void setNumberOfWon(int numberOfWon)
    {
        this.numberOfWon = numberOfWon;
    }
    // ask user to input the remove number
    abstract int removeStone();

    void setType(String type)
    {
        this.type = type;
    }

    String getUserName()
    {
        return userName;
    }

    String getFamilyName()
    {
        return familyName;
    }

    int getNumberOfPlayed()
    {
        return numberOfPlayed;
    }

    int getNumberOfWon()
    {
        return numberOfWon;
    }


    String getType()
    {
        return type;
    }

    public boolean equals(Object otherObject)
    {
        NimPlayer otherPlayer = (NimPlayer)otherObject;
        return ((this.userName.equals(otherPlayer.userName)));
    }
    // display the win rate as percentage format
    String calculatePercentage()
    {
        String result;
        // display the ranking in percentage format
        NumberFormat percentage = NumberFormat.getPercentInstance();
        percentage.setMinimumFractionDigits(0);
        result = percentage.format(getRanking());
        return result;
    }
    // calculate the win rate
    double getRanking()
    {
        return numberOfPlayed == 0 ? 0 : (double)numberOfWon/numberOfPlayed;
    }
    // return the player family name and given name as this format
    String getPlayerName()
    {
        return familyName + " " + givenName;
    }
    // for display the information of player
    String displayNumberOfPlayed()
    {
        String infoNumberOfPlayed;
        infoNumberOfPlayed = String.format("%02d games",numberOfPlayed);
        return  infoNumberOfPlayed;
    }
    // display the info of player
    public String toString()
    {
        String playerInfo = "";
        playerInfo += userName + "," + familyName + "," + givenName;
        playerInfo += "," + numberOfPlayed + " games," + numberOfWon + " wins";
        return playerInfo;
    }
    // overload
    String toString(String addMessage)
    {
        return this.toString() + "," + addMessage;
    }
}
