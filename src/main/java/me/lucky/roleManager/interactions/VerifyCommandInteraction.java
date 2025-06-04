package me.lucky.roleManager.interactions;

import an.awesome.pipelinr.Pipeline;
import com.google.inject.Inject;
import me.lucky.roleManager.RoleManager;
import me.lucky.roleManager.events.discord.VerifyUser.VerifyUserCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.math.BigInteger;

public class VerifyCommandInteraction extends ListenerAdapter {

    @Inject
    private Pipeline pipeline;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals(Commands.VERIFY.getCommandName())) {
            return;
        }

        event.deferReply(true).queue();


        if (!BigInteger.valueOf(event.getGuild().getIdLong()).equals(RoleManager.GUILD_ID)) {
            event.getHook().sendMessage("Der Command darf nur auf dem offiziellen Asunaras Discord benutzt werden!").queue();
            return;
        }


        var minecraftName = event.getInteraction().getOption(CommandsConst.PARAMETER_MINECRAFT_NAME).getAsString();
        pipeline.send(new VerifyUserCommand(event.getHook(), minecraftName, event.getUser().getIdLong()));
    }
}
