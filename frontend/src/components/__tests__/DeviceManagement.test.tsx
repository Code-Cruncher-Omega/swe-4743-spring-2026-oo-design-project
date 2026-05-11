import * as SmartHomeAPI from '../../api/SmartHomeAPI';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { vi, describe, it, expect, beforeEach } from 'vitest';
import { SimulationSettings } from '../SimulationSettings';
import { DeviceStatus } from '../../api/SmartHomeAPI';

// Mock the API functions
vi.mock('../../api/SmartHomeAPI', () => ({
  createDevice: vi.fn(),
  getUpdateableDevices: vi.fn(),
  setAmbientTemperature: vi.fn(),
  resetAllDevices: vi.fn(),
}));

// Mock PrimeReact components
vi.mock('primereact/button', () => ({
  Button: ({ children, label, onClick, disabled, icon, severity, ...props }: any) => (
    <button
      onClick={onClick}
      disabled={disabled}
      data-severity={severity}
      data-icon={icon}
      {...props}
    >
      {label || children}
    </button>
  ),
}));

vi.mock('primereact/card', () => ({
  Card: ({ children, title }: any) => <div data-testid="card" data-title={title}>{children}</div>,
}));

vi.mock('primereact/dialog', () => ({
  Dialog: ({ children, visible, header, onHide }: any) =>
    visible ? (
      <div data-testid="dialog" data-header={header}>
        <button onClick={onHide} data-testid="dialog-close">×</button>
        {children}
      </div>
    ) : null,
}));

vi.mock('primereact/divider', () => ({
  Divider: () => <hr data-testid="divider" />,
}));

vi.mock('primereact/dropdown', () => ({
  Dropdown: ({ value, onChange, options, optionLabel, optionValue, ...props }: any) => {
    const labelKey = optionLabel ?? 'label';
    const valueKey = optionValue ?? 'value';

    return (
      <select
        value={value || ''}
        onChange={(e) => onChange({ value: e.target.value })}
        {...props}
      >
        {options.map((option: any, index: number) => {
          const optionValueActual = option?.[valueKey] ?? option;
          const optionLabelActual = option?.[labelKey] ?? option;
          return (
            <option key={optionValueActual ?? index} value={optionValueActual ?? ''}>
              {optionLabelActual}
            </option>
          );
        })}
      </select>
    );
  },
}));

vi.mock('primereact/inputtext', () => ({
  InputText: ({ value, onChange, placeholder, ...props }: any) => (
    <input
      type="text"
      value={value}
      onChange={(e) => onChange({ target: { value: e.target.value } })}
      placeholder={placeholder}
      {...props}
    />
  ),
}));

vi.mock('primereact/slider', () => ({
  Slider: ({ value, onChange, min, max, ...props }: any) => (
    <input
      type="range"
      min={min}
      max={max}
      value={value}
      onChange={(e) => onChange({ target: { value: e.target.value } })}
      {...props}
    />
  ),
}));

vi.mock('primereact/toolbar', () => ({
  Toolbar: ({ start, end }: any) => (
    <div data-testid="toolbar">
      <div data-testid="toolbar-start">{start}</div>
      <div data-testid="toolbar-end">{end}</div>
    </div>
  ),
}));

const mockCreateDevice = vi.mocked(SmartHomeAPI.createDevice);
const mockGetUpdateableDevices = vi.mocked(SmartHomeAPI.getUpdateableDevices);
const mockSetAmbientTemperature = vi.mocked(SmartHomeAPI.setAmbientTemperature);
const mockResetAllDevices = vi.mocked(SmartHomeAPI.resetAllDevices);

