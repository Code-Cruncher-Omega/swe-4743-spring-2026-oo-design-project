import { render, screen } from '@testing-library/react';
import { vi, describe, it, expect } from 'vitest';
import { DeviceRenderer } from '../DeviceRenderer';
import { DeviceStatus } from '../../api/SmartHomeAPI';

// Mock all device components
vi.mock('../Light', () => ({
  Light: ({ device }: { device: DeviceStatus }) => <div data-testid="light-component">{device.name}</div>,
}));

vi.mock('../Fan', () => ({
  Fan: ({ device }: { device: DeviceStatus }) => <div data-testid="fan-component">{device.name}</div>,
}));

vi.mock('../Thermostat', () => ({
  Thermostat: ({ device }: { device: DeviceStatus }) => <div data-testid="thermostat-component">{device.name}</div>,
}));

vi.mock('../DoorLock', () => ({
  DoorLock: ({ device }: { device: DeviceStatus }) => <div data-testid="doorlock-component">{device.name}</div>,
}));

describe('DeviceRenderer Component', () => {
  it('renders Light component for LIGHT device type', () => {
    const device: DeviceStatus = {
      id: 'light-1',
      name: 'Test Light',
      location: 'Living Room',
      state: 'ON' as const,
      deviceType: 'LIGHT' as const,
      attributes: { brightness: 80 as Object, red: 216 as Object, green: 190 as Object, blue: 216 as Object },
    };

    render(<DeviceRenderer device={device} />);

    expect(screen.getByTestId('light-component')).toBeInTheDocument();
    expect(screen.getByText('Test Light')).toBeInTheDocument();
  });

  it('renders Fan component for FAN device type', () => {
    const device: DeviceStatus = {
      id: 'fan-1',
      name: 'Test Fan',
      location: 'Living Room',
      state: 'ON' as const,
      deviceType: 'FAN' as const,
      attributes: { speed: 'MEDIUM' as Object },
    };

    render(<DeviceRenderer device={device} />);

    expect(screen.getByTestId('fan-component')).toBeInTheDocument();
    expect(screen.getByText('Test Fan')).toBeInTheDocument();
  });

  it('renders Thermostat component for THERMOSTAT device type', () => {
    const device: DeviceStatus = {
      id: 'thermostat-1',
      name: 'Test Thermostat',
      location: 'Living Room',
      state: 'IDLE' as const,
      deviceType: 'THERMOSTAT' as const,
      attributes: { mode: 'AUTO' as Object, desired: 72 as Object, ambient: 70 as Object },
    };

    render(<DeviceRenderer device={device} />);

    expect(screen.getByTestId('thermostat-component')).toBeInTheDocument();
    expect(screen.getByText('Test Thermostat')).toBeInTheDocument();
  });

  it('renders DoorLock component for DOOR_LOCK device type', () => {
    const device: DeviceStatus = {
      id: 'doorlock-1',
      name: 'Test Door Lock',
      location: 'Front Door',
      state: 'LOCKED' as const,
      deviceType: 'DOOR_LOCK' as const,
      attributes: {},
    };

    render(<DeviceRenderer device={device} />);

    expect(screen.getByTestId('doorlock-component')).toBeInTheDocument();
    expect(screen.getByText('Test Door Lock')).toBeInTheDocument();
  });

  it('renders unknown device type message for unsupported device type', () => {
    const device: DeviceStatus = {
      id: 'unknown-1',
      name: 'Unknown Device',
      location: 'Living Room',
      state: 'ON' as const,
      deviceType: 'UNKNOWN' as any,
      attributes: {},
    };

    render(<DeviceRenderer device={device} />);

    expect(screen.getByText('Unknown device type: UNKNOWN')).toBeInTheDocument();
  });

  it('passes device prop correctly to rendered component', () => {
    const device: DeviceStatus = {
      id: 'light-1',
      name: 'Specific Light Name',
      location: 'Living Room',
      state: 'ON' as const,
      deviceType: 'LIGHT' as const,
      attributes: { brightness: 50 as Object },
    };

    render(<DeviceRenderer device={device} />);

    expect(screen.getByText('Specific Light Name')).toBeInTheDocument();
  });
});



