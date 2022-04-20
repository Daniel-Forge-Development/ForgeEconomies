# ForgeEconomies [![Discord](https://img.shields.io/discord/831966641586831431)](https://discord.gg/7vqgtrjDGw)

ForgeEconomies is designed to be an economy mod for MinecraftForge with support for 1.12.2 and 1.16.5.

# Installation Instructions
Head over to the releases section and grab `ForgeEconomies-Forge-x.x.x-1.12.2.jar` OR `ForgeEconomies-Forge-x.x.x-1.16.5.jar`  depending on whichever Forge version you are running. Place this file in the `/mods` folder.

If you are running Sponge or a Hybrid (Magma/Mohist/Arclight) you may also want to complement your ForgeEconomies install. These bridges will allow your Sponge plugins or Spigot plugins to hook into ForgeEconomies to use for currency handling.

If you are using Sponge (API 7 or 1.12.2), grab `ForgeEconomies-Sponge-Bridge-x.x.x.jar` from the releases. Place this file in the `/mods` folder.

If you are using a Hybrid (Magma/Mohist/Arclight), grab `ForgeEconomies-Spigot-Bridge-x.x.x.jar` from the releases. Place this file in the `/plugins` folder.

# Configuration Instructions

Default Config:

    database:
        pool-name: EconomiesForge
        ip:
        port:
        username:
        password:
        database:
        max-pool-size: 30
    economies:
        one:
            id: one
            display-name: coin
            display-name-plural: coins
            identifier: $
            prefix: true
            is-default: true
            default-value: 0.0
            minimum-pay-amount: 1.0
            economy-format: '%.2f'
    balance-shows-all: false

Database settings (**This is required for this to function! If you are hosting through a hosting company, you usually get MySQL Databases included**)

 - Pool-name - Can be left as Is, only change If storing multiple server's ForgeEconomies on the same database server
 - IP - IP Address of your MySQL database
 - Port - Port of your MySQL database (Default: 3306)
 - Username - Username of your MySQL database
 - Password - Password of your MySQL database
 - Database - Database name of your MySQL database
 - Max pool size - leave this alone unless told to change It

Economies - This Is where you define your currencies to be used on your server. Should be self explanatory

# Hooking Into Pixelmon
If you are wanting to link ForgeEconomies to Pixelmon you will need to get the following:

Sponge:
[EconomyBridge](https://pixelmonmod.com/mirror/sidemods/PixelmonEconomyBridge/2.0.8/)

Install this EconomyBridge Into your `/mods` folder

Spigot:
[PixelmonEconomyBridge](https://www.spigotmc.org/resources/pixelmoneconomybridge.97889/), 
[Evernife Core](https://www.spigotmc.org/resources/evernifecore.97739/), 
[Vault](https://www.spigotmc.org/resources/vault.34315/)

Install all these things Into your `/plugins` folder
