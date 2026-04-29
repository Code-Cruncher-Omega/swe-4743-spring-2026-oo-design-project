import { DeviceStatus, performDeviceAction } from "../api/SmartHomeAPI";
import { refreshDevices } from "./Simulation";

export function DoorLock({ device }: { device: DeviceStatus }) {
  const isLocked = device.state === 'LOCKED';

  const handleClick = async () => {
    await performDeviceAction(device.id, {action: 'TOGGLE_LOCK', parameters: []});
    await refreshDevices();
  };

  return (
    <div>
      <h3>{device.name} - {isLocked ? 'Locked' : 'Unlocked'}</h3>
      <p>Door Lock</p>

      <p>Lock<button onClick={handleClick}>{isLocked ? 'Unlock' : 'Lock'}</button></p>
      <p>ID: {device.id}</p>
    </div>
  );
}