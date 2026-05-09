import { Button } from 'primereact/button';
import { Divider } from 'primereact/divider';

import { DeviceStatus, performDeviceAction } from "../api/SmartHomeAPI";
import { useRefresh } from './RefreshContext';

export function DoorLock({ device }: { device: DeviceStatus }) {
  const refreshDevices = useRefresh();

  const isLocked = device.state === 'LOCKED';

  const handleClick = async () => {
    await performDeviceAction(device.id, {action: 'TOGGLE_LOCK', parameters: []});
    await refreshDevices();
  };

  return (
    <div className="flex flex-column gap-2">
      <Divider className="my-1" />
      <div className="flex align-items-center justify-content-between">
        <span className="font-semibold">
          Lock
        </span>
        <Button
          label={isLocked ? 'Unlock' : 'Lock'}
          icon={isLocked ? 'pi pi-lock-open' : 'pi pi-lock'}
          severity={isLocked ? 'warning' : 'success'}
          onClick={handleClick}
        />
      </div>
    </div>
  );
}