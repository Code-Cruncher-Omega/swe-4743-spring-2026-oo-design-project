import { confirmDialog } from 'primereact/confirmdialog';
import { Button } from 'primereact/button';
import { Card } from 'primereact/card';
import { Divider } from 'primereact/divider';

import { deleteDevice, EnvironmentStatus } from '../api/SmartHomeAPI';
import { DeviceRenderer } from './DeviceRenderer';
import { useRefresh } from './RefreshContext';

export function EnvironmentList({ environmentStatuses }: { environmentStatuses: EnvironmentStatus[] }) {
  const refreshDevices = useRefresh();
  
  return (
    <div className="p-3">
      <h2>Environments</h2>
      {environmentStatuses.map((environmentStatus) => (
        <div key={environmentStatus.name} className="mb-4">
          <h3>{environmentStatus.name} - {environmentStatus.deviceCount} devices</h3>
          <Divider />
          <div className="grid">
            {environmentStatus.devices.map((deviceStatus) => (
              <div key={deviceStatus.id} className="col-12 sm:col-6 md:col-4 lg:col-3">
                <Card className="h-full" style={{ overflow: 'auto' }}>
                  <div className="flex flex-column gap-2">
                    <div className="flex align-items-start justify-content-between">
                      <div>
                        <div className="text-xl font-bold">{deviceStatus.name}</div>
                        <div className="text-sm text-color-secondary">
                          {deviceStatus.deviceType.toString().toLowerCase().replace(/_/g, ' ')
                            .replace(/^\w/, (initial) => initial.toUpperCase())}
                        </div>
                      </div>
                      <div className="flex align-items-center gap-2">
                        <div style={{
                          width: '12px',
                          height: '12px',
                          borderRadius: '50%',
                          backgroundColor: deviceStatus.state === 'ON' ? '#22c55e'
                            : deviceStatus.state === 'LOCKED' ? '#22c55e'
                            : deviceStatus.state === 'UNLOCKED' ? '#eab308'
                            : deviceStatus.state === 'HEATING' ? '#f97316'
                            : deviceStatus.state === 'COOLING' ? '#3b82f6'
                            : '#6b7280'
                        }} />
                        <span className="text-sm font-bold">
                          {deviceStatus.state.toLowerCase().replace(/_/g, ' ')
                            .replace(/^\w/, (initial) => initial.toUpperCase())}
                        </span>
                        <Button
                          icon="pi pi-times"
                          severity="danger"
                          text
                          aria-label={`Delete ${deviceStatus.name}`}
                          onClick={() => confirmDialog({
                            message: `Are you sure you want to delete ${deviceStatus.name}?`,
                            header: 'Delete Device',
                            icon: 'pi pi-exclamation-triangle',
                            acceptClassName: 'p-button-danger',
                            accept: async () => {
                              await deleteDevice(deviceStatus.id);
                              await refreshDevices();
                            }
                          })}
                        />
                      </div>
                    </div>

                    <DeviceRenderer device={deviceStatus} />
                    <br/>
                  </div>
                </Card>
              </div>
            ))}
          </div>
        </div>
      ))}
    </div>
  );
}