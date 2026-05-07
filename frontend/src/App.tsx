import { useRef, useState } from 'react';
import './App.css';

import { DeviceFilterRequest } from './api/SmartHomeAPI';
import { Simulation } from './components/Simulation';
import { SimulationSettings } from './components/SimulationSettings';
import { DeviceHistory } from './components/DeviceHistory';

function App() {
  const [timeMultiplier, setTimeMultiplier] = useState(1);
  const [filter, setFilter] = useState<DeviceFilterRequest>({location: '', activity: '', type: ''})
  const refreshRef = useRef<() => Promise<void>>(async () => {});

  return (
    <div>
      <h1>
        Smart Home App
      </h1>
      <SimulationSettings
        timeMultiplier={timeMultiplier}
        setTimeMultiplier={setTimeMultiplier}
        onDeviceChanged={() => refreshRef.current()}
        filter={filter}
        setFilter={setFilter}
      />
      <Simulation
        timeMultiplier={timeMultiplier}
        filter={filter}
        onRefreshReady={(fn) => { refreshRef.current = fn; }}
      />
      <DeviceHistory />
    </div>
  )
}

export default App
