import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { vi, describe, it, expect, beforeEach } from 'vitest';
import { EnvironmentList } from '../Environment';
import { EnvironmentStatus, DeviceStatus } from '../../api/SmartHomeAPI';
import * as SmartHomeAPI from '../../api/SmartHomeAPI';

// Mock the API and context
vi.mock('../../api/SmartHomeAPI', () => ({
  deleteDevice: vi.fn(),
}));

vi.mock('../RefreshContext', () => ({
  useRefresh: vi.fn(),
}));

vi.mock('primereact/confirmdialog', () => ({
  ConfirmDialog: () => null,
  confirmDialog: vi.fn(),
}));

import { confirmDialog } from 'primereact/confirmdialog';
import { useRefresh } from '../RefreshContext';

const mockDeleteDevice = vi.mocked(SmartHomeAPI.deleteDevice);
const mockConfirmDialog = vi.mocked(confirmDialog);
const mockUseRefresh = vi.mocked(useRefresh);
const mockRefresh = vi.fn();

describe('Environment Component - Device Deletion', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mockDeleteDevice.mockResolvedValue(undefined);
    mockUseRefresh.mockReturnValue(mockRefresh);
  });

  const createMockDevice = (overrides: Partial<DeviceStatus> = {}): DeviceStatus => ({
    id: 'light-1',
    name: 'Test Light',
    location: 'Living Room',
    state: 'ON' as const,
    deviceType: 'LIGHT' as const,
    ...overrides,
    attributes: {
      brightness: 75 as Object,
      red: 255 as Object,
      green: 255 as Object,
      blue: 255 as Object,
      ...(overrides.attributes ?? {}),
    },
  });

  const createMockEnvironment = (devices: DeviceStatus[], name = 'Living Room'): EnvironmentStatus => ({
    name,
    deviceCount: devices.length,
    devices,
  });

  it('shows confirmation dialog when delete button is clicked', () => {
    const device = createMockDevice();
    const environment = createMockEnvironment([device]);

    render(<EnvironmentList environmentStatuses={[environment]} />);

    const deleteButton = screen.getByRole('button', { name: /delete test light/i });
    fireEvent.click(deleteButton);

    expect(mockConfirmDialog).toHaveBeenCalledWith({
      message: 'Are you sure you want to delete Test Light?',
      header: 'Delete Device',
      icon: 'pi pi-exclamation-triangle',
      acceptClassName: 'p-button-danger',
      accept: expect.any(Function),
    });
  });

  it('deletes device when confirmed', async () => {
    const device = createMockDevice();
    const environment = createMockEnvironment([device]);

    // Mock the confirmDialog to call the accept function
    mockConfirmDialog.mockImplementation((options: any) => {
      options.accept?.();
      return {
        show: () => {},
        hide: () => {},
      };
    });

    render(<EnvironmentList environmentStatuses={[environment]} />);

    const deleteButton = screen.getByRole('button', { name: /delete test light/i });
    fireEvent.click(deleteButton);

    await waitFor(() => {
      expect(mockDeleteDevice).toHaveBeenCalledWith('light-1');
      expect(mockRefresh).toHaveBeenCalled();
    });
  });

  it('does not delete device when cancelled', () => {
    const device = createMockDevice();
    const environment = createMockEnvironment([device]);

    // Mock the confirmDialog to not call accept (simulating cancel)
    mockConfirmDialog.mockImplementation(() => ({
      show: () => {},
      hide: () => {},
    }));

    render(<EnvironmentList environmentStatuses={[environment]} />);

    const deleteButton = screen.getByRole('button', { name: /delete test light/i });
    fireEvent.click(deleteButton);

    expect(mockConfirmDialog).toHaveBeenCalled();
    expect(mockDeleteDevice).not.toHaveBeenCalled();
    expect(mockRefresh).not.toHaveBeenCalled();
  });

  it('displays device information correctly', () => {
    const device = createMockDevice();
    const environment = createMockEnvironment([device]);

    render(<EnvironmentList environmentStatuses={[environment]} />);

    expect(screen.getByRole('heading', { name: 'Living Room - 1 devices' })).toBeInTheDocument();
    expect(screen.getByText('Test Light')).toBeInTheDocument();
    expect(screen.getByText('Light')).toBeInTheDocument();
    expect(screen.getByText('On')).toBeInTheDocument();
  });

  it('displays multiple devices in environment', () => {
    const light = createMockDevice({ id: 'light-1', name: 'Living Room Light' });
    const fan = createMockDevice({
      id: 'fan-1',
      name: 'Living Room Fan',
      deviceType: 'FAN',
      attributes: { speed: 'HIGH' as Object },
    });
    const environment = createMockEnvironment([light, fan]);

    render(<EnvironmentList environmentStatuses={[environment]} />);

    expect(screen.getByRole('heading', { name: 'Living Room - 2 devices' })).toBeInTheDocument();
    expect(screen.getByText('Living Room Light')).toBeInTheDocument();
    expect(screen.getByText('Living Room Fan')).toBeInTheDocument();
  });

  it('displays multiple environments', () => {
    const livingRoomLight = createMockDevice({ name: 'Living Room Light' });
    const bedroomLight = createMockDevice({ name: 'Bedroom Light', location: 'Bedroom' });

    const livingRoom = createMockEnvironment([livingRoomLight], 'Living Room');
    const bedroom = createMockEnvironment([bedroomLight], 'Bedroom');

    render(<EnvironmentList environmentStatuses={[livingRoom, bedroom]} />);

    expect(screen.getByRole('heading', { name: 'Living Room - 1 devices' })).toBeInTheDocument();
    expect(screen.getByRole('heading', { name: 'Bedroom - 1 devices' })).toBeInTheDocument();
    expect(screen.getByText('Living Room Light')).toBeInTheDocument();
    expect(screen.getByText('Bedroom Light')).toBeInTheDocument();
  });
});





