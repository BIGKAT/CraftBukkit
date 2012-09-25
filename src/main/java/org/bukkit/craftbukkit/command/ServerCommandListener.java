package org.bukkit.craftbukkit.command;

import java.lang.reflect.Method;

import net.minecraft.src.ICommandSender;
import net.minecraft.src.StringTranslate;

import org.bukkit.command.CommandSender;

public class ServerCommandListener implements ICommandSender {
    private final CommandSender commandSender;
    private final String prefix;

    public ServerCommandListener(CommandSender commandSender) {
        this.commandSender = commandSender;
        String[] parts = commandSender.getClass().getName().split("\\.");
        this.prefix = parts[parts.length - 1];
    }

    public void sendChatToPlayer(String msg) {
        this.commandSender.sendMessage(msg);
    }

    public CommandSender getSender() {
        return commandSender;
    }

    public String getCommandSenderName() {
        try {
            Method getName = commandSender.getClass().getMethod("getName");

            return (String) getName.invoke(commandSender);
        } catch (Exception e) {}

        return this.prefix;
    }

    public String translateString(String s, Object... aobject) {
        return StringTranslate.getInstance().translateKeyFormat(s, aobject);
    }

    public boolean canCommandSenderUseCommand(String s) {
        return true;
    }
}