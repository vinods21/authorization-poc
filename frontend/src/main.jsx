import React, { useEffect, useMemo, useState } from 'react';
import ReactDOM from 'react-dom/client';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

function useSession() {
  const [token, setToken] = useState(null);
  const [user, setUser] = useState(null);
  const [status, setStatus] = useState('signed-out');

  useEffect(() => {
    const stored = localStorage.getItem('authorization-poc-session');
    if (stored) {
      const session = JSON.parse(stored);
      setToken(session.token);
      setUser(session.user);
      setStatus('signed-in');
    }
  }, []);

  const saveSession = (session) => {
    localStorage.setItem('authorization-poc-session', JSON.stringify(session));
    setToken(session.token);
    setUser(session.user);
    setStatus('signed-in');
  };

  const login = () => {
    saveSession({
      token: 'dev-token',
      user: {
        userId: 'teacher-a',
        tenantCode: 'school-a',
        displayName: 'Teacher A',
        roles: ['teacher'],
        features: {
          canManageSchool: false,
          canCreateAssignment: true,
          canViewReports: true
        }
      }
    });
  };

  const logout = () => {
    localStorage.removeItem('authorization-poc-session');
    setToken(null);
    setUser(null);
    setStatus('signed-out');
  };

  return { token, user, status, login, logout, saveSession };
}

async function apiGet(path, token) {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: token ? { Authorization: `Bearer ${token}` } : {}
  });
  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(`${response.status}: ${errorText || response.statusText}`);
  }
  return response.json();
}

function App() {
  const { token, user, status, login, logout, saveSession } = useSession();
  const [classes, setClasses] = useState([]);
  const [selectedClass, setSelectedClass] = useState(null);
  const [studentProfile, setStudentProfile] = useState(null);
  const [report, setReport] = useState(null);
  const [message, setMessage] = useState('Ready');
  const [loading, setLoading] = useState(false);

  const features = useMemo(() => user?.features || {}, [user]);

  useEffect(() => {
    if (!token) {
      setClasses([]);
      setSelectedClass(null);
      setStudentProfile(null);
      setReport(null);
      return;
    }

    let mounted = true;
    setLoading(true);
    setMessage('Loading backend data');

    Promise.all([
      apiGet('/api/me', token),
      apiGet('/api/classes', token)
    ])
      .then(([me, classList]) => {
        if (!mounted) return;
        setUserFromMe(me);
        setClasses(classList);
        setSelectedClass(classList[0] || null);
        setMessage('Loaded backend data');
      })
      .catch((error) => {
        if (!mounted) return;
        setMessage(error.message);
      })
      .finally(() => {
        if (mounted) setLoading(false);
      });

    return () => {
      mounted = false;
    };
  }, [token]);

  useEffect(() => {
    if (!token || !selectedClass) return;
    let mounted = true;
    setLoading(true);
    apiGet(`/api/classes/${selectedClass.id}`, token)
      .then((classDetails) => {
        if (!mounted) return;
        setSelectedClass(classDetails);
        setMessage('Loaded class details');
      })
      .catch((error) => {
        if (mounted) setMessage(error.message);
      })
      .finally(() => {
        if (mounted) setLoading(false);
      });
    return () => {
      mounted = false;
    };
  }, [token, selectedClass?.id]);

  function setUserFromMe(me) {
    saveSession({
      token,
      user: {
        userId: me.userId,
        tenantCode: me.tenantCode,
        displayName: me.displayName,
        roles: me.roles,
        features: me.features
      }
    });
  }

  const loadStudent = () => {
    if (!token) return;
    setLoading(true);
    apiGet('/api/students/44444444-4444-4444-4444-444444444441', token)
      .then(setStudentProfile)
      .catch((error) => setMessage(error.message))
      .finally(() => setLoading(false));
  };

  const loadReport = () => {
    if (!token) return;
    setLoading(true);
    apiGet('/api/reports/66666666-6666-6666-6666-666666666661', token)
      .then(setReport)
      .catch((error) => setMessage(error.message))
      .finally(() => setLoading(false));
  };

  return (
    <div style={styles.shell}>
      <header style={styles.header}>
        <div>
          <div style={styles.brand}>Authorization PoC</div>
          <div style={styles.subtle}>Multi-tenant school SaaS</div>
        </div>
        <div style={styles.actions}>
          {status === 'signed-in' ? (
            <button style={styles.button} onClick={logout}>Logout</button>
          ) : (
            <button style={styles.button} onClick={login}>Login</button>
          )}
        </div>
      </header>

      <main style={styles.grid}>
        <section style={styles.panel}>
          <h2 style={styles.h2}>Current User</h2>
          {user ? (
            <dl style={styles.dl}>
              <dt>User</dt><dd>{user.userId}</dd>
              <dt>Tenant</dt><dd>{user.tenantCode}</dd>
              <dt>Display</dt><dd>{user.displayName}</dd>
              <dt>Roles</dt><dd>{user.roles.join(', ')}</dd>
            </dl>
          ) : (
            <p style={styles.subtle}>Not signed in.</p>
          )}
        </section>

        <section style={styles.panel}>
          <h2 style={styles.h2}>Tenant View</h2>
          <p style={styles.subtle}>{user ? `Scoped to ${user.tenantCode}` : 'No tenant context.'}</p>
          <div style={styles.chips}>
            <span style={styles.chip}>{features.canManageSchool ? 'Manage school' : 'Read only'}</span>
            <span style={styles.chip}>{features.canCreateAssignment ? 'Can create assignment' : 'Cannot create assignment'}</span>
            <span style={styles.chip}>{features.canViewReports ? 'Can view reports' : 'Cannot view reports'}</span>
          </div>
        </section>

        <section style={styles.panel}>
          <h2 style={styles.h2}>Classes</h2>
          <div style={styles.list}>
            {classes.map((item) => (
              <button
                key={item.id}
                style={selectedClass?.id === item.id ? styles.listItemActive : styles.listItem}
                onClick={() => setSelectedClass(item)}
              >
                <div>{item.code}</div>
                <div style={styles.subtle}>{item.name}</div>
              </button>
            ))}
          </div>
        </section>

        <section style={styles.panel}>
          <h2 style={styles.h2}>Class Details</h2>
          {selectedClass ? (
            <dl style={styles.dl}>
              <dt>Code</dt><dd>{selectedClass.code}</dd>
              <dt>Name</dt><dd>{selectedClass.name}</dd>
              <dt>Section</dt><dd>{selectedClass.section}</dd>
            </dl>
          ) : (
            <p style={styles.subtle}>Select a class.</p>
          )}
        </section>

        <section style={styles.panel}>
          <h2 style={styles.h2}>Assignment Creation</h2>
          <div style={styles.form}>
            <input style={styles.input} placeholder="Assignment code" />
            <input style={styles.input} placeholder="Title" />
            <textarea style={styles.textarea} placeholder="Description" rows={3} />
            <button style={styles.button} disabled={!features.canCreateAssignment}>Create Assignment</button>
          </div>
        </section>

        <section style={styles.panel}>
          <h2 style={styles.h2}>Student and Report</h2>
          <div style={styles.form}>
            <button style={styles.button} onClick={loadStudent}>Open Student Profile</button>
            <button style={styles.button} onClick={loadReport}>Open Report</button>
          </div>
          {studentProfile ? (
            <p style={styles.subtle}>Student loaded: {studentProfile.code}</p>
          ) : null}
          {report ? (
            <p style={styles.subtle}>Report loaded: {report.code}</p>
          ) : null}
        </section>

        <section style={styles.panelWide}>
          <h2 style={styles.h2}>Access Denied</h2>
          <p style={styles.subtle}>Backend returns HTTP 403 for unauthorized operations. UI state is advisory only.</p>
          <p style={styles.status}>{message}</p>
          <p style={styles.subtle}>API base: {API_BASE_URL}</p>
          <p style={styles.subtle}>Token: {token ? 'present' : 'missing'}</p>
          <p style={styles.subtle}>Loading: {loading ? 'yes' : 'no'}</p>
        </section>
      </main>
    </div>
  );
}

