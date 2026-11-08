# Minecraft Companion Plugin: Gaslight Companion

Gaslight Companion is a Minecraft plugin for Paper servers that adds an affectionate fart companion, a custom spawn egg, owner-bound interaction, automatic nearby toots, particle effects, sounds, and permission-controlled spawning.

## Install

1. Build the plugin with Java 21 and Maven:
   ```bash
   mvn package
   ```
2. Copy the generated `target/plugin.jar` file into your server's `plugins` directory.
3. Start or restart a Paper Minecraft server running version 1.21.11 or a compatible 1.21 server.
4. Confirm that the plugin loads successfully in the server console.

## Use

1. Ensure the player has the `fartgirlfriend.use` permission. This permission is enabled by default.
2. Run:
   ```text
   /fartgirlfriend
   ```
   The command also supports the alias:
   ```text
   /fgf
   ```
3. Right-click a block with the Fart Girlfriend Spawn Egg.
4. The plugin spawns an invulnerable villager companion named `Fart Girlfriend`.
5. The companion is assigned to the player who spawned her.
6. Right-click the companion to trigger an affectionate toot, cloud and composter particles, a sound effect, and a message.
7. When the owner is within six blocks, the companion periodically has a chance to toot automatically.
8. Other players cannot activate the companion and receive a message stating that she belongs to another player.

---

More Minecraft plugin projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/dd08f7b3-e842-411f-a67c-77e2b5f7ebe6?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=dd08f7b3-e842-411f-a67c-77e2b5f7ebe6
