package p0ke.refbot.command;

import java.util.List;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;

import p0ke.refbot.RefBot;
import p0ke.refbot.util.RMessageBuilder;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

public class UptimeCommand extends CommandBase {

	@Override
	public void run(List<String> args, MessageReceivedEvent event, RMessageBuilder msg) throws Exception {
		
		Duration duration = new Interval(RefBot.instance.login, new Instant()).toDuration();
		
		msg.withContent("RefereeBot has been up for `" + duration.getStandardDays() + "D:" + duration.getStandardHours()%24 + "H:" + duration.getStandardMinutes()%60 + "M:" + duration.getStandardSeconds()%60 + "S`").build();
		
	}
}