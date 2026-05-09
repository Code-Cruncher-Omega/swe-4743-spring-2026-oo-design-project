import { useEffect, useState } from 'react';

import { Button } from 'primereact/button';
import { Card } from 'primereact/card';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';

import { AuditEntry, getDeviceHistory, clearDeviceHistory } from '../api/SmartHomeAPI';

export function DeviceHistory() {
  const [history, setHistory] = useState<AuditEntry[]>([]);
  const [show, setShow] = useState(false);

  const fetchHistory = async () => {
    getDeviceHistory().then(setHistory);
  };

  useEffect(() => {
    if(!show) return;
      fetchHistory();
      const interval = setInterval(fetchHistory, 5000);
      return () => clearInterval(interval);
    }, [show]);

    const handleClear = async () => {
      await clearDeviceHistory();
      setHistory([]);
  };

  return (
        <Card className="mt-3">
            <div className="flex align-items-center justify-content-between mb-3">
                <h3 className="m-0">Device History</h3>
                <div className="flex gap-2">
                    <Button
                        label={show ? 'Hide' : 'Show'}
                        icon={show ? 'pi pi-chevron-up' : 'pi pi-chevron-down'}
                        severity="secondary"
                        onClick={() => setShow(prev => !prev)}
                    />
                    {show && (
                        <Button
                            label="Clear"
                            icon="pi pi-trash"
                            severity="danger"
                            onClick={handleClear}
                        />
                    )}
                </div>
            </div>

            {show && (
                <DataTable
                    value={history}
                    emptyMessage="No history yet."
                    stripedRows
                    size="small"
                    scrollable
                >
                    <Column
                        header="Timestamp"
                        body={(entry: AuditEntry) => new Date(entry.timestamp).toLocaleString()}
                    />
                    <Column field="id" header="Device ID" />
                    <Column field="operation" header="Operation" />
                </DataTable>
            )}
        </Card>
    );
}