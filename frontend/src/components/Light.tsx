import { DeviceStatus, performDeviceAction } from "../api/SmartHomeAPI";
import { refreshDevices } from "./Simulation";

export function Light({ device }: { device: DeviceStatus }) {
  const { brightness, red, green, blue } = device.attributes;

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

  return (
    <div>
      <h3>{device.name} - {isOn ? 'On' : 'Off'}</h3>
      <p>Light</p>
      <p>Power: <button onClick={togglePower}>{isOn ? 'Turn Off' : 'Turn On'}</button></p>
      <p>Brightness: {brightness} 
      <input
        type="range"
        min={10}
        max={100}
        value={Number(brightness)}
        onChange={(event) => setBrightness(Number(event.target.value))}
        onMouseUp={(event) => setBrightness(Number(event.currentTarget.value))}
        onTouchEnd={(event) => setBrightness(Number(event.currentTarget.value))}
        disabled={!isOn}
      /></p>
      <p>Color: (R) {red}, (G) {green}, (B) {blue}
      <input
        type="number"
        min={0}
        max={255}
        value={Number(red)}
        onChange={(event) => setColor(Number(event.target.value), Number(blue), Number(green))}
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
        onChange={(event) => setColor(Number(red), Number(blue), Number(event.target.value))}
        disabled={!isOn}
        placeholder="B"
      />
      </p>
      <p>ID: {device.id}</p>
    </div>
  );
}