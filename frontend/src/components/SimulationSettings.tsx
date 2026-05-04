import { useState, useEffect } from 'react';
import { createDevice, DeviceStatus, getUpdateableDevices, performDeviceAction } from '../api/SmartHomeAPI';
import { useRefresh } from './RefreshContext';

type Multipliers = {
  timeMultiplier: number,
  setTimeMultiplier: (parameter: number) => void;
}

export function SimulationSettings({
  timeMultiplier,
  setTimeMultiplier
}: Multipliers) {
  const refreshDevices = useRefresh();

  const [showControls, setShowControls] = useState(false);
  const [currentTime, setCurrentTime] = useState(new Date());
  const [deviceStatuses, setDeviceStatuses] = useState<DeviceStatus[]>([]);
  
  const [creationMenu, setCreationMenu] = useState(false);
  const [deviceType, setDeviceType] = useState('LIGHT');
  const [name, setName] = useState('');
  const [location, setLocation] = useState('');

  const fetchDevices = async () => {
        const statuses = await getUpdateableDevices();
        setDeviceStatuses(statuses);
  };
  
  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentTime(new Date());
    }, 1000);
    fetchDevices();

    return () => clearInterval(interval);
  }, []);

  return (
    <div style={{ border: '1px solid #ccc', padding: '12px', borderRadius: '8px' }}>
      
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        
        <button onClick={() => setCreationMenu(prev => !prev)}>
            {creationMenu ? 'Cancel' : 'Create Device'}
        </button>

        {creationMenu && (
            <div
            style={{
                marginTop: '10px',
                padding: '12px',
                border: '1px solid #ccc',
                borderRadius: '8px',
                background: '#f9f9f9',
                maxWidth: '300px'
            }}
            >
            <div style={{ marginBottom: '10px' }}>
                <label>Device Type:</label>
                <select
                value={deviceType}
                onChange={(event) => setDeviceType(event.target.value)}
                style={{ width: '100%' }}
                >
                <option value="LIGHT">Light</option>
                <option value="FAN">Fan</option>
                <option value="THERMOSTAT">Thermostat</option>
                <option value="DOOR_LOCK">Door Lock</option>
                </select>
            </div>

            <div style={{ marginBottom: '10px' }}>
                <label>Name:</label>
                <input
                type="text"
                value={name}
                onChange={(event) => setName(event.target.value)}
                placeholder="ex. Living Room Light"
                style={{ width: '100%' }}
                />
            </div>

            <div style={{ marginBottom: '10px' }}>
                <label>Location:</label>
                <input
                type="text"
                value={location}
                onChange={(event) => setLocation(event.target.value)}
                placeholder="ex. Living Room"
                style={{ width: '100%' }}
                />
            </div>

            {/* ERROR PRONE AREA HERE WITH NO ERROR HANDLING*/}
            <button     
                style={{ width: '100%' }}
                onClick={() => {
                    createDevice({name: name, location: location, deviceType: deviceType})
                    fetchDevices();
                }}
            >
                Create
            </button>
            </div>
        )}

        <div>
          <strong>Time:</strong> {currentTime.toLocaleTimeString()}
        </div>

        <div>
          <strong>Speed:</strong> {timeMultiplier + ''}x
        </div>

        <button onClick={() => setShowControls(prev => !prev)}>
          {showControls ? 'Hide Controls' : 'Show Controls'}
        </button>

      </div>

      {/* Simulation Settings sub-menu */}
      {showControls && (
        <div style={{ marginTop: '12px', padding: '10px', background: '#f9f9f9', borderRadius: '6px' }}>
          
          <div style={{ marginBottom: '10px' }}>
            <label>
              Ambient Temperature:
            </label>
            <ul>
                {deviceStatuses.map((status) => (
                  <div key={status.id}>
                    <label htmlFor={status.id}>{status.name}</label>
                    <input
                    id={status.id}
                    type="range"
                    min={0}
                    max={140}
                    value={Number(status.attributes["ambient"])}
                    onChange={(event) =>
                    performDeviceAction(status.id, {
                      action: 'SET_AMBIENCE',
                      parameters: [Number(event.target.value)],
                    })
                    }
                    style={{ width: '100%' }}
                  />
                  </div>
                ))}
            </ul>
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

        </div>
      )}
    </div>
  );
}