<h3 align="center">
	<img src="https://raw.githubusercontent.com/SILENCE-SIMSOOL/SilenceUtils/main/docs/icon.png" alt="Silence Icon" width="128" height="128" />
</h3>

<h1 align="center">Silence Utils</h1>

<p align="center">
	The ultimate SkyBlock all-in-one mod.
	This advanced mod packs an extensive feature set, combining exceptional quality-of-life enhancements with optimized performance.
	Break free from the clutter of numerous mods and lag in SkyBlock.
</p>

<p align="center">
	<a href="https://github.com/SILENCE-SIMSOOL/SilenceUtils/releases" target="_blank">
		<img alt="release" src="https://img.shields.io/github/v/release/SILENCE-SIMSOOL/SilenceUtils?color=E0E0E0&style=flat-square" />
	</a>
	<a href="https://github.com/SILENCE-SIMSOOL/SilenceUtils/releases" target="_blank">
		<img alt="downloads" src="https://img.shields.io/github/downloads/SILENCE-SIMSOOL/SilenceUtils/total?color=8b4db6&style=flat-square" />
	</a>
	<a href="https://discord.gg/2Zt8HDksJs" target="_blank">
		<img alt="discord" src="https://img.shields.io/discord/1312525891225784421?color=5865F2&label=discord&style=flat-square" />
	</a>
</p>

## Vision of Silence Utils
Silence Utils pursues three ultimate goals:
1. **Consolidated Features:** Implement all useful and essential features within a single mod.
2. **Lightweight & High Performance:** Keep the mod lightweight and performance-friendly to elevate the gameplay experience.
3. **Focused Development:** Avoid unnecessary features to keep the mod streamlined and suitable for everyone.

Development of Silence Utils is heavily focused on Dungeons and tailored toward veteran players.

