/*
 * The MIT License
 * Copyright © 2017-2024 RezzedUp and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.rezzedup.discordsrv.staffchat;

import com.rezzedup.discordsrv.staffchat.config.MessagesConfig;
import com.rezzedup.discordsrv.staffchat.events.ConsoleStaffChatMessageEvent;
import com.rezzedup.discordsrv.staffchat.events.ConsoleTeamChatMessageEvent;
import com.rezzedup.discordsrv.staffchat.events.DiscordStaffChatMessageEvent;
import com.rezzedup.discordsrv.staffchat.events.DiscordTeamChatMessageEvent;
import com.rezzedup.discordsrv.staffchat.events.PlayerStaffChatMessageEvent;
import com.rezzedup.discordsrv.staffchat.events.PlayerTeamChatMessageEvent;
import com.rezzedup.discordsrv.staffchat.util.MappedPlaceholder;
import com.rezzedup.discordsrv.staffchat.util.Strings;
import community.leaf.configvalues.bukkit.DefaultYamlValue;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.emoji.EmojiParser;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Message;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Role;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import github.scarsz.discordsrv.util.DiscordUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import pl.tlinkowski.annotation.basic.NullOr;

import java.awt.*;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

public class MessageProcessor {
	private final StaffChatPlugin plugin;
	
	MessageProcessor(StaffChatPlugin plugin) {
		this.plugin = plugin;
	}
	
	private boolean hasPlaceholderAPI() {
		return plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
	}
	
	private String parsePlaceholders(Player player, String text) {
		return (hasPlaceholderAPI())
			? PlaceholderAPI.setPlaceholders(player, text)
			: text;
	}
	
	// Process Staff Chat
	
	private void sendFormattedChatMessage(@NullOr Object author, DefaultYamlValue<String> format, MappedPlaceholder placeholders) {
		// If the value of %message% doesn't exist for some reason, don't announce.
		if (Strings.isEmptyOrNull(placeholders.get("message"))) {
			return;
		}
		
		String formatted = plugin.messages().getOrDefault(format);
		
		if (plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			// Update format's PAPI placeholders before inserting the message
			// (which *could* contain arbitrary placeholders itself, ah placeholder injection).
			@NullOr Player player = (author instanceof Player) ? (Player) author : null;
			formatted = PlaceholderAPI.setPlaceholders(player, formatted);
		}
		
		String content = Strings.colorful(placeholders.update(formatted));
		
		if (author instanceof Player) {
			Player player = (Player) author;
			StaffChatProfile profile = plugin.data().getOrCreateProfile(player);
			
			// Author left the staff chat but is sending a message there...
			if (!profile.receivesStaffChatMessages()) {
				String reminder = Strings.colorful(placeholders.update(
					plugin.messages().getOrDefault(MessagesConfig.LEFT_CHAT_NOTIFICATION_REMINDER))
				);
				
				player.sendMessage(content);
				player.sendMessage(reminder);
				
				plugin.config().playNotificationSound(player);
			}
		}
		
		plugin.onlineStaffChatParticipants().forEach(staff -> {
			staff.sendMessage(content);
			plugin.config().playMessageSound(staff);
		});
		
		plugin.getServer().getConsoleSender().sendMessage(content);
	}
	
	private void sendToDiscord(String channel, Consumer<TextChannel> sender) {
		@NullOr TextChannel discordChannel = (channel.equals(StaffChatPlugin.TEAM_CHANNEL)) 
			? plugin.getTeamDiscordChannelOrNull() 
			: plugin.getDiscordChannelOrNull();
		
		if (discordChannel == null) {
			plugin.debug(getClass()).log(ChatService.MINECRAFT, "Message", () ->
				"Unable to send message to discord: " + channel + " => null"
			);
			return;
		}
		
		plugin.debug(getClass()).log(ChatService.MINECRAFT, "Message", () ->
			"Sending message to discord channel: " + channel + " => " + discordChannel
		);
		
		sender.accept(discordChannel);
	}
	
	// Staff Chat message processing
	
	public void processConsoleChat(String message) {
		Objects.requireNonNull(message, "message");
		
		plugin.debug(getClass()).logConsoleChatMessage(message);
		
		ConsoleStaffChatMessageEvent event =
			plugin.events().call(new ConsoleStaffChatMessageEvent(message));
		
		if (event.isCancelled() || event.getText().isEmpty()) {
			plugin.debug(getClass()).log(ChatService.MINECRAFT, event, () -> "Cancelled or text is empty");
			return;
		}
		
		MappedPlaceholder placeholders = plugin.messages().placeholders();
		placeholders.map("message", "content", "text").to(event::getText);
		
		sendFormattedChatMessage(null, MessagesConfig.IN_GAME_CONSOLE_FORMAT, placeholders);
		
		if (plugin.isDiscordSrvHookEnabled()) {
			String discordMessage = placeholders.update(
				plugin.messages().getOrDefault(MessagesConfig.DISCORD_CONSOLE_FORMAT)
			);
			
			sendToDiscord(StaffChatPlugin.CHANNEL, channel -> DiscordUtil.queueMessage(channel, discordMessage, true));
		} else {
			plugin.debug(getClass()).log(ChatService.MINECRAFT, "Message", () ->
				"DiscordSRV hook is not enabled, cannot send to discord"
			);
		}
	}
	
	public void processPlayerChat(Player author, String message) {
		Objects.requireNonNull(author, "author");
		Objects.requireNonNull(message, "message");
		
		plugin.debug(getClass()).logPlayerChatMessage(author, message);
		
		PlayerStaffChatMessageEvent event =
			plugin.events().call(new PlayerStaffChatMessageEvent(author, message));
		
		if (event.isCancelled() || event.getText().isEmpty()) {
			plugin.debug(getClass()).log(ChatService.MINECRAFT, event, () -> "Cancelled or text is empty");
			return;
		}
		
		MappedPlaceholder placeholders = plugin.messages().placeholders(author);
		placeholders.map("message", "content", "text").to(event::getText);
		
		sendFormattedChatMessage(author, MessagesConfig.IN_GAME_PLAYER_FORMAT, placeholders);
		
		if (plugin.isDiscordSrvHookEnabled()) {
			sendToDiscord(StaffChatPlugin.CHANNEL, channel -> {
				// Send to discord off the main thread (just like DiscordSRV does)
				plugin.async().run(() ->
					DiscordSRV.getPlugin().processChatMessage(author, message, StaffChatPlugin.CHANNEL, false)
				);
			});
		} else {
			plugin.debug(getClass()).log(ChatService.MINECRAFT, "Message", () ->
				"DiscordSRV hook is not enabled, cannot send to discord"
			);
		}
	}
	
	public void processDiscordChat(User author, Message message) {
		Objects.requireNonNull(author, "author");
		Objects.requireNonNull(message, "message");
		
		plugin.debug(getClass()).logDiscordChatMessage(author, message);
		
		DiscordStaffChatMessageEvent event =
			plugin.events().call(new DiscordStaffChatMessageEvent(author, message, message.getContentStripped()));
		
		if (event.isCancelled() || event.getText().isEmpty()) {
			plugin.debug(getClass()).log(ChatService.DISCORD, "Message", () -> "Cancelled or text is empty");
			return;
		}
		
		// Emoji Unicode -> Alias (library included with DiscordSRV)
		String text = EmojiParser.parseToAliases(event.getText());
		
		MappedPlaceholder placeholders = plugin.messages().placeholders();
		
		placeholders.map("message", "content", "text").to(() -> text);
		placeholders.map("user", "name", "username", "sender").to(author::getName);
		placeholders.map("discriminator", "discrim").to(author::getDiscriminator);
		
		@NullOr Member member = message.getMember();
		
		if (member != null) {
			placeholders.map("nickname", "displayname").to(member::getEffectiveName);
			
			// Simulate placeholders from DiscordSRV:
			// https://github.com/DiscordSRV/DiscordSRV/blob/1d08598206b1af5dcc29e411cead8e152e4c3f94/src/main/java/github/scarsz/discordsrv/listeners/DiscordChatListener.java#L293
			
			DiscordSRV discordSrv = DiscordSRV.getPlugin();
			List<Role> selectedRoles = discordSrv.getSelectedRoles(member);
			@NullOr Role topRole = (selectedRoles.isEmpty()) ? null : selectedRoles.get(0);
			
			if (topRole != null) {
				placeholders.map("toprole").to(topRole::getName);
				placeholders.map("toproleinitial").to(() -> topRole.getName().substring(0, 1));
				
				placeholders.map("toprolealias").to(() ->
					discordSrv.getRoleAliases().getOrDefault(
						topRole.getId(),
						discordSrv.getRoleAliases().getOrDefault(
							topRole.getName().toLowerCase(Locale.ROOT),
							topRole.getName()
						)
					)
				);
				
				placeholders.map("toprolecolor").to(() -> ChatColor.of(new Color(topRole.getColorRaw())));
				placeholders.map("allroles").to(() -> DiscordUtil.getFormattedRoles(selectedRoles));
			}
		}
		
		sendFormattedChatMessage(author, MessagesConfig.IN_GAME_DISCORD_FORMAT, placeholders);
	}
	
	// Team Chat message processing
	
	private void sendFormattedTeamChatMessage(@NullOr Object author, DefaultYamlValue<String> format, MappedPlaceholder placeholders) {
		// If the value of %message% doesn't exist for some reason, don't announce.
		if (Strings.isEmptyOrNull(placeholders.get("message"))) {
			return;
		}
		
		String formatted = plugin.messages().getOrDefault(format);
		
		if (plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			// Update format's PAPI placeholders before inserting the message
			@NullOr Player player = (author instanceof Player) ? (Player) author : null;
			formatted = PlaceholderAPI.setPlaceholders(player, formatted);
		}
		
		String content = Strings.colorful(placeholders.update(formatted));
		
		if (author instanceof Player) {
			Player player = (Player) author;
			StaffChatProfile profile = plugin.data().getOrCreateProfile(player);
			
			// Author left the team chat but is sending a message there...
			if (!profile.receivesTeamChatMessages()) {
				String reminder = Strings.colorful(placeholders.update(
					plugin.messages().getOrDefault(MessagesConfig.LEFT_TEAM_CHAT_NOTIFICATION_REMINDER))
				);
				
				player.sendMessage(content);
				player.sendMessage(reminder);
				
				plugin.config().playTeamNotificationSound(player);
			}
		}
		
		plugin.onlineTeamChatParticipants().forEach(team -> {
			team.sendMessage(content);
			plugin.config().playTeamMessageSound(team);
		});
		
		plugin.getServer().getConsoleSender().sendMessage(content);
	}
	
	public void processConsoleTeamChat(String message) {
		Objects.requireNonNull(message, "message");
		
		plugin.debug(getClass()).logConsoleChatMessage(message);
		
		ConsoleTeamChatMessageEvent event =
			plugin.events().call(new ConsoleTeamChatMessageEvent(message));
		
		if (event.isCancelled() || event.getText().isEmpty()) {
			plugin.debug(getClass()).log(ChatService.MINECRAFT, event, () -> "Cancelled or text is empty");
			return;
		}
		
		MappedPlaceholder placeholders = plugin.messages().placeholders();
		placeholders.map("message", "content", "text").to(event::getText);
		
		sendFormattedTeamChatMessage(null, MessagesConfig.TEAM_IN_GAME_CONSOLE_FORMAT, placeholders);
		
		if (plugin.isDiscordSrvHookEnabled()) {
			String discordMessage = placeholders.update(
				plugin.messages().getOrDefault(MessagesConfig.TEAM_DISCORD_CONSOLE_FORMAT)
			);
			
			sendToDiscord(StaffChatPlugin.TEAM_CHANNEL, channel -> DiscordUtil.queueMessage(channel, discordMessage, true));
		} else {
			plugin.debug(getClass()).log(ChatService.MINECRAFT, "Message", () ->
				"DiscordSRV hook is not enabled, cannot send to discord"
			);
		}
	}
	
	public void processPlayerTeamChat(Player author, String message) {
		Objects.requireNonNull(author, "author");
		Objects.requireNonNull(message, "message");
		
		plugin.debug(getClass()).logPlayerChatMessage(author, message);
		
		PlayerTeamChatMessageEvent event =
			plugin.events().call(new PlayerTeamChatMessageEvent(author, message));
		
		if (event.isCancelled() || event.getText().isEmpty()) {
			plugin.debug(getClass()).log(ChatService.MINECRAFT, event, () -> "Cancelled or text is empty");
			return;
		}
		
		MappedPlaceholder placeholders = plugin.messages().placeholders(author);
		placeholders.map("message", "content", "text").to(event::getText);
		
		sendFormattedTeamChatMessage(author, MessagesConfig.TEAM_IN_GAME_PLAYER_FORMAT, placeholders);
		
		if (plugin.isDiscordSrvHookEnabled()) {
			sendToDiscord(StaffChatPlugin.TEAM_CHANNEL, channel -> {
				// Send to discord off the main thread (just like DiscordSRV does)
				plugin.async().run(() ->
					DiscordSRV.getPlugin().processChatMessage(author, message, StaffChatPlugin.TEAM_CHANNEL, false)
				);
			});
		} else {
			plugin.debug(getClass()).log(ChatService.MINECRAFT, "Message", () ->
				"DiscordSRV hook is not enabled, cannot send to discord"
			);
		}
	}
	
	public void processDiscordTeamChat(User author, Message message) {
		Objects.requireNonNull(author, "author");
		Objects.requireNonNull(message, "message");
		
		plugin.debug(getClass()).logDiscordChatMessage(author, message);
		
		DiscordTeamChatMessageEvent event =
			plugin.events().call(new DiscordTeamChatMessageEvent(author, message, message.getContentStripped()));
		
		if (event.isCancelled() || event.getText().isEmpty()) {
			plugin.debug(getClass()).log(ChatService.DISCORD, "Message", () -> "Cancelled or text is empty");
			return;
		}
		
		// Emoji Unicode -> Alias (library included with DiscordSRV)
		String text = EmojiParser.parseToAliases(event.getText());
		
		MappedPlaceholder placeholders = plugin.messages().placeholders();
		
		placeholders.map("message", "content", "text").to(() -> text);
		placeholders.map("user", "name", "username", "sender").to(author::getName);
		placeholders.map("discriminator", "discrim").to(author::getDiscriminator);
		
		@NullOr Member member = message.getMember();
		
		if (member != null) {
			placeholders.map("nickname", "displayname").to(member::getEffectiveName);
			
			// Simulate placeholders from DiscordSRV
			DiscordSRV discordSrv = DiscordSRV.getPlugin();
			List<Role> selectedRoles = discordSrv.getSelectedRoles(member);
			@NullOr Role topRole = (selectedRoles.isEmpty()) ? null : selectedRoles.get(0);
			
			if (topRole != null) {
				placeholders.map("toprole").to(topRole::getName);
				placeholders.map("toproleinitial").to(() -> topRole.getName().substring(0, 1));
				
				placeholders.map("toprolealias").to(() ->
					discordSrv.getRoleAliases().getOrDefault(
						topRole.getId(),
						discordSrv.getRoleAliases().getOrDefault(
							topRole.getName().toLowerCase(Locale.ROOT),
							topRole.getName()
						)
					)
				);
				
				placeholders.map("toprolecolor").to(() -> ChatColor.of(new Color(topRole.getColorRaw())));
				placeholders.map("allroles").to(() -> DiscordUtil.getFormattedRoles(selectedRoles));
			}
		}
		
		sendFormattedTeamChatMessage(author, MessagesConfig.TEAM_IN_GAME_DISCORD_FORMAT, placeholders);
	}
}
