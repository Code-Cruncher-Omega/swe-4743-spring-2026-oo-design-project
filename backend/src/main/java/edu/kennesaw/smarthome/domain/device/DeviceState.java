package edu.kennesaw.smarthome.domain.device;

public interface DeviceState<D extends Device<D, ?, A>, A extends Action> {
    ActionResult execute(D context, A action);
}
