package com.gmail.trentech.easykits.init;

import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import com.gmail.trentech.easykits.commands.CMDCooldown;
import com.gmail.trentech.easykits.commands.CMDCreate;
import com.gmail.trentech.easykits.commands.CMDGive;
import com.gmail.trentech.easykits.commands.CMDKit;
import com.gmail.trentech.easykits.commands.CMDLimit;
import com.gmail.trentech.easykits.commands.CMDList;
import com.gmail.trentech.easykits.commands.CMDView;
import com.gmail.trentech.easykits.commands.delete.CMDDelete;
import com.gmail.trentech.easykits.commands.delete.CMDYes;
import com.gmail.trentech.easykits.commands.elements.KitElement;
import com.gmail.trentech.easykits.commands.reset.CMDAll;

public class Commands {

	private CommandSpec cmdCreate = CommandSpec.builder()
		    .permission("easykits.cmd.kit.create")    
		    .arguments(
		    		GenericArguments.optional(GenericArguments.string(Text.of("name"))))
		    .executor(new CMDCreate())
		    .build();

	private CommandSpec cmdYes = CommandSpec.builder()
		    .permission("easykits.cmd.kit.delete")
		    .executor(new CMDYes())
		    .build();
	
	private CommandSpec cmdDelete = CommandSpec.builder()
		    .permission("easykits.cmd.kit.delete")
		    .arguments(
		    		GenericArguments.optional(new KitElement(Text.of("kit"))))
		    .child(cmdYes, "yes", "y")
		    .executor(new CMDDelete())
		    .build();

	private CommandSpec cmdList = CommandSpec.builder()
		    .permission("easykits.cmd.kit.list")
		    .executor(new CMDList())
		    .build();
	
	private CommandSpec cmdCooldown = CommandSpec.builder()
		    .permission("easykits.cmd.kit.cooldown")
		    .arguments(
		    		GenericArguments.optional(new KitElement(Text.of("kit"))), 
		    		GenericArguments.optional(GenericArguments.string(Text.of("cooldown"))))
		    .executor(new CMDCooldown())
		    .build();

	private CommandSpec cmdLimit = CommandSpec.builder()
		    .permission("easykits.cmd.kit.limit")
		    .arguments(
		    		GenericArguments.optional(new KitElement(Text.of("kit"))), 
		    		GenericArguments.optional(GenericArguments.integer(Text.of("limit"))))
		    .executor(new CMDLimit())
		    .build();
	
	private CommandSpec cmdPrice = CommandSpec.builder()
		    .permission("easykits.cmd.kit.price")
		    .arguments(
		    		GenericArguments.optional(new KitElement(Text.of("kit"))), 
		    		GenericArguments.optional(GenericArguments.doubleNum(Text.of("price"))))
		    .executor(new CMDLimit())
		    .build();
	
	private CommandSpec cmdView = CommandSpec.builder()
		    .permission("easykits.cmd.kit.view")
		    .arguments(
		    		GenericArguments.optional(new KitElement(Text.of("kit"))))
		    .executor(new CMDView())
		    .build();
	
	private CommandSpec cmdGive = CommandSpec.builder()
		    .permission("easykits.cmd.kit.give")
		    .arguments(
		    		GenericArguments.optional(new KitElement(Text.of("kit"))), 
		    		GenericArguments.optional(GenericArguments.player(Text.of("player"))))
		    .executor(new CMDGive())
		    .build();
	
	private CommandSpec cmdResetCooldown = CommandSpec.builder()
		    .permission("easykits.cmd.kit.reset")
		    .arguments(
		    		GenericArguments.optional(new KitElement(Text.of("kit"))), 
		    		GenericArguments.optional(GenericArguments.player(Text.of("player"))))
		    .executor(new com.gmail.trentech.easykits.commands.reset.CMDCooldown())
		    .build();
	
	private CommandSpec cmdResetLimit = CommandSpec.builder()
		    .permission("easykits.cmd.kit.reset")
		    .arguments(
		    		GenericArguments.optional(new KitElement(Text.of("kit"))), 
		    		GenericArguments.optional(GenericArguments.player(Text.of("player"))))
		    .executor(new com.gmail.trentech.easykits.commands.reset.CMDLimit())
		    .build();

	private CommandSpec cmdResetAll = CommandSpec.builder()
		    .permission("easykits.cmd.kit.reset")
		    .arguments(
		    		GenericArguments.optional(new KitElement(Text.of("kit"))), 
		    		GenericArguments.optional(GenericArguments.player(Text.of("player"))))
		    .executor(new CMDAll())
		    .build();
	
	public CommandSpec cmdReset = CommandSpec.builder()
			.permission("easykits.cmd.kit.reset")
			.child(cmdResetLimit, "limit", "l")
			.child(cmdResetCooldown, "cooldown", "c")
			.child(cmdResetAll, "all", "a")
			.executor(new CMDKit())
			.build();
	
	public CommandSpec cmdKit = CommandSpec.builder()
			.permission("easykits.cmd.kit")
			.child(cmdPrice, "price", "p")
			.child(cmdLimit, "limit", "l")
			.child(cmdCooldown, "cooldown", "cd")
			.child(cmdCreate, "create", "c")
			.child(cmdDelete, "delete", "d")
			.child(cmdList, "list", "ls")
			.child(cmdView, "view", "v")
			.child(cmdReset, "reset", "r")
			.child(cmdGive, "give", "g")
			.executor(new CMDKit())
			.build();
}