import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { vi, describe, it, expect, beforeEach } from 'vitest';

// Mock PrimeReact Slider to be a simple input for testing
vi.mock('primereact/slider', () => ({
  Slider: ({ value, onChange, onSlideEnd, min, max, ...props }: any) => {
    const clampedValue = Math.max(min, Math.min(max, value));
    return (
      <input
        type="range"
        value={clampedValue}
        onChange={(e) => onChange({ value: Number(e.target.value) })}
        onMouseUp={() => onSlideEnd && onSlideEnd({ value: clampedValue })}
        min={min}
        max={max}
        {...props}
      />
    );
  },
}));

import { Fan } from '../Fan';
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

describe('Fan Component Rendering', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mockPerformDeviceAction.mockResolvedValue({
      success: true,
      action: 'TOGGLE_POWER',
      message: 'Fan turned on successfully'
    });
  });

  const createMockDevice = (overrides: Partial<DeviceStatus> = {}): DeviceStatus => ({
    id: 'fan-1',
    name: 'Test Fan',
    location: 'Living Room',
    state: 'ON' as const,
    deviceType: 'FAN' as const,
    ...overrides,
    attributes: {
      speed: 'MEDIUM' as Object,
      ...(overrides.attributes ?? {}),
    },
  });

  it('renders correctly when On - shows speed buttons with active state', () => {
    const device = createMockDevice({ state: 'ON', attributes: { speed: 'HIGH' as Object } });

    render(<Fan device={device} />);

    expect(screen.getByText('Power')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /turn off/i })).toBeInTheDocument();

    expect(screen.getByText('Speed')).toBeInTheDocument();

    const lowButton = screen.getByRole('button', { name: /low/i });
    const mediumButton = screen.getByRole('button', { name: /medium/i });
    const highButton = screen.getByRole('button', { name: /high/i });

    expect(lowButton).not.toBeDisabled();
    expect(mediumButton).not.toBeDisabled();
    expect(highButton).not.toBeDisabled();

    // High should be the active one by label
    expect(highButton).toHaveTextContent(/high/i);
    expect(lowButton).toHaveTextContent(/low/i);
    expect(mediumButton).toHaveTextContent(/medium/i);
  });

  it('renders correctly when Off - speed buttons disabled', () => {
    const device = createMockDevice({ state: 'OFF' });

    render(<Fan device={device} />);

    expect(screen.getByRole('button', { name: /turn on/i })).toBeInTheDocument();

    const lowButton = screen.getByRole('button', { name: /low/i });
    const mediumButton = screen.getByRole('button', { name: /medium/i });
    const highButton = screen.getByRole('button', { name: /high/i });

    expect(lowButton).toBeDisabled();
    expect(mediumButton).toBeDisabled();
    expect(highButton).toBeDisabled();
  });

  it('toggles power successfully', async () => {
    const device = createMockDevice({ state: 'OFF' });

    render(<Fan device={device} />);

    const toggleButton = screen.getByRole('button', { name: /turn on/i });
    fireEvent.click(toggleButton);

    await waitFor(() => {
      expect(mockPerformDeviceAction).toHaveBeenCalledWith('fan-1', {
        action: 'TOGGLE_POWER',
        parameters: []
      });
    });
  });

  it('changes speed when speed button is clicked', async () => {
    const device = createMockDevice({ state: 'ON', attributes: { speed: 'LOW' as Object } });

    render(<Fan device={device} />);

    const mediumButton = screen.getByRole('button', { name: /medium/i });
    fireEvent.click(mediumButton);

    await waitFor(() => {
      expect(mockPerformDeviceAction).toHaveBeenCalledWith('fan-1', {
        action: 'SET_SPEED_MEDIUM',
        parameters: []
      });
    });
  });

  it('does not change speed when fan is off', () => {
    const device = createMockDevice({ state: 'OFF' });

    render(<Fan device={device} />);

    const mediumButton = screen.getByRole('button', { name: /medium/i });
    expect(mediumButton).toBeDisabled();

    fireEvent.click(mediumButton);

    expect(mockPerformDeviceAction).not.toHaveBeenCalled();
  });
});





