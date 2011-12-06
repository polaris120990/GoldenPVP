package me.polaris120990.GoldenPVP;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import com.earth2me.essentials.api.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;

public class GoldenPVP extends JavaPlugin
{
	static File ConfigFile;
	static File PlayerDataFile;
	public static FileConfiguration PlayerData;
	public static FileConfiguration Config;
	public static PermissionHandler Permissions = null;
	public static boolean UsePermissions;
	public final Logger logger = Logger.getLogger("Minecraft");
	GoldenPVPEntityListener Elistener = new GoldenPVPEntityListener();
	GoldenPVPPlayerListener Plistener = new GoldenPVPPlayerListener();
	public void onEnable()
	{
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, Plistener, Event.Priority.Highest, this);
		pm.registerEvent(Event.Type.ENTITY_DEATH, Elistener, Event.Priority.Highest, this);
		ConfigFile = new File(getDataFolder(), "config.yml");
		PlayerDataFile = new File(getDataFolder(), "player.yml");
		 try
		 {
			 firstRun();
		 }
		 catch (Exception e)
		 {
			 e.printStackTrace();
		 }
		 Config = new YamlConfiguration();
		 PlayerData = new YamlConfiguration();
		 loadYamls();
		 PluginDescriptionFile pdfFile = this.getDescription();
		 this.logger.info("[" + pdfFile.getName() + "] v" + pdfFile.getVersion() + " has been enabled.");
		 if(Config.get("minbounty") == null)
		 {
			 Config.set("minbounty", 500);
			 saveYamls();
		 }
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args)
	{
		readCommand((Player) sender, CommandLabel, args);
		return false;
	}
	
	public void readCommand(Player sender, String command, String[] args)
	{
		/*if(command.equalsIgnoreCase("setmin"))
		{
			if(args.length == 1)
			{
				int price = Integer.parseInt(args[0]);
				Config.set("minbounty", price);
				saveYamls();
			}
		}*/
		if(command.equalsIgnoreCase("kdr"))
		{
			if(args.length == 0)
			{
				int sendkills = PlayerData.getInt(sender.getName() + ".kills");
				int senddeath = PlayerData.getInt(sender.getName() + ".deaths");
				String sendratio = PlayerData.getString(sender.getName() + ".ratio");
				sender.sendMessage(ChatColor.AQUA + sender.getName() + " " + ChatColor.GREEN + sendkills + ChatColor.WHITE + " / " + ChatColor.RED + senddeath + " " + ChatColor.GOLD + "RATIO: " + sendratio);
			}
			if(args.length == 1)
			{
				@SuppressWarnings("unchecked")
				List<String> playerlist = PlayerData.getList("player.list");
				String[] players;
				players = playerlist.toArray(new String[]{});
				int i = 0;
				while(i < players.length)
				{
					if(args[0].equalsIgnoreCase(players[i]))
					{
						int sendkills = PlayerData.getInt(players[i] + ".kills");
						int senddeath = PlayerData.getInt(players[i] + ".deaths");
						String sendratio = PlayerData.getString(players[i] + ".ratio");
						sender.sendMessage(ChatColor.AQUA + players[i] + " " + ChatColor.GREEN + sendkills + ChatColor.WHITE + " / " + ChatColor.RED + senddeath + " " + ChatColor.GOLD + "RATIO: " + sendratio);
						return;
					}
					i++;
				}
			}
			
		}
		
		if(command.equalsIgnoreCase("kdrtop"))
		{
			@SuppressWarnings("unchecked")
			List<String> playerlist = PlayerData.getList("player.list");
			String[] players;
			players = playerlist.toArray(new String[]{});
			String[] ratios = new String[players.length];
			int i = 0;
			while((i < players.length))
			{
				ratios[i] = PlayerData.getString(players[i] + ".ratio");
				i++;
			}
			Arrays.sort(ratios);
			i = (ratios.length - 1);
			int j = 0;
			Integer k = 1;
			while(i > -1)
			{
				while(j < players.length)
				{
					if(k > 10) break;
					if(ratios[i].equalsIgnoreCase(PlayerData.getString(players[j] + ".ratio")))
					{
						int sendkills = PlayerData.getInt(players[j] + ".kills");
						int senddeath = PlayerData.getInt(players[j] + ".deaths");
						String sendratio = PlayerData.getString(players[j] + ".ratio");
						sender.sendMessage(ChatColor.GRAY + k.toString() + ". " + ChatColor.AQUA + players[j] + " " + ChatColor.GREEN + sendkills + ChatColor.WHITE + " / " + ChatColor.RED + senddeath + " " + ChatColor.GOLD + "RATIO: " + sendratio);
						players[j] = null;
						int L = 0;
						List<String> playerholder = new ArrayList<String>();
						while(L < players.length)
						{
							if(players[L] != null)
							{
								playerholder.add(players[L]);
							}
							L++;
						}
						players = playerholder.toArray(new String[]{});
						k++;
						break;
					}
					j++;
				}
				j = 0;
				i--;
			}
		}
		if(command.equalsIgnoreCase("bounty"))
		{
			if(args.length == 0)
			{
				if(PlayerData.getInt(sender.getName() + ".bounty.amount") == 0)
				{
					sender.sendMessage(ChatColor.DARK_AQUA + "You do not have a bounty on your head!");
				}
				if(PlayerData.getInt(sender.getName() + ".bounty.amount") > 0)
				{
					String bountyamt = Integer.toString(PlayerData.getInt(sender.getName() + ".bounty.amount"));
					sender.sendMessage(ChatColor.AQUA + PlayerData.getString(sender.getName() + ".bounty.host") + ChatColor.DARK_AQUA + " has a bounty for " + ChatColor.AQUA + "$" + bountyamt + ChatColor.DARK_AQUA + " on your head!!");
				}
			}
			else if(args.length == 1)
			{
				@SuppressWarnings("unchecked")
				List<String> playerlist = PlayerData.getList("player.list");
				String[] players;
				players = playerlist.toArray(new String[]{});
				int i = 0;
				while(i < players.length)
				{
					if(players[i].equalsIgnoreCase(args[0]))
					{
						if(PlayerData.getInt(players[i] + ".bounty.amount") == 0)
						{
							sender.sendMessage(ChatColor.AQUA + players[i] + ChatColor.DARK_AQUA + " does not have a bounty on their head!");
							return;
						}
						else if(PlayerData.getInt(players[i] + ".bounty.amount") > 0)
						{
							String bountyamt = Integer.toString(PlayerData.getInt(players[i] + ".bounty.amount"));
							sender.sendMessage(ChatColor.AQUA + PlayerData.getString(players[i] + ".bounty.host") + ChatColor.DARK_AQUA + " has a bounty for " + ChatColor.AQUA + "$" + bountyamt + ChatColor.DARK_AQUA + " on " + ChatColor.AQUA + players[i] + "'s" + ChatColor.DARK_AQUA + " head!");
							return;
						}
					}
					i++;
				}
				sender.sendMessage(ChatColor.DARK_AQUA + "Sorry, could not find player: " + ChatColor.AQUA + args[0]);
			}
			else if(args.length == 2)
			{
				int minbounty = Config.getInt("minbounty");
				Player[] players = Bukkit.getOnlinePlayers();
				int i = 0;
				while(i < players.length)
				{
					if(players[i].getName().equalsIgnoreCase(args[0]))
					{
						if(players[i].getName().equalsIgnoreCase(sender.getName()))
						{
							sender.sendMessage("You cannot placec a bounty on yourself");
						}
						else{
						int price = Integer.parseInt(args[1]);
						if(PlayerData.getInt(players[i].getName() + ".bounty.amount") == 0)
						{
							if(price >= minbounty)
							{
								try {
									if(Economy.hasEnough(sender.getName(), price))
									{
										Economy.subtract(sender.getName(), price);
										PlayerData.set(players[i].getName() + ".bounty.amount", price);
										PlayerData.set(players[i].getName() + ".bounty.host", sender.getName());
										players[i].sendMessage(ChatColor.AQUA + sender.getName() + ChatColor.RED + " has set a " + ChatColor.AQUA + "$" + price + ChatColor.RED + " bounty on your head!!");
										Bukkit.broadcastMessage(ChatColor.AQUA + sender.getName() + ChatColor.DARK_AQUA + " has set a bounty of " + ChatColor.AQUA + "$" + price + ChatColor.DARK_AQUA + " on " + ChatColor.AQUA + players[i].getName());
										saveYamls();
										return;
									}
									else
									{
										sender.sendMessage(ChatColor.RED + "You cannot afford that bounty!");
										return;
									}
								} 	catch (UserDoesNotExistException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (NoLoanPermittedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							else
							{
								sender.sendMessage(ChatColor.RED + "Minimum bounty is " + minbounty);
								return;
							}
						}
						else
						{
							sender.sendMessage(ChatColor.AQUA + players[i].getName() + ChatColor.DARK_AQUA + " already has a bounty on their head!!");
							return;
						}
					}
					}
					i++;
				}
				sender.sendMessage(ChatColor.DARK_AQUA + "The player you are trying to set a bounty on is not online!!");
			}
		}
	}
	public void onDisable()
	{
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info("[" + pdfFile.getName() + "] has been disabled.");
	}
	
	private void firstRun() throws Exception
	{
	    if(!ConfigFile.exists()){
	        ConfigFile.getParentFile().mkdirs();
	        copy(getResource("config.yml"), ConfigFile);
	    }
	    if(!PlayerDataFile.exists()){
	        PlayerDataFile.getParentFile().mkdirs();
	        copy(getResource("player.yml"), PlayerDataFile);
	    }
	}
	
	public static void saveYamls() {
	    try {
	        Config.save(ConfigFile);
	        PlayerData.save(PlayerDataFile);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	public void loadYamls() {
	    try {
	        Config.load(ConfigFile);
	        PlayerData.load(PlayerDataFile);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	private void copy(InputStream in, File file) {
	    try {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf))>0){
	            out.write(buf,0,len);
	        }
	        out.close();
	        in.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
