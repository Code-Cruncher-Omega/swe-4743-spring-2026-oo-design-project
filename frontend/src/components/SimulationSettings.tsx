import { useState, useEffect } from 'react';

import { Button } from 'primereact/button';
import { Card } from 'primereact/card';
import { Dialog } from 'primereact/dialog';
import { Divider } from 'primereact/divider';
import { InputText } from 'primereact/inputtext';
import { Slider, SliderChangeEvent, SliderSlideEndEvent } from 'primereact/slider';
import { Toolbar } from 'primereact/toolbar';
import { Dropdown } from 'primereact/dropdown';

import { createDevice, DeviceStatus, DeviceType, getUpdateableDevices, resetAllDevices, setAmbientTemperature } from '../api/SmartHomeAPI';


type Parameters = {
  timeMultiplier: number;
  setTimeMultiplier: (parameter: number) => void;
  onDeviceChanged: () => void;
}

export function SimulationSettings({ timeMultiplier, setTimeMultiplier, onDeviceChanged}: Parameters) {
  
  const [showControls, setShowControls] = useState(false);
  const [currentTime, setCurrentTime] = useState(new Date());

  const [creationMenu, setCreationMenu] = useState(false);
  const [deviceType, setDeviceType] = useState<DeviceType>('LIGHT');
  const [name, setName] = useState('');
  const [location, setLocation] = useState('');
  const trimmedName = name.trim();
  const trimmedLocation = location.trim();

  const [ambientValues, setAmbientValues] = useState<Record<string, number>>({});
  const [updatableDeviceStatuses, setUpdatableDeviceStatuses] = useState<DeviceStatus[]>([]);

  const deviceTypeOptions: { label: string; value: DeviceType }[] = [
    { label: 'Light', value: 'LIGHT' },
    { label: 'Fan', value: 'FAN' },
    { label: 'Thermostat', value: 'THERMOSTAT' },
    { label: 'Door Lock', value: 'DOOR_LOCK' }
  ];

  const speedOptions = [1, 2, 5, 10];

  const thermostatExists = updatableDeviceStatuses.some(
    status =>
      status.deviceType === ('THERMOSTAT' as DeviceType) &&
      status.location.toLowerCase() === trimmedLocation.toLowerCase()
  );

  const invalidCreation =
    trimmedName.length === 0 ||
    trimmedLocation.length === 0 ||
    (deviceType === ('THERMOSTAT' as DeviceType) && thermostatExists);

  const fetchDevices = async () => {
    const statuses = await getUpdateableDevices();
    const initial: Record<string, number> = {};
    statuses.forEach(status => {
      initial[status.id] = Number(status.attributes["ambient"]);
    });
    setUpdatableDeviceStatuses(statuses);
    setAmbientValues(initial);
  };

  useEffect(() => {
    const interval = setInterval(() => setCurrentTime(new Date()), 1000);
    fetchDevices();
    return () => clearInterval(interval);
  }, []);

  return (
    <div>
      <div style={{ position: 'fixed', top: 0, left: 0, right: 0, zIndex: 1000 }}>
        <Card className="mb-0" style={{ borderRadius: 0 }}>
          <Toolbar
            start={
              <div className="flex align-items-center gap-2">
                <span style={{ fontSize: '2rem', fontWeight: 'bold' }} className="mr-3 hidden md:inline">Smart Home</span>
                <span style={{ fontSize: '2rem', fontWeight: 'bold' }} className="mr-3 md:hidden">SH</span>
                <Button
                  className="p-button-success"
                  icon="pi pi-plus"
                  severity={creationMenu ? 'danger' : 'success'}
                  onClick={() => {setCreationMenu(prev => !prev);}}
                >
                  <span className="hidden md:inline ml-2">{creationMenu ? 'Cancel' : 'Create Device'}</span>
                </Button>
              </div>
            }
            end={
              <div className="flex align-items-center gap-3">
                <span className="hidden md:inline"><strong>Time:</strong> {currentTime.toLocaleTimeString()}</span>
                <span className="md:inline"><strong>Speed:</strong> {timeMultiplier}x</span>
                <Button
                  icon={showControls ? 'pi pi-times' : 'pi pi-cog'}
                  severity="secondary"
                  onClick={() => {setShowControls(prev => !prev);}}
                >
                  <span className="hidden md:inline ml-2">{showControls ? 'Hide Controls' : 'Show Controls'}</span>
                </Button>
              </div>
            }
          />
        </Card>
      </div>

      <Dialog
        header="Simulation Controls"
        visible={showControls}
        style={{ width: 'min(500px, 90vw)' }} 
        onHide={() => setShowControls(false)}>
        <div className="flex flex-column gap-4">
          <div className="flex flex-column gap-2">
            <span className="font-semibold text-sm">Ambient Temperature</span>
            {updatableDeviceStatuses.length === 0 && (
              <span className="text-color-secondary text-sm">No thermostats available.</span>
            )}
            {updatableDeviceStatuses.map((status) => (
              <div key={status.id} className="flex flex-column gap-1">
                <div className="flex align-items-center justify-content-between">
                  <span className="text-sm">{status.location}</span>
                  <span className="text-color-secondary text-sm">{ambientValues[status.id]}°F</span>
                </div>
                <Slider
                  style={{ margin: '8px 0' }}
                  min={0}
                  max={140}
                  value={ambientValues[status.id] ?? Number(status.attributes["ambient"])}
                  onChange={(event: SliderChangeEvent) => {
                    setAmbientValues(prev => ({ ...prev, [status.id]: Number(event.value) }));
                  }}
                  onSlideEnd={async (event: SliderSlideEndEvent) => {
                    await setAmbientTemperature(status.location, Number(event.value));
                    onDeviceChanged();
                  }}
                />
              </div>
            ))}
          </div>

          <Divider className="my-1" />
          <div className="flex flex-column gap-2">
            <span className="font-semibold text-sm">Simulation Speed</span>
            <div className="flex gap-2">
              {speedOptions.map((speed) => (
                <Button
                  key={speed}
                  label={`${speed}x`}
                  severity={timeMultiplier === speed ? undefined : 'secondary'}
                  onClick={() => setTimeMultiplier(speed)}
                  style={{ flex: 1 }}
                />
              ))}
            </div>
          </div>

          <Divider className="my-1" />
          <div className="flex align-items-center justify-content-between">
            <span className="font-semibold text-sm">Reset all devices to defaults</span>
            <Button
              label="Reset All"
              icon="pi pi-refresh"
              severity="warning"
              onClick={async () => { await resetAllDevices(); onDeviceChanged(); }}
            />
          </div>
        </div>
      </Dialog>

      <Dialog
        header="Create Device"
        visible={creationMenu} 
        style={{ width: 'min(400px, 90vw)' }} 
        onHide={() => setCreationMenu(false)}>
        <div className="flex flex-column gap-3">
          <div className="flex flex-column gap-1">
            <label className="font-semibold text-sm">Device Type</label>
            <Dropdown
              value={deviceType.toString()}
              onChange={(event) => setDeviceType(event.value as DeviceType)}
              options={deviceTypeOptions}
              optionLabel="label"
              style={{ width: '100%' }}
            />
          </div>
          <div className="flex flex-column gap-1">
            <label className="font-semibold text-sm">Name</label>
            <InputText
              value={name}
              onChange={(event) => setName(event.target.value)}
              placeholder="ex. Living Room Light"
            />
            {trimmedName.length === 0 && (
              <small className="p-error">Name is required.</small>
            )}
          </div>
          <div className="flex flex-column gap-1">
            <label className="font-semibold text-sm">Location</label>
            <InputText
              value={location}
              onChange={(event) => setLocation(event.target.value)}
              placeholder="ex. Living Room"
            />
            {trimmedLocation.length === 0 && (
              <small className="p-error">Location is required.</small>
            )}

            {deviceType === ('THERMOSTAT' as DeviceType) && thermostatExists && (
              <small className="p-error">
                A thermostat already exists in this environment.
              </small>
            )}
          </div>
          <Button
            label="Create"
            disabled={invalidCreation}
            icon="pi pi-check"
            onClick={async () => {
              await createDevice({ name, location, deviceType });
              await fetchDevices();
              onDeviceChanged();
              setCreationMenu(false);
            }}
          />
        </div>
      </Dialog>
    </div>
  );
}