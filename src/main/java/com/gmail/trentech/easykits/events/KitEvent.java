package com.gmail.trentech.easykits.events;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.impl.AbstractEvent;

import com.gmail.trentech.easykits.kit.Kit;
import com.gmail.trentech.easykits.kit.KitService;

public class KitEvent extends AbstractEvent implements Cancellable {

	protected Kit kit;
	protected KitService kitService;
	protected Cause cause;
	protected boolean cancelled = false;

	public KitEvent(Kit kit, Cause cause) {
		this.kit = kit;
		this.cause = cause;
		this.kitService = Sponge.getServiceManager().provideUnchecked(KitService.class);
	}

	public Kit getKit() {
		return kit;
	}

	public KitService getKitService() {
		return kitService;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Override
	public Cause getCause() {
		return cause;
	}

	public static class Create extends KitEvent {

		public Create(Kit kit, Cause cause) {
			super(kit, cause);
		}		
	}
	
	public static class Delete extends KitEvent {
		
		public Delete(Kit kit, Cause cause) {
			super(kit, cause);
		}
	}
	
	public static class View extends KitEvent {
		
		public View(Kit kit, Cause cause) {
			super(kit, cause);
		}	
	}
	
	public static class Get extends KitEvent {
		
		public Get(Kit kit, Cause cause) {
			super(kit, cause);
		}
	}
	
	public static class Give extends KitEvent {
		
		public Give(Kit kit, Cause cause) {
			super(kit, cause);
		}
	}
}
