import { EnvironmentStatus } from '../api/SmartHomeAPI';
import { DeviceRenderer } from './DeviceRenderer';

export function EnvironmentList({ environmentStatuses }: { environmentStatuses: EnvironmentStatus[] }) {
  return (
    <div>
      <h2>Environments</h2>
      {environmentStatuses.map((environmentStatus) => (
        <div key={environmentStatus.name}>
          <h3>{environmentStatus.name} - {environmentStatus.deviceCount} devices</h3>
          {environmentStatus.devices.map((deviceStatus) => (
            <DeviceRenderer key={deviceStatus.id} device={deviceStatus} />
          ))}
        </div>
      ))}
    </div>
  );
}