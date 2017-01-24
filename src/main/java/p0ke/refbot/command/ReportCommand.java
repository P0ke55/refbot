package p0ke.refbot.command;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import p0ke.refbot.RefBot;
import p0ke.refbot.util.RMessageBuilder;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

public class ReportCommand extends CommandBase {
	
	Timer timer = new Timer();

	@Override
	public void run(List<String> args, MessageReceivedEvent event, RMessageBuilder msg) throws Exception {
		if (args.size() > 0 && event.getMessage().getChannel().getID().equals("256639339939758101")) {
			if (args.get(0).equalsIgnoreCase("confirm")) {
				if(!event.getMessage().getMentions().isEmpty()){
					if(RefBot.instance.reportManager.resolveReport(event.getMessage().getMentions().get(0).getID(), event.getMessage().getAuthor().getID())){
						delayDelete(msg.withContent("Resolved report!").build());
					} else {
						delayDelete(msg.withContent("That player does not have an open report on you!").build());
					}
				} else {
					delayDelete(msg.withContent("Please tag your opponent!").build());
				}
			} else if(args.get(0).equalsIgnoreCase("deny")){
				if(!event.getMessage().getMentions().isEmpty()){
					if(RefBot.instance.reportManager.denyReport(event.getMessage().getMentions().get(0).getID(), event.getMessage().getAuthor().getID())){
						delayDelete(msg.withContent("Denied report!").build());
					} else {
						delayDelete(msg.withContent("That player does not have an open report on you!").build());
					}
				} else {
					delayDelete(msg.withContent("Please tag your opponent!").build());
				}
			} else if (args.get(0).equalsIgnoreCase("cancel")) {
				if(RefBot.instance.reportManager.cancelReport(event.getMessage().getAuthor().getID())){
					delayDelete(msg.withContent("Report canceled!").build());
				} else {
					delayDelete(msg.withContent("You do not have an open report!").build());
				}
			} else {
				if (args.get(0).contains("-")) {
					if (!event.getMessage().getMentions().isEmpty()) {
						String idL = event.getMessage().getMentions().get(0).getID();
						String idW = event.getMessage().getAuthor().getID();
						String scoreString[] = args.get(0).split("-");
						int scores[] = { 0, 0 };
						try {
							scores[0] = Integer.parseInt(scoreString[0]);
							scores[1] = Integer.parseInt(scoreString[1]);
						} catch (Exception e) {
							delayDelete(msg.withContent("Invalid score! Please use !reportwin W-L @Player").build());
							return;
						}

						if (scores[0] > scores[1] && (scores[0]) == 3) {
							
							if(RefBot.instance.squadManager.getSquadFromMember(idW) != null
									&& (RefBot.instance.squadManager.getSquadFromMember(idL) != null)){
							
								if (!RefBot.instance.squadManager.getSquadFromMember(idW).getName()
										.equals(RefBot.instance.squadManager.getSquadFromMember(idL).getName())) {
									if (RefBot.instance.reportManager.addReport(idW, idL, scores[0], scores[1])) {
										RefBot.instance.reportManager.addMessage(idW, msg.appendContent("Logged report! "
												+ event.getMessage().getGuild().getUserByID(idL).mention()
												+ " please confirm or deny " + event.getMessage().getAuthor().mention() + "'s "
												+ scores[0] + "-" + scores[1] + " victory using !reportwin confirm/deny @Player")
												.build());
									} else {
										delayDelete(msg.appendContent("You already have an open report!").build());
									}
	
								} else {
									delayDelete(msg.withContent("You cannot report a win over a squad member!").build());
								}
							
							} else {
								delayDelete(msg.withContent("Both players must be in squads!").build());
							}

						} else {
							delayDelete(msg.withContent("Invalid score! Please use !reportwin W-L @Player").build());
						}

					} else {
						delayDelete(msg.withContent("You didn't include your opponent! Please use !reportwin W-L @Player").build());

					}

				} else {
					delayDelete(msg.withContent("Invalid score! Please use !reportwin W-L @Player").build());
				}
			}
		} else {
			delayDelete(msg.withContent("Please check out #bot-info to learn how to use this command!").build());
		}
	}
	
	
	public void delayDelete(IMessage m){
		TimerTask delay = new TimerTask() {
			public void run(){
				try {
					m.delete();
				} catch (Exception e){
					
				}
			}
		};
		timer.schedule(delay, 240000);
	}

}
