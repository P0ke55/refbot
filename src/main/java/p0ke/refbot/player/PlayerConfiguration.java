package p0ke.refbot.player;

import p0ke.refbot.report.Report;

public class PlayerConfiguration implements Comparable<PlayerConfiguration> {
	
	private String id;
	private double wins;
	private double losses;
	private double gamesWon;
	private double gamesLost;
	
	public String getID(){
		return id;
	}
	
	public int getWins(){
		return (int) wins;
	}
	
	public int getLosses(){
		return (int) losses;
	}
	
	public int getGamesWon(){
		return (int) gamesWon;
	}
	
	public int getGamesLost(){
		return (int) gamesLost;
	}
	
	public double getWLR(){
		if(wins != 0 && losses != 0){
			return (wins/losses);
		} else if(wins != 0 && losses == 0){
			return 999.9;
		} else {
			return 0.0;
		}
	}
	
	public double getGameWLR(){
		return (gamesWon/gamesLost);
	}
	
	public void addWin(Report report){
		wins++;
		gamesWon += report.getGamesWon();
		gamesLost += report.getGamesLost();
	}
	
	public void addLoss(Report report){
		losses++;
		gamesWon += report.getGamesLost();
		gamesLost += report.getGamesWon();
	}
	
	public PlayerConfiguration(String id){
		this.id = id;
		wins = 0;
		losses = 0;
		gamesWon = 0;
		gamesLost = 0;
	}

	@Override
	public int compareTo(PlayerConfiguration arg1) {
		if((wins) < (arg1.wins)){
			return 1;
		} else if((wins) > (arg1.wins)){
			return -1;
		} else {
			if(losses > arg1.losses){
				return 1;
			} else if(losses < arg1.losses){
				return -1;
			} else {
				return 0;
			}
		}
	}

}
