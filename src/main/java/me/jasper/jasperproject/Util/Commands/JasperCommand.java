package me.jasper.jasperproject.Util.Commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public interface JasperCommand {
    LiteralArgumentBuilder<CommandSourceStack> createCommand();
}
