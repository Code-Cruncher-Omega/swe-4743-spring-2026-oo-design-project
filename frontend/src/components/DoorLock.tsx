import { DeviceStatus, deleteDevice, performDeviceAction } from "../api/SmartHomeAPI";
import { useRefresh } from './RefreshContext';

export function DoorLock({ device }: { device: DeviceStatus }) {
  const refreshDevices = useRefresh();

  const isLocked = device.state === 'LOCKED';

  const handleClick = async () => {
    await performDeviceAction(device.id, {action: 'TOGGLE_LOCK', parameters: []});
    await refreshDevices();
  };

  return (
    <div>
      <button onClick={() => deleteDevice(device.id)} style={{ marginLeft: '10px' }}>
        X
      </button>
      <h3>{device.name} - {isLocked ? 'Locked' : 'Unlocked'}</h3>
      <p>Door Lock</p>

      <p>Lock
      <button onClick={handleClick}>
        {isLocked ? 'Unlock' : 'Lock'}
      </button>
      </p>
      <p>ID: {device.id}</p>
    </div>
  );
}