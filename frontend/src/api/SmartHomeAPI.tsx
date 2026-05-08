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

  if (!response.ok) {
    const text = await response.text();
    throw new Error(`Device creation failed: ${response.status} ${text}`);
  }

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

  if (!response.ok) {
    const text = await response.text();
    throw new Error(`Device action failed: ${response.status} ${text}`);
  }

  return await response.json(); // DeviceResult
}

export async function getDeviceHistory(): Promise<AuditEntry[]> {
  const response = await fetch(apiPath('/smarthome/devices/history'));
  
  if (!response.ok) {
    const text = await response.text();
    throw new Error(`Failed to fetch device history: ${response.status} ${text}`);
  }
  
  return await response.json();
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

  if (!response.ok) {
    const text = await response.text();
    throw new Error(`Failed to update simulation: ${response.status} ${text}`);
  }
}

export async function queryEnvironments(request: DeviceFilterRequest): Promise<EnvironmentStatus[]> {
  const response = await fetch(apiPath('/smarthome/environments/status'), {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(request)
  });

  if (!response.ok) {
    const text = await response.text();
    throw new Error(`Failed to query environments: ${response.status} ${text}`);
  }

  return await response.json(); // EnvironmentStatus[]
}

export async function getEnvironmentNames(): Promise<string[]> {
    const response = await fetch(apiPath('/smarthome/environments'));
    
    if (!response.ok) {
      const text = await response.text();
      throw new Error(`Failed to fetch environment names: ${response.status} ${text}`);
    }
    
    return await response.json();
}

export async function getUpdateableDevices(): Promise<DeviceStatus[]> {
  const response = await fetch(apiPath('/smarthome/devices/updateable'));
  
  if (!response.ok) {
    const text = await response.text();
    throw new Error(`Failed to fetch updateable devices: ${response.status} ${text}`);
  }
  
  return await response.json();
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

export type DeviceStateType = 'ON' | 'OFF' | 'LOCKED' | 'UNLOCKED' | 'IDLE' | 'HEATING' | 'COOLING';

export type DeviceType = 'LIGHT' | 'FAN' | 'THERMOSTAT' | 'DOOR_LOCK';

export interface DeviceStatus {
  id: string;
  name: string;
  location: string;
  state: DeviceStateType;
  deviceType: DeviceType;
  attributes: Record<string, object>;
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

export type StateActivity = 'ON' | 'OFF';

export interface DeviceFilterRequest {
  location: string | null;
  activity: StateActivity | null;
  deviceType: DeviceType | null;
}

export interface DeviceCreationRequest {
  name: string;
  location: string;
  deviceType: DeviceType;
}