const styles = {
  shell: { fontFamily: 'system-ui, sans-serif', color: '#e5eef8', background: '#101821', minHeight: '100vh', padding: 24 },
  header: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 20 },
  brand: { fontSize: 24, fontWeight: 700 },
  subtle: { color: '#9fb0c2', marginTop: 4 },
  actions: { display: 'flex', gap: 8 },
  button: { background: '#d7e3f4', color: '#102030', border: 'none', padding: '10px 14px', borderRadius: 6, cursor: 'pointer' },
  grid: { display: 'grid', gridTemplateColumns: 'repeat(2, minmax(0, 1fr))', gap: 16 },
  panel: { background: '#172230', border: '1px solid #263549', borderRadius: 8, padding: 16 },
  panelWide: { gridColumn: '1 / -1', background: '#172230', border: '1px solid #263549', borderRadius: 8, padding: 16 },
  h2: { margin: '0 0 12px', fontSize: 18 },
  dl: { display: 'grid', gridTemplateColumns: '140px 1fr', gap: '8px 12px', margin: 0 },
  chips: { display: 'flex', flexWrap: 'wrap', gap: 8 },
  chip: { background: '#24354a', color: '#d7e3f4', borderRadius: 999, padding: '6px 10px', fontSize: 12 },
  list: { display: 'grid', gap: 8 },
  listItem: { textAlign: 'left', background: '#213246', color: '#e5eef8', border: '1px solid #31465f', borderRadius: 6, padding: 10 },
  listItemActive: { textAlign: 'left', background: '#38506d', color: '#fff', border: '1px solid #5f86b6', borderRadius: 6, padding: 10 },
  form: { display: 'grid', gap: 8 },
  input: { background: '#0f1720', color: '#e5eef8', border: '1px solid #31465f', borderRadius: 6, padding: 10 },
  textarea: { background: '#0f1720', color: '#e5eef8', border: '1px solid #31465f', borderRadius: 6, padding: 10, resize: 'vertical' },
  status: { marginTop: 8, color: '#d7e3f4' }
};

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
