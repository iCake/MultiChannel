package us.kpvpdev.multichannel.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Arrays;

import us.kpvpdev.multichannel.MultiChannel;
import us.kpvpdev.multichannel.objects.ChatChannel;

public class Channel implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String tag, String[] args) {
		if(sender instanceof Player) {
			if(args.length == 0) {
				sender.sendMessage("§cMultiChannel §f-§c Created by CakePvP");
				sender.sendMessage("§cUsage:§f /" + tag + " <create, join, leave, info, setpass, setowner, delete>");
			} else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("leave")) {
					if(sender.hasPermission("multichannel.leave")) {
						if(MultiChannel.playerChannels.containsKey(sender.getName())) {
							ChatChannel channel = MultiChannel.playerChannels.get(sender.getName());

							if(channel.getOwner().equals(sender.getName()) && channel.getMembers().size() -1 >= 1) {
								sender.sendMessage("§cYou must assign somebody else as the owner of the group");
								sender.sendMessage("§cUsage:§f /" + tag + " setowner <member>");
							} else {
								if(channel.getMembers().size() - 1 <= 0) {
									sender.sendMessage("§cYou were the last remaining member of§f " + channel.getName() + " §cchannel");
									sender.sendMessage("§cIt has been removed because it is empty");
									channel.remove();
								} else {
									channel.broadcast(sender.getName() + " has left the channel");
									channel.getMembers().remove(sender.getName());
									channel.saveData();
								}
							}

							MultiChannel.playerChannels.remove(sender.getName());
						} else {
							sender.sendMessage("§cYou're currently not in a channel!");
						}
					} else {
						sender.sendMessage("§cYou do not have permission to use this!");
					}
				} else if(args[0].equalsIgnoreCase("info")) {
					if(sender.hasPermission("multichannel.info")) {
						if(MultiChannel.playerChannels.containsKey(sender.getName())) {
							ChatChannel channel = MultiChannel.playerChannels.get(sender.getName());

							sender.sendMessage("§cMultiChannel §f-§c Created by CakePvP");
							sender.sendMessage("§cChannel name:§f " + channel.getName());
							sender.sendMessage("§cChannel owner:§f " + channel.getOwner());

							if(!channel.getPassword().equalsIgnoreCase("")) {
								sender.sendMessage("§cChannel password:§f " + channel.getPassword());
							}

							sender.sendMessage("§cChannel members:§f " + channel.getMembers());
						} else {
							sender.sendMessage("§cYou're currently not in a channel!");
						}
					} else {
						sender.sendMessage("§cYou do not have permission to use this!");
					}
				} else if(args[0].equalsIgnoreCase("delete")) {
					if(sender.hasPermission("multichannel.delete")) {
						if(MultiChannel.playerChannels.containsKey(sender.getName())) {
							ChatChannel channel = MultiChannel.playerChannels.get(sender.getName());

							if(channel.getOwner().equals(sender.getName())) {
								channel.broadcast("This channel has been deleted by the owner");

								for(String str : channel.getMembers()) {
									if(MultiChannel.chatting.contains(str)) {
										MultiChannel.chatting.remove(str);
									}

									if(MultiChannel.playerChannels.containsKey(str)) {
										MultiChannel.playerChannels.remove(str);
									}
								}

								channel.remove();
							} else {
								sender.sendMessage("§cYou must be channel owner to use this!");
							}
						} else {
							sender.sendMessage("§cYou're currently not in a channel!");
						}
					} else {
						sender.sendMessage("§cYou do not have permission to use this!");
					}
				} else {
					sender.sendMessage("§cSubcommand not recognised");
					sender.sendMessage("§cUsage:§f /" + tag + " <create, join, leave, info, setpass, setowner, delete>");
				}
			} else if(args.length >= 2) {
				if(args[0].equalsIgnoreCase("create")) {
					if(sender.hasPermission("multichannel.create")) {
						if(!MultiChannel.playerChannels.containsKey(sender.getName())) {
							ChatChannel channel = new ChatChannel(args[1], sender.getName(), args.length >=3 ? args[2] : "", Arrays.asList(sender.getName()));
							channel.saveData();

							MultiChannel.channels.put(channel.getName(), channel);
							MultiChannel.playerChannels.put(sender.getName(), channel);

							sender.sendMessage("§cYou created the §f" + channel.getName() + " §cchannel");
							sender.sendMessage("§cTo talk in this channel, use §f/chat");
						} else {
							sender.sendMessage("§cYou're already in the channel §f" + MultiChannel.playerChannels.get(sender.getName()).getName());
						}
					} else {
						sender.sendMessage("§cYou do not have permission to use this!");
					}
				} else if(args[0].equalsIgnoreCase("join")) {
					if(sender.hasPermission("multichannel.join")) {
						if(!MultiChannel.playerChannels.containsKey(sender.getName())) {
							if(MultiChannel.channels.containsKey(args[1])) {
								ChatChannel channel = MultiChannel.channels.get(args[1]);

								if(!channel.getPassword().equalsIgnoreCase("")) {
									if(args.length >= 3) {
										String password = args[2];

										if(channel.getPassword().equals(password) || (sender.hasPermission("multichannel.bypass"))) {
											channel.getMembers().add(sender.getName());
											channel.broadcast(sender.getName() + " has joined the channel");
											channel.saveData();

											MultiChannel.playerChannels.put(sender.getName(), channel);
										} else {
											sender.sendMessage("§cIncorrect password");
										}
									} else {
										sender.sendMessage("§cThis channel requires a §fpassword§c.");
									}
								} else {
									channel.getMembers().add(sender.getName());
									channel.broadcast(sender.getName() + " has joined the channel");
									channel.saveData();
									
									MultiChannel.playerChannels.put(sender.getName(), channel);
								}
							} else {
								sender.sendMessage("§cCouldn't find the§f " + args[1] + " §cchannel!");
							}
						} else {
							sender.sendMessage("§cYou're already in the channel " + MultiChannel.playerChannels.get(sender.getName()).getName());
						}
					} else {
						sender.sendMessage("§cYou do not have permission to use this!");
					}
				} else if(args[0].equalsIgnoreCase("info")) {
					if(sender.hasPermission("multichannel.info.other")) {
						Player target = Bukkit.getPlayer(args[1]);

						if(target != null) {
							if(MultiChannel.playerChannels.containsKey(target.getName())) {
								ChatChannel channel = MultiChannel.playerChannels.get(target.getName());

								sender.sendMessage("§cMultiChannel §f-§c Created by CakePvP");
								sender.sendMessage("§cChannel name:§f " + channel.getName());
								sender.sendMessage("§cChannel owner:§f " + channel.getOwner());

								if(channel.getMembers().contains(sender.getName()) && !channel.getPassword().equalsIgnoreCase("")) {
									sender.sendMessage("§cChannel password:§f " + channel.getPassword());
								}

								sender.sendMessage("§cChannel members:§f " + channel.getMembers());
							} else {
								sender.sendMessage("§c" + target.getName() + " isn't in a channel!");
							}
						} else {
							sender.sendMessage("§cPlayer not found!");
						}
					} else {
						sender.sendMessage("§cYou do not have permission to use this!");
					}
				} else if(args[0].equalsIgnoreCase("setpass")) {
					if(sender.hasPermission("multichannel.setpass")) {
						if(MultiChannel.playerChannels.containsKey(sender.getName())) {
							ChatChannel channel = MultiChannel.playerChannels.get(sender.getName());

							if(channel.getOwner().equals(sender.getName())) {
								channel.setPassword(args[1]);
								channel.broadcast("The channel password has been updated");
								channel.saveData();
							} else {
								sender.sendMessage("§cYou must be channel owner to use this!");
							}
						} else {
							sender.sendMessage("§cYou're currently not in a channel!");
						}
					} else {
						sender.sendMessage("§cYou do not have permission to use this!");
					}
				} else if(args[0].equalsIgnoreCase("setowner")) {
					if(sender.hasPermission("multichannel.setowner")) {
						if(MultiChannel.playerChannels.containsKey(sender.getName())) {
							ChatChannel channel = MultiChannel.playerChannels.get(sender.getName());

							if(channel.getOwner().equals(sender.getName())) {
								if(channel.getMembers().contains(args[1])) {
									channel.setOwner(args[1]);
									channel.broadcast("The channel owner is now " + channel.getOwner());
									channel.saveData();
								} else {
									sender.sendMessage("§cThat member isn't in your channel!");
								}
							} else {
								sender.sendMessage("§cYou must be channel owner to use this!");
							}
						} else {
							sender.sendMessage("§cYou're currently not in a channel!");
						}
					} else {
						sender.sendMessage("§cYou do not have permission to use this!");
					}
				} else if(args[0].equalsIgnoreCase("delete")) {
					if(sender.hasPermission("multichannel.delete.other")) {
						if(MultiChannel.channels.containsKey(args[1])) {
							ChatChannel channel = MultiChannel.channels.get(args[1]);
							channel.broadcast("This channel has been deleted by an admin");

							for(String str : channel.getMembers()) {
								if(MultiChannel.chatting.contains(str)) {
									MultiChannel.chatting.remove(str);
								}

								if(MultiChannel.playerChannels.containsKey(str)) {
									MultiChannel.playerChannels.remove(str);
								}
							}

							channel.remove();
							sender.sendMessage("§cYou have deleted the §f" + channel.getName() + " §cchannel");
						} else {
							sender.sendMessage("§cChannel not recognised");
						}
					} else {
						sender.sendMessage("§cYou do not have permission to use this!");
					}
				} else {
					sender.sendMessage("§cSubcommand not recognised");
					sender.sendMessage("§cUsage:§f /" + tag + " <create, join, leave, info, setpass, setowner, delete>");
				}
			}

			return true;
		}

		return false;
	}

}