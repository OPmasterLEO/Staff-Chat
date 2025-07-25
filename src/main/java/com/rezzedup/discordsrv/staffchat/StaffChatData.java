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

import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public interface StaffChatData {
	StaffChatProfile getOrCreateProfile(UUID uuid);
	
	Optional<StaffChatProfile> getProfile(UUID uuid);
	
	default StaffChatProfile getOrCreateProfile(Player player) {
		return getOrCreateProfile(player.getUniqueId());
	}
	
	default Optional<StaffChatProfile> getProfile(Player player) {
		// If they're a staff member, then they will always have a profile
		// otherwise, return the possibly existing profile for non-staff
		return (Permissions.ACCESS.allows(player) || Permissions.TEAM_ACCESS.allows(player))
			? Optional.of(getOrCreateProfile(player.getUniqueId()))
			: getProfile(player.getUniqueId());
	}
	
	default boolean isAutomaticStaffChatEnabled(Player player) {
		return getProfile(player).filter(StaffChatProfile::automaticStaffChat).isPresent();
	}
	
	default boolean isReceivingStaffChatMessages(Player player) {
		return getProfile(player).filter(StaffChatProfile::receivesStaffChatMessages).isPresent();
	}
	
	default boolean isAutomaticTeamChatEnabled(Player player) {
		return getProfile(player).filter(StaffChatProfile::automaticTeamChat).isPresent();
	}
	
	default boolean isReceivingTeamChatMessages(Player player) {
		return getProfile(player).filter(StaffChatProfile::receivesTeamChatMessages).isPresent();
	}
	
	void updateProfile(Player player);
}
