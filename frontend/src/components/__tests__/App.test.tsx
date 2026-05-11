import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { vi, describe, it, expect, beforeEach } from 'vitest';
import App from '../../App';
import { DeviceStatus, EnvironmentStatus } from '../../api/SmartHomeAPI';
import * as SmartHomeAPI from '../../api/SmartHomeAPI';

// Mock the API functions
vi.mock('../../api/SmartHomeAPI', () => ({
  getEnvironmentNames: vi.fn(),
  queryEnvironments: vi.fn(),
  performDeviceAction: vi.fn(),
  createDevice: vi.fn(),
  deleteDevice: vi.fn(),
  getDeviceHistory: vi.fn(),
  clearDeviceHistory: vi.fn(),
  getUpdateableDevices: vi.fn(),
  resetAllDevices: vi.fn(),
  setAmbientTemperature: vi.fn(),
}));

// Mock PrimeReact components that might cause issues
vi.mock('primereact/card', () => ({
  Card: ({ children, title }: any) => <div data-testid="card" data-title={title}>{children}</div>,
}));

vi.mock('primereact/dropdown', () => ({
  Dropdown: ({ value, onChange, options, optionLabel, optionValue, ...props }: any) => (
    <select
      value={value || ''}
      onChange={(e) => onChange({ value: e.target.value })}
      {...props}
    >
      {options.map((option: any) => (
        <option key={option[optionValue]} value={option[optionValue]}>
          {option[optionLabel]}
        </option>
      ))}
    </select>
  ),
}));

vi.mock('primereact/divider', () => ({
  Divider: () => <hr data-testid="divider" />,
}));

vi.mock('primereact/confirmdialog', () => ({
  ConfirmDialog: () => null,
}));

const mockGetEnvironmentNames = vi.mocked(SmartHomeAPI.getEnvironmentNames);
const mockQueryEnvironments = vi.mocked(SmartHomeAPI.queryEnvironments);

