# Smart Home API Collection

This is a Bruno API collection for testing the Smart Home application endpoints.

## Structure

The collection is organized into the following categories:

### Devices
- **Create Device** - POST `/api/smarthome/devices` - Create a new smart home device
- **Delete Device** - DELETE `/api/smarthome/devices/{id}` - Delete a device by ID
- **Get Updateable Devices** - GET `/api/smarthome/devices` - Get list of devices that can be updated

### Device Actions
- **Toggle Light Power** - POST `/api/smarthome/devices/{id}/actions` - Toggle device power state
- **Set Light Brightness** - POST `/api/smarthome/devices/{id}/actions` - Set brightness level for lights

### Environments
- **Query Environments** - POST `/api/smarthome/environments/status` - Query environment statuses with filtering
- **Get Environment Names** - GET `/api/smarthome/environments` - Get all available environment names
- **Set Ambient Temperature** - POST `/api/smarthome/environments/{name}/ambient` - Set ambient temperature for an environment

### History
- **Get Device History** - GET `/api/smarthome/devices/history` - Retrieve audit log of device actions
- **Clear Device History** - DELETE `/api/smarthome/devices/history` - Clear audit log

### Simulation
- **Update Simulation** - POST `/api/smarthome/simulation/update` - Update simulation tick rate
- **Reset All Devices** - POST `/api/smarthome/devices/reset` - Reset all devices to initial state

## Environment Variables

The collection uses the following environment variables:

- `base_url` - Base URL (default: `http://localhost:8080`)
- `api_base` - API base URL (default: `http://localhost:8080/api/smarthome`)

## Usage

1. Import the collection into Bruno
2. Set the `local` environment or create your own with the required variables
3. Run individual requests or execute the entire collection
4. View test results for assertions on status codes and response structure

## Running Tests

Each request includes basic tests for:
- Status code validation
- Response structure validation
- Success flags

Run tests individually or batch test the entire collection to validate API functionality.
