# Monster Stats Plugin

## Description

The Monster Stats Plugin provides a quick way to access the defensive stats of NPCs in Old School RuneScape (OSRS).

Shoutout to 'Coinmagnet_rs' on reddit for the original idea/request.

[![Active Installs](http://img.shields.io/endpoint?url=https://api.runelite.net/pluginhub/shields/installs/plugin/monsterstats)](https://runelite.net/plugin-hub/show/monsterstats)
[![Plugin Rank](http://img.shields.io/endpoint?url=https://api.runelite.net/pluginhub/shields/rank/plugin/monsterstats)](https://runelite.net/plugin-hub/show/monsterstats)


## Features

- **Hover Tooltip**: Toggle-able tooltip that displays elemental weaknesses and percentages as well as defensive stats for all styles. Compatible with other tooltips (like Runelite's Mouse Tooltips Plugin)
  ![image](https://github.com/user-attachments/assets/eb7be465-da79-4db5-b25a-6183aa991f03)

- **Right-click Stats Option**: Adds a 'Stats' option to the right-click menu for NPCs, opening a detailed defensive stats panel in the sidebar.
  
 ![image](https://github.com/Koitere/monster-stats/assets/48294933/cc73c955-01e5-40d5-bd4f-07ddaed1a24e)
 
- **Search Functionality**: Allows users to search for specific NPCs and view their defensive stats in the sidebar. Support for selecting alternate variants/forms of monsters by searching for the monster and using the variant buttons.
<img width="295" height="1506" alt="image" src="https://github.com/user-attachments/assets/918f65f6-0d69-46c1-ad45-d8c903d34d42" />

- **Stats on Examine**: Allows users to received a quick summary of the defensive stats of monsters when examining them.
<img width="698" height="146" alt="image" src="https://github.com/user-attachments/assets/64feb216-bc8f-4216-a555-69c921e062f3" />


## Configuration Options

- **Show Stats Menu Option**
  - **Description**: Enable or disable the right-click 'Stats' option for NPCs.
  - **Default**: Enabled

- **Show Hover Tooltip**
  - **Description**: Show a tooltip with elemental weakness and weakness percentage when hovering over monsters.
  - **Default**: Enabled

- **Modifier key for Tooltip**
  - **Description**: Display the hover tooltip only when the selected modifier key is held.
  - **Default**: Enabled

- **Modifier key selection**
  - **Description**: Customizable key bind for tooltip on hover.
  - **Default**: Shift

- **Enable Side Panel**
  - **Description**: Enables the searchable side panel to display more monster stats.
  - **Default**: Enabled

- **Show Stats on Examine**
  - **Description**: Displays a quick rundown of monster defensive stats in the chat box when a monster is examined.
  - **Default**: Enabled

## Disclaimer

Please note that this plugin is still in development. Some features may not work as expected, and there may be occasional bugs or performance issues. We appreciate your feedback and patience as we continue to improve the plugin.

If you enjoy the plugin and feel like being generous, feel free to buy me a coffee! https://buymeacoffee.com/koitere

## Change Log

### Version 1.3.0
- Added function
  - Added 'Combat Stats' to side panel and tooltip including monster attack speed
  - Added ability to see monster stats in chat when a monster is examined
  - Added ability to customize keybind used for displaying tooltips on hover

### Version 1.2.1
- Added function
  - Added flat armour stat for display
  - No longer add 'Stats' menu option for NPCs with no stats available
  - Added ability to view tooltips while sailing

### Version 1.2.0
- Improved functionalityL
  - Added displaying of maximum hits for monsters in the database
  - Added displaying of attack style for monsters in the database
  - Improved database population for quicker updating in the future
  - Added ability to enable/disable the side bar panel

### Version 1.1.0
- Improvements and Bug Fixes:
  - Added support for monsters with multiple variants.
  - Improved visuals for mouseover tooltips.
  - Fixed mouseover tooltips not working with other tooltip plugins.
  - Added support for new/more NPCs.
  - NPC right click is now by ID and should be accurate to wiki data, all NPCs with wiki data stats/NPC Ids are available.

### Version 1.0.0
- Initial release with core features:
  - Added hover tooltip displaying elemental weaknesses.
  - Implemented right-click 'Stats' option for NPCs.
  - Added search functionality in the sidebar.
  - Basic configuration options for enabling/disabling features.

---

I hope you find the Monster Stats Plugin helpful in your OSRS adventures. For any issues or feature requests, please open an issue on the GitHub repository.
