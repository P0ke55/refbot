package p0ke.refbot.command;

import java.util.ArrayList;
import java.util.List;

import p0ke.refbot.util.RMessageBuilder;
import p0ke.refbot.util.Util;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class ClearCommand extends CommandBase {

	@Override
	public void run(List<String> args, MessageReceivedEvent event, RMessageBuilder msg) throws Exception {
		if (Util.getRoleNames(event.getMessage().getAuthor(), event.getMessage().getGuild()).contains("Owner")) {
			if (args.size() > 1) {

				int messages = 0;
				try {
					messages = Integer.parseInt(args.get(1));
				} catch (NumberFormatException e) {
					msg.withContent("Use !clear @<user> <#>").build();
					return;
				}

				int i = 0;
				List<IMessage> messageList = new ArrayList<IMessage>();

				if (event.getMessage().getMentions().size() > 0) {
					IUser user = event.getMessage().getMentions().get(0);

					for (IMessage m : event.getMessage().getChannel().getMessages()) {
						if (m.getAuthor().equals(user)) {
							messageList.add(m);
							i++;
						}
						if (i >= messages) {
							break;
						}
					}

					event.getMessage().getChannel().getMessages().bulkDelete(messageList);

					//msg.withContent("Cleared the last " + messages + " of "
							//+ user.getDisplayName(event.getMessage().getGuild()) + "'s messages!").build();

				} else if (args.get(0).equalsIgnoreCase("all")) {

					for (IMessage m : event.getMessage().getChannel().getMessages()) {
						messageList.add(m);
						i++;
						if (i >= messages) {
							break;
						}
					}

					event.getMessage().getChannel().getMessages().bulkDelete(messageList);

					//msg.withContent("Cleared the last " + messages + " messages!").build();

				} else {
					msg.withContent("Use !clear @<user> <#>").build();
				}

			} else {
				msg.withContent("Use !clear @<user> <#>").build();
			}
		} else {
			msg.withContent("Insufficient permissions!").build();
		}
	}

}
