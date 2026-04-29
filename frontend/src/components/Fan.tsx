import { DeviceStatus, performDeviceAction } from "../api/SmartHomeAPI";
import { refreshDevices } from "./Simulation";

export function Fan({ device }: { device: DeviceStatus }) {
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
    <div>
      <h3>{device.name} - {isOn ? 'On' : 'Off'}</h3>
      <p>Fan</p>
      <p>Power: <button onClick={togglePower}>{isOn ? 'Turn Off' : 'Turn On'}</button></p>
      <p>Speed: {['LOW', 'MEDIUM', 'HIGH'].map((level) => (
          <button
            key={level}
            onClick={() => setSpeed(level)}
            disabled={!isOn}
            style={{
              fontWeight: speed === level ? 'bold' : 'normal',
              marginRight: '5px',
            }}
          >
            {level}
          </button>
        ))}</p>
      <p>ID: {device.id}</p>
    </div>
  );
}