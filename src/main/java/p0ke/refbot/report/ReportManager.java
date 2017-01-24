package p0ke.refbot.report;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import p0ke.refbot.squad.SquadConfiguration;
import p0ke.refbot.squad.SquadManager;
import p0ke.refbot.util.RMessageBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;

public class ReportManager {

	private HashMap<String, Report> reports = new HashMap<String, Report>();
	private HashMap<String, IMessage> messages = new HashMap<String, IMessage>();
	private SquadManager squadManager;
	private IGuild guild;
	private IChannel leaderboard;
	private IDiscordClient client;

	public ReportManager(SquadManager sm, IGuild g, IDiscordClient c) {
		squadManager = sm;
		guild = g;
		leaderboard = g.getChannelByID("254956597355347970");
		client = c;
	}

	public boolean addReport(String w, String l, int wG, int lG) {
		if (!reports.containsKey(w)) {
			reports.put(w, new Report(w, l, wG, lG));
			return true;
		} else {
			return false;
		}

	}
	
	public void addMessage(String filer, IMessage m){
		messages.put(filer, m);
	}

	public boolean cancelReport(String filer) {
		if (reports.containsKey(filer)) {
			reports.remove(filer);
			try {
				messages.get(filer).delete();
			} catch (Exception e){
				
			}
			messages.remove(filer);
			return true;
		} else {
			return false;
		}
	}

	public boolean resolveReport(String filer, String author) throws Exception {
		if (reports.containsKey(filer)) {
				if(reports.get(filer).getLoser().equals(author)){
				try {
					messages.get(filer).delete();
				} catch (Exception e){
					
				}
				Report report = reports.get(filer);
				reports.remove(filer);
				messages.remove(filer);
				try {
				SquadConfiguration squadW = squadManager.getSquadFromMember(report.getWinner());
				SquadConfiguration squadL = squadManager.getSquadFromMember(report.getLoser());
				double sW = 0;
				double sL = 0;
				double scoreW = squadW.getScore();
				double scoreL = squadL.getScore();
				if (scoreW > (scoreL + 7)) {
					sW = ((5 + 3 * (scoreL / scoreW)));
					sL = (-1 * (5 + 3 * (scoreL / scoreW)));
				} else if (scoreW < (scoreL - 7)) {
					sW = (10 + 3 * (scoreL / scoreW));
					sL = (-1 * (10 + 3 * (scoreL / scoreW)));
				} else {
					sW = 8;
					sL = -8;
				}
	
				squadManager.fileReport(report, sW, sL);
				RMessageBuilder mb = new RMessageBuilder(client);
				mb.withChannel(leaderboard)
						.withQuote(getName(report.getWinner()) + " of " + squadW.getName() + " (" + (Math.round((scoreW)*10)/10.0) + "->"
								+ (Math.round((scoreW + sW)*10)/10.0) + ") wins " + report.getGamesWon() + "-" + report.getGamesLost() + " over "
								+ getName(report.getLoser()) + " of " + squadL.getName() + " (" + (Math.round((scoreL)*10)/10.0) + "->"
								+ (Math.round((scoreL + sL)*10)/10.0) + ")!")
						.build();
				} catch (Exception e){
					
				}
	
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public boolean denyReport(String filer, String author){
		if(reports.containsKey(filer)){
			if(reports.get(filer).getLoser().equals(author)){
				reports.remove(filer);
				try {
					messages.get(filer).delete();
				} catch (Exception e){
					
				}
				messages.remove(filer);
				return true;
			} 
		}
		return false;
	}

	private String getName(String id) {
		return StringUtils.substringBefore(guild.getUserByID(id).getDisplayName(guild), "|").trim();
	}

}
