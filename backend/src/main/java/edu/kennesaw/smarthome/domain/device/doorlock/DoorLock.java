package edu.kennesaw.smarthome.domain.device.doorlock;

import edu.kennesaw.smarthome.domain.device.Action;
import edu.kennesaw.smarthome.domain.device.ActionResult;
import edu.kennesaw.smarthome.domain.device.Device;
import edu.kennesaw.smarthome.domain.device.DeviceState;
import edu.kennesaw.smarthome.domain.device.DeviceType;

public class DoorLock extends Device<DoorLock, DoorLockState, DoorLockAction> {
    public DoorLock(String name, String location, DoorLockState initialState) {
        super(name, location, initialState);
    }

    @Override
    protected void setState(DoorLockState state) {
        this.state = state;
    }

    @Override
    public ActionResult execute(DoorLockAction action) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }

    @Override
    public DeviceType getType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getType'");
    }

    @Override
    public ActionResult reset() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reset'");
    }

    
}