## 🔌 Dependencies
Please make sure to use [LucentClient](https://silencedev.kro.kr/en/products/lucent-client).  
LucentClient includes all useful vanilla enhancements and optimization features.

## 📘 Other Valuable Mods
Silence Utils does not implement features for specific themes or events. Here's why:
- They offer limited general utility while adding significant weight and complexity.
- Excellent dedicated mods for those specific themes already exist.

Therefore, SILENCE recommends the following mods:

| Mod Name | Description | Notes |
| :--- | :--- | :--- |
| [SBO](https://github.com/SkyblockOverhaul/SBO) | Well-implemented features and community tools for the Diana Event. | Responds very quickly to Hypixel updates. |
| [SkyHanni](https://github.com/hannibal002/SkyHanni) | Contains various useful features like Rift and Foraging. Great for SkyBlock leveling. | Based on NEU, inheriting the heavy, performance-degrading structure of the 1.8.9 era. However, it is still a solid choice for beginners due to the exclusive features it offers. |
| [SkyCofl](https://github.com/Coflnet/SkyblockmodFabric) | Provides a powerful API for item pricing and market data. | |

## ✨ First-in-class Features Created by Silence Utils

| Date | Feature / Mod Name | Description |
| :--- | :--- | :--- |
| Feb 15, 2024 | Dungeon Queue Ready | Starts the dungeon after checking the ready status of party members. |
| Feb 18, 2024 | Dungeon Class Alert | Plays a notification sound and displays a title message based on your selected class. |
| Feb 18, 2024 | Terminal Waypoint | Displays color-coded waypoints for each class in terminals. Also displays where to aim when using a Terminator on the 4th device, and visual boundaries separating Goldor's Door and the terminal. |
| Feb 18, 2024 | Farming Trigger | Leverages overlapping keybinds introduced in 1.20 to bind left-click to the A and D keys, allowing farming without using a mouse. |
| Mar 2, 2024 | GUI Icon (ArrowShop, Abiphone..) | Replaces default slot item rendering with custom icons to make items easily distinguishable. |
| Mar 2, 2024 | PartyFinder Manager | Filters parties by tracking included and missing classes. |
| Mar 19, 2024 | i4(Pre4) Status (DeviceStatus) | Indicates whether the 4th device has been cleared. (Later renamed to Device Status). |
| Apr 23, 2024 | Fix Teleport | Parallels item usage to enable teleportation independent of latency. Inspired by the ZPEW ChatTriggers module, this was developed by researching calculation formulas for standard teleports like Hyperion and Aspect of the End. (No longer functional due to Hypixel updates). |
| Apr 25, 2024 | Multiple Keybind | Assigns a chat message or command to execute when middle-clicking a SkyBlock item. |
| Apr 28, 2024 | SlotBind | Unlike traditional SlotBind, this specifies the exact slot for SkyBlock items for versatile layouts. Items automatically return to their designated slots, making organization effortless. |
| Aug 11, 2024 | SpiritLeap | Improves the SpiritLeap Screen designed by BetterMap with UI adjustments and new features. Leap to a specific class via keybinds, or perform blood rush easily using a dedicated keybind. Buttons are color-coded and display items for easier class identification. Pressing the map keybind opens a map overlay to quickly locate and leap to players. |
| May 4, 2024 | GUI Click Fix | Optimizes GUI clicks using smarter methods. |
| May 4, 2024 | MiningFix | Resolves client-side block breaking delays caused by latency. |
| Mar 22, 2025 | Optimized Boulder Solver | Since the Boulder Puzzle can be triggered by clicking signs, this solver calculates and shows the absolute most optimized path. Its extremely fast pattern detection and solution computation provide the answer instantly upon entering the room, unlike other mods such as Skytils. |
| Jun 23, 2025 | Skyblock Culling | Applies custom culling optimized for Skyblock to save hardware resources and significantly boost FPS. |
| Sep 28, 2025 | Call & Dungeon Overlay | Simple UI overlay to execute the `/call` command or start a dungeon with a single click. |
| Oct 20, 2024 | WardrobeScreen | Inspired by SkyHanni. Features larger, highly intuitive buttons for easier wardrobe navigation. Integrates legacy WardrobeControl keybinds to select slots and supports the popular "Swap Keybind" feature. |
| Oct 25, 2025 | Melody Solver | Prevents clicks when they shouldn't happen. Allows safe spam clicking, helping high-ping players easily achieve 4-instant clicks. |
| Oct 29, 2025 | Fix DungeonBreaker | Fixes the latency dependency when mining non-stone blocks in 1.8.9. For version 1.20+, it fixes a delay issue when starting to mine with another item. It also automatically protects blocks that shouldn't be broken, such as chests and redstone blocks. |
| Nov 14, 2025 | Leap Message | Detects leap messages and reformats them with class names and colors for improved readability (e.g., `[A] Steve ➜ [M] SimSool`). |
| Nov 20, 2025 | Clean Dungeon Message | Filters out unnecessary chat spam and restructures messages to keep the chat clean and readable. |
| Dec 14, 2025 | 3x3 Necron Waypoint | Added waypoints showing the 3x3 positions the Healer needs to break during the Necron Phase. |
| Nov 26, 2025 | Ability Keybind | Maps the Ctrl+Drop action to a single key, enabling dungeon abilities with a single keystroke. |
| Jul 9, 2026 | Loadouts | Renders the Loadouts with a beautiful UI similar to WardrobeScreen, supporting keybinds and the Swap feature. |

## ❓ FAQ
### Why is Silence Utils not open-source?
Originally, Silence Utils was developed as a private mod, and there are currently no plans to distribute it on public platforms like Modrinth.

However, the mod is completely unobfuscated. Since Minecraft version 26+, mappings are no longer required, meaning you can easily view the entire source code using any decompiler. If you suspect any malicious code (like a RAT), you are more than welcome to run it through any malware/RAT scanner of your choice.

## 📖 Terms of Service
1. Users assume full responsibility for using this mod.
2. Minimal user information may be collected for error reporting and diagnostics.
3. The mod must not be used for malicious purposes.

## 🙏 Credits & Contributors
> Translation
- _limu

> Ideas & Assistance
- [Catsino](https://github.com/HexPaw)
- [yua](https://www.youtube.com/@whiteups)
- [PLG4](https://www.youtube.com/@SEXYPLUG)
- s._.un.
- wbwi

> Development
- [Daniel](https://github.com/Gentor-a11y): Co-developer
- _Light: Initial version of the inventory button
