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
package com.rezzedup.discordsrv.staffchat.config;

import com.github.zafarkhaja.semver.Version;
import com.rezzedup.discordsrv.staffchat.StaffChatPlugin;
import com.rezzedup.discordsrv.staffchat.StaffChatProfile;
import com.rezzedup.util.constants.Aggregates;
import com.rezzedup.util.constants.annotations.AggregatedResult;
import community.leaf.configvalues.bukkit.DefaultYamlValue;
import community.leaf.configvalues.bukkit.YamlValue;
import community.leaf.configvalues.bukkit.data.Load;
import community.leaf.configvalues.bukkit.data.YamlDataFile;
import community.leaf.configvalues.bukkit.migrations.Migration;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class StaffChatConfig extends YamlDataFile {
	public static final YamlValue<Version> VERSION =
		YamlValue.of("meta.config-version", Configs.VERSION).maybe();
	
	public static final DefaultYamlValue<Boolean> METRICS_ENABLED =
		YamlValue.ofBoolean("plugin.metrics")
			.migrates(Migration.move("metrics"))
			.defaults(true);
	
	public static final DefaultYamlValue<Boolean> UPDATE_CHECKER_ENABLED =
		YamlValue.ofBoolean("plugin.updates.check-for-updates").defaults(true);
	
	public static final DefaultYamlValue<Boolean> NOTIFY_IF_UPDATE_AVAILABLE =
		YamlValue.ofBoolean("plugin.updates.notify-in-game").defaults(true);
	
	public static final DefaultYamlValue<Boolean> PERSIST_TOGGLES =
		YamlValue.ofBoolean("staff-chat.toggles.chat-toggles-persist-after-restart").defaults(true);
	
	public static final DefaultYamlValue<Boolean> LEAVING_STAFFCHAT_ENABLED =
		YamlValue.ofBoolean("staff-chat.toggles.let-staff-members-leave-staffchat").defaults(true);
	
	public static final DefaultYamlValue<Boolean> LEAVING_TEAMCHAT_ENABLED =
		YamlValue.ofBoolean("team-chat.toggles.let-team-members-leave-teamchat").defaults(true);
	
	public static final DefaultYamlValue<Boolean> NOTIFY_IF_TOGGLE_ENABLED =
		YamlValue.ofBoolean("staff-chat.toggles.notify-toggle-status-on-join")
			.migrates(Migration.move("notify-staff-chat-enabled-on-join"))
			.defaults(true);
	
	public static final DefaultYamlValue<Boolean> NOTIFY_IF_TEAM_TOGGLE_ENABLED =
		YamlValue.ofBoolean("team-chat.toggles.notify-toggle-status-on-join").defaults(true);
	
	public static final DefaultYamlValue<Boolean> PREFIXED_CHAT_ENABLED =
		YamlValue.ofBoolean("staff-chat.prefixed.enable-prefixed-chat-messages")
			.migrates(Migration.move("enable-prefixed-chat-messages"))
			.defaults(false);
	
	public static final DefaultYamlValue<String> PREFIXED_CHAT_IDENTIFIER =
		YamlValue.ofString("staff-chat.prefixed.prefixed-chat-identifier")
			.migrates(Migration.move("prefixed-chat-identifier"))
			.defaults("@");
	
	public static final DefaultYamlValue<Boolean> PREFIXED_TEAM_CHAT_ENABLED =
		YamlValue.ofBoolean("team-chat.prefixed.enable-prefixed-chat-messages").defaults(false);
	
	public static final DefaultYamlValue<String> PREFIXED_TEAM_CHAT_IDENTIFIER =
		YamlValue.ofString("team-chat.prefixed.prefixed-chat-identifier").defaults("#");
	
	// Message Sound
	
	public static final DefaultYamlValue<Boolean> MESSAGE_SOUND_ENABLED =
		YamlValue.ofBoolean("sounds.messages.enabled").defaults(true);
	
	public static final DefaultYamlValue<Sound> MESSAGE_SOUND_NAME =
		YamlValue.ofSound("sounds.messages.name").defaults(Sound.ENTITY_ITEM_PICKUP);
	
	public static final DefaultYamlValue<Float> MESSAGE_SOUND_VOLUME =
		YamlValue.ofFloat("sounds.messages.volume").defaults(1.0F);
	
	public static final DefaultYamlValue<Float> MESSAGE_SOUND_PITCH =
		YamlValue.ofFloat("sounds.messages.pitch").defaults(0.5F);
	
	// Notification Sound
	
	public static final DefaultYamlValue<Boolean> NOTIFICATION_SOUND_ENABLED =
		YamlValue.ofBoolean("sounds.notifications.enabled").defaults(true);
	
	public static final DefaultYamlValue<Sound> NOTIFICATION_SOUND_NAME =
		YamlValue.ofSound("sounds.notifications.name").defaults(Sound.ENTITY_ITEM_PICKUP);
	
	public static final DefaultYamlValue<Float> NOTIFICATION_SOUND_VOLUME =
		YamlValue.ofFloat("sounds.notifications.volume").defaults(1.0F);
	
	public static final DefaultYamlValue<Float> NOTIFICATION_SOUND_PITCH =
		YamlValue.ofFloat("sounds.notifications.pitch").defaults(0.75F);
	
	// Team Chat Sound
	
	public static final DefaultYamlValue<Boolean> TEAM_MESSAGE_SOUND_ENABLED =
		YamlValue.ofBoolean("sounds.team-messages.enabled").defaults(true);
	
	public static final DefaultYamlValue<Sound> TEAM_MESSAGE_SOUND_NAME =
		YamlValue.ofSound("sounds.team-messages.name").defaults(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
	
	public static final DefaultYamlValue<Float> TEAM_MESSAGE_SOUND_VOLUME =
		YamlValue.ofFloat("sounds.team-messages.volume").defaults(1.0F);
	
	public static final DefaultYamlValue<Float> TEAM_MESSAGE_SOUND_PITCH =
		YamlValue.ofFloat("sounds.team-messages.pitch").defaults(0.6F);
	
	// Team Notification Sound
	
	public static final DefaultYamlValue<Boolean> TEAM_NOTIFICATION_SOUND_ENABLED =
		YamlValue.ofBoolean("sounds.team-notifications.enabled").defaults(true);
	
	public static final DefaultYamlValue<Sound> TEAM_NOTIFICATION_SOUND_NAME =
		YamlValue.ofSound("sounds.team-notifications.name").defaults(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
	
	public static final DefaultYamlValue<Float> TEAM_NOTIFICATION_SOUND_VOLUME =
		YamlValue.ofFloat("sounds.team-notifications.volume").defaults(1.0F);
	
	public static final DefaultYamlValue<Float> TEAM_NOTIFICATION_SOUND_PITCH =
		YamlValue.ofFloat("sounds.team-notifications.pitch").defaults(0.8F);
	
	@AggregatedResult
	public static final List<YamlValue<?>> VALUES =
		Aggregates.fromThisClass().constantsOfType(YamlValue.type()).toList();
	
	private final StaffChatPlugin plugin;
	
	public StaffChatConfig(StaffChatPlugin plugin) {
		super(plugin.directory(), "staff-chat.config.yml", Load.LATER);
		this.plugin = plugin;
		
		reloadsWith(() ->
		{
			if (isInvalid()) {
				Configs.couldNotLoad(plugin.getLogger(), getFilePath());
				plugin.debug(getClass()).log("Reload", () -> "Couldn't load: " + getInvalidReason());
				return;
			}
			
			Version existing = get(VERSION).orElse(Configs.NO_VERSION);
			boolean isOutdated = existing.isLowerThan(plugin.version());
			
			if (isOutdated) {
				plugin.debug(getClass()).log("Reload", () -> "Updating outdated config: " + existing);
				set(VERSION, plugin.version());
			}
			
			headerFromResource("staff-chat.config.header.txt");
			defaultValues(VALUES);
			
			if (isUpdated()) {
				plugin.debug(getClass()).log("Reload", () -> "Saving updated config and backing up old config: v" + existing);
				backupThenSave(plugin.backups(), "v" + existing);
			}
		});
	}
	
	private void playSound(
		Player player,
		DefaultYamlValue<Boolean> enabled,
		DefaultYamlValue<Sound> sound,
		DefaultYamlValue<Float> volume,
		DefaultYamlValue<Float> pitch
	) {
		if (!getOrDefault(enabled)) {
			return;
		}
		
		boolean sounds = plugin.data().getProfile(player)
			.map(StaffChatProfile::receivesStaffChatSounds)
			.orElse(true);
		
		if (sounds) {
			player.playSound(
				player.getLocation().add(0, 0.5, 0),
				getOrDefault(sound),
				getOrDefault(volume),
				getOrDefault(pitch)
			);
		}
	}
	
	private void playTeamSound(
		Player player,
		DefaultYamlValue<Boolean> enabled,
		DefaultYamlValue<Sound> sound,
		DefaultYamlValue<Float> volume,
		DefaultYamlValue<Float> pitch
	) {
		if (!getOrDefault(enabled)) {
			return;
		}
		
		boolean sounds = plugin.data().getProfile(player)
			.map(StaffChatProfile::receivesTeamChatSounds)
			.orElse(true);
		
		if (sounds) {
			player.playSound(
				player.getLocation().add(0, 0.5, 0),
				getOrDefault(sound),
				getOrDefault(volume),
				getOrDefault(pitch)
			);
		}
	}
	
	public void playMessageSound(Player player) {
		playSound(player, MESSAGE_SOUND_ENABLED, MESSAGE_SOUND_NAME, MESSAGE_SOUND_VOLUME, MESSAGE_SOUND_PITCH);
	}
	
	public void playNotificationSound(Player player) {
		playSound(player, NOTIFICATION_SOUND_ENABLED, NOTIFICATION_SOUND_NAME, NOTIFICATION_SOUND_VOLUME, NOTIFICATION_SOUND_PITCH);
	}
	
	public void playTeamMessageSound(Player player) {
		playTeamSound(player, TEAM_MESSAGE_SOUND_ENABLED, TEAM_MESSAGE_SOUND_NAME, TEAM_MESSAGE_SOUND_VOLUME, TEAM_MESSAGE_SOUND_PITCH);
	}
	
	public void playTeamNotificationSound(Player player) {
		playTeamSound(player, TEAM_NOTIFICATION_SOUND_ENABLED, TEAM_NOTIFICATION_SOUND_NAME, TEAM_NOTIFICATION_SOUND_VOLUME, TEAM_NOTIFICATION_SOUND_PITCH);
	}
}
