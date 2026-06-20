import React from 'react';
import ReactDOM from 'react-dom/client';

function App() {
  return React.createElement('main', null, 'Authorization PoC frontend scaffold');
}

ReactDOM.createRoot(document.getElementById('root')).render(
  React.createElement(React.StrictMode, null, React.createElement(App))
);
