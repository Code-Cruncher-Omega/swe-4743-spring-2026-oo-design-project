import { useEffect, useState } from 'react';
import { DeviceFilterRequest, EnvironmentStatus, queryEnvironments, updateSimulation } from '../api/SmartHomeAPI';
import { EnvironmentList } from './Environment';
import RefreshContext from './RefreshContext';

type Parameters = {
  timeMultiplier: number;
  filter: DeviceFilterRequest;
};

export function Simulation({timeMultiplier, filter}: Parameters) {

const [environmentStatuses, setEnvironmentStatuses] = useState<EnvironmentStatus[]>([]);

  const refreshEnvironments = async () => {
    try {
      const data = await queryEnvironments(filter);
      if (Array.isArray(data)) {
        setEnvironmentStatuses(data);
      } else {
        console.error("Unexpected response:", data);
      }
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    refreshEnvironments();

    const interval = setInterval(async () => {
      try {
          await updateSimulation(timeMultiplier);
          await refreshEnvironments();
        } catch (error) {
          console.error(error);
      }
    }, 5000); {/* Update every 5 seconds */}
    return () => clearInterval(interval);
  }, [filter, timeMultiplier]);

  return (
    <RefreshContext.Provider value={refreshEnvironments}>
      <EnvironmentList environmentStatuses={environmentStatuses} />
    </RefreshContext.Provider>
  );
}