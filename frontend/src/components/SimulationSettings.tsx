import { useState, useEffect } from 'react';
import { createDevice, DeviceStatus, DeviceFilterRequest, getUpdateableDevices, resetAllDevices, getEnvironmentNames, setAmbientTemperature } from '../api/SmartHomeAPI';

type Parameters = {
  timeMultiplier: number;
  setTimeMultiplier: (parameter: number) => void;
  onDeviceChanged: () => void;
  filter: DeviceFilterRequest;
  setFilter: (filter: DeviceFilterRequest) => void;
}

export function SimulationSettings({ timeMultiplier, setTimeMultiplier, onDeviceChanged, filter, setFilter }: Parameters) {
  
  const [showControls, setShowControls] = useState(false);
  const [currentTime, setCurrentTime] = useState(new Date());

  const [creationMenu, setCreationMenu] = useState(false);
  const [deviceType, setDeviceType] = useState('LIGHT');
  const [name, setName] = useState('');
  const [location, setLocation] = useState('');

  const [ambientValues, setAmbientValues] = useState<Record<string, number>>({});
  const [updatableDeviceStatuses, setUpdatableDeviceStatuses] = useState<DeviceStatus[]>([]);
  const [environmentNames, setEnvironmentNames] = useState<string[]>([]);

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
    getEnvironmentNames().then(setEnvironmentNames);
    return () => clearInterval(interval);
  }, []);

  return (
    <div style={{ border: '1px solid #ccc', padding: '12px', borderRadius: '8px' }}>
      <div>
        <h3>Simulation Settings</h3>
      </div>

      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <button onClick={() => setCreationMenu(prev => !prev)}>
          {creationMenu ? 'Cancel' : 'Create Device'}
        </button>

        {creationMenu && (
          <div style={{ marginTop: '10px', padding: '12px', border: '1px solid #ccc', borderRadius: '8px', background: '#f9f9f9', maxWidth: '300px' }}>
            <div style={{ marginBottom: '10px' }}>
              <label>Device Type:</label>
              <select value={deviceType} onChange={(event) => setDeviceType(event.target.value)} style={{ width: '100%' }}>
                <option value="LIGHT">Light</option>
                <option value="FAN">Fan</option>
                <option value="THERMOSTAT">Thermostat</option>
                <option value="DOOR_LOCK">Door Lock</option>
              </select>
            </div>
            <div style={{ marginBottom: '10px' }}>
              <label>Name:</label>
              <input type="text" value={name} onChange={(event) => setName(event.target.value)} placeholder="ex. Living Room Light" style={{ width: '100%' }} />
            </div>
            <div style={{ marginBottom: '10px' }}>
              <label>Location:</label>
              <input type="text" value={location} onChange={(event) => setLocation(event.target.value)} placeholder="ex. Living Room" style={{ width: '100%' }} />
            </div>
            <button style={{ width: '100%' }} onClick={async () => {
              await createDevice({ name, location, deviceType });
              await fetchDevices();
              onDeviceChanged();
            }}>
              Create
            </button>
          </div>
        )}

        <div><strong>Time:</strong> {currentTime.toLocaleTimeString()}</div>
        <div><strong>Speed:</strong> {timeMultiplier}x</div>

        <button onClick={() => setShowControls(prev => !prev)}>
          {showControls ? 'Hide Controls' : 'Show Controls'}
        </button>
      </div>

      {showControls && (
        <div style={{ marginTop: '12px', padding: '10px', background: '#f9f9f9', borderRadius: '6px' }}>
          <div style={{ marginBottom: '10px' }}>
            <label>Ambient Temperature by Environment:</label>
            {updatableDeviceStatuses.map((status) => (
              status.type === 'THERMOSTAT' ?
                <div key={status.id} style={{ marginBottom: '8px' }}>
                  <strong>{status.location}</strong>
                  <input
                    type="range"
                    min={0}
                    max={140}
                    value={ambientValues[status.id] ?? Number(status.attributes["ambient"])}
                    onChange={async (event) => {
                      setAmbientValues(prev => ({ ...prev, [status.id]: Number(event.target.value) }));
                    }}
                    onMouseUp={async (event) => {
                      await setAmbientTemperature(status.location, Number(event.currentTarget.value));
                      onDeviceChanged();
                    }}
                    onTouchEnd={async (event) => {
                      await setAmbientTemperature(status.location, Number(event.currentTarget.value));
                      onDeviceChanged();
                    }}
                    style={{ width: '100%' }}
                  />
                </div>
                
                : <div>{/* If device type not recognized, do nothing */}</div>
            ))}
          </div>

          <div>
            <label>Simulation Speed</label>
            <div style={{ display: 'flex', gap: '8px', marginTop: '5px' }}>
              Thermostat tick rate
              <button onClick={() => setTimeMultiplier(1)}>1x</button>
              <button onClick={() => setTimeMultiplier(2)}>2x</button>
              <button onClick={() => setTimeMultiplier(5)}>5x</button>
              <button onClick={() => setTimeMultiplier(10)}>10x</button>
            </div>
          </div>

          <div style={{ marginTop: '10px' }}>
            <button onClick={async () => { await resetAllDevices(); onDeviceChanged(); }}>
              Reset All Devices to Defaults
            </button>
          </div>
        </div>
      )}

      <div style={{ marginTop: '12px', padding: '10px', background: '#f9f9f9', borderRadius: '6px' }}>
        <h4 style={{ margin: '0 0 8px 0' }}>Filter Devices</h4>
        <div style={{ marginBottom: '8px' }}>
          <label>Status: </label>
          <select value={filter.activity} onChange={(event) => setFilter({ ...filter, activity: event.target.value })}>
            <option value="">All</option>
            <option value="ON">On</option>
            <option value="OFF">Off</option>
          </select>
        </div>
        <div style={{ marginBottom: '8px' }}>
          <label>Location: </label>
          <select value={filter.location} onChange={(event) => setFilter({ ...filter, location: event.target.value })}>
            <option value="">All</option>
            {environmentNames.map((name) => (
              <option key={name} value={name}>{name}</option>
            ))}
          </select>
        </div>
        <div style={{ marginBottom: '8px' }}>
          <label>Device Type: </label>
          <select value={filter.type} onChange={(event) => setFilter({ ...filter, type: event.target.value })}>
            <option value="">All</option>
            <option value="LIGHT">Light</option>
            <option value="FAN">Fan</option>
            <option value="THERMOSTAT">Thermostat</option>
            <option value="DOOR_LOCK">Door Lock</option>
          </select>
        </div>
      </div>
    </div>
  );
}