describe('Filter Tests', () => {
  const mockDevices: DeviceStatus[] = [
    {
      id: 'light-1',
      name: 'Living Room Light',
      location: 'Living Room',
      state: 'ON' as const,
      deviceType: 'LIGHT' as const,
      attributes: { brightness: 80 as Object, red: 255 as Object, green: 255 as Object, blue: 255 as Object },
    },
    {
      id: 'fan-1',
      name: 'Living Room Fan',
      location: 'Living Room',
      state: 'OFF' as const,
      deviceType: 'FAN' as const,
      attributes: { speed: 'MEDIUM' as Object },
    },
    {
      id: 'thermostat-1',
      name: 'Living Room Thermostat',
      location: 'Living Room',
      state: 'IDLE' as const,
      deviceType: 'THERMOSTAT' as const,
      attributes: { mode: 'AUTO' as Object, desired: 72 as Object, ambient: 70 as Object },
    },
    {
      id: 'doorlock-1',
      name: 'Front Door Lock',
      location: 'Front Door',
      state: 'LOCKED' as const,
      deviceType: 'DOOR_LOCK' as const,
      attributes: {},
    },
    {
      id: 'light-2',
      name: 'Bedroom Light',
      location: 'Bedroom',
      state: 'ON' as const,
      deviceType: 'LIGHT' as const,
      attributes: { brightness: 60 as Object, red: 255 as Object, green: 200 as Object, blue: 150 as Object },
    },
  ];

  const mockEnvironments: EnvironmentStatus[] = [
    {
      name: 'Living Room',
      deviceCount: 3,
      devices: mockDevices.slice(0, 3),
    },
    {
      name: 'Front Door',
      deviceCount: 1,
      devices: [mockDevices[3]],
    },
    {
      name: 'Bedroom',
      deviceCount: 1,
      devices: [mockDevices[4]],
    },
  ];

  beforeEach(() => {
    vi.clearAllMocks();
    mockGetEnvironmentNames.mockResolvedValue(['Living Room', 'Front Door', 'Bedroom']);
    mockQueryEnvironments.mockResolvedValue(mockEnvironments);
    vi.mocked(SmartHomeAPI.getUpdateableDevices).mockResolvedValue([]);
    vi.mocked(SmartHomeAPI.resetAllDevices).mockResolvedValue(undefined);
    vi.mocked(SmartHomeAPI.setAmbientTemperature).mockResolvedValue(undefined);
  });

  it('"All" filter shows all devices', async () => {
    render(<App />);

    await waitFor(() => {
      expect(mockQueryEnvironments).toHaveBeenCalledWith({
        location: null,
        activity: null,
        deviceType: null,
      });
    });

    // Check that all devices are displayed
    await waitFor(() => {
      expect(screen.getByText('Living Room Light')).toBeInTheDocument();
      expect(screen.getByText('Living Room Fan')).toBeInTheDocument();
      expect(screen.getByText('Living Room Thermostat')).toBeInTheDocument();
      expect(screen.getByText('Front Door Lock')).toBeInTheDocument();
      expect(screen.getByText('Bedroom Light')).toBeInTheDocument();
    });
  });

  it('"On" filter shows only devices in an "on" state (includes all door locks)', async () => {
    render(<App />);

    // Wait for initial load
    await waitFor(() => {
      expect(mockQueryEnvironments).toHaveBeenCalledTimes(1);
    });

    // Change filter to "On"
    const [statusDropdown] = screen.getAllByRole('combobox');
    fireEvent.change(statusDropdown, { target: { value: 'ON' } });

    await waitFor(() => {
      expect(mockQueryEnvironments).toHaveBeenCalledWith({
        location: null,
        activity: 'ON',
        deviceType: null,
      });
    });

    // Mock the filtered response - only ON devices and LOCKED/UNLOCKED door locks
    const onDevices: EnvironmentStatus[] = [
      {
        name: 'Living Room',
        deviceCount: 2,
        devices: [mockDevices[0], mockDevices[2]], // Light ON, Thermostat IDLE (considered "on")
      },
      {
        name: 'Front Door',
        deviceCount: 1,
        devices: [mockDevices[3]], // Door lock LOCKED (always shown in "on" filter)
      },
      {
        name: 'Bedroom',
        deviceCount: 1,
        devices: [mockDevices[4]], // Light ON
      },
    ];

    mockQueryEnvironments.mockResolvedValue(onDevices);

    // Trigger re-render by changing filter again
    fireEvent.change(statusDropdown, { target: { value: null } });
    fireEvent.change(statusDropdown, { target: { value: 'ON' } });

    await waitFor(() => {
      expect(screen.getByText('Living Room Light')).toBeInTheDocument();
      expect(screen.getByText('Living Room Thermostat')).toBeInTheDocument();
      expect(screen.getByText('Front Door Lock')).toBeInTheDocument();
      expect(screen.getByText('Bedroom Light')).toBeInTheDocument();
      expect(screen.queryByText('Living Room Fan')).not.toBeInTheDocument(); // OFF fan should not be shown
    });
  });

  it('"Off" filter shows only powered devices that are off (excludes door locks)', async () => {
    render(<App />);

    await waitFor(() => {
      expect(mockQueryEnvironments).toHaveBeenCalledTimes(1);
    });

    const [statusDropdown] = screen.getAllByRole('combobox');
    fireEvent.change(statusDropdown, { target: { value: 'OFF' } });

    await waitFor(() => {
      expect(mockQueryEnvironments).toHaveBeenCalledWith({
        location: null,
        activity: 'OFF',
        deviceType: null,
      });
    });

    // Mock filtered response - only OFF devices, excluding door locks
    const offDevices: EnvironmentStatus[] = [
      {
        name: 'Living Room',
        deviceCount: 1,
        devices: [mockDevices[1]], // Fan OFF
      },
    ];

    mockQueryEnvironments.mockResolvedValue(offDevices);

    fireEvent.change(statusDropdown, { target: { value: null } });
    fireEvent.change(statusDropdown, { target: { value: 'OFF' } });

    await waitFor(() => {
      expect(screen.getByText('Living Room Fan')).toBeInTheDocument();
      expect(screen.queryByText('Living Room Light')).not.toBeInTheDocument();
      expect(screen.queryByText('Front Door Lock')).not.toBeInTheDocument(); // Door locks excluded
    });
  });

  it('Location filter shows only devices in the selected location', async () => {
    render(<App />);

    await waitFor(() => {
      expect(mockQueryEnvironments).toHaveBeenCalledTimes(1);
    });

    // Find location dropdown and change to "Living Room"
    const locationDropdowns = screen.getAllByRole('combobox');
    const locationDropdown = locationDropdowns[1]; // Second dropdown is location
    fireEvent.change(locationDropdown, { target: { value: 'Living Room' } });

    await waitFor(() => {
      expect(mockQueryEnvironments).toHaveBeenCalledWith({
        location: 'Living Room',
        activity: null,
        deviceType: null,
      });
    });

    // Mock filtered response
    const livingRoomDevices: EnvironmentStatus[] = [
      {
        name: 'Living Room',
        deviceCount: 3,
        devices: mockDevices.slice(0, 3),
      },
    ];

    mockQueryEnvironments.mockResolvedValue(livingRoomDevices);

    fireEvent.change(locationDropdown, { target: { value: null } });
    fireEvent.change(locationDropdown, { target: { value: 'Living Room' } });

    await waitFor(() => {
      expect(screen.getByText('Living Room Light')).toBeInTheDocument();
      expect(screen.getByText('Living Room Fan')).toBeInTheDocument();
      expect(screen.getByText('Living Room Thermostat')).toBeInTheDocument();
      expect(screen.queryByText('Front Door Lock')).not.toBeInTheDocument();
      expect(screen.queryByText('Bedroom Light')).not.toBeInTheDocument();
    });
  });

  it('Device Type filter shows only devices of the selected type', async () => {
    render(<App />);

    await waitFor(() => {
      expect(mockQueryEnvironments).toHaveBeenCalledTimes(1);
    });

    // Find device type dropdown and change to "LIGHT"
    const typeDropdowns = screen.getAllByRole('combobox');
    const typeDropdown = typeDropdowns[2]; // Third dropdown is device type
    fireEvent.change(typeDropdown, { target: { value: 'LIGHT' } });

    await waitFor(() => {
      expect(mockQueryEnvironments).toHaveBeenCalledWith({
        location: null,
        activity: null,
        deviceType: 'LIGHT',
      });
    });

    // Mock filtered response - only lights
    const lightDevices: EnvironmentStatus[] = [
      {
        name: 'Living Room',
        deviceCount: 1,
        devices: [mockDevices[0]],
      },
      {
        name: 'Bedroom',
        deviceCount: 1,
        devices: [mockDevices[4]],
      },
    ];

    mockQueryEnvironments.mockResolvedValue(lightDevices);

    fireEvent.change(typeDropdown, { target: { value: null } });
    fireEvent.change(typeDropdown, { target: { value: 'LIGHT' } });

    await waitFor(() => {
      expect(screen.getByText('Living Room Light')).toBeInTheDocument();
      expect(screen.getByText('Bedroom Light')).toBeInTheDocument();
      expect(screen.queryByText('Living Room Fan')).not.toBeInTheDocument();
      expect(screen.queryByText('Living Room Thermostat')).not.toBeInTheDocument();
      expect(screen.queryByText('Front Door Lock')).not.toBeInTheDocument();
    });
  });

  it('Filters combine correctly (e.g., "On" + "Living Room" shows only on devices in the living room)', async () => {
    render(<App />);

    await waitFor(() => {
      expect(mockQueryEnvironments).toHaveBeenCalledTimes(1);
    });

    // Set status to "ON"
    const [statusDropdown] = screen.getAllByRole('combobox');
    fireEvent.change(statusDropdown, { target: { value: 'ON' } });

    // Set location to "Living Room"
    const locationDropdowns = screen.getAllByRole('combobox');
    const locationDropdown = locationDropdowns[1];
    fireEvent.change(locationDropdown, { target: { value: 'Living Room' } });

    await waitFor(() => {
      expect(mockQueryEnvironments).toHaveBeenCalledWith({
        location: 'Living Room',
        activity: 'ON',
        deviceType: null,
      });
    });

    // Mock combined filter response - only ON devices in Living Room
    const combinedDevices: EnvironmentStatus[] = [
      {
        name: 'Living Room',
        deviceCount: 2,
        devices: [mockDevices[0], mockDevices[2]], // Light ON, Thermostat IDLE
      },
    ];

    mockQueryEnvironments.mockResolvedValue(combinedDevices);

    // Trigger re-render
    fireEvent.change(statusDropdown, { target: { value: null } });
    fireEvent.change(statusDropdown, { target: { value: 'ON' } });

    await waitFor(() => {
      expect(screen.getByText('Living Room Light')).toBeInTheDocument();
      expect(screen.getByText('Living Room Thermostat')).toBeInTheDocument();
      expect(screen.queryByText('Living Room Fan')).not.toBeInTheDocument(); // OFF
      expect(screen.queryByText('Front Door Lock')).not.toBeInTheDocument(); // Different location
      expect(screen.queryByText('Bedroom Light')).not.toBeInTheDocument(); // Different location
    });
  });
});





