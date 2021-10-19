package cs301.Soccer;

import android.util.Log;
import cs301.Soccer.soccerPlayer.SoccerPlayer;
import java.io.File;
import java.io.PrintWriter;
import java.util.*;

/**
 * Soccer player database -- presently, all dummied up
 *
 * @author *** put your name here ***
 * @version *** put date of completion here ***
 *
 */
public class SoccerDatabase implements SoccerDB {

    // dummied up variable; you will need to change this
    private Hashtable<String, SoccerPlayer> database = new Hashtable<String,SoccerPlayer>();

    /**
     * add a player
     *
     * @see SoccerDB#addPlayer(String, String, int, String)
     */
    @Override
    public boolean addPlayer(String firstName, String lastName,
                             int uniformNumber, String teamName) {
        String key = firstName + " ## " + lastName;
        if(database.containsKey(key)){
            return false;
        }else{
            SoccerPlayer soccerPlayer = new SoccerPlayer(firstName,lastName,uniformNumber,teamName);
            database.put(key,soccerPlayer);
            return true;
        }
    }

    /**
     * remove a player
     *
     * @see SoccerDB#removePlayer(String, String)
     */
    @Override
    public boolean removePlayer(String firstName, String lastName) {
        String key = firstName + " ## " + lastName;
        if(database.containsKey(key)){
            database.remove(key);
            return true;
        }else{
            return false;

        }
        }

    /**
     * look up a player
     *
     * @see SoccerDB#getPlayer(String, String)
     */
    @Override
    public SoccerPlayer getPlayer(String firstName, String lastName) {
        String key = firstName + " ## " + lastName;
        if(database.containsKey(key)){
            return database.get(key);
        }
        return null;
    }

    /**
     * increment a player's goals
     *
     * @see SoccerDB#bumpGoals(String, String)
     */
    @Override
    public boolean bumpGoals(String firstName, String lastName) {
        String key = firstName + " ## " + lastName;
        if(database.containsKey(key)){
            database.get(key).bumpGoals();
            return true;
        }else {
            return false;
        }
    }

    /**
     * increment a player's yellow cards
     *
     * @see SoccerDB#bumpYellowCards(String, String)
     */
    @Override
    public boolean bumpYellowCards(String firstName, String lastName) {
        String key = firstName + " ## " + lastName;
        if(database.containsKey(key)){
            database.get(key).bumpYellowCards();
            return true;
        }else {
            return false;
        }
    }

    /**
     * increment a player's red cards
     *
     * @see SoccerDB#bumpRedCards(String, String)
     */
    @Override
    public boolean bumpRedCards(String firstName, String lastName) {
        String key = firstName + " ## " + lastName;
        if(database.containsKey(key)){
            database.get(key).bumpRedCards();
            return true;
        }else {
            return false;
        }
    }

    /**
     * tells the number of players on a given team
     *
     * @see SoccerDB#numPlayers(String)
     */
    @Override
    // report number of players on a given team (or all players, if null)
    public int numPlayers(String teamName) {
        int teamCounter = 0;
        if(teamName == null){
            return database.size();
        }else{
            Set<String> keySet = database.keySet();
           for(String key : keySet){
                if(teamName.equals(database.get(key).getTeamName())){
                    teamCounter++;
                }
           }
           return teamCounter;
        }

    }

