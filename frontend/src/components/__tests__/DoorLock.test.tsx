import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { vi, describe, it, expect, beforeEach } from 'vitest';
import { DoorLock } from '../DoorLock';
import { DeviceStatus } from '../../api/SmartHomeAPI';
import * as SmartHomeAPI from '../../api/SmartHomeAPI';

// Mock the API and context
vi.mock('../../api/SmartHomeAPI', () => ({
  performDeviceAction: vi.fn(),
}));

vi.mock('../components/RefreshContext', () => ({
  useRefresh: () => vi.fn(),
}));

const mockPerformDeviceAction = vi.mocked(SmartHomeAPI.performDeviceAction);

describe('DoorLock Component Rendering', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mockPerformDeviceAction.mockResolvedValue({
      success: true,
      action: 'TOGGLE_LOCK',
      message: 'Door unlocked successfully'
    });
  });

  const createMockDevice = (overrides: Partial<DeviceStatus> = {}): DeviceStatus => ({
    id: 'doorlock-1',
    name: 'Test Door Lock',
    location: 'Front Door',
    state: 'LOCKED' as const,
    deviceType: 'DOOR_LOCK' as const,
    attributes: {},
    ...overrides
  });

  it('renders correctly in Locked state', () => {
    const device = createMockDevice({ state: 'LOCKED' });

    render(<DoorLock device={device} />);

    expect(screen.getByText('Lock', { selector: 'span' })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /unlock/i })).toBeInTheDocument();
  });

  it('renders correctly in Unlocked state', () => {
    const device = createMockDevice({ state: 'UNLOCKED' });

    render(<DoorLock device={device} />);

    expect(screen.getByText('Lock', { selector: 'span' })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /lock/i })).toBeInTheDocument();
  });

  it('toggles lock successfully from locked to unlocked', async () => {
    const device = createMockDevice({ state: 'LOCKED' });

    render(<DoorLock device={device} />);

    const toggleButton = screen.getByRole('button', { name: /unlock/i });
    fireEvent.click(toggleButton);

    await waitFor(() => {
      expect(mockPerformDeviceAction).toHaveBeenCalledWith('doorlock-1', {
        action: 'TOGGLE_LOCK',
        parameters: []
      });
    });
  });

  it('toggles lock successfully from unlocked to locked', async () => {
    const device = createMockDevice({ state: 'UNLOCKED' });

    render(<DoorLock device={device} />);

    const toggleButton = screen.getByRole('button', { name: /lock/i });
    fireEvent.click(toggleButton);

    await waitFor(() => {
      expect(mockPerformDeviceAction).toHaveBeenCalledWith('doorlock-1', {
        action: 'TOGGLE_LOCK',
        parameters: []
      });
    });
  });
});





