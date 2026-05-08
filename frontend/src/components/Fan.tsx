import { Button } from 'primereact/button';
import { Divider } from 'primereact/divider';

import { DeviceStatus, performDeviceAction } from "../api/SmartHomeAPI";
import { useRefresh } from './RefreshContext';

export function Fan({ device }: { device: DeviceStatus }) {
  const refreshDevices = useRefresh();

  const { speed } = device.attributes;
  const isOn = device.state === 'ON';

  const togglePower = async () => {
    await performDeviceAction(device.id, {action: 'TOGGLE_POWER', parameters: []});
    await refreshDevices();
  };

  const setSpeed = async (speed: string) => {
    await performDeviceAction(device.id, {action: `SET_SPEED_${speed}`, parameters: []});
    await refreshDevices();
  };

  return (
    <div className="flex flex-column gap-2">
      <Divider className="my-1" />

      <div className="flex align-items-center justify-content-between">
        <span className="font-semibold">Power</span>
        <Button
          label={isOn ? 'Turn Off' : 'Turn On'}
          icon={isOn ? 'pi pi-power-off' : 'pi pi-power-off'}
          severity={isOn ? 'danger' : 'success'}
          onClick={togglePower}
        />
      </div>

      <Divider className="my-1" />

      <div className="flex flex-column gap-2">
        <span className="font-semibold">Speed</span>
        <div className="flex gap-2">
          {['LOW', 'MEDIUM', 'HIGH'].map((level) => (
            <Button
              key={level}
              label={level.charAt(0) + level.slice(1).toLowerCase()}
              severity={speed.toString() === level ? undefined : 'secondary'}
              disabled={!isOn}
              onClick={() => setSpeed(level)}
              style={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                whiteSpace: 'nowrap'
              }}
            />
          ))}
        </div>
      </div>
    </div>
  );
}