<div align="center">

# NX Client

**A clean, high-performance Minecraft 1.8.9 PvP client mod built on Forge + Mixin.**

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](LICENSE)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.8.9-green.svg)](https://www.minecraft.net)
[![Forge](https://img.shields.io/badge/Forge-11.15.1.2318-orange.svg)](https://files.minecraftforge.net)
[![Java](https://img.shields.io/badge/Java-8-red.svg)](https://adoptium.net)

</div>

---

## Overview

NX Client is an open-source Minecraft 1.8.9 client-side Forge mod designed for competitive PvP players, primarily targeting the **Hypixel** network. It delivers visual enhancements, HUD overlays, and quality-of-life features without modifying game mechanics, keeping you compliant with server rules.

Key design goals:
- **Zero combat advantage** — purely visual, HUD, and cosmetic features
- **Mixin-based injection** — clean, non-invasive patching of vanilla code
- **Modular architecture** — every feature is an independent, toggleable module
- **Persistent config** — all settings saved on shutdown and restored on launch

---

## Features

### Render
| Module | Description | Settings |
|---|---|---|
| **FPS Boost** | Reduces GPU load by stripping non-essential rendering | Reduced Particles, Disable Fog, Fast Render |
| **Zoom** | Smooth, adjustable zoom on a keybind | Zoom Level, Smooth Toggle |
| **Fullbright** | Raises gamma to eliminate darkness | Gamma (1.0 to 15.0) |
| **Custom Crosshair** | Replaces the vanilla crosshair with a styled one | Style, Color, Size, Gap, Dot |
| **Motion Blur** | Applies an accumulation-based motion blur effect | Strength |
| **Block Overlay** | Replaces the block selection highlight with a clean outline | Color, Line Width, Fill Opacity |
| **Nametag Tweaks** | Customizes how player nametags render | Scale, Hide Background, Show Own Tag, Show Health |
| **Chroma UI** | Applies a global RGB chroma cycle to all HUD elements | Speed, Saturation |

### HUD
| Module | Description | Settings |
|---|---|---|
| **FPS Display** | Shows current frames per second | Style, Color |
| **CPS Counter** | Tracks left and right mouse click-per-second in real time | Separate Colors, Show Both |
| **Keystrokes** | Visual WASD + LMB/RMB keystroke overlay | Active Color, Inactive Color, Show Mouse |
| **Coordinates** | Displays X/Y/Z position and facing direction | Show Direction, Label Color, Value Color |
| **Armor HUD** | Shows your equipped armor with durability bars | Show Durability, Show Held Item, Orientation |
| **Potion HUD** | Lists all active potion effects with timers | Show Amplifier, Time Format, Color |
| **Ping Display** | Shows your current server latency in milliseconds | Color |
| **Reach Display** | Shows the distance of your last hit on a player | Color |
| **Combo Counter** | Counts consecutive hits on an entity | Color, Fade Timeout |
| **Clock HUD** | Displays your real-world system time | 24h Mode, Show Date |
| **Scoreboard Tweaks** | Cleans up the vanilla scoreboard rendering | Hide Numbers, Custom Title Color, Scale |

### Animation
| Module | Description | Settings |
|---|---|---|
| **Old Animations** | Restores 1.7-style item swing and hold animations | Old Swing, Old Sword, Old Blocking, Old Bow, Old Rod, Old Eating |

### Hypixel
| Module | Description | Settings |
|---|---|---|
| **Auto GG** | Automatically sends a message when a game ends | Message Style, Delay |
| **Bedwars Level Display** | Fetches and shows each player's Bedwars prestige star | Show Star Symbol, Style |
| **Network Level Head** | Renders Hypixel network level above player heads | Display Location, Show Brackets |

### Cosmetics
| Feature | Description |
|---|---|
| **NX Cape** | Exclusive animated cape rendered for all registered NX Client users |

---

## Architecture

```
net.nx.client
├── NXClient.java              — Forge @Mod entry point, lifecycle hooks
├── config/
│   └── ConfigManager.java     — Gson-based JSON save/load for all module settings
├── cosmetic/
│   └── CapeManager.java       — Renders the NX cape texture on registered players
├── event/
│   └── EventManager.java      — Central Forge event bus registration hub
├── gui/
│   ├── clickgui/
│   │   ├── ClickGUI.java      — Full-screen GUI with animated fade-in and search bar
│   │   ├── CategoryPanel.java — Collapsible draggable panel per module category
│   │   └── ModuleButton.java  — Individual toggle button with settings expander
│   ├── hud/
│   │   └── HUDEditor.java     — Drag-and-drop overlay editor for HUD positions
│   └── mainmenu/
│       └── CustomMainMenu.java — Branded main menu with NX Client branding
├── mixin/
│   ├── MixinAbstractClientPlayer.java  — Cape injection
│   ├── MixinEntityRenderer.java        — FOV override for Zoom
│   ├── MixinGuiMainMenu.java           — Main menu replacement hook
│   ├── MixinItemRenderer.java          — Old animation transforms
│   ├── MixinModelBiped.java            — Arm/blocking animation override
│   ├── MixinNetworkPlayerInfo.java     — Cape texture override per player
│   └── MixinRenderManager.java         — Nametag render hook
├── module/
│   ├── Module.java            — Abstract base: name, category, keybind, settings, toggle
│   ├── ModuleManager.java     — Registry, tick dispatch, keybind listener
│   ├── Category.java          — Enum: RENDER, HUD, ANIMATION, HYPIXEL, MISC
│   ├── modules/               — All feature implementations
│   └── settings/
│       ├── Setting.java        — Generic typed setting base class
│       ├── BooleanSetting.java — Toggle on/off
│       ├── SliderSetting.java  — Numeric range with step
│       ├── ModeSetting.java    — Enum choice
│       ├── ColorSetting.java   — RGBA color picker
│       └── KeybindSetting.java — Keyboard key assignment
├── ui/
│   └── NXColors.java          — Centralized color constants and chroma tick
└── util/
    └── RenderUtil.java        — Shared OpenGL helpers (rect, rounded rect, gradient)
```

---

## Requirements

| Requirement | Version |
|---|---|
| Minecraft | 1.8.9 |
| Minecraft Forge | 11.15.1.2318-1.8.9 |
| Java | 8 (JDK 8 required to build) |
| OS | Windows / macOS / Linux |

---

## Installation

### Pre-built JAR (Recommended)

1. Install [Minecraft Forge 1.8.9](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.8.9.html) build `11.15.1.2318`
2. Download the latest `NXClient-1.0.0.jar` from the [Releases](../../releases) page
3. Place the JAR into your `.minecraft/mods/` folder
4. Launch Minecraft using the **Forge 1.8.9** profile

### Build from Source

Prerequisites: Git, JDK 8

```bash
# Clone the repository
git clone https://github.com/NX-developer/NXClient.git
cd NXClient

# Linux / macOS only
chmod +x gradlew

# Download Minecraft + MCP mappings (takes a few minutes the first time)
./gradlew setupDecompWorkspace

# Build the mod JAR
./gradlew build
```

Output: `build/libs/NXClient-1.0.0.jar`

> On Windows use `gradlew.bat` instead of `./gradlew`

---

## Usage

### Opening the ClickGUI

Press **Right Shift** (default) to open the module menu.

### ClickGUI Controls

| Action | Input |
|---|---|
| Open / Close | Right Shift |
| Toggle module | Left click |
| Expand settings | Right click |
| Search modules | Type in the search bar at the top |
| Drag panel | Left click + drag the panel header |
| Scroll modules | Mouse wheel over a panel |
| Close | Escape |

### HUD Editor

Press **Right Alt** (default) to enter the drag-and-drop HUD editor. Drag each overlay to your preferred screen position, then press Escape to save.

### Keybinds

Right-click a module in the ClickGUI, expand its settings, click the Keybind field, and press any key to assign it.

### Configuration

Settings are saved automatically to:

```
.minecraft/config/nxclient/settings.json
```

---

## Development

### Setting Up the IDE

```bash
git clone https://github.com/NX-developer/NXClient.git
cd NXClient

./gradlew setupDecompWorkspace idea     # IntelliJ IDEA
./gradlew setupDecompWorkspace eclipse  # Eclipse
```

### Adding a New Module

1. Create a class in the relevant category package under `src/main/java/net/nx/client/module/modules/`
2. Extend `Module` (or `HUDModule` for on-screen overlays)
3. Define settings in the constructor using `addSetting(...)`
4. Override `onEnable()`, `onDisable()`, `onUpdate()` as needed
5. Register it in `ModuleManager.registerAll()`

```java
public class MyModule extends Module {

    private final BooleanSetting enabled;

    public MyModule() {
        super("My Module", "Short description", Category.RENDER);
        this.enabled = addSetting(new BooleanSetting("My Toggle", "Enables the effect", true));
    }

    @Override
    public void onEnable() { /* setup */ }

    @Override
    public void onUpdate() { /* per-tick logic while active */ }
}
```

### Adding a New Mixin

1. Create your class in `src/main/java/net/nx/client/mixin/`
2. Register it in `src/main/resources/mixins.nxclient.json`

### Gradle Tasks

| Task | Description |
|---|---|
| `./gradlew setupDecompWorkspace` | Downloads MC + MCP mappings |
| `./gradlew build` | Compiles and packages the JAR |
| `./gradlew runClient` | Launches Minecraft in dev mode |

---

## Technology Stack

| Component | Technology |
|---|---|
| Mod loader | MinecraftForge 11.15.1.2318 |
| Bytecode patching | SpongePowered Mixin 0.7.11 |
| Build system | ForgeGradle 2.1 |
| JSON / Config | Gson 2.8.9 |
| Language | Java 8 |
| Obfuscation mappings | MCP Stable 22 (1.8.9) |

---

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/my-feature`
3. Commit your changes with a descriptive message
4. Open a Pull Request against `main`

Keep PRs focused on a single feature or fix. Do not mix unrelated changes in one PR.

---

## License

Licensed under the **GNU General Public License v3.0**.
See the [LICENSE](LICENSE) file for the full text.

You are free to use, modify, and distribute this software, but any derivative work must also be released as open source under GPL-3.0.

---

## Disclaimer

NX Client provides visual and quality-of-life enhancements only. It does not automate gameplay, modify network packets, or provide any combat advantage. Use in accordance with the rules of the server you play on. The authors are not responsible for any account actions taken by server operators.

---

<div align="center">
Made with care by the NX Team &nbsp;|&nbsp; GPL-3.0
</div>
