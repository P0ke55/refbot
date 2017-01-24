package p0ke.refbot.command;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import p0ke.refbot.RefBot;
import p0ke.refbot.player.PlayerConfiguration;
import p0ke.refbot.util.RMessageBuilder;
import p0ke.refbot.util.Util;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;


public class ScoreCommand extends CommandBase {
	
	private DecimalFormat df = new DecimalFormat("###.#");
	
	
	@Override
	public void run(List<String> args, MessageReceivedEvent event, RMessageBuilder msg) throws Exception {
		if(args.size() > 0 && args.get(0).equalsIgnoreCase("trim")){
			if(Util.getRoleNames(event.getMessage().getAuthor(), event.getMessage().getGuild()).contains("Owner")){
				RefBot.instance.playerManager.trimPlayers();
			}
		} else if(!RefBot.instance.playerManager.getPlayers().isEmpty()){
			StringBuilder b = new StringBuilder();
			List<PlayerConfiguration> players = RefBot.instance.playerManager.getPlayers();
			Collections.sort(players);
			b.append("------------------------------|--Wins--|--Losses--|--WLR--|\n");
			for(PlayerConfiguration player : players){
				String name = StringUtils.substringBefore(event.getMessage().getGuild().getUserByID(player.getID()).getDisplayName(event.getMessage().getGuild()), "|").trim();
				b.append(name);
				for(int i = 0; i < (30 - name.length()); i++){
					b.append(" ");
				}
				b.append("|   ");
				String wins = Integer.toString(player.getWins());
				b.append(wins);
				for(int i = 0; i < (5 - wins.length()); i++){
					b.append(" ");
				}
				b.append("|    ");
				String losses = Integer.toString(player.getLosses());
				b.append(losses);
				for(int i = 0; i < (6 - losses.length()); i++){
					b.append(" ");
				}
				b.append("| ");
				String wlr = df.format(player.getWLR());
				b.append(wlr);
				for(int i = 0; i < (6 - wlr.length()); i++){
					b.append(" ");
				}
				b.append("|\n");
				
			}
			b.setLength(b.length() - 1);
			
			msg.withQuote(b.toString()).build();
		
		} else {
			msg.withContent("No players to list!").build();
		}
	}

}
