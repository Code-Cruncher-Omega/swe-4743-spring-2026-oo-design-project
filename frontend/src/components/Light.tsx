import { useEffect, useState } from "react";
import { deleteDevice, DeviceStatus, performDeviceAction } from "../api/SmartHomeAPI";
import { useRefresh } from './RefreshContext';

export function Light({ device }: { device: DeviceStatus }) {
  const refreshDevices = useRefresh();

  const { brightness, red, green, blue } = device.attributes;

  const [brightnessDisplay, setBrightnessDisplay] = useState(Number(brightness));

  const isOn = device.state === 'ON';
  
  const togglePower = async () => {
    await performDeviceAction(device.id, {action: 'TOGGLE_POWER', parameters: []});
    await refreshDevices();
  };

  const setBrightness = async (value: number) => {
    try {
      await performDeviceAction(device.id, {action: 'SET_BRIGHTNESS', parameters: [value]});
      await refreshDevices();
    } catch (error) {
      console.error(error);
    }
  };

  const setColor = async (red: number, green: number, blue: number) => {
    try {
      await performDeviceAction(device.id, {action: 'SET_COLOR', parameters: [red, green, blue]});
      await refreshDevices();
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    setBrightnessDisplay(Number(brightness));
  }, [brightness]);

  return (
    <div>
      <button onClick={async () => {
        await deleteDevice(device.id);
        await refreshDevices();
        }} style={{ marginLeft: '10px' }}>
        X
      </button>
      <h3>{device.name} - {isOn ? 'On' : 'Off'}</h3>
      <p>Light</p>
      <p>Power: <button onClick={togglePower}>{isOn ? 'Turn Off' : 'Turn On'}</button></p>
      <p>Brightness: {brightness + '%'} 
        <input
          type="range"
          min={10}
          max={100}
          value={brightnessDisplay}
          onChange={(event) => setBrightnessDisplay(Number(event.target.value))}
          onMouseUp={async (event) => await setBrightness(Number(event.currentTarget.value))}
          onTouchEnd={async (event) => await setBrightness(Number(event.currentTarget.value))}
          disabled={!isOn}
        />
      </p>
      <p>Color: (R) {red}, (G) {green}, (B) {blue}
      <input
        type="number"
        min={0}
        max={255}
        value={Number(red)}
        onChange={(event) => setColor(Number(event.target.value), Number(green), Number(blue))}
        disabled={!isOn}
        placeholder="R"
      />

      <input
        type="number"
        min={0}
        max={255}
        value={Number(green)}
        onChange={(event) => setColor(Number(red), Number(event.target.value), Number(blue))}
        disabled={!isOn}
        placeholder="G"
      />

      <input
        type="number"
        min={0}
        max={255}
        value={Number(blue)}
        onChange={(event) => setColor(Number(red), Number(green), Number(event.target.value))}
        disabled={!isOn}
        placeholder="B"
      />
      </p>
    </div>
  );
}