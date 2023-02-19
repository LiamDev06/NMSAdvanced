# NMSAdvanced
This repository includes a variety of features and creations relating to NMS plugin development. A majority of the repo is code I have written while watching [MineAcademy's NMSAdvanced course](https://mineacademy.org/nms-advanced).

## Information
The **NMSAdvanced** repository is a Spigot 1.8.8 plugin including everything from particles, animations, commands, NPCs, NBT data, GUIs and much more. As I build onto this project, I will keep updating the features section down below. The plugin does not serve any direct purpose - it is a general collection of things I have built that requires the use of NMS during my journey of learning the advanced parts of plugin development.

## "Fork if you want to learn!"
This project covers a wide aspect of NMS topics and features. Quality has been focused over quantity to provide as many different NMS topics as possible but few at each one. As a bonus, the project is very well commenteted which makes it a great starting point if you are trying to learn NMS. Fork it, view, experiment, learn!

## Note: '@author Liam'
Certain classes/methods may be documented with '@author Liam'. Code from there is not taken from the course but instead developed by me.

## Features
- Commands
  - `/directping` - get the users ping by accessing NMS directly
  - `/reflectionping` - get the users ping by accessing NMS using reflection
  - `/display` - display an action bar or title using direct NMS with a specified message 
  - `/particlesbehind` - spawn particles behind a player when they walk using direct NMS
- Models
  - `Server Menu Info Modifier` - modifies the server menu info by listening for the OutServerInfo packet and intercepting it
- Custom Entities
  - `Killer Snowman` - custom entity snowman that overrides target+goal selectors to target nearby players and shots them with snowballs
