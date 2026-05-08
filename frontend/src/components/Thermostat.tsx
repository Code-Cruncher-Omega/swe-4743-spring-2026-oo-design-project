import { useEffect, useState } from "react";

import { Button } from 'primereact/button';
import { Divider } from 'primereact/divider';
import { Slider, SliderChangeEvent, SliderSlideEndEvent } from 'primereact/slider';

import { DeviceStatus, performDeviceAction } from "../api/SmartHomeAPI";
import { useRefresh } from './RefreshContext';

export function Thermostat({ device }: { device: DeviceStatus }) {
  
  const refreshDevices = useRefresh();
  
  const { mode, desired, ambient } = device.attributes;

  const [desiredDisplay, setDesiredDisplay] = useState(Number(desired));

  const isNotOff = device.state !== 'OFF';
    
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

  useEffect(() => {
    setDesiredDisplay(Number(desired));
  }, [desired]);

  return (
    <div className="flex flex-column gap-2">
      <Divider className="my-1" />

      {/* Power */}
      <div className="flex align-items-center justify-content-between">
        <span className="font-semibold">Power</span>
        <Button
          label={isNotOff ? 'Turn Off' : 'Turn On' }
          icon="pi pi-power-off"
          severity={isNotOff ? 'danger' : 'success' }
          onClick={togglePower}
        />
      </div>

      <Divider className="my-1" />
      <div className="flex flex-column gap-2">
        <div className="flex align-items-center justify-content-between">
          <span className="font-semibold">Target Temperature</span>
          <span className="text-color-secondary">{desiredDisplay}°F</span>
        </div>
        <Slider
          min={60}
          max={80}
          value={desiredDisplay}
          onChange={(event: SliderChangeEvent) => setDesiredDisplay(Number(event.value))}
          onSlideEnd={async (event: SliderSlideEndEvent) => await setDesired(Number(event.value))}
          disabled={!isNotOff}
        />
        <div className="flex align-items-center justify-content-between">
          <span className="font-semibold">Ambient Temperature</span>
          <span className="text-color-secondary">{ambient.toString()}°F</span>
        </div>
      </div>

      <Divider className="my-1" />
      <div className="flex flex-column gap-2">
        <span className="font-semibold">Mode</span>
        <div className="flex gap-2">
          {['HEAT', 'COOL', 'AUTO'].map((technique) => (
            <Button
              key={technique}
              label={technique.charAt(0) + technique.slice(1).toLowerCase()}
              icon={
                technique === 'HEAT' ? 'pi pi-sun' :
                technique === 'COOL' ? 'pi pi-cloud' :
                'pi pi-refresh'
              }
              severity={mode.toString() === technique ? undefined : 'secondary'}
              disabled={!isNotOff}
              onClick={() => setMode(technique)}
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