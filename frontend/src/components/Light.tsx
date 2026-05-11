import { useEffect, useState } from "react";

import { Button } from 'primereact/button';
import { Divider } from 'primereact/divider';
import { InputNumber, InputNumberValueChangeEvent } from 'primereact/inputnumber';
import { Slider, SliderChangeEvent, SliderSlideEndEvent } from 'primereact/slider';

import { DeviceStatus, performDeviceAction } from "../api/SmartHomeAPI";
import { useRefresh } from './RefreshContext';

export function Light({ device }: { device: DeviceStatus }) {
  const refreshDevices = useRefresh();

  const { brightness, red, green, blue } = device.attributes;

  const [brightnessDisplay, setBrightnessDisplay] = useState(Number(brightness));
  const [redDisplay, setRedDisplay] = useState(Number(red));
  const [greenDisplay, setGreenDisplay] = useState(Number(green));
  const [blueDisplay, setBlueDisplay] = useState(Number(blue));
  const colorPreview = `rgb(${redDisplay}, ${greenDisplay}, ${blueDisplay})`;

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
    setRedDisplay(Number(red));
    setGreenDisplay(Number(green));
    setBlueDisplay(Number(blue));
  }, [brightness, red, green, blue]);

  return (
    <div className="flex flex-column gap-2">
      <Divider className="my-1" />
      <div className="flex align-items-center justify-content-between">
        <span className="font-semibold">Power</span>
        <Button
          label={isOn ? 'Turn Off' : 'Turn On'}
          icon="pi pi-power-off"
          severity={isOn ? 'danger' : 'success'}
          onClick={togglePower}
        />
      </div>

      <Divider className="my-1" />
      <div className="flex flex-column gap-2">
        <div className="flex align-items-center justify-content-between">
          <span className="font-semibold">Brightness</span>
          <span className="text-color-secondary">{brightness.toString()}%</span>
        </div>
        <Slider
          min={10}
          max={100}
          value={brightnessDisplay}
          onChange={(event: SliderChangeEvent) => setBrightnessDisplay(Number(event.value))}
          onSlideEnd={async (event: SliderSlideEndEvent) => await setBrightness(Number(event.value))}
          disabled={!isOn}
        />
      </div>

      <Divider className="my-1" />
      <div className="flex flex-column gap-2">
        <div className="flex align-items-center justify-content-between">
          <span className="font-semibold">Color</span>
          <div style={{
            width: '24px',
            height: '24px',
            borderRadius: '50%',
            backgroundColor: isOn ? colorPreview : '#6b7280',
            border: '1px solid #ccc'
          }} data-testid="color-preview" />
        </div>
        <div className="flex gap-2">
          {[
            { label: 'R', value: redDisplay, setter: setRedDisplay },
            { label: 'G', value: greenDisplay, setter: setGreenDisplay },
            { label: 'B', value: blueDisplay, setter: setBlueDisplay }
          ].map(({ label, value, setter }) => (
            <div key={label} className="flex flex-column align-items-center gap-1" style={{ flex: 1 }}>
              <span className="text-sm text-color-secondary">{label}</span>
              <InputNumber
                min={0}
                max={255}
                value={value}
                onValueChange={(event: InputNumberValueChangeEvent) => setter(Number(event.value))}
                onBlur={async () => await setColor(redDisplay, greenDisplay, blueDisplay)}
                disabled={!isOn}
                inputStyle={{ width: '100%' }}
              />
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}