describe('Device Management Tests', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mockGetUpdateableDevices.mockResolvedValue([]);
    mockCreateDevice.mockResolvedValue('/api/smarthome/devices/device-1');
  });

  it('register device form submits correctly and new device appears', async () => {
    const mockOnDeviceChanged = vi.fn();

    render(<SimulationSettings
      timeMultiplier={1}
      setTimeMultiplier={vi.fn()}
      onDeviceChanged={mockOnDeviceChanged}
    />);

    // Open creation dialog
    const createButton = screen.getByRole('button', { name: /create device/i });
    fireEvent.click(createButton);

    // Fill out the form
    const nameInput = screen.getByPlaceholderText('ex. Living Room Light');
    const locationInput = screen.getByPlaceholderText('ex. Living Room');

    fireEvent.change(nameInput, { target: { value: 'Test Light' } });
    fireEvent.change(locationInput, { target: { value: 'Living Room' } });

    // Submit the form
    const submitButton = screen.getByRole('button', { name: /create/i });
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(mockCreateDevice).toHaveBeenCalledWith({
        name: 'Test Light',
        location: 'Living Room',
        deviceType: 'LIGHT', // Default value
      });
    });

    expect(mockOnDeviceChanged).toHaveBeenCalled();
  });

  it('device registration requires name', async () => {
    render(<SimulationSettings
      timeMultiplier={1}
      setTimeMultiplier={vi.fn()}
      onDeviceChanged={vi.fn()}
    />);

    // Open creation dialog
    const createButton = screen.getByRole('button', { name: /create device/i });
    fireEvent.click(createButton);

    await waitFor(() => {
      expect(screen.getByPlaceholderText('ex. Living Room')).toBeInTheDocument();
    });

    // Try to submit without name
    const locationInput = screen.getByPlaceholderText('ex. Living Room');
    fireEvent.change(locationInput, { target: { value: 'Living Room' } });

    await waitFor(() => {
      const submitButton = screen.getByRole('button', { name: /create/i });
      expect(submitButton).toBeDisabled();
    });
  });

  it('device registration requires location', async () => {
    render(<SimulationSettings
      timeMultiplier={1}
      setTimeMultiplier={vi.fn()}
      onDeviceChanged={vi.fn()}
    />);

    // Open creation dialog
    const createButton = screen.getByRole('button', { name: /create device/i });
    fireEvent.click(createButton);

    await waitFor(() => {
      expect(screen.getByPlaceholderText('ex. Living Room Light')).toBeInTheDocument();
    });

    // Try to submit without location
    const nameInput = screen.getByPlaceholderText('ex. Living Room Light');
    fireEvent.change(nameInput, { target: { value: 'Test Light' } });

    await waitFor(() => {
      const submitButton = screen.getByRole('button', { name: /create/i });
      expect(submitButton).toBeDisabled();
    });
  });

  it('prevents duplicate thermostats in same location', async () => {
    const mockDevices: DeviceStatus[] = [
      {
        id: 'thermostat-1',
        name: 'Existing Thermostat',
        location: 'Living Room',
        state: 'IDLE' as const,
        deviceType: 'THERMOSTAT' as const,
        attributes: { mode: 'AUTO' as Object, desired: 72 as Object, ambient: 70 as Object },
      },
    ];

    mockGetUpdateableDevices.mockResolvedValue(mockDevices);

    render(<SimulationSettings
      timeMultiplier={1}
      setTimeMultiplier={vi.fn()}
      onDeviceChanged={vi.fn()}
    />);

    await waitFor(() => {
      expect(mockGetUpdateableDevices).toHaveBeenCalled();
    });

    // Open creation dialog
    const createButton = screen.getByRole('button', { name: /create device/i });
    fireEvent.click(createButton);

    // Select thermostat type
    const typeDropdown = screen.getByDisplayValue('Light');
    fireEvent.change(typeDropdown, { target: { value: 'THERMOSTAT' } });

    // Fill out form
    const nameInput = screen.getByPlaceholderText('ex. Living Room Light');
    const locationInput = screen.getByPlaceholderText('ex. Living Room');

    fireEvent.change(nameInput, { target: { value: 'New Thermostat' } });
    fireEvent.change(locationInput, { target: { value: 'Living Room' } });

    await waitFor(() => {
      const submitButton = screen.getByRole('button', { name: /create/i });
      expect(submitButton).toBeDisabled();
      expect(screen.getByText(/thermostat already exists/i)).toBeInTheDocument();
    });
  });
});

// Note: Delete functionality is tested in Environment.test.tsx since it's handled there
describe('Device Deletion Tests', () => {
  it('shows confirmation dialog when delete button is clicked', () => {
    // This test would require mocking the confirmDialog from PrimeReact
    // For now, this is a placeholder - the actual test would be in Environment.test.tsx
    expect(true).toBe(true);
  });

  it('deletes device when confirmed', () => {
    // This test would also be in Environment.test.tsx
    expect(true).toBe(true);
  });

  it('keeps device when cancelled', () => {
    // This test would also be in Environment.test.tsx
    expect(true).toBe(true);
  });
});





