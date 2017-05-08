import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Team;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.TreeMap;

public class LeagueManager {
  private Scanner scanner;
  private Set<Player> availablePlayers;
  private Set<Team> teams;
  private List<String> menu;
  private Map<Team, String> experienceRatio;
  
  public LeagueManager() {
    scanner = new Scanner(System.in);
    availablePlayers = new TreeSet<>();
    teams = new TreeSet<>();
    experienceRatio = new TreeMap<>();
    
    menu = new LinkedList<>();
    menu.add("Create a new team");
    menu.add("Add a player to a team");
    menu.add("Remove player from team");
    menu.add("View team stats by height");
    menu.add("View teams experience");
    menu.add("View a team roster");
    menu.add("Exit the program");
  }

  public static void main(String[] args) {
    Player[] players = Players.load();
    System.out.printf("There are currently %d registered players.%n", players.length);
    // Your code here!
    
    LeagueManager lm = new LeagueManager();
    lm.availablePlayers.addAll(Arrays.asList(players));
    lm.run();
  }
  
  public void run() {
    String menuOption = "";
    boolean wantToExit = false;
    do {
      System.out.printf("%n%n-------------Menu------------%n");
      menuOption = (String) presentWithOptions(menu);
      switch (menuOption) {
        case "Create a new team" :
          createTeam();
          System.out.printf("There are %d teams now.%n", teams.size());
          break;
        case "Add a player to a team" : 
          addPlayerToTeam();
          break;
        case "Remove player from team" :
          removePlayerFromTeam();
          break;
        case "View team stats by height" : 
          showPlayersByHeight();
          break;
        case "View teams experience" : 
          showTeamsExperienceRatio();
          break;
        case "View a team roster" :
          viewTeamRoster();
          break;
        case "Exit the program" :
          wantToExit = true;
          break;
        default :
          System.out.println("Didn't get the input");
          break;
      }
    } while (!wantToExit);
  }
  
  private void addPlayerToTeam() {
    //1.check if there are teams and players
    if (availablePlayers.isEmpty()) {
      System.out.println("There are no available players left!");
      return;
    }
    if (teams.isEmpty()) {
      System.out.println("There are no teams, create one first!");
      return;
    }
    
    //2.ask what team 
    System.out.printf("There are %d teams. Which one do you want to add to?%n", teams.size());
    Team team = (Team) presentWithOptions(teams);
    //3.check if there is still available position in the team
    if (team.getSize() >= Team.MAX_PLAYERS) {
      System.out.println("The team is complete, you can not add more players in it");
      return;
    }
    //4. ask for player and add
    Player player = (Player) presentWithOptions(availablePlayers);
    team.addPlayer(player);
    System.out.printf("%s. It consists of %d players now%n", team, team.getSize());
    
    //5. remove the player from available players
    availablePlayers.remove(player);
  }
  
  private void removePlayerFromTeam() {
    if (teams.isEmpty()) {
      System.out.println("There are no teams, create one first!");
      return;
    }
    
    System.out.printf("There are %d teams. Which one do you want to remove from?%n", teams.size());
    Team team = (Team) presentWithOptions(teams);
    if (team.getPlayers().isEmpty()) {
      System.out.println("There are already no players in the team!");
      return;
    }
    
    System.out.printf("There are %d players in the team. Which one do you want to remove?%n", team.getPlayers().size());
    Player player = (Player) presentWithOptions(team.getPlayers());
    if (team.removePlayer(player))
       System.out.printf("%s. It consists of %d players now%n", team, team.getSize());
    
  }
  
  private Team createTeam() {
    //1. check if there are any available players
    if (availablePlayers.isEmpty()) {
      System.out.println("There are no available players left, you cannot create a new team!");
      return null;
    }
    
    //2. ask for the team name
    System.out.println("Enter team name:");
    String teamName = scanner.nextLine();
    //3. ask for coach name
    System.out.println("Enter coach name:");
    String coachName = scanner.nextLine();
    
    //4. create the team
    Team team = new Team(teamName, coachName);
    //5. add it to teams set
    teams.add(team);
    
    return team;
  }
  
  private void showPlayersByHeight() {
    if (teams.isEmpty()) {
      System.out.println("There are no teams, create one and fill with players first!");
      return;
    }
    //1. ask which team to show
    System.out.println("Which team do you want to inspect?");
    Team team = (Team) presentWithOptions(teams);
    if (team.getPlayers().isEmpty()) {
      System.out.println("There are already no players in the team!");
      return;
    }
    
    System.out.println("The players by height are:");
    //2. group by height
    for (Map.Entry<String, Set<Player>> height : team.getPlayersByHeight().entrySet()) {
      System.out.printf("%n--- There are %d players of height %s. ---%n", height.getValue().size(), height.getKey());
      for(Player player : height.getValue()) {
        System.out.println(player);
      }
    }
  }
  
  public void viewTeamRoster() {
    if (teams.isEmpty()) {
      System.out.println("There are no teams, create one and fill with players first!");
      return;
    }
    //1. Ask for a team
    System.out.println("Which team roster do you want to see?");
    Team team = (Team) presentWithOptions(teams);
    if (team.getPlayers().isEmpty()) {
      System.out.println("There are no players in the team!");
      return;
    }
    //2. show every player
    System.out.println(" - Last Name - First Name --- Height in Inches --- Has experience - ");
    for (Player player : team.getPlayers()) {
      System.out.printf(" - %s - %s --- %d --- %s - ", 
                        player.getLastName(),
                        player.getFirstName(),
                        player.getHeightInInches(),
                        player.isPreviousExperience());
    }
    System.out.println("--------------------********----------------");
  }
  
  private void showTeamsExperienceRatio() {
    if (teams.isEmpty()) {
      System.out.println("There are no teams, create one and fill with players first!");
      return;
    }
    System.out.printf("%n%nTeam  ---------------------------   Experienced / Unexperienced -----%n");
    //for each team show the stats
    for (Team team : teams) {
      String teamExp = "";
      int experienced = 0;
      int unexperienced = 0;
      for (Player player : team.getPlayers()) {
        if (player.isPreviousExperience()) {
          experienced ++;
        } else {
          unexperienced ++;
        }
      }
      
      System.out.printf("%s  -------- %d / %d --------%n", team, experienced, unexperienced);
    }
  }
  
  private Object presentWithOptions(List list) {
    int choice = -1;
    int counter = 1;
    boolean gotAnswer = false;
    for (Object o : list) {
      System.out.printf("%d)  %s;%n", counter, o);
      counter++;
    }
    do {
      try {
        System.out.printf("%nChoose one and enter the number:  ");
        String choiceString = scanner.nextLine().trim();
        choice = Integer.parseInt(choiceString);
        if (choice > 0 && choice < counter)
          gotAnswer = true;
        else 
          System.out.printf("%nEnter an integer from 1 to %d%n", counter - 1);
      } catch (NumberFormatException nfe) {
        System.out.println("Incorrect input, please enter an integer");
      }
    } while (!gotAnswer);
    return list.get(choice - 1);
  }
  
  private Object presentWithOptions(Collection collection) {
    List list = new ArrayList(collection);
    return presentWithOptions(list);
  }

}
