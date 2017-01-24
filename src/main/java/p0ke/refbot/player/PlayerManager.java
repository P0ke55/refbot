package p0ke.refbot.player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import p0ke.refbot.RefBot;
import p0ke.refbot.report.Report;
import sx.blah.discord.handle.obj.IGuild;

public class PlayerManager {
	
	List<PlayerConfiguration> players;
	Gson gson = new Gson();
	File playerFile;
	IGuild guild;
	
	public PlayerManager(IGuild g){
		guild = g;
		playerFile = new File("./players.json");
		if(!playerFile.exists()){
			try {
				playerFile.createNewFile();
			} catch (Exception e){
				System.out.println("Error creating player file! Stacktrace below.");
				e.printStackTrace();
			}
		}
		
		
	}
	
	public void loadPlayers(){
		
		players = new ArrayList<PlayerConfiguration>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(playerFile));
			Type type = new TypeToken<List<PlayerConfiguration>>(){}.getType();
			List<PlayerConfiguration> filePlayers = gson.fromJson(reader, type);
			if(filePlayers != null){
				players = filePlayers;
			}
			reader.close();
			System.out.println("Succesfully read " + players.size() + " players!");
		} catch (Exception e){
			System.out.println("Failed to read squad file! Stacktrace below.");
			e.printStackTrace();
			return;
		}
		
		
	}
	
	public void refreshPlayers(List<String> p){
		
		for(String id : p){
			if(!this.getTrackedPlayers().contains(id)){
				players.add(new PlayerConfiguration(id));
			}
		}
		
		this.savePlayers();
		
	}
	
	public void trimPlayers(){
		List<String> registeredPlayers = RefBot.instance.squadManager.getPlayers();
		List<PlayerConfiguration> playersToRemove = new ArrayList<PlayerConfiguration>();
		for(PlayerConfiguration p : players){
			if(!registeredPlayers.contains(p.getID()) || guild.getUserByID(p.getID()) == null){
				playersToRemove.add(p);
			}
		}
		players.removeAll(playersToRemove);
		this.savePlayers();
	}
	
	public void savePlayers(){
		try {
			Writer writer = new FileWriter(playerFile);
			writer.write(gson.toJson(players));
			writer.close();
		} catch(Exception e){
			System.out.println("Could not save players to file! Stacktrace below.");
			e.printStackTrace();
		}
	}
	
	public void fileReport(Report report){
		for(PlayerConfiguration player : players){
			if(player.getID().equals(report.getWinner())){
				player.addWin(report);
			}
			if(player.getID().equals(report.getLoser())){
				player.addLoss(report);
			}
		}
		
		this.savePlayers();
		
	}
	
	public List<PlayerConfiguration> getPlayers(){
		return players;
	}
	
	public List<String> getTrackedPlayers(){
		List<String> playerIDs = new ArrayList<String>();
		for(PlayerConfiguration p : players){
			if(!playerIDs.contains(p.getID())){
				playerIDs.add(p.getID());
			}
		}
		return playerIDs;
	}
	

}
