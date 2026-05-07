import { useEffect, useState } from 'react';
import { AuditEntry, getDeviceHistory, clearDeviceHistory } from '../api/SmartHomeAPI';

export function DeviceHistory() {
    const [history, setHistory] = useState<AuditEntry[]>([]);
    const [show, setShow] = useState(false);

    const fetchHistory = async () => {
        getDeviceHistory().then(setHistory);
    };

    useEffect(() => {
        if (!show) return;
        fetchHistory();

        const interval = setInterval(fetchHistory, 5000);
        return () => clearInterval(interval);
    }, [show]);

    const handleClear = async () => {
        await clearDeviceHistory();
        setHistory([]);
    };

    return (
        <div>
            <button onClick={() => setShow(prev => !prev)}>
                {show ? 'Hide History' : 'Show History'}
            </button>
            {show && (
                <>
                    <button onClick={handleClear}>Clear History</button>
                    <ul>
                        {history.length === 0 && <li>No history yet.</li>}
                        {history.map((entry, i) => (
                            <li key={i}>
                                <strong>{new Date(entry.timestamp).toLocaleString()}</strong> — {entry.id} — {entry.operation}
                            </li>
                        ))}
                    </ul>
                </>
            )}
        </div>
    );
}