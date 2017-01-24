package p0ke.refbot;

import org.joda.time.DateTime;

import p0ke.refbot.command.CommandHandler;
import p0ke.refbot.player.PlayerManager;
import p0ke.refbot.report.ReportManager;
import p0ke.refbot.squad.RoleManager;
import p0ke.refbot.squad.SquadManager;
import p0ke.refbot.util.Util;
import sx.blah.discord.Discord4J;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.util.DiscordException;

public class RefBot {
	
	public static RefBot instance;
	
	public IDiscordClient client;
	public SquadManager squadManager;
	public CommandHandler commandHandler;
	public RoleManager roleManager;
	public PlayerManager playerManager;
	public ReportManager reportManager;
	
	public DateTime login;
	
	public static void main(String[] args) throws Exception {
		
		((Discord4J.Discord4JLogger) Discord4J.LOGGER).setLevel(Discord4J.Discord4JLogger.Level.NONE);
		
		try {
			instance = login("-------------------------------------------------------------");
		} catch (Exception e){
			System.out.println("Failed to connect to server. Stacktrace below:");
			e.printStackTrace();
			System.exit(1);
		}
		
	}

	@EventSubscriber
	public void ready(ReadyEvent event){
		
		roleManager = new RoleManager(client.getGuilds().get(0));
		playerManager = new PlayerManager(client.getGuilds().get(0));
		squadManager = new SquadManager(roleManager, playerManager, client.getGuilds().get(0));
		reportManager = new ReportManager(squadManager, client.getGuilds().get(0), client);
		
		
		commandHandler = new CommandHandler();
		
		
		
		System.out.println("Ready!");
	}
	
	@EventSubscriber
	public void onMessage(MessageReceivedEvent event){
		if(event.getMessage().getContent().startsWith("!")){
			commandHandler.handleCommand(event);
		}
		if(event.getMessage().getChannel().getID().equals("256639339939758101") && !Util.getRoleNames(event.getMessage().getAuthor(), event.getMessage().getGuild()).contains("Mew2King")){
			try {
				event.getMessage().delete(); //if a message in match-reporting is still around and isn't a bot/owner message
			} catch (Exception e){
				
			}
		}
		
		if(event.getMessage().getChannel().getID().equals("256603225518637068")){
			if(!(event.getMessage().getContent().contains("?") || Util.getRoleNames(event.getMessage().getAuthor(), event.getMessage().getGuild()).contains("Owner"))){
				try {
					event.getMessage().delete(); //if a message in questions doesn't have a question mark and isn't an owner message
				} catch (Exception e){
				
				}
			}
		}
	}
	
	public static RefBot login(String token) throws DiscordException { // Returns an instance of the Discord client
	    ClientBuilder clientBuilder = new ClientBuilder(); // Creates the ClientBuilder instance
	    clientBuilder.withToken(token); // Adds the login info 1to the builder
	    return new RefBot(clientBuilder.login()); // Creates the client instance and logs the client in
	}
	
	public RefBot(IDiscordClient c){
		this.client = c;
		this.login = DateTime.now();
		c.getDispatcher().registerListener(this);
	}
}
