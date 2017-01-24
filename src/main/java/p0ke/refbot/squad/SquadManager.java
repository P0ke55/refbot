package p0ke.refbot.squad;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import p0ke.refbot.player.PlayerManager;
import p0ke.refbot.report.Report;
import sx.blah.discord.handle.obj.IGuild;

public class SquadManager {
	
	private List<SquadConfiguration> squads;
	private Gson gson = new Gson();
	private File squadFile;
	private RoleManager roleManager;
	private PlayerManager playerManager;
	private IGuild guild;
	
	public SquadManager(RoleManager rm, PlayerManager pm, IGuild g){
		roleManager = rm;
		playerManager = pm;
		guild = g;
		squadFile = new File("./squads.json");
		if(!squadFile.exists()){
			try {
				squadFile.createNewFile();
			} catch (IOException e) {
				System.out.println("Could not create squads.json! Stacktrace below.");
				e.printStackTrace();
			}
		}
		
		this.loadSquads();
	}
	
	public void loadSquads(){
		squads = new ArrayList<SquadConfiguration>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(squadFile));
			Type type = new TypeToken<List<SquadConfiguration>>(){}.getType();
			List<SquadConfiguration> fileSquads = gson.fromJson(reader, type);
			if(fileSquads != null){
				squads = fileSquads;
			}
			reader.close();
			System.out.println("Succesfully read " + squads.size() + " squads!");
		} catch (Exception e){
			System.out.println("Failed to read squad file! Stacktrace below.");
			e.printStackTrace();
			return;
		}
		
		playerManager.loadPlayers();
		this.saveSquads();
		
	}
	
	public void saveSquads(){
		
		try {
			playerManager.refreshPlayers(this.getRegisteredPlayers());
			roleManager.updateRoles(squads);
			Writer writer = new FileWriter(squadFile);
			writer.write(gson.toJson(squads));
			writer.close();
			
		} catch (Exception e){
			System.out.println("Could not save squads to file! Stacktrace below.");
			e.printStackTrace();
		}
		
	}
	
	public void addSquad(SquadConfiguration newSquad){
		squads.add(newSquad);
		this.saveSquads();
	}
	
	public boolean removeSquad(String name){
		for(int i = 0; i < squads.size(); i++){
			if(squads.get(i).getName().equalsIgnoreCase(name)){
				squads.remove(i);
				this.saveSquads();
				return true;
			}
		}
		return false;
	}
	
	public boolean addPlayers(String squad, List<String> players){
		for(int i = 0; i < squads.size(); i++){
			if(squads.get(i).getName().equalsIgnoreCase(squad)){
				squads.get(i).addMembers(players);
				this.saveSquads();
				return true;
			}
		}
		return false;
	}
	
	public boolean removePlayers(String squad, List<String> players){
		for(int i = 0; i < squads.size(); i++){
			if(squads.get(i).getName().equalsIgnoreCase(squad)){
				squads.get(i).removeMembers(players);
				this.saveSquads();
				return true;
			}
		}	
		return false;
	}
	
	public boolean changeColor(String id, Color color){
		SquadConfiguration squad = this.getSquadFromMember(id);
		if(squad != null){
			squad.setColor(color);
			this.saveSquads();
			return true;
		}
		return false;
	}
	
	public SquadConfiguration getSquadFromMember(String id){
		for(SquadConfiguration squad : squads){
			if(squad.getMembers().contains(id)){
				return squad;
			}
		}
		return null;
	}
	
	public List<String> getPlayers(){
		List<String> players = new ArrayList<String>();
		for(SquadConfiguration s : squads){
			players.addAll(s.getMembers());
		}
		return players;
	}
	
	public void reloadSquads(){
		this.loadSquads();
		this.trimSquads();
	}
	
	public void resetSquads(){
		for(SquadConfiguration squad : squads){
			squad.resetScore();
		}
		
		this.saveSquads();
	}
	
	public void trimSquads(){
		for(SquadConfiguration squad : squads){
			for(String p : squad.getMembers()){
				if(guild.getUserByID(p) == null){
					squad.removeMembers(Arrays.asList(p));
				}
			}
		}
		this.saveSquads();
	}
	
	public List<String> getRegisteredPlayers(){
		List<String> players = new ArrayList<String>();
		for(SquadConfiguration squad : squads){
			for(String id : squad.getMembers()){
				if(!players.contains(id)){
					players.add(id);
				}
			}
		}
		return players;
	}
	
	public List<SquadConfiguration> getSquads(){
		return squads;
	}
	
	public void fileReport(Report report, double sW, double sL){
		try {
			
			this.getSquadFromMember(report.getWinner()).addScore(sW);
			this.getSquadFromMember(report.getLoser()).addScore(sL);
			
			this.saveSquads();
			playerManager.fileReport(report);
			
		} catch(Exception e){
			
		}
	}

}
