package me.polaris120990.GoldenPVP;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class GoldenPVPPlayerListener extends PlayerListener
{
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		if(GoldenPVP.PlayerData.get(event.getPlayer().getName() + ".kills") == null)
		{
			if(GoldenPVP.PlayerData.get("player.list") == null)
			{
				List<String> players = new ArrayList<String>();
				players.add(event.getPlayer().getName());
				GoldenPVP.PlayerData.set("player.list", players);
			}
			else
			{
				@SuppressWarnings("unchecked")
				List<String> playerlist = GoldenPVP.PlayerData.getList("player.list");
				playerlist.add(event.getPlayer().getName());
				GoldenPVP.PlayerData.set("player.list", playerlist);		
			}
			GoldenPVP.PlayerData.set(event.getPlayer().getName() + ".kills", 0);
			GoldenPVP.PlayerData.set(event.getPlayer().getName() + ".deaths", 0);
			GoldenPVP.PlayerData.set(event.getPlayer().getName() + ".ratio", "0");
			GoldenPVP.PlayerData.set(event.getPlayer().getName() + ".bounty.amount", 0);

			GoldenPVP.saveYamls();
		}
	}
}
