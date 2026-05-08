import { DeviceStatus, DeviceType } from "../api/SmartHomeAPI";
import { Light } from "./Light";
import { Fan } from "./Fan";
import { Thermostat } from "./Thermostat";
import { DoorLock } from "./DoorLock";

type DeviceRendererProps = {
  device: DeviceStatus;
};

const deviceComponents: Partial<Record<DeviceType, React.ComponentType<DeviceRendererProps>>> = {
  LIGHT: Light,
  FAN: Fan,
  THERMOSTAT: Thermostat,
  DOOR_LOCK: DoorLock,
};

export function DeviceRenderer({ device }: DeviceRendererProps) {
  const Component = deviceComponents[device.deviceType];

  if (!Component) {
    return <div>Unknown device type: {device.deviceType.toString()}</div>;
  }

  return <Component device={device}/>;
}