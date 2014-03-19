package us.kpvpdev.multichannel.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import us.kpvpdev.multichannel.MultiChannel;
import us.kpvpdev.multichannel.config.*;
import us.kpvpdev.multichannel.objects.ChatChannel;

import java.util.Arrays;

public class Channel implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String tag, String[] args) {
		if(sender instanceof Player) {
			if(args.length == 0) {
				sender.sendMessage(Lang.colorize("&cMultiChannel &f-&c Created by CakePvP"));
				sender.sendMessage(Lang.COMMAND_USAGE.replace("{command}", tag));

				if(sender.hasPermission("multichannel.admin")) {
					sender.sendMessage(Lang.ADMIN_COMMAND_USAGE.replace("{command}", tag));
				}
			} else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("leave")) {
					if(sender.hasPermission("multichannel.leave")) {
						if(MultiChannel.playerChannels.containsKey(sender.getName())) {
							ChatChannel channel = MultiChannel.playerChannels.get(sender.getName());

							if(channel.getOwner().equals(sender.getName()) && channel.getMembers().size() -1 >= 1) {
								sender.sendMessage(Lang.LEAVE_CHANNEL_OWNER);
								sender.sendMessage(Lang.LEAVE_COMMAND_USAGE.replace("{command}", tag));
							} else {
								if(channel.getMembers().size() - 1 <= 0) {
									sender.sendMessage(Lang.CHANNEL_LEAVE_LAST_MEMBER.replace("{channel}", channel.getName()));
									sender.sendMessage(Lang.CHANNEL_LEAVE_EMPTY);
									channel.remove();
								} else {
									channel.broadcast(Lang.CHANNEL_LEAVE);
									channel.getMembers().remove(sender.getName());
									channel.saveData();
								}
							}

							MultiChannel.playerChannels.remove(sender.getName());
						} else {
							sender.sendMessage(Lang.NOT_IN_CHANNEL);
						}
					} else {
						sender.sendMessage(Lang.NO_PERMS);
					}
				} else if(args[0].equalsIgnoreCase("info")) {
					if(sender.hasPermission("multichannel.info")) {
						if(MultiChannel.playerChannels.containsKey(sender.getName())) {
							ChatChannel channel = MultiChannel.playerChannels.get(sender.getName());

							sender.sendMessage("§cMultiChannel §f-§c Created by CakePvP");
							sender.sendMessage(Lang.CHANNEL_NAME.replace("{name}", channel.getName()));
							sender.sendMessage(Lang.CHANNEL_OWNER.replace("{owner}", channel.getOwner()));

							if(!channel.getPassword().equalsIgnoreCase("")) {
								sender.sendMessage(Lang.CHANNEL_PASSWORD.replace("{password}", channel.getPassword()));
							}

							sender.sendMessage(Lang.CHANNEL_MEMBERS.replace("{members}", String.valueOf(channel.getMembers())));
						} else {
							sender.sendMessage(Lang.OTHER_NOT_IN_CHANNEL);
						}
					} else {
						sender.sendMessage(Lang.NO_PERMS);
					}
				} else if(args[0].equalsIgnoreCase("delete")) {
					if(sender.hasPermission("multichannel.delete")) {
						if(MultiChannel.playerChannels.containsKey(sender.getName())) {
							ChatChannel channel = MultiChannel.playerChannels.get(sender.getName());

							if(channel.getOwner().equals(sender.getName())) {
								channel.broadcast(Lang.CHANNEL_DELETED_OWNER);

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
								sender.sendMessage(Lang.CHANNEL_NOT_OWNER);
							}
						} else {
							sender.sendMessage(Lang.OTHER_NOT_IN_CHANNEL);
						}
					} else {
						sender.sendMessage(Lang.NO_PERMS);
					}
				} else if(args[0].equalsIgnoreCase("spy")) {
					if(sender.hasPermission("multichannel.admin")) {
						if(MultiChannel.spying.contains(sender.getName())) {
							MultiChannel.spying.remove(sender.getName());
						} else {
							MultiChannel.spying.add(sender.getName());
						}

						sender.sendMessage(MultiChannel.spying.contains(sender.getName()) ? Lang.SPYING_ENABLED : Lang.SPYING_DISABLED);
					} else {
						sender.sendMessage(Lang.NO_PERMS);
					}
				} else if(args[0].equalsIgnoreCase("reload")) {
					if(sender.hasPermission("multichannel.admin")) {
						MultiChannel.getInstance().saveResource("settings.yml", false);
						MultiChannel.getInstance().saveResource("lang.yml", false);

						Config.reloadConfig();
						Lang.reloadConfig();
						Settings.loadConfig();

						sender.sendMessage(Lang.RELOADED_CONFIGS);
					} else {
						sender.sendMessage(Lang.NO_PERMS);
					}
				} else {
					sender.sendMessage(Lang.SUBCOMMAND_NOT_RECOGINSED);
					sender.sendMessage(Lang.COMMAND_USAGE.replace("{command}", tag));

					if(sender.hasPermission("multichannel.admin")) {
						sender.sendMessage(Lang.ADMIN_COMMAND_USAGE.replace("{command}", tag));
					}
				}
			} else if(args.length >= 2) {
				if(args[0].equalsIgnoreCase("create")) {
					if(sender.hasPermission("multichannel.create")) {
						if(!MultiChannel.playerChannels.containsKey(sender.getName())) {
							ChatChannel channel = new ChatChannel(args[1], sender.getName(), args.length >=3 ? args[2] : "", Arrays.asList(sender.getName()));
							channel.saveData();

							MultiChannel.channels.put(channel.getName(), channel);
							MultiChannel.playerChannels.put(sender.getName(), channel);

							sender.sendMessage(Lang.CHANNEL_CREATED.replace("{channel}", channel.getName()));
							sender.sendMessage(Lang.CHANNEL_CREATED_TALK);
						} else {
							sender.sendMessage(Lang.ALREADY_IN_CHANNEL);
						}
					} else {
						sender.sendMessage(Lang.NO_PERMS);
					}
				} else if(args[0].equalsIgnoreCase("join")) {
					if(sender.hasPermission("multichannel.join")) {
						if(!MultiChannel.playerChannels.containsKey(sender.getName())) {
							if(MultiChannel.channels.containsKey(args[1])) {
								ChatChannel channel = MultiChannel.channels.get(args[1]);

								if(!channel.getPassword().equalsIgnoreCase("")) {
									if(args.length >= 3) {
										String password = args[2];

										if(channel.getPassword().equals(password) || (sender.hasPermission("multichannel.bypass") && Settings.ADMINS_BYPASS_JOIN)) {
											if(Settings.PERMS_PER_CHANNEL) {
												if(sender.hasPermission("multichannel.join." + channel.getName().toLowerCase())) {
													channel.getMembers().add(sender.getName());
													channel.broadcast(Lang.CHANNEL_JOIN.replace("{player}", sender.getName()));
													channel.saveData();

													MultiChannel.playerChannels.put(sender.getName(), channel);
												} else {
													sender.sendMessage(Lang.NO_PERMS_CHANNEL.replace("{channel}", channel.getName().toLowerCase()));
												}
											} else {
												channel.getMembers().add(sender.getName());
												channel.broadcast(Lang.CHANNEL_JOIN.replace("{player}", sender.getName()));
												channel.saveData();

												MultiChannel.playerChannels.put(sender.getName(), channel);
											}
										} else {
											sender.sendMessage(Lang.CHANNEL_INCORRECT_PASSWORD);
										}
									} else {
										sender.sendMessage(Lang.CHANNEL_REQUIRES_PASSWORD);
									}
								} else {
									if(Settings.PERMS_PER_CHANNEL) {
										if(sender.hasPermission("multichannel.join." + channel.getName().toLowerCase())) {
											channel.getMembers().add(sender.getName());
											channel.broadcast(Lang.CHANNEL_JOIN.replace("{player}", sender.getName()));
											channel.saveData();

											MultiChannel.playerChannels.put(sender.getName(), channel);
										} else {
											sender.sendMessage(Lang.NO_PERMS_CHANNEL.replace("{channel}", channel.getName().toLowerCase()));
										}
									} else {
										channel.getMembers().add(sender.getName());
										channel.broadcast(Lang.CHANNEL_JOIN.replace("{player}", sender.getName()));
										channel.saveData();

										MultiChannel.playerChannels.put(sender.getName(), channel);
									}
								}
							} else {
								sender.sendMessage(Lang.CHANNEL_NOT_FOUND.replace("{channel}", args[1]));
							}
						} else {
							sender.sendMessage(Lang.ALREADY_IN_CHANNEL);
						}
					} else {
						sender.sendMessage(Lang.NO_PERMS);
					}
				} else if(args[0].equalsIgnoreCase("info")) {
					if(sender.hasPermission("multichannel.info.other")) {
						Player target = Bukkit.getPlayer(args[1]);

						if(target != null) {
							if(MultiChannel.playerChannels.containsKey(target.getName())) {
								ChatChannel channel = MultiChannel.playerChannels.get(target.getName());

								sender.sendMessage("§cMultiChannel §f-§c Created by CakePvP");
								sender.sendMessage(Lang.CHANNEL_NAME.replace("{name}", channel.getName()));
								sender.sendMessage(Lang.CHANNEL_OWNER.replace("{owner}", channel.getOwner()));

								if(!channel.getPassword().equalsIgnoreCase("")) {
									sender.sendMessage(Lang.CHANNEL_PASSWORD.replace("{password}", channel.getPassword()));
								}

								sender.sendMessage(Lang.CHANNEL_MEMBERS.replace("{members}", String.valueOf(channel.getMembers())));
							} else {
								sender.sendMessage(Lang.NOT_IN_CHANNEL.replace("{player}", target.getName()));
							}
						} else {
							sender.sendMessage(Lang.PLAYER_NOT_FOUND);
						}
					} else {
						sender.sendMessage(Lang.NO_PERMS);
					}
				} else if(args[0].equalsIgnoreCase("setpass")) {
					if(sender.hasPermission("multichannel.setpass")) {
						if(MultiChannel.playerChannels.containsKey(sender.getName())) {
							ChatChannel channel = MultiChannel.playerChannels.get(sender.getName());

							if(channel.getOwner().equals(sender.getName())) {
								channel.setPassword(args[1]);
								channel.broadcast(Lang.CHANNEL_PASSWORD_UPDATED);
								channel.saveData();
							} else {
								sender.sendMessage(Lang.CHANNEL_NOT_OWNER);
							}
						} else {
							sender.sendMessage(Lang.OTHER_NOT_IN_CHANNEL);
						}
					} else {
						sender.sendMessage(Lang.NO_PERMS);
					}
				} else if(args[0].equalsIgnoreCase("setowner")) {
					if(sender.hasPermission("multichannel.setowner")) {
						if(MultiChannel.playerChannels.containsKey(sender.getName())) {
							ChatChannel channel = MultiChannel.playerChannels.get(sender.getName());

							if(channel.getOwner().equals(sender.getName())) {
								if(channel.getMembers().contains(args[1])) {
									channel.setOwner(args[1]);
									channel.broadcast(Lang.CHANNEL_NEW_OWNER.replace("{owner}", channel.getOwner()));
									channel.saveData();
								} else {
									sender.sendMessage(Lang.PLAYER_NOT_IN_CHANNEL);
								}
							} else {
								sender.sendMessage(Lang.CHANNEL_NOT_OWNER);
							}
						} else {
							sender.sendMessage(Lang.OTHER_NOT_IN_CHANNEL);
						}
					} else {
						sender.sendMessage(Lang.NO_PERMS);
					}
				} else if(args[0].equalsIgnoreCase("delete")) {
					if(sender.hasPermission("multichannel.delete.other")) {
						if(MultiChannel.channels.containsKey(args[1])) {
							ChatChannel channel = MultiChannel.channels.get(args[1]);
							channel.broadcast(Lang.CHANNEL_DELETED_ADMIN);

							for(String str : channel.getMembers()) {
								if(MultiChannel.chatting.contains(str)) {
									MultiChannel.chatting.remove(str);
								}

								if(MultiChannel.playerChannels.containsKey(str)) {
									MultiChannel.playerChannels.remove(str);
								}
							}

							channel.remove();
							sender.sendMessage(Lang.CHANNEL_DELETED.replace("{channel}", channel.getName()));
						} else {
							sender.sendMessage(Lang.CHANNEL_NOT_FOUND.replace("{channel}", args[1]));
						}
					} else {
						sender.sendMessage(Lang.NO_PERMS);
					}
				} else {
					sender.sendMessage(Lang.SUBCOMMAND_NOT_RECOGINSED);
					sender.sendMessage(Lang.COMMAND_USAGE.replace("{command}", tag));

					if(sender.hasPermission("multichannel.admin")) {
						sender.sendMessage(Lang.ADMIN_COMMAND_USAGE.replace("{command}", tag));
					}
				}
			}

			return true;
		}

		return false;
	}

}