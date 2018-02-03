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
		
		if (args.hasAny("help")) {			
			help.execute(src);
			return CommandResult.empty();
		}

		if (!args.hasAny("kit")) {
			throw new CommandException(Text.builder().onClick(TextActions.executeCallback(help.execute())).append(help.getUsageText()).build(), false);
		}
		Kit kit = args.<Kit>getOne("kit").get();
		
		if (!args.hasAny("cooldown")) {
			throw new CommandException(Text.builder().onClick(TextActions.executeCallback(help.execute())).append(help.getUsageText()).build(), false);
		}
		String coold = args.<String>getOne("cooldown").get();
		
		if(!isValid(coold.split(","))) {
			throw new CommandException(Text.builder().onClick(TextActions.executeCallback(help.execute())).append(help.getUsageText()).build(), false);
		}
		long cooldown = getTimeInSeconds(coold);
		
		kit.setCooldown(cooldown);
		
		KitService kitService = Sponge.getServiceManager().provideUnchecked(KitService.class);
		
		kitService.save(kit);
		
		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set cooldown of kit ", kit.getName(), " to ", Resource.getReadableTime(cooldown)));
		
		return CommandResult.success();
	}
	
	public static long getTimeInSeconds(String time){
		String[] times = time.split(" ");
		long seconds = 0;
		for(String t : times){
			if(t.matches("(\\d+)[s]$")){
				seconds = Integer.parseInt(t.replace("s", "")) + seconds;
			}else if(t.matches("(\\d+)[m]$")){
				seconds = (Integer.parseInt(t.replace("m", "")) * 60) + seconds;
			}else if(t.matches("(\\d+)[h]$")){
				seconds = (Integer.parseInt(t.replace("h", "")) * 3600) + seconds;
			}else if(t.matches("(\\d+)[d]$")){
				seconds = (Integer.parseInt(t.replace("d", "")) * 86400) + seconds;
			}else if(t.matches("(\\d+)[w]$")){
				seconds = (Integer.parseInt(t.replace("w", "")) * 604800) + seconds;
			}
		}
		return seconds;
	}
	
	private static boolean isValid(String[] args){
		int loop = 0;	
		for(String arg : args) {
			if(arg.matches("(\\d+)[w]$") && loop == 0) {
				
			}else if(arg.matches("(\\d+)[d]$") && (loop == 0 || loop == 1)) {
				
			}else if(arg.matches("(\\d+)[h]$") && (loop == 0 || loop == 1 || loop == 2)) {
				
			}else if(arg.matches("(\\d+)[m]$") && (loop == 0 || loop == 1 || loop == 2 || loop == 3)) {
				
			}else if(arg.matches("(\\d+)[s]$") && (loop == 0 || loop == 1 || loop == 2 || loop == 3 || loop == 4)) {
				
			}else if(arg.equalsIgnoreCase("0") && args.length == 1) {
				return true;
			}else{
				return false;
			}
			loop++;
		}
		return true;
	}
}