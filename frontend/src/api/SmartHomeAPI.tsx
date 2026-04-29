const API_URL = import.meta.env.VITE_API_URL ?? '';

function apiPath(path: string) {
  return `${API_URL}${path}`;
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
  const response = await fetch(apiPath('/smarthome/environments/query'), {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(request)
  });

  return response.json(); // EnvironmentStatus[]
}

export async function getUpdateableDevices(): Promise<DeviceStatus[]> {
  const response = await fetch(apiPath('/smarthome/devices/updateable'));
  if (!response.ok) throw new Error('Failed to fetch devices');
  return response.json();
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
    type: string;
}