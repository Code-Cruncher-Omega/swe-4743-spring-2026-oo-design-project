import { useEffect, useState } from 'react';
import { EnvironmentStatus, queryEnvironments, updateSimulation } from '../api/SmartHomeAPI';
import { EnvironmentList } from './Environment';

const [filter, setFilter] = useState({
    location: '',
    activity: '',
    type: ''
});

{/* MIGHT CAUSE ISSUES */}
const [environmentStatuses, setEnvironmentStatuses] = useState<EnvironmentStatus[]>(await queryEnvironments(filter));
const tickRate = useState(5000);    // Update simulation every 5 seconds.
const [timeMultiplier, setMultiplier] = useState<number>(1);

export function Simulation() {

useEffect(() => {
  const interval = setInterval(async () => {
    try {
      await updateSimulation(timeMultiplier);

      const data = await queryEnvironments(filter);
      setEnvironmentStatuses(data);
    } catch (error) {
      console.error(error);
    }
  }, timeMultiplier);

  return () => clearInterval(interval);
}, [filter, timeMultiplier]);

  return (
    <EnvironmentList environmentStatuses={environmentStatuses} />
  );
}

export async function refreshDevices() {
    setEnvironmentStatuses(await queryEnvironments(filter))
}

export function getTickRate() {
    return tickRate;
}

export function getTimeMultiplier() {
    return timeMultiplier;
}

export function setTimeMultiplier(multiplier: number) {
    setMultiplier(multiplier);
}