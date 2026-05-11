import { ReactNode } from 'react';
import { act, render, waitFor } from '@testing-library/react';
import { vi, describe, it, expect, beforeEach, afterEach } from 'vitest';
import { Simulation } from '../Simulation';
import { EnvironmentStatus } from '../../api/SmartHomeAPI';
import * as SmartHomeAPI from '../../api/SmartHomeAPI';

// Mock the API and components
vi.mock('../../api/SmartHomeAPI', () => ({
  queryEnvironments: vi.fn(),
  updateSimulation: vi.fn(),
}));

vi.mock('../Environment', () => ({
  EnvironmentList: ({ environmentStatuses }: { environmentStatuses: EnvironmentStatus[] }) => (
    <div data-testid="environment-list">
      {environmentStatuses.map(env => (
        <div key={env.name} data-testid={`environment-${env.name}`}>
          {env.name}: {env.deviceCount} devices
        </div>
      ))}
    </div>
  ),
}));

vi.mock('../RefreshContext', () => ({
  default: {
    Provider: ({ children }: { children: ReactNode }) => <div>{children}</div>,
  },
}));

const mockQueryEnvironments = vi.mocked(SmartHomeAPI.queryEnvironments);
const mockUpdateSimulation = vi.mocked(SmartHomeAPI.updateSimulation);

describe('Simulation Component', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  afterEach(() => {
    vi.useRealTimers();
  });

  const mockEnvironments: EnvironmentStatus[] = [
    {
      name: 'Living Room',
      deviceCount: 2,
      devices: [
        {
          id: 'light-1',
          name: 'Living Room Light',
          location: 'Living Room',
          state: 'ON' as const,
          deviceType: 'LIGHT' as const,
          attributes: { brightness: 80 as Object },
        },
        {
          id: 'fan-1',
          name: 'Living Room Fan',
          location: 'Living Room',
          state: 'OFF' as const,
          deviceType: 'FAN' as const,
          attributes: { speed: 'MEDIUM' as Object },
        },
      ],
    },
  ];

  it('loads environments on component mount', async () => {
    mockQueryEnvironments.mockResolvedValue(mockEnvironments);

    const mockOnRefreshReady = vi.fn();

    render(
      <Simulation
        timeMultiplier={1}
        filter={{ location: null, activity: null, deviceType: null }}
        onRefreshReady={mockOnRefreshReady}
      />
    );

    await waitFor(() => {
      expect(mockQueryEnvironments).toHaveBeenCalledWith({
        location: null,
        activity: null,
        deviceType: null,
      });
    });

    expect(mockOnRefreshReady).toHaveBeenCalled();
  });

  it('updates environments when filter changes', async () => {
    mockQueryEnvironments.mockResolvedValue(mockEnvironments);

    const { rerender } = render(
      <Simulation
        timeMultiplier={1}
        filter={{ location: null, activity: null, deviceType: null }}
        onRefreshReady={vi.fn()}
      />
    );

    await waitFor(() => {
      expect(mockQueryEnvironments).toHaveBeenCalledTimes(1);
    });

    // Change filter
    rerender(
      <Simulation
        timeMultiplier={1}
        filter={{ location: 'Living Room', activity: null, deviceType: null }}
        onRefreshReady={vi.fn()}
      />
    );

    await waitFor(() => {
      expect(mockQueryEnvironments).toHaveBeenCalledWith({
        location: 'Living Room',
        activity: null,
        deviceType: null,
      });
    });
  });

  it('updates simulation periodically', async () => {
    mockQueryEnvironments.mockResolvedValue(mockEnvironments);
    mockUpdateSimulation.mockResolvedValue(undefined);

    render(
      <Simulation
        timeMultiplier={1}
        filter={{ location: null, activity: null, deviceType: null }}
        onRefreshReady={vi.fn()}
        updateIntervalMs={50}
      />
    );

    await waitFor(() => {
      expect(mockQueryEnvironments).toHaveBeenCalledTimes(1);
    });

    await waitFor(() => {
      expect(mockUpdateSimulation).toHaveBeenCalledWith(1);
      expect(mockQueryEnvironments).toHaveBeenCalledTimes(2);
    });
  });

  it('adjusts simulation speed based on timeMultiplier', async () => {
    mockQueryEnvironments.mockResolvedValue(mockEnvironments);
    mockUpdateSimulation.mockResolvedValue(undefined);

    const { rerender } = render(
      <Simulation
        timeMultiplier={2}
        filter={{ location: null, activity: null, deviceType: null }}
        onRefreshReady={vi.fn()}
        updateIntervalMs={50}
      />
    );

    await waitFor(() => {
      expect(mockQueryEnvironments).toHaveBeenCalledTimes(1);
    });

    await waitFor(() => {
      expect(mockUpdateSimulation).toHaveBeenCalledWith(2);
    });

    // Change time multiplier
    rerender(
      <Simulation
        timeMultiplier={5}
        filter={{ location: null, activity: null, deviceType: null }}
        onRefreshReady={vi.fn()}
        updateIntervalMs={50}
      />
    );

    await waitFor(() => {
      expect(mockUpdateSimulation).toHaveBeenCalledWith(5);
    });
  });

  it('provides refresh function to parent component', async () => {
    mockQueryEnvironments.mockResolvedValue(mockEnvironments);

    const mockOnRefreshReady = vi.fn();

    render(
      <Simulation
        timeMultiplier={1}
        filter={{ location: null, activity: null, deviceType: null }}
        onRefreshReady={mockOnRefreshReady}
      />
    );

    await waitFor(() => {
      expect(mockOnRefreshReady).toHaveBeenCalled();
    });

    const refreshFunction = mockOnRefreshReady.mock.calls[0][0];
    expect(typeof refreshFunction).toBe('function');

    // Call the refresh function
    mockQueryEnvironments.mockClear();
    await act(async () => {
      await refreshFunction();
    });

    await waitFor(() => {
      expect(mockQueryEnvironments).toHaveBeenCalledWith({
        location: null,
        activity: null,
        deviceType: null,
      });
    });
  });

  it('handles API errors gracefully', async () => {
    mockQueryEnvironments.mockRejectedValue(new Error('API Error'));

    // Mock console.error to avoid test output pollution
    const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {});

    render(
      <Simulation
        timeMultiplier={1}
        filter={{ location: null, activity: null, deviceType: null }}
        onRefreshReady={vi.fn()}
      />
    );

    // Component should not crash, error should be logged
    await waitFor(() => {
      expect(consoleSpy).toHaveBeenCalledWith(expect.any(Error));
    });

    consoleSpy.mockRestore();
  });

  it('renders EnvironmentList with loaded data', async () => {
    mockQueryEnvironments.mockResolvedValue(mockEnvironments);

    const { getByTestId } = render(
      <Simulation
        timeMultiplier={1}
        filter={{ location: null, activity: null, deviceType: null }}
        onRefreshReady={vi.fn()}
      />
    );

    await waitFor(() => {
      expect(getByTestId('environment-Living Room')).toBeInTheDocument();
      expect(getByTestId('environment-Living Room')).toHaveTextContent('Living Room: 2 devices');
    });
  });
});





