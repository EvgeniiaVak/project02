package com.teamtreehouse.model;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Team implements Comparable<Team>{
  public static final int MAX_PLAYERS = 11;
  public static final String HIGHT_35_40 = "35-40";
  public static final String HIGHT_41_46 = "41-46";
  public static final String HIGHT_47_50 = "47-50";
  private String teamName;
  private String coachName;
  private Set<Player> players;
  private Map<String, Set<Player>> playersByHeight;
  
  public Team (String teamName, String coachName) {
    this.teamName = teamName;
    this.coachName = coachName;
    players = new TreeSet<>();
    
    playersByHeight = new TreeMap<>();
    playersByHeight.put(HIGHT_35_40, new TreeSet<>());
    playersByHeight.put(HIGHT_41_46, new TreeSet<>());
    playersByHeight.put(HIGHT_47_50, new TreeSet<>());
  }
  
  public boolean addPlayer(Player player) {
    return players.add(player);
  }
  
  public boolean removePlayer(Player player) {
    return players.remove(player);
  }
  
  public Set<Player> getPlayers() {
    return players;
  }
  
  public int getSize() {
    return players.size();
  }
  
  public Map<String, Set<Player>> getPlayersByHeight() {
    sortPlayersByHeight();
    return playersByHeight;
  }
  
  public void sortPlayersByHeight() {
    for (Player player : players) {
      if (player.getHeightInInches() < 41) {
        playersByHeight.get(HIGHT_35_40).add(player);
      } else
      if (player.getHeightInInches() >= 41 && player.getHeightInInches() <= 46) {
        playersByHeight.get(HIGHT_41_46).add(player);
      } else {
        playersByHeight.get(HIGHT_47_50).add(player);
      }
    }
  }
  
  @Override
  public int compareTo(Team other) {
    if (equals(other))
      return 0;
    return (teamName+coachName).compareTo(other.teamName+other.coachName);
  }
  
  @Override
  public String toString() {
    return String.format("Team \"%s\", coached by %s", teamName, coachName);
  }
}