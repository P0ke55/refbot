package p0ke.refbot.squad;

import java.awt.Color;
import java.util.List;

public class SquadConfiguration implements Comparable<SquadConfiguration> {
	
	private String name;
	private List<String> members;
	private double score;
	private String color;
	
	
	public String getName(){
		return name;
	}
	
	public List<String> getMembers(){
		return members;
	}
	
	public double getScore(){
		return score;
	}
	
	public Color getColor(){
		return Color.decode(color);
	}
	
	public void addMembers(List<String> id){
		members.addAll(id);
	}
	
	public void removeMembers(List<String> id){
		for(String r : id){
			for(int i = 0; i < members.size(); i++){
				if(members.get(i).equalsIgnoreCase(r)){
					members.remove(i);
				}
			}
		}
	}
	
	public void setColor(Color c){
		color = "#"+Integer.toHexString(c.getRGB()).substring(2);
	}
	
	public void addScore(double s){
		score += s;
	}
	
	public void resetScore(){
		score = 1000;
	}
	
	public SquadConfiguration(String n, List<String> m, int s){
		name = n;
		members = m;
		score = s;
		color = "#"+Integer.toHexString(Color.gray.getRGB()).substring(2);
	}

	@Override
	public int compareTo(SquadConfiguration o2) {
		if(score < o2.score){
			return 1;
		} else if(score > o2.score){
			return -1;
		} else {
			return 0;
		}
	}

}
