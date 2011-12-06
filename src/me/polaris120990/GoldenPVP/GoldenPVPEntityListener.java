package me.polaris120990.GoldenPVP;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;

public class GoldenPVPEntityListener extends EntityListener
{
	DecimalFormat df = new DecimalFormat("#.##");
	public void onEntityDeath(EntityDeathEvent event)
	{
		Player killer;
		Player victim;
		Entity e = event.getEntity();
		if(e.getLastDamageCause() instanceof EntityDamageByEntityEvent)
		{
			EntityDamageByEntityEvent nEvent = (EntityDamageByEntityEvent) e.getLastDamageCause();
			if(nEvent.getDamager() instanceof Player)
			{
				killer = (Player) nEvent.getDamager();
				if(nEvent.getEntity() instanceof Player)
				{
					victim = (Player) nEvent.getEntity();
					int vdeath = GoldenPVP.PlayerData.getInt(victim.getName() + ".deaths");
					int kkill = GoldenPVP.PlayerData.getInt(killer.getName() + ".kills");
					int kdeath = GoldenPVP.PlayerData.getInt(killer.getName() + ".deaths");
					int vkill = GoldenPVP.PlayerData.getInt(victim.getName() + ".kills");
					vdeath++;
					kkill++;
					GoldenPVP.PlayerData.set(victim.getName() + ".deaths", vdeath);
					GoldenPVP.PlayerData.set(killer.getName() + ".kills", kkill);
					double vdeathd = vdeath;
					double kkilld = kkill;
					double kdeathd = kdeath;
					double vkilld = vkill;
					double ratiok = (kkilld/(kdeathd + kkilld));
					GoldenPVP.PlayerData.set(killer.getName() + ".ratio", df.format(ratiok));
					double ratiov = (vkilld / (vkilld + vdeathd));
					GoldenPVP.PlayerData.set(victim.getName() + ".ratio", df.format(ratiov));
					if(GoldenPVP.PlayerData.getInt(victim.getName() + ".bounty.amount") > 0)
					{
						Integer i = GoldenPVP.PlayerData.getInt(victim.getName() + ".bounty.amount");
						String bounty = i.toString();
						try {
							Economy.add(killer.getName(), GoldenPVP.PlayerData.getInt(victim.getName() + ".bounty.amount"));
						} catch (UserDoesNotExistException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (NoLoanPermittedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						Bukkit.broadcastMessage(ChatColor.AQUA + killer.getName() + ChatColor.RED + " has claimed the bounty on " + ChatColor.AQUA + victim.getName() + "'s" + ChatColor.RED + " head!!");
						GoldenPVP.PlayerData.set(victim.getName() + ".bounty.amount", 0);
						GoldenPVP.PlayerData.set(victim.getName() + ".bounty.host", null);
						killer.sendMessage(ChatColor.GREEN + "You have claimed a " + ChatColor.AQUA + "$" + bounty + ChatColor.GREEN + " bounty!!");
						
					}
					
					GoldenPVP.saveYamls();
					
				}
			}
		}
	}
}
