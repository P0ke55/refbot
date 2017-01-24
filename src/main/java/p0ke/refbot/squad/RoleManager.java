package p0ke.refbot.squad;

import java.util.List;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.RoleBuilder;

public class RoleManager {

	private IGuild guild;
	
	public RoleManager(IGuild g){
		guild = g;
	}
	
	public void updateRoles(List<SquadConfiguration> squads){
		
		for(SquadConfiguration squad : squads){
			if(guild.getRolesByName(squad.getName()).isEmpty()){
				try {
					RoleBuilder rb = new RoleBuilder(guild);
					rb.withName(squad.getName());
					rb.setMentionable(true);
					rb.setHoist(true);
					rb.withColor(squad.getColor());
					rb.build();
				} catch (Exception e){
					
				}
			}
			
			for(String id : squad.getMembers()){
				IUser user = guild.getUserByID(id);
				boolean in = false;
				for(IRole r : user.getRolesForGuild(guild)){
					if(r.getName().equalsIgnoreCase(squad.getName())){
						in = true;
					}
				}
				if(!in){
					try {
						user.addRole(guild.getRolesByName(squad.getName()).get(0));
					} catch (Exception e){
						
					}
				}
			}
			
		}
		
		this.updateColors(squads);
		
	}
	
	public void updateColors(List<SquadConfiguration> squads){
		for(SquadConfiguration squad : squads){
			try {
				IRole role = guild.getRolesByName(squad.getName()).get(0);
				if(!role.getColor().equals(squad.getColor())){
					role.changeColor(squad.getColor());
				}
			} catch (Exception e){
				//color is probably null, we can ignore it
			}
		}
	}
	
}
