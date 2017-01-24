package p0ke.refbot.report;

public class Report {
	
	private String winner;
	private String loser;
	private int winnerGames;
	private int loserGames;
	
	public String getWinner(){
		return winner;
	}
	
	public String getLoser(){
		return loser;
	}
	
	public int getGamesWon(){
		return winnerGames;
	}
	
	public int getGamesLost(){
		return loserGames;
	}
	
	public Report(String w, String l, int wG, int lG){
		winner = w;
		loser = l;
		winnerGames = wG;
		loserGames = lG;
	}

}