    /**
     * gives the nth player on a the given team
     *
     * @see SoccerDB#playerIndex(int, String)
     */
    // get the nTH player
    @Override
    //TODO: Record the method working
    public SoccerPlayer playerIndex(int idx, String teamName) {
        if (idx > numPlayers(teamName)){
            return null;
        }
        Set<String> keySet = database.keySet();
        Iterator<SoccerPlayer> it = database.values().iterator();
        int count = -1;
        while(it.hasNext()){
            SoccerPlayer currPlaya = (SoccerPlayer) it.next();
            if(teamName == null || currPlaya.getTeamName().equals(teamName)){
                count++;
            }
            if(count == idx){
                return currPlaya;
            }
        }
        return null;
    }
    /**
     * reads database data from a file
     *
     * @see SoccerDB#readData(java.io.File)
     */
    // read data from file
    @Override
    public boolean readData(File file) {
        try{
            if(file.exists()) {
                Scanner sc = new Scanner(file);
                while(sc.hasNextLine()){
                    SoccerPlayer tempPlayer;
                    String firstName = sc.nextLine();
                    String lastName = sc.nextLine();
                    int uniformNum = Integer.parseInt(sc.nextLine());
                    String teamName = sc.nextLine();
                    int goals = Integer.parseInt(sc.nextLine());
                    int yellowCards = Integer.parseInt(sc.nextLine());
                    int redCards = Integer.parseInt(sc.nextLine());
                    tempPlayer = getPlayer(firstName,lastName);
                    if(tempPlayer != null){
                        removePlayer(firstName,lastName);
                    }
                    addPlayer(firstName,lastName,uniformNum,teamName);
                    tempPlayer = getPlayer(firstName,lastName);
                    for (int i = 0; i < goals; i++) {
                        bumpGoals(tempPlayer.getFirstName(),tempPlayer.getLastName());
                    }
                    for (int i = 0; i < yellowCards; i++) {
                        bumpYellowCards(tempPlayer.getFirstName(),tempPlayer.getLastName());
                    }
                    for (int i = 0; i < redCards; i++) {
                        bumpRedCards(tempPlayer.getFirstName(),tempPlayer.getLastName());
                    }
                }
                return true;
            }
        }catch(Exception FileNotFoundException){
            logString("NO file exists");
        }
        return false;
    }

    /**
     * write database data to a file
     *
     * @see SoccerDB#writeData(java.io.File)
     */
    // write data to file
    @Override
    //TODO: Take sc of LogCat being written
    public boolean writeData(File file) {

        try{
            PrintWriter pw = new PrintWriter(file);
            Iterator<SoccerPlayer> it = database.values().iterator();
            while(it.hasNext()) {
                SoccerPlayer currPlaya = (SoccerPlayer) it.next();
                pw.println(logString(currPlaya.getFirstName()));
                pw.println(logString(currPlaya.getLastName()));
                pw.println(logString("" + currPlaya.getUniform()));
                pw.println(logString(currPlaya.getTeamName()));
                pw.println(logString("" + currPlaya.getGoals()));
                pw.println(logString("" + currPlaya.getYellowCards()));
                pw.println(logString("" + currPlaya.getRedCards()));
                }
            //logString(file.getAbsolutePath());
            pw.close();
            return true;
        }catch(Exception FileNotFoundException){
            Log.i("Error", "File Not found");
            file = new File(file.getPath());
            writeData(file);
        }
        return false;
    }

    /**
     * helper method that logcat-logs a string, and then returns the string.
     * @param s the string to log
     * @return the string s, unchanged
     */
    private String logString(String s) {
        Log.i("write string", s);
        return s;
    }

    /**
     * returns the list of team names in the database
     *
     * @see cs301.Soccer.SoccerDB#getTeams()
     */
    // return list of teams
    @Override
    public HashSet<String> getTeams() {
        HashSet<String> teamHash = new HashSet<String>();
        Iterator<SoccerPlayer> it = database.values().iterator();
        SoccerPlayer tempPlaya;
        while(it.hasNext()){
            tempPlaya = it.next();
            if(!teamHash.contains(tempPlaya.getTeamName())){
                teamHash.add(tempPlaya.getTeamName());
            }
        }
        return teamHash;
    }

    /**
     * Helper method to empty the database and the list of teams in the spinner;
     * this is faster than restarting the app
     */
    public boolean clear() {
        if(database != null) {
            database.clear();
            return true;
        }
        return false;
    }
}
