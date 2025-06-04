package me.lucky.roleManager.interactions;

import an.awesome.pipelinr.Pipeline;
import com.google.inject.Inject;
import me.lucky.roleManager.events.discord.AddWhitelist.AddWhitelistCommand;
import me.lucky.roleManager.events.discord.BanFromWhitelist.BanFromWhitelistCommand;
import me.lucky.roleManager.events.discord.RemoveWhitelistDiscord.RemoveWhitelistDiscordCommand;
import me.lucky.roleManager.events.discord.RemoveWhitelistMinecraft.RemoveWhitelistMinecraftCommand;
import me.lucky.roleManager.events.discord.ShowDiscord.ShowDiscordCommand;
import me.lucky.roleManager.events.discord.ShowMinecraft.ShowMinecraftCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class WhitelistCommandInteraction extends ListenerAdapter {

    @Inject
    private Pipeline pipeline;


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals(Commands.WHITELIST.getCommandName())) {
            return;
        }

        event.deferReply(true).queue();

        if (event.getSubcommandName() == null) {
            return;
        }

        // Handle subcommand groups
        if (event.getSubcommandGroup() != null) {
            switch (event.getSubcommandGroup()) {
                case CommandsConst.Whitelist.Show.SHOW_GROUP_NAME ->
                        this.handleShowGroup(event);
                case CommandsConst.Whitelist.Remove.REMOVE_GROUP_NAME ->
                        this.handleRemoveGroup(event);
                default -> throw new IllegalStateException("Unexpected value: " + event.getSubcommandGroup());
            }

            return;
        }

        // Handle subcommands
        switch (event.getSubcommandName()) {
            case CommandsConst.Whitelist.Add.ADD_SUBCOMMAND ->
                    this.handleAdd(event);
            case CommandsConst.Whitelist.Ban.BAN_SUBCOMMAND ->
                    this.handleBan(event);
            default -> throw new IllegalStateException("Unexpected value: " + event.getSubcommandName());
        }
    }

    private void handleAdd(SlashCommandInteractionEvent event) {
        pipeline.send(new AddWhitelistCommand(event.getHook(), event.getOption(CommandsConst.PARAMETER_DISCORD_USER).getAsUser().getIdLong(), event.getOption(CommandsConst.PARAMETER_MINECRAFT_NAME).getAsString()));
    }

    private void handleBan(SlashCommandInteractionEvent event) {
        pipeline.send(new BanFromWhitelistCommand(event.getHook(), event.getOption(CommandsConst.PARAMETER_DISCORD_USER).getAsUser().getIdLong(), event.getOption(CommandsConst.Whitelist.Ban.BAN_REASON).getAsString()));
    }

    private void handleShowGroup(SlashCommandInteractionEvent event) {
        if (event.getSubcommandName() == null) {
            throw new IllegalStateException("Unexpected value: " + event.getSubcommandName());
        }

        switch (event.getSubcommandName()) {
            case CommandsConst.Whitelist.DISCORD_USER_SUBCOMMAND -> this.handleShowDiscordUser(event);
            case CommandsConst.Whitelist.MINECRAFT_USER_SUBCOMMAND -> this.handleShowMinecraft(event);
            default -> throw new IllegalStateException("Unexpected value: " + event.getSubcommandName());
        }
    }

    private void handleShowDiscordUser(SlashCommandInteractionEvent event) {
        pipeline.send(new ShowMinecraftCommand(event.getHook(), event.getOption(CommandsConst.PARAMETER_DISCORD_USER).getAsUser().getIdLong()));
    }

    private void handleShowMinecraft(SlashCommandInteractionEvent event) {
        pipeline.send(new ShowDiscordCommand(event.getHook(), event.getOption(CommandsConst.PARAMETER_MINECRAFT_NAME).getAsString()));
    }

    private void handleRemoveGroup(SlashCommandInteractionEvent event) {
        if (event.getSubcommandName() == null) {
            throw new IllegalStateException("Unexpected value: " + event.getSubcommandName());
        }

        switch (event.getSubcommandName()) {
            case CommandsConst.Whitelist.DISCORD_USER_SUBCOMMAND -> this.handleRemoveDiscordUser(event);
            case CommandsConst.Whitelist.MINECRAFT_USER_SUBCOMMAND -> this.handleRemoveMinecraftUser(event);
            default -> throw new IllegalStateException("Unexpected value: " + event.getSubcommandName());
        }
    }

    private void handleRemoveDiscordUser(SlashCommandInteractionEvent event) {
        pipeline.send(new RemoveWhitelistDiscordCommand(event.getHook(), event.getOption(CommandsConst.PARAMETER_DISCORD_USER).getAsUser().getIdLong()));
    }

    private void handleRemoveMinecraftUser(SlashCommandInteractionEvent event) {
        pipeline.send(new RemoveWhitelistMinecraftCommand(event.getHook(), event.getOption(CommandsConst.PARAMETER_MINECRAFT_NAME).getAsString()));
    }
}
