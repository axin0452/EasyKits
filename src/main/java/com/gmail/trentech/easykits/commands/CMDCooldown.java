package com.gmail.trentech.easykits.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.easykits.kit.Kit;
import com.gmail.trentech.easykits.kit.KitService;
import com.gmail.trentech.easykits.utils.Resource;
import com.gmail.trentech.pjc.help.Help;

public class CMDCooldown implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Help help = Help.get("kit cooldown").get();

		if (!args.hasAny("kit")) {
			throw new CommandException(Text.builder().onClick(TextActions.executeCallback(help.execute())).append(help.getUsageText()).build(), false);
		}
		Kit kit = args.<Kit>getOne("kit").get();
		
		if (!args.hasAny("cooldown")) {
			throw new CommandException(Text.builder().onClick(TextActions.executeCallback(help.execute())).append(help.getUsageText()).build(), false);
		}
		String time = args.<String>getOne("cooldown").get();
		
		if(!isValid(time)) {
			throw new CommandException(Text.builder().onClick(TextActions.executeCallback(help.execute())).append(help.getUsageText()).build(), false);
		}
		long cooldown = getTimeInSeconds(time);
		
		kit.setCooldown(cooldown);
		
		KitService kitService = Sponge.getServiceManager().provideUnchecked(KitService.class);
		
		kitService.save(kit);
		
		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set cooldown of kit ", kit.getName(), " to ", Resource.getReadableTime(cooldown)));
		
		return CommandResult.success();
	}
	
	public static long getTimeInSeconds(String time){
		if(time.equalsIgnoreCase("0")) {
			return 0;
		}
		
		long seconds = 0;
		
		for(String arg : time.split(",")){
			if(arg.matches("(\\d+)[s]$")){
				seconds = Integer.parseInt(arg.replace("s", "")) + seconds;
			}else if(arg.matches("(\\d+)[m]$")){
				seconds = (Integer.parseInt(arg.replace("m", "")) * 60) + seconds;
			}else if(arg.matches("(\\d+)[h]$")){
				seconds = (Integer.parseInt(arg.replace("h", "")) * 3600) + seconds;
			}else if(arg.matches("(\\d+)[d]$")){
				seconds = (Integer.parseInt(arg.replace("d", "")) * 86400) + seconds;
			}else if(arg.matches("(\\d+)[w]$")){
				seconds = (Integer.parseInt(arg.replace("w", "")) * 604800) + seconds;
			}
		}
		return seconds;
	}
	
	private static boolean isValid(String time){
		if(time.equalsIgnoreCase("0")) {
			return true;
		}
		
		int loop = 0;
		
		for(String arg : time.split(",")) {
			if(arg.matches("(\\d+)[w]$") && loop == 0) {
				
			}else if(arg.matches("(\\d+)[d]$") && (loop == 0 || loop == 1)) {
				
			}else if(arg.matches("(\\d+)[h]$") && (loop == 0 || loop == 1 || loop == 2)) {
				
			}else if(arg.matches("(\\d+)[m]$") && (loop == 0 || loop == 1 || loop == 2 || loop == 3)) {
				
			}else if(arg.matches("(\\d+)[s]$") && (loop == 0 || loop == 1 || loop == 2 || loop == 3 || loop == 4)) {
				
			}else{
				return false;
			}
			loop++;
		}
		return true;
	}
}