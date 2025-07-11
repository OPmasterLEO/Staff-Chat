<div align="center>

# ![](https://i.imgur.com/DZwTm1u.png "DiscordSRV-Staff-Chat Logo")

[![](https://img.shields.io/badge/License-MIT-blue)](./LICENSE "Project License: MIT")
[![](https://img.shields.io/badge/Java-11-orange)](# "Java Version: 11")
[![](https://img.shields.io/github/v/release/DiscordSRV/Staff-Chat.svg?label=Release&color=ok)](https://github.com/DiscordSRV/Staff-Chat/releases/latest "Latest Release")
[![](https://img.shields.io/spiget/downloads/44245?color=yellow&label=Spigot%20Downloads)](https://www.spigotmc.org/resources/discordsrv-staff-chat.44245/ "Spigot Resource Page")
[![](https://img.shields.io/modrinth/dt/uD7Bzf5q?color=%2300af5c&label=Modrinth%20Downloads&logo=modrinth)](https://modrinth.com/plugin/uD7Bzf5q "Modrinth Project Page")

</div>

DiscordSRV-Staff-Chat is a staff chat plugin that connects to a Discord channel (via [DiscordSRV](https://github.com/DiscordSRV/DiscordSRV)), allowing in-game staff to communicate with staff on Discord.

![](https://i.imgur.com/ssKGDTJ.gif) 

## Installation

* Add **DiscordSRV** and **DiscordSRV-Staff-Chat** to your `plugins/` directory.
* Restart the server.
* Add a `staff-chat` channel to **DiscordSRV**'s config.
* If you want to use team chat as well, add a `team-chat` channel.
* Execute: `/discord reload` *or* restart the server again.

### Adding a staff-chat channel

Add a **"staff-chat"** entry to DiscordSRV's config. Note: it's very important that this entry is called `"staff-chat"`.

```yaml
# Channel links from game to Discord
# syntax is Channels: {"in-game channel name from Minecraft": "numerical channel ID from Discord", "another in-game channel name from Minecraft": "another numerical channel ID from Discord"}
#
Channels: {"global": "000000000000000000", "staff-chat": "000000000000000000"}
```

Replace all those zeros with your staff chat's channel ID.

![](https://i.imgur.com/tXNU6Ei.gif)

```yaml
Channels: {"global": "000000000000000000", "staff-chat": "337769984539361281"}
```

### Adding a team-chat channel

Similarly, add a **"team-chat"** entry to DiscordSRV's config if you want to use team chat functionality.

```yaml
# Channel links from game to Discord
# syntax is Channels: {"in-game channel name from Minecraft": "numerical channel ID from Discord"}
#
Channels: {"global": "000000000000000000", "staff-chat": "000000000000000000", "team-chat": "000000000000000000"}
```

Replace all those zeros with your team chat's channel ID.


## Commands & Permissions

### Staff Chat Commands

#### /staffchat

> ![](https://i.imgur.com/ILwkaqa.gif)
> 
> **Permission:** `staffchat.access`
> 
> **Aliases:** `/adminchat`, `/schat`, `/achat`, `/sc`, `/ac`, and `/a`
> 
> **Usage:**
> - `/staffchat` - Toggle automatic staff chat.
> - `/staffchat <message>` - Send a message to the staff chat.

#### /leavestaffchat

> ![](https://i.imgur.com/BO3fgmC.png)
> 
> **Permission:** `staffchat.access`
> 
> **Aliases:** `/leaveadminchat`
> 
> **Usage:**
> - `/leavestaffchat` - Stop receiving staff chat messages.
>   - Useful to avoid leaking staff chat messages if a staff member is filming or streaming.
>   - This can be disabled in the config if you don't want staff members to turn off their staff chat.

#### /joinstaffchat

> ![](https://i.imgur.com/7EriNrS.png)
> 
> **Permission:** `staffchat.access`
> 
> **Aliases:** `/joinadminchat`
> 
> **Usage:**
> - `/joinstaffchat` - Start receiving staff chat messages again if you previously left.

#### /togglestaffchatsounds

> ![](https://i.imgur.com/MYbaRtZ.png)
> 
> **Permission:** `staffchat.access`
> 
> **Aliases:** `/toggleadminchatsounds`
> 
> **Usage:**
> - `/togglestaffchatsounds` - Mute or unmute staff chat sounds for yourself.

#### /managestaffchat

> **Permission:** `staffchat.manage`
> 
> **Aliases:** `/discordsrv-staff-chat`, `/discordsrvstaffchat`, `/discordstaffchat`, `/discordadminchat`, `/manageadminchat`
> 
> **Usage:**
> - `/managestaffchat` - Display plugin information and command usage.
> - `/managestaffchat reload` - Reload configs.
> - `/managestaffchat debug` - Enable or disable debugging.

### Team Chat Commands

#### /teamchat

> **Permission:** `teamchat.access`
> 
> **Aliases:** `/tchat`, `/tc`, `/t`
> 
> **Usage:**
> - `/teamchat` - Toggle automatic team chat.
> - `/teamchat <message>` - Send a message to the team chat.

#### /leaveteamchat

> **Permission:** `teamchat.access`
> 
> **Aliases:** `/leavetc`
> 
> **Usage:**
> - `/leaveteamchat` - Stop receiving team chat messages.
>   - Useful to avoid leaking team chat messages during streaming.
>   - This can be disabled in the config if you don't want team members to turn off their team chat.

#### /jointeamchat

> **Permission:** `teamchat.access`
> 
> **Aliases:** `/jointc`
> 
> **Usage:**
> - `/jointeamchat` - Start receiving team chat messages again if you previously left.

#### /toggleteamchatsounds

> **Permission:** `teamchat.access`
> 
> **Aliases:** `/toggletcsounds`
> 
> **Usage:**
> - `/toggleteamchatsounds` - Mute or unmute team chat sounds for yourself.

#### /manageteamchat

> **Permission:** `teamchat.manage`
> 
> **Aliases:** `/discordsrv-team-chat`, `/discordsrvteamchat`, `/discordteamchat`
> 
> **Usage:**
> - `/manageteamchat` - Display plugin information and command usage.
> - `/manageteamchat reload` - Reload configs.
> - `/manageteamchat debug` - Enable or disable debugging.

**Note:** Staff chat and team chat are mutually exclusive. Enabling one will automatically disable the other.

[![](https://bstats.org/signatures/bukkit/DiscordSRV-Staff-Chat.svg)](https://bstats.org/plugin/bukkit/DiscordSRV-Staff-Chat/11056)
