import { vi, describe, it, expect, beforeEach, afterEach } from 'vitest';
import {
  createDevice,
  performDeviceAction,
  queryEnvironments,
  getEnvironmentNames,
  deleteDevice,
  getDeviceHistory,
  updateSimulation,
  getUpdateableDevices,
  setAmbientTemperature,
  resetAllDevices,
} from '../SmartHomeAPI';

const originalImportMetaBase = (import.meta.env as any).VITE_API_BASE_URL;

// Mock fetch globally
const fetchMock = vi.fn();
globalThis.fetch = fetchMock as typeof fetch;

describe('API Integration Tests (with mocked HTTP)', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    delete (globalThis as any).VITE_API_BASE_URL;
    (globalThis as any).VITE_API_BASE_URL = 'http://localhost:8080';
    (import.meta.env as any).VITE_API_BASE_URL = 'http://localhost:8080';
  });

  afterEach(() => {
    vi.restoreAllMocks();
    if (originalImportMetaBase !== undefined) {
      (import.meta.env as any).VITE_API_BASE_URL = originalImportMetaBase;
    } else {
      delete (import.meta.env as any).VITE_API_BASE_URL;
    }
  });

  describe('Device List Loading', () => {
    it('loads device list on queryEnvironments call', async () => {
      const mockResponse = [
        {
          name: 'Living Room',
          deviceCount: 2,
          devices: [
            {
              id: 'light-1',
              name: 'Living Room Light',
              location: 'Living Room',
              state: 'ON',
              deviceType: 'LIGHT',
              attributes: { brightness: 80 },
            },
            {
              id: 'fan-1',
              name: 'Living Room Fan',
              location: 'Living Room',
              state: 'OFF',
              deviceType: 'FAN',
              attributes: { speed: 'MEDIUM' },
            },
          ],
        },
      ];

      fetchMock.mockResolvedValueOnce({
        ok: true,
        json: () => Promise.resolve(mockResponse),
      });

      const result = await queryEnvironments({
        location: null,
        activity: null,
        deviceType: null,
      });

      expect(fetchMock).toHaveBeenCalledWith(
        'http://localhost:8080/api/smarthome/environments/status',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            location: null,
            activity: null,
            deviceType: null,
          }),
        }
      );

      expect(result).toEqual(mockResponse);
    });

    it('loads environment names on getEnvironmentNames call', async () => {
      const mockResponse = ['Living Room', 'Bedroom', 'Kitchen'];

      fetchMock.mockResolvedValueOnce({
        ok: true,
        json: () => Promise.resolve(mockResponse),
      });

      const result = await getEnvironmentNames();

      expect(fetchMock).toHaveBeenCalledWith(
        'http://localhost:8080/api/smarthome/environments'
      );

      expect(result).toEqual(mockResponse);
    });
  });

  describe('Device Control', () => {
    it('sends correct API request when controlling a device', async () => {
      const mockResponse = {
        success: true,
        action: 'TOGGLE_POWER',
        message: 'Light turned on successfully',
      };

      fetchMock.mockResolvedValueOnce({
        ok: true,
        json: () => Promise.resolve(mockResponse),
      });

      const result = await performDeviceAction('light-1', {
        action: 'TOGGLE_POWER',
        parameters: [],
      });

      expect(fetchMock).toHaveBeenCalledWith(
        'http://localhost:8080/api/smarthome/devices/light-1/actions',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            action: 'TOGGLE_POWER',
            parameters: [],
          }),
        }
      );

      expect(result).toEqual(mockResponse);
    });

    it('sends correct API request for brightness change', async () => {
      const mockResponse = {
        success: true,
        action: 'SET_BRIGHTNESS',
        message: 'Brightness set to 75%',
      };

      fetchMock.mockResolvedValueOnce({
        ok: true,
        json: () => Promise.resolve(mockResponse),
      });

      const result = await performDeviceAction('light-1', {
        action: 'SET_BRIGHTNESS',
        parameters: [75],
      });

      expect(fetchMock).toHaveBeenCalledWith(
        'http://localhost:8080/api/smarthome/devices/light-1/actions',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            action: 'SET_BRIGHTNESS',
            parameters: [75],
          }),
        }
      );

      expect(result).toEqual(mockResponse);
    });
  });

  describe('Device Creation', () => {
    it('sends correct API request when creating a device', async () => {
      const mockResponse = '/api/smarthome/devices/light-1';

      fetchMock.mockResolvedValueOnce({
        ok: true,
        headers: {
          get: (header: string) => header === 'location' ? mockResponse : null,
        },
      });

      const result = await createDevice({
        name: 'New Light',
        location: 'Living Room',
        deviceType: 'LIGHT',
      });

      expect(fetchMock).toHaveBeenCalledWith(
        'http://localhost:8080/api/smarthome/devices',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            name: 'New Light',
            location: 'Living Room',
            deviceType: 'LIGHT',
          }),
        }
      );

      expect(result).toBe(mockResponse);
    });
  });

  describe('Device Deletion', () => {
    it('sends correct API request when deleting a device', async () => {
      fetchMock.mockResolvedValueOnce({
        ok: true,
      });

      await deleteDevice('light-1');

      expect(fetchMock).toHaveBeenCalledWith(
        'http://localhost:8080/api/smarthome/devices/light-1',
        {
          method: 'DELETE',
        }
      );
    });
  });

  describe('Device History', () => {
    it('loads device history correctly', async () => {
      const mockResponse = [
        {
          timestamp: '2024-01-01T10:00:00Z',
          id: 'light-1',
          operation: 'Light turned on',
        },
        {
          timestamp: '2024-01-01T10:05:00Z',
          id: 'fan-1',
          operation: 'Fan speed changed to HIGH',
        },
      ];

      fetchMock.mockResolvedValueOnce({
        ok: true,
        json: () => Promise.resolve(mockResponse),
      });

      const result = await getDeviceHistory();

      expect(fetchMock).toHaveBeenCalledWith(
        'http://localhost:8080/api/smarthome/devices/history'
      );

      expect(result).toEqual(mockResponse);
    });
  });

  describe('Simulation Control', () => {
    it('sends correct API request for simulation update', async () => {
      fetchMock.mockResolvedValueOnce({
        ok: true,
      });

      await updateSimulation(2);

      expect(fetchMock).toHaveBeenCalledWith(
        'http://localhost:8080/api/smarthome/simulation/update',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(2),
        }
      );
    });

    it('sends correct API request for ambient temperature change', async () => {
      fetchMock.mockResolvedValueOnce({
        ok: true,
      });

      await setAmbientTemperature('Living Room', 75);

      expect(fetchMock).toHaveBeenCalledWith(
        'http://localhost:8080/api/smarthome/environments/Living%20Room/ambient',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(75),
        }
      );
    });

    it('sends correct API request for resetting all devices', async () => {
      fetchMock.mockResolvedValueOnce({
        ok: true,
      });

      await resetAllDevices();

      expect(fetchMock).toHaveBeenCalledWith(
        'http://localhost:8080/api/smarthome/devices/reset',
        {
          method: 'POST',
        }
      );
    });
  });

  describe('Error Handling', () => {
    it('displays appropriate error message for failed device action', async () => {
      const errorMessage = 'Device not found';

      fetchMock.mockResolvedValueOnce({
        ok: false,
        status: 404,
        text: () => Promise.resolve(errorMessage),
      });

      await expect(
        performDeviceAction('invalid-id', {
          action: 'TOGGLE_POWER',
          parameters: [],
        })
      ).rejects.toThrow(`Device action failed: 404 ${errorMessage}`);
    });

    it('displays appropriate error message for failed device creation', async () => {
      const errorMessage = 'Invalid device type';

      fetchMock.mockResolvedValueOnce({
        ok: false,
        status: 400,
        text: () => Promise.resolve(errorMessage),
      });

      await expect(
        createDevice({
          name: 'Test',
          location: 'Room',
          deviceType: 'INVALID' as any,
        })
      ).rejects.toThrow(`Device creation failed: 400 ${errorMessage}`);
    });

    it('displays appropriate error message for failed environment query', async () => {
      const errorMessage = 'Server error';

      fetchMock.mockResolvedValueOnce({
        ok: false,
        status: 500,
        text: () => Promise.resolve(errorMessage),
      });

      await expect(
        queryEnvironments({
          location: null,
          activity: null,
          deviceType: null,
        })
      ).rejects.toThrow(`Failed to query environments: 500 ${errorMessage}`);
    });
  });

  describe('Environment Variables', () => {
    it('uses default API base URL when VITE_API_BASE_URL is not set', async () => {
      delete (globalThis as any).VITE_API_BASE_URL;
    delete (import.meta.env as any).VITE_API_BASE_URL;
      fetchMock.mockResolvedValueOnce({
        ok: true,
        json: () => Promise.resolve([]),
      });

      await queryEnvironments({
        location: null,
        activity: null,
        deviceType: null,
      });

      expect(fetchMock).toHaveBeenCalledWith(
        '/api/smarthome/environments/status',
        expect.any(Object)
      );
    });

    it('uses custom API base URL when VITE_API_BASE_URL is set', async () => {
      (globalThis as any).VITE_API_BASE_URL = 'https://api.example.com';
      (import.meta.env as any).VITE_API_BASE_URL = 'https://api.example.com';

      fetchMock.mockResolvedValueOnce({
        ok: true,
        json: () => Promise.resolve([]),
      });

      await queryEnvironments({
        location: null,
        activity: null,
        deviceType: null,
      });

      expect(fetchMock).toHaveBeenCalledWith(
        'https://api.example.com/api/smarthome/environments/status',
        expect.any(Object)
      );
    });
  });
});
