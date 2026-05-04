import { DeviceStatus } from "../api/SmartHomeAPI";
import { Light } from "./Light";
import { Fan } from "./Fan";
import { Thermostat } from "./Thermostat";
import { DoorLock } from "./DoorLock";

const deviceComponents: Record<string, React.FC<{ device: DeviceStatus }>> = {
    LIGHT: Light,
    FAN: Fan,
    THERMOSTAT: Thermostat,
    DOOR_LOCK: DoorLock
};

export function DeviceRenderer({ device }: { device: DeviceStatus }) {
  const Component = deviceComponents[device.type];

  if (!Component) {
    return <div>Unknown device type: {device.type}</div>;
  }

  return (
  <div>
    <Component device={device} />
  </div>
  );
}