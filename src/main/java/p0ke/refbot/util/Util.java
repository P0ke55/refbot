package p0ke.refbot.util;

import java.util.ArrayList;
import java.util.List;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class Util {
	
	public static List<String> getRoleNames(IUser u, IGuild g){
		List<String> n = new ArrayList<String>();
		for(IRole role : u.getRolesForGuild(g)){
			n.add(role.getName());
		}
		return n;
	}

}
