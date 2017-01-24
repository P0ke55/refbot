package p0ke.refbot.command;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import p0ke.refbot.RefBot;
import p0ke.refbot.squad.SquadConfiguration;
import p0ke.refbot.util.RMessageBuilder;
import p0ke.refbot.util.Util;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

public class SquadCommand extends CommandBase {
	
	@Override
	public void run(List<String> args, MessageReceivedEvent event, RMessageBuilder msg) throws Exception {
		if(args.size() > 0){
			if(args.get(0).equalsIgnoreCase("add")){
				if(isOwner(event)){
					String squadName = StringUtils.substringAfter(
							StringUtils.substringAfter(StringUtils.substringBefore(event.getMessage().getContent(), "<"), " "),
							" ").trim();
					List<String> members = new ArrayList<String>();
					for(IUser u : event.getMessage().getMentions()){
						members.add(u.getID());
					}
					if(!(squadName.isEmpty() && members.isEmpty())){
						RefBot.instance.squadManager.addSquad(new SquadConfiguration(squadName, members, 1000));
						msg.withContent("Added squad: " + squadName).build();
					} else {
						msg.withContent("Not a valid squad! Make sure you tag all members and include the squad's name!").build();
					}
				} else {
					msg.withContent("Insufficient permissions!");
				}
			} else if(args.get(0).equalsIgnoreCase("list")){
				if(!RefBot.instance.squadManager.getSquads().isEmpty()){
					StringBuilder builder = new StringBuilder();
					for(SquadConfiguration squad : RefBot.instance.squadManager.getSquads()){
						builder.append(squad.getName());
						builder.append(" - ");
						for(String id : squad.getMembers()){
							builder.append(StringUtils.substringBefore(event.getMessage().getGuild().getUserByID(id).getDisplayName(event.getMessage().getGuild()), "|").trim());
							builder.append(", ");
						}
						builder.setLength(builder.length() - 2);
						builder.append("\n");
					}
					builder.setLength(builder.length() - 1);
					msg.withQuote(builder.toString()).build();
				} else {
					msg.withContent("There are no squads to list!").build();
				}
			} else if(args.get(0).equalsIgnoreCase("remove")){
				if(isOwner(event)){
					String squadName = StringUtils
							.substringAfter(StringUtils.substringAfter(event.getMessage().getContent(), " "), " ").trim();
					if(RefBot.instance.squadManager.removeSquad(squadName)){
						msg.withContent("Removed squad: " + squadName).build();
					} else {
						msg.withContent("That squad does not exist!").build();
					}
				} else {
					msg.withContent("Insufficient permissions!");
				}
			} else if(args.get(0).equalsIgnoreCase("addplayer")){
				if(isOwner(event)){
					String squadName = StringUtils.substringAfter(
							StringUtils.substringAfter(StringUtils.substringBefore(event.getMessage().getContent(), "<"), " "),
							" ").trim();
					List<String> members = new ArrayList<String>();
					for(IUser u : event.getMessage().getMentions()){
						members.add(u.getID());
					}
					if(!(squadName.isEmpty() && members.isEmpty())){
						if(RefBot.instance.squadManager.addPlayers(squadName, members)){
							msg.withContent("Added " + (members.size()) + " members to " + squadName).build();
						} else {
							msg.withContent("That squad does not exist!").build();
						}
					} else {
						msg.withContent("Please include the squad's name and the tags of the players you wish to add!").build();
					}
				} else {
					msg.withContent("Insufficient permissions!");
				}
			} else if(args.get(0).equalsIgnoreCase("removeplayer")){
				if(isOwner(event)){
					String squadName = StringUtils.substringAfter(
							StringUtils.substringAfter(StringUtils.substringBefore(event.getMessage().getContent(), "<"), " "),
							" ").trim();
					List<String> members = new ArrayList<String>();
					for(IUser u : event.getMessage().getMentions()){
						members.add(u.getID());
					}
					if(!(squadName.isEmpty() && members.isEmpty())){
						if(RefBot.instance.squadManager.removePlayers(squadName, members)){
							msg.withContent("Removed " + (members.size()) + " members from " + squadName).build();
						} else {
							msg.withContent("That squad does not exist!").build();
						}
					} else {
						msg.withContent("Please include the squad's name and the tags of the players you wish to add!").build();
					}
				} else {
					msg.withContent("Insufficient permissions!");
				}
				
			} else if(args.get(0).equalsIgnoreCase("reload")){
				if(isOwner(event)){
					RefBot.instance.squadManager.reloadSquads();
				} else {
					msg.withContent("Insufficient permissions!");
				}
				
			} else if(args.get(0).equalsIgnoreCase("score")){
				if(!RefBot.instance.squadManager.getSquads().isEmpty()){
					StringBuilder b = new StringBuilder();
					List<SquadConfiguration> orderedSquads = RefBot.instance.squadManager.getSquads();
					Collections.sort(orderedSquads);
					for(SquadConfiguration squad : orderedSquads){
						b.append(squad.getName());
						b.append(" - ");
						b.append(Math.round((squad.getScore()) * 10)/10.0);
						b.append("\n");
					}
					b.setLength(b.length() - 1);
					
					msg.withQuote(b.toString()).build();
				
				} else {
					msg.withContent("No squads to list!").build();
				}
			
			} else if(args.get(0).equalsIgnoreCase("reset")){
				if(isOwner(event)){
					RefBot.instance.squadManager.resetSquads();
					msg.withContent("Reset squads!").build();
				}
			} else if(args.get(0).equalsIgnoreCase("color")){
				
				if(args.size() > 1){
					Color color = null;
					try {
						if(args.get(1).startsWith("#")){
							color = Color.decode(args.get(1));
						} else {
							color = Color.decode("#" + args.get(1));
						}
					} catch (Exception e){
						
					}
					
					SquadConfiguration squad = RefBot.instance.squadManager.getSquadFromMember(event.getMessage().getAuthor().getID());
					
					if(color != null){
						if(RefBot.instance.squadManager.changeColor(event.getMessage().getAuthor().getID(), color)){
							msg.withContent("Changed " + squad.getName() + "'s color!").build();
						} else {
							msg.withContent("You are not in a squad!").build();
						}
					} else {
						msg.withContent("Not a valid hex color! Use http://www.2createawebsite.com/build/hex-colors.html").build();
					}
					
				} else {
					msg.withContent("Use !squad color <color>").build();
				}
				
			} else if(args.get(0).equalsIgnoreCase("help")){
				StringBuilder b = new StringBuilder();
				b.append("!squad list\n");
				b.append("!squad score\n");
				b.append("!squad color #COLOR\n");
				b.append("!squad add <Squad Name> <Member Tags>\n");
				b.append("!squad remove <Squad Name>\n");
				b.append("!squad addplayer <Squad Name> <Member Tag>\n");
				b.append("!squad removeplayer <Squad Name> <Member Tag>\n");
				b.append("!squad reset");
				msg.withQuote(b.toString()).build();
			} else {
				msg.withContent("Use !squad help").build();
			}
				
		} else {
			msg.withContent("Use !squad help").build();
		}
	}
	
	private static boolean isOwner(MessageReceivedEvent e){
		return (Util.getRoleNames(e.getMessage().getAuthor(), e.getMessage().getGuild()).contains("Owner") ? true : false);
	}
	
}
