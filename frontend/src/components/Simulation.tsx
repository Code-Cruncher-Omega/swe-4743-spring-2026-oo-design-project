import { useCallback, useEffect, useState } from 'react';
import { DeviceFilterRequest, EnvironmentStatus, queryEnvironments, updateSimulation } from '../api/SmartHomeAPI';
import { EnvironmentList } from './Environment';
import RefreshContext from './RefreshContext';

type Parameters = {
  timeMultiplier: number;
  filter: DeviceFilterRequest;
  onRefreshReady: (fn: () => Promise<void>) => void;
  updateIntervalMs?: number;
};

export function Simulation({
  timeMultiplier,
  filter,
  onRefreshReady,
  updateIntervalMs = 5000,
}: Parameters) {

  const [environmentStatuses, setEnvironmentStatuses] = useState<EnvironmentStatus[]>([]);

  const refreshEnvironments = useCallback(async () => {
    try {
      const data = await queryEnvironments(filter);
      if(Array.isArray(data)) {
        setEnvironmentStatuses(data);
      }
    } catch (error) {
        console.error(error);
    }
  }, [filter]);

  
  useEffect(() => {
      onRefreshReady(refreshEnvironments);
      refreshEnvironments();

      const interval = setInterval(async () => {
        try {
          await updateSimulation(timeMultiplier);
          await refreshEnvironments();
        } catch (error) {
          console.error(error);
        }
      }, updateIntervalMs);

      return () => clearInterval(interval);
  }, [filter, timeMultiplier, refreshEnvironments, updateIntervalMs]);

  return (
    <RefreshContext.Provider value={refreshEnvironments}>
      <EnvironmentList environmentStatuses={environmentStatuses} />
    </RefreshContext.Provider>
  );
}