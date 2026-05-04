import { deleteDevice, DeviceStatus, performDeviceAction } from "../api/SmartHomeAPI";
import { useRefresh } from './RefreshContext';

export function Thermostat({ device }: { device: DeviceStatus }) {
  const refreshDevices = useRefresh();
  
  const { mode, desired, ambient } = device.attributes;
    
  const togglePower = async () => {
        await performDeviceAction(device.id, {action: 'TOGGLE_POWER', parameters: []});
        await refreshDevices();
    };

  const setDesired = async (value: number) => {
    try {
      await performDeviceAction(device.id, {action: 'SET_DESIRED', parameters: [value]});
      await refreshDevices();
    } catch (error) {
      console.error(error);
    }
  };

  const setMode = async (mode: string) => {
    await performDeviceAction(device.id, {action: `SET_MODE_${mode}`, parameters: []});
    await refreshDevices();
  };

  return (
    <div>
      <button onClick={() => deleteDevice(device.id)} style={{ marginLeft: '10px' }}>
        X
      </button>
      <h3>{device.name} - { device.state === 'OFF' ? 'Off' 
                            : device.state === 'IDLE' ? 'Idle'
                            : device.state === 'HEATING' ? 'Heating' 
                            : device.state === 'COOLING' ? 'Cooling' 
                            : device.state}</h3>
      <p>Thermostat</p>
      <p>Power: <button onClick={togglePower}>{device.state !== 'OFF' ? 'Turn off' : 'Turn on'}</button></p>
      <p>Target Temperature: {desired} Farenheit <input
        type="range"
        min={60}
        max={80}
        value={Number(ambient)}
        onChange={(event) => setDesired(Number(event.target.value))}
        onMouseUp={(event) => setDesired(Number(event.currentTarget.value))}
        onTouchEnd={(event) => setDesired(Number(event.currentTarget.value))}
        disabled={device.state === 'OFF'}
      /> Farenheit</p>
      <p>Ambient Temperature: {ambient} Farenheit</p>
      <p>Mode: {mode}{['HEAT', 'COOL', 'AUTO'].map((technique) => (
          <button
            key={technique}
            onClick={() => setMode(technique)}
            disabled={device.state === 'OFF'}
            style={{
              fontWeight: mode === technique ? 'bold' : 'normal',
              marginRight: '5px',
            }}
          >
            {technique}
          </button>
        ))}</p>
      <p>ID: {device.id}</p>
    </div>
  );
}