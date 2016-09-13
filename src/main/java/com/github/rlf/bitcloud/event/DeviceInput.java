package com.github.rlf.bitcloud.event;

import com.github.rlf.bitcloud.model.Device;
import org.bukkit.event.HandlerList;

/**
 * An event fired when one of the registered devices receives input (from redstone).
 */
public class DeviceInput extends DeviceEvent {
    private static final HandlerList handlers = new HandlerList();

    public DeviceInput(Device device) {
        super(device);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
