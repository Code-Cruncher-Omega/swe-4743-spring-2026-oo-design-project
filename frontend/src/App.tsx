import { useState } from 'react';
import './App.css';

import { DeviceFilterRequest } from './api/SmartHomeAPI';
import { Simulation } from './components/Simulation';
import { SimulationSettings } from './components/SimulationSettings';

function App() {
  const [timeMultiplier, setTimeMultiplier] = useState(1);
  const [filter, setFilter] = useState<DeviceFilterRequest>({location: '', activity: '', type: ''})

  return (
    <div>
      <h1>
        Smart Home App
      </h1>
      <SimulationSettings
        timeMultiplier={timeMultiplier}
        setTimeMultiplier={setTimeMultiplier}/>
      <Simulation
        timeMultiplier={timeMultiplier}
        filter={filter}/>
    </div>
  )
}

export default App
