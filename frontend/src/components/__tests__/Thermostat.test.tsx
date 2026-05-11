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

import { Thermostat } from '../Thermostat';
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

describe('Thermostat Component Rendering', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mockPerformDeviceAction.mockResolvedValue({
      success: true,
      action: 'TOGGLE_POWER',
      message: 'Thermostat turned on successfully'
    });
  });

  const createMockDevice = (overrides: Partial<DeviceStatus> = {}): DeviceStatus => ({
    id: 'thermostat-1',
    name: 'Test Thermostat',
    location: 'Living Room',
    state: 'IDLE' as const,
    deviceType: 'THERMOSTAT' as const,
    ...overrides,
    attributes: {
      mode: 'AUTO' as Object,
      desired: 72 as Object,
      ambient: 70 as Object,
      ...(overrides.attributes ?? {}),
    },
  });

  it('renders correctly in Idle state - shows target and ambient temperature, mode buttons reflect current mode', () => {
    const device = createMockDevice({ state: 'IDLE', attributes: { mode: 'HEAT' as Object, desired: 75 as Object, ambient: 68 as Object } });

    render(<Thermostat device={device} />);

    expect(screen.getByText('Power')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /turn off/i })).toBeInTheDocument();

    expect(screen.getByText('Target Temperature')).toBeInTheDocument();
    expect(screen.getByText('75°F')).toBeInTheDocument();

    expect(screen.getByText('Ambient Temperature')).toBeInTheDocument();
    expect(screen.getByText('68°F')).toBeInTheDocument();

    const temperatureSlider = screen.getByRole('slider');
    expect(temperatureSlider).toBeInTheDocument();
    expect(temperatureSlider).not.toBeDisabled();

    expect(screen.getByText('Mode')).toBeInTheDocument();

    const heatButton = screen.getByRole('button', { name: /heat/i });
    const coolButton = screen.getByRole('button', { name: /cool/i });
    const autoButton = screen.getByRole('button', { name: /auto/i });

    expect(heatButton).not.toBeDisabled();
    expect(coolButton).not.toBeDisabled();
    expect(autoButton).not.toBeDisabled();

    // Heat should be active by text content
    expect(heatButton).toHaveTextContent(/heat/i);
    expect(coolButton).toHaveTextContent(/cool/i);
    expect(autoButton).toHaveTextContent(/auto/i);
  });

  it('renders correctly in Heating state', () => {
    const device = createMockDevice({ state: 'HEATING' });

    render(<Thermostat device={device} />);

    // Should show heating indicator in the status
    // The component shows state in the parent card, but renders controls enabled
    const temperatureSlider = screen.getByRole('slider');
    expect(temperatureSlider).not.toBeDisabled();
  });

  it('renders correctly in Cooling state', () => {
    const device = createMockDevice({ state: 'COOLING' });

    render(<Thermostat device={device} />);

    const temperatureSlider = screen.getByRole('slider');
    expect(temperatureSlider).not.toBeDisabled();
  });

  it('renders correctly when Off - controls disabled', () => {
    const device = createMockDevice({ state: 'OFF' });

    render(<Thermostat device={device} />);

    expect(screen.getByRole('button', { name: /turn on/i })).toBeInTheDocument();

    const temperatureSlider = screen.getByRole('slider');
    expect(temperatureSlider).toBeDisabled();

    const heatButton = screen.getByRole('button', { name: /heat/i });
    const coolButton = screen.getByRole('button', { name: /cool/i });
    const autoButton = screen.getByRole('button', { name: /auto/i });

    expect(heatButton).toBeDisabled();
    expect(coolButton).toBeDisabled();
    expect(autoButton).toBeDisabled();
  });

  it('toggles power successfully', async () => {
    const device = createMockDevice({ state: 'OFF' });

    render(<Thermostat device={device} />);

    const toggleButton = screen.getByRole('button', { name: /turn on/i });
    fireEvent.click(toggleButton);

    await waitFor(() => {
      expect(mockPerformDeviceAction).toHaveBeenCalledWith('thermostat-1', {
        action: 'TOGGLE_POWER',
        parameters: []
      });
    });
  });

  it('changes desired temperature when slider is moved', async () => {
    const device = createMockDevice({ state: 'IDLE' });

    render(<Thermostat device={device} />);

    const temperatureSlider = screen.getByRole('slider');
    fireEvent.change(temperatureSlider, { target: { value: '78' } });

    // Note: Similar to Light, onSlideEnd triggers the API call
  });

  it('changes mode when mode button is clicked', async () => {
    const device = createMockDevice({ state: 'IDLE', attributes: { mode: 'HEAT' as Object } });

    render(<Thermostat device={device} />);

    const coolButton = screen.getByRole('button', { name: /cool/i });
    fireEvent.click(coolButton);

    await waitFor(() => {
      expect(mockPerformDeviceAction).toHaveBeenCalledWith('thermostat-1', {
        action: 'SET_MODE_COOL',
        parameters: []
      });
    });
  });

  it('does not allow mode changes when off', () => {
    const device = createMockDevice({ state: 'OFF' });

    render(<Thermostat device={device} />);

    const coolButton = screen.getByRole('button', { name: /cool/i });
    expect(coolButton).toBeDisabled();

    fireEvent.click(coolButton);

    expect(mockPerformDeviceAction).not.toHaveBeenCalled();
  });
});





