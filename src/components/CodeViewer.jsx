import React, { useState } from 'react';

export default function CodeViewer({ code, onReset }) {
  const [copied, setCopied] = useState(false);

  const handleCopyToLaptop = async () => {
    try {
      await navigator.clipboard.writeText(code);
      setCopied(true);
      setTimeout(() => setCopied(false), 3000);
    } catch (err) {
      console.error("Failed to copy code", err);
    }
  };

  const handleDownload = () => {
    const blob = new Blob([code], { type: 'text/javascript' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'GeneratedComponent.jsx';
    a.click();
    URL.revokeObjectURL(url);
  };

  // Very basic pseudo-syntax highlighting for the demo
  const highlightCode = (rawCode) => {
    return rawCode
      .replace(/import|export|default|const|return|function/g, '<span style="color: #ff7b72;">$&</span>')
      .replace(/from|useState/g, '<span style="color: #d2a8ff;">$&</span>')
      .replace(/<[/]?[a-zA-Z0-9]+>?/g, '<span style="color: #7ee787;">$&</span>')
      .replace(/('.*?'|".*?")/g, '<span style="color: #a5d6ff;">$&</span>')
      .replace(/ className=| type=| value=| onChange=| onSubmit=| placeholder=/g, '<span style="color: #79c0ff;">$&</span>')
      .replace(/\/\/.*/g, '<span style="color: #8b949e;">$&</span>');
  };

  return (
    <div className="glass-panel animate-in" style={styles.container}>
      <div style={styles.ideHeader}>
        <div style={styles.macButtons}>
          <div style={{...styles.macBtn, backgroundColor: '#ff5f56'}}></div>
          <div style={{...styles.macBtn, backgroundColor: '#ffbd2e'}}></div>
          <div style={{...styles.macBtn, backgroundColor: '#27c93f'}}></div>
        </div>
        <div style={styles.tab}>
          <span style={{color: '#00e5ff'}}>react</span> GeneratedLoginScreen.jsx
        </div>
      </div>

      <div style={styles.ideBody}>
        <div style={styles.lineNumbers} className="code-font">
          {code.split('\n').map((_, i) => <div key={i}>{i + 1}</div>)}
        </div>
        <pre style={styles.codeBlock} className="code-font">
          <code dangerouslySetInnerHTML={{ __html: highlightCode(code) }}></code>
        </pre>
      </div>

      <div style={styles.actions}>
        <button onClick={handleCopyToLaptop} className="primary" style={styles.primaryAction}>
          {copied ? '✓ Synced to Laptop' : 'Sync via Office Kit'}
        </button>
        <button onClick={handleDownload} style={styles.secondaryAction}>
          Download .jsx File
        </button>
      </div>
      
      <div style={styles.footer}>
        <button onClick={onReset} style={styles.textAction}>
          ← Scan another wireframe
        </button>
        <span style={styles.officeKitBadge}>Office Kit Bridge Active</span>
      </div>
    </div>
  );
}

const styles = {
  container: {
    display: 'flex',
    flexDirection: 'column',
    width: '100%',
    maxWidth: '800px',
    margin: '0 auto',
    padding: '0',
    backgroundColor: '#0d1117', // GitHub Dark Dimmed background
    borderColor: '#30363d',
    overflow: 'hidden'
  },
  ideHeader: {
    display: 'flex',
    alignItems: 'center',
    backgroundColor: '#010409',
    padding: '10px 16px',
    borderBottom: '1px solid #21262d'
  },
  macButtons: {
    display: 'flex',
    gap: '8px',
    marginRight: '20px'
  },
  macBtn: {
    width: '12px',
    height: '12px',
    borderRadius: '50%'
  },
  tab: {
    backgroundColor: '#0d1117',
    padding: '8px 16px',
    borderRadius: '8px 8px 0 0',
    fontSize: '0.85em',
    color: '#c9d1d9',
    border: '1px solid #21262d',
    borderBottom: 'none',
    display: 'flex',
    gap: '8px',
    transform: 'translateY(1px)' // cover bottom border
  },
  ideBody: {
    display: 'flex',
    padding: '16px 0',
    maxHeight: '500px',
    overflowY: 'auto',
    backgroundColor: '#0d1117'
  },
  lineNumbers: {
    padding: '0 16px',
    color: '#484f58',
    textAlign: 'right',
    userSelect: 'none',
    fontSize: '0.9em',
    lineHeight: '1.5'
  },
  codeBlock: {
    margin: 0,
    fontSize: '0.9em',
    color: '#c9d1d9',
    whiteSpace: 'pre-wrap',
    lineHeight: '1.5',
    flex: 1
  },
  actions: {
    display: 'flex',
    gap: '16px',
    padding: '24px',
    borderTop: '1px solid #21262d',
    backgroundColor: '#010409'
  },
  primaryAction: {
    flex: 1,
    padding: '16px'
  },
  secondaryAction: {
    flex: 1,
    backgroundColor: 'transparent',
    color: '#c9d1d9',
    borderColor: '#30363d'
  },
  footer: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: '0 24px 24px 24px',
    backgroundColor: '#010409'
  },
  textAction: {
    backgroundColor: 'transparent',
    color: '#8b949e',
    boxShadow: 'none',
    border: 'none',
    padding: 0,
    fontSize: '0.9em'
  },
  officeKitBadge: {
    fontSize: '0.75em',
    color: '#00e5ff',
    padding: '4px 8px',
    border: '1px solid rgba(0, 229, 255, 0.3)',
    borderRadius: '12px',
    backgroundColor: 'rgba(0, 229, 255, 0.05)'
  }
};
