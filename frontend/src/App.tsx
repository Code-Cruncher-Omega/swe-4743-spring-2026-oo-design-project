import { useEffect, useRef, useState } from 'react';
import './App.css';

import { Card } from 'primereact/card';
import { ConfirmDialog } from 'primereact/confirmdialog';
import { DeviceHistory } from './components/DeviceHistory';
import { Divider } from 'primereact/divider';
import { Dropdown } from 'primereact/dropdown';

import { DeviceFilterRequest, DeviceType, getEnvironmentNames, StateActivity } from './api/SmartHomeAPI';
import { Simulation } from './components/Simulation';
import { SimulationSettings } from './components/SimulationSettings';


function App() {
  const [timeMultiplier, setTimeMultiplier] = useState(1);
  const [filter, setFilter] = useState<DeviceFilterRequest>({location: null, activity: null, deviceType: null})
  const refreshRef = useRef<() => Promise<void>>(async () => {});

  type Option = {
    label: string;
    value: string | null;
  };
  const [locationOptions, setLocationOptions] = useState<Option[]>([
    { label: 'All', value: null }
  ]);
  
  useEffect(() => {
      getEnvironmentNames().then(names => {
          setLocationOptions([
              { label: 'All', value: null },
              ...names.map(name => ({ label: name, value: name }))
          ]);
      });
  }, [filter]);

  return (
    <>
      <ConfirmDialog />
      <SimulationSettings
        timeMultiplier={timeMultiplier}
        setTimeMultiplier={setTimeMultiplier}
        onDeviceChanged={() => refreshRef.current()}
      />
      <div style={{ marginTop: '180px' }} className="p-3">
        <Card title="Filter Devices" className="mb-3">
          <div className="flex flex-row gap-3 align-items-center flex-wrap">
            <div className="flex align-items-center gap-2" style={{ flex: 1, minWidth: '150px' }}>
              <label className="font-semibold" style={{ minWidth: '60px' }}>Status</label>
              <Dropdown
                value={filter.activity}
                onChange={(event) => setFilter({ ...filter, activity: event.value as StateActivity })}
                options={[
                  { label: 'All', value: null },
                  { label: 'On', value: 'ON' },
                  { label: 'Off', value: 'OFF' }
                ]}
                optionLabel="label"
                optionValue="value"
                style={{ width: '100%' }}
              />
            </div>

            <Divider layout="vertical" className="mx-1 hidden md:flex" style={{ height: '40px' }}/>

            <div className="flex align-items-center gap-2" style={{ flex: 1, minWidth: '150px' }}>
              <label className="font-semibold" style={{ minWidth: '70px' }}>Location</label>
              <Dropdown
                value={filter.location}
                onChange={(event) => setFilter({ ...filter, location: event.value })}
                options={locationOptions}
                optionLabel="label"
                optionValue="value"
                style={{ width: '100%' }}
              />
            </div>

            <Divider layout="vertical" className="mx-1 hidden md:flex" style={{ height: '40px' }}/>

            <div className="flex align-items-center gap-2" style={{ flex: 1, minWidth: '150px' }}>
              <label className="font-semibold" style={{ minWidth: '90px' }}>Device Type</label>
              <Dropdown
                value={filter.deviceType}
                onChange={(event) => setFilter({ ...filter, deviceType: event.value as DeviceType })}
                options={[
                  { label: 'All', value: null },
                  { label: 'Light', value: 'LIGHT' },
                  { label: 'Fan', value: 'FAN' },
                  { label: 'Thermostat', value: 'THERMOSTAT' },
                  { label: 'Door Lock', value: 'DOOR_LOCK' }
                ]}
                optionLabel="label"
                optionValue="value"
                style={{ width: '100%' }}
              />
            </div>
          </div>
        </Card>
        <Simulation
          timeMultiplier={timeMultiplier}
          filter={filter}
          onRefreshReady={(fn) => { refreshRef.current = fn; }}
        />
        <DeviceHistory />
      </div>
    </>
  )
}

export default App
