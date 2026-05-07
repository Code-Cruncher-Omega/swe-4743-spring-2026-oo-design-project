const API_URL = import.meta.env.VITE_API_URL ?? '';

function apiPath(path: string) {
  return `${API_URL}/api${path}`;
}

export async function createDevice(request: DeviceCreationRequest) {
  const response = await fetch(apiPath('/smarthome/devices'), {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(request)
  });

  if (!response.ok) throw new Error('Failed to create device');

  return response.headers.get('location'); // contains /api/smarthome/devices/{id}
}

export async function performDeviceAction(id: string, request: DeviceActionRequest): Promise<DeviceResult> {
  const response = await fetch(apiPath(`/smarthome/devices/${id}/actions`), {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(request)
  });

  return response.json(); // DeviceResult
}

export async function getDeviceHistory(): Promise<AuditEntry[]> {
  const response = await fetch(apiPath('/smarthome/devices/history'));
  return response.json();
}

export async function clearDeviceHistory(): Promise<void> {
  await fetch(apiPath('/smarthome/devices/history'), {
    method: 'DELETE'
  });
}

export async function deleteDevice(id: string) {
  await fetch(apiPath(`/smarthome/devices/${id}`), {
    method: 'DELETE'
  });
}

export async function updateSimulation(tickRate: number) {
  const response = await fetch(apiPath('/smarthome/simulation/update'), {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(tickRate)
  });

  if (!response.ok) throw new Error('Update failed');
}

export async function queryEnvironments(request: DeviceFilterRequest): Promise<EnvironmentStatus[]> {
  const response = await fetch(apiPath('/smarthome/environments/status'), {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(request)
  });

  return response.json(); // EnvironmentStatus[]
}

export async function getEnvironmentNames(): Promise<string[]> {
    const response = await fetch(apiPath('/smarthome/environments'));
    return response.json();
}

export async function getUpdateableDevices(): Promise<DeviceStatus[]> {
  const response = await fetch(apiPath('/smarthome/devices/updateable'));
  if (!response.ok) throw new Error('Failed to fetch devices');
  return response.json();
}

export async function resetAllDevices(): Promise<void> {
    await fetch(apiPath('/smarthome/devices/reset'), {
        method: 'POST'
    });
}

export async function setAmbientTemperature(environmentName: string, temperature: number): Promise<void> {
    await fetch(apiPath(`/smarthome/environments/${environmentName}/ambient`), {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(temperature)
    });
}

export interface AuditEntry {
  timestamp: string;
  id: string;
  operation: string;
}

export interface DeviceResult {
  success: boolean;
  action: string;
  message: string;
}

export interface DeviceStatus {
  id: string;
  name: string;
  location: string;
  state: string;
  type: string;
  attributes: Record<string, string>;
}

export interface EnvironmentStatus {
  name: string;
  deviceCount: number;
  devices: DeviceStatus[];
}

export interface DeviceActionRequest {
  action: string;
  parameters: Object[];
}

export interface DeviceFilterRequest {
  location: string;
  activity: string;
  type: string;
}

export interface DeviceCreationRequest {
  name: string;
  location: string;
  deviceType: string;
}