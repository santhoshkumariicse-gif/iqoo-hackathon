import React, { useState } from 'react';
import CameraView from './components/CameraView';
import CodeViewer from './components/CodeViewer';
import { analyzeWireframe } from './services/aiMock';
import './index.css';

function App() {
  const [appState, setAppState] = useState('idle'); // 'idle', 'scanning', 'processing', 'completed'
  const [generatedCode, setGeneratedCode] = useState('');

  const handleStartScan = () => {
    setAppState('scanning');
  };

  const handleSnap = async (imageData) => {
    setAppState('processing');
    try {
      const code = await analyzeWireframe(imageData);
      setGeneratedCode(code);
      setAppState('completed');
    } catch (error) {
      console.error("Inference failed", error);
      setAppState('idle');
    }
  };

  const handleReset = () => {
    setAppState('idle');
    setGeneratedCode('');
  };

  return (
    <div style={styles.appContainer}>
      <header className="animate-in" style={styles.header}>
        <div style={styles.logo}>
          <span style={styles.brandAccent}>Wire</span>Snap
        </div>
        <div style={styles.tagline}>
          <div style={styles.dot}></div>
          NPU Engine Active
        </div>
      </header>

      <main style={styles.mainContent}>
        {appState === 'idle' && (
          <div className="glass-panel animate-in delay-1" style={styles.hero}>
            <div style={styles.glowOrb}></div>
            <h1 style={styles.heroTitle}>From sketch to <br/><span style={styles.codeAccent}>production code</span>.</h1>
            <p style={styles.heroSubtitle}>
              Point your iQOO camera at any UI wireframe. Our on-device Vision-Language Model instantly generates pixel-perfect React code for your laptop. Zero cloud dependency.
            </p>
            <div style={styles.buttonGroup}>
              <button onClick={handleStartScan} className="primary" style={styles.ctaButton}>
                Launch Camera Experience
              </button>
            </div>
          </div>
        )}

        {appState === 'scanning' && (
          <div className="animate-in">
             <CameraView onSnap={handleSnap} />
          </div>
        )}

        {appState === 'processing' && (
          <div className="glass-panel animate-in" style={styles.processing}>
            <div style={styles.scannerLineWrapper}>
              <div style={styles.scannerLine}></div>
            </div>
            <h2 style={styles.processingTitle}>Decoding Wireframe...</h2>
            <div style={styles.consoleText}>
               <p className="code-font">> Initializing Snapdragon NPU...</p>
               <p className="code-font" style={{animationDelay: '0.5s', opacity: 0, animation: 'fadeIn 0.1s forwards 0.5s'}}>> Running YOLOv8-Edge for bounding boxes...</p>
               <p className="code-font" style={{animationDelay: '1.2s', opacity: 0, animation: 'fadeIn 0.1s forwards 1.2s'}}>> Extracting handwritten text via OCR...</p>
               <p className="code-font" style={{animationDelay: '1.8s', opacity: 0, animation: 'fadeIn 0.1s forwards 1.8s'}}>> Assembling JSX syntax tree...</p>
            </div>
          </div>
        )}

        {appState === 'completed' && (
          <div className="animate-in">
             <CodeViewer code={generatedCode} onReset={handleReset} />
          </div>
        )}
      </main>
    </div>
  );
}

const styles = {
  appContainer: {
    display: 'flex',
    flexDirection: 'column',
    minHeight: '100vh',
    width: '100%',
    padding: '24px 40px',
    boxSizing: 'border-box'
  },
  header: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: '10px 0',
    width: '100%',
  },
  logo: {
    fontSize: '1.5em',
    fontWeight: '800',
    letterSpacing: '-0.5px'
  },
  brandAccent: {
    color: 'var(--primary)'
  },
  tagline: {
    display: 'flex',
    alignItems: 'center',
    gap: '8px',
    fontSize: '0.8em',
    fontWeight: '600',
    letterSpacing: '1px',
    textTransform: 'uppercase',
    color: '#00e5ff',
    border: '1px solid rgba(0, 229, 255, 0.2)',
    padding: '6px 14px',
    borderRadius: '20px',
    backgroundColor: 'rgba(0, 229, 255, 0.05)',
    boxShadow: '0 0 10px rgba(0, 229, 255, 0.1)'
  },
  dot: {
    width: '6px',
    height: '6px',
    borderRadius: '50%',
    backgroundColor: '#00e5ff',
    boxShadow: '0 0 8px #00e5ff',
    animation: 'pulse 2s infinite'
  },
  mainContent: {
    flex: 1,
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    width: '100%',
  },
  hero: {
    maxWidth: '700px',
    textAlign: 'center',
    padding: '60px 40px',
    position: 'relative'
  },
  glowOrb: {
    position: 'absolute',
    top: '20%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: '300px',
    height: '300px',
    background: 'radial-gradient(circle, rgba(247,164,0,0.15) 0%, transparent 70%)',
    zIndex: -1,
    pointerEvents: 'none'
  },
  heroTitle: {
    marginBottom: '20px',
  },
  codeAccent: {
    fontFamily: 'JetBrains Mono',
    color: 'var(--primary)',
    fontWeight: '500',
    fontSize: '0.9em',
    letterSpacing: '-2px'
  },
  heroSubtitle: {
    fontSize: '1.15em',
    marginBottom: '40px',
    lineHeight: '1.7',
    maxWidth: '600px',
    margin: '0 auto 40px auto'
  },
  buttonGroup: {
    display: 'flex',
    justifyContent: 'center',
    gap: '16px'
  },
  ctaButton: {
    fontSize: '1.1em',
    padding: '18px 48px',
    borderRadius: '40px',
    letterSpacing: '0.5px'
  },
  processing: {
    maxWidth: '500px',
    width: '100%',
    textAlign: 'left',
    padding: '40px'
  },
  processingTitle: {
    fontSize: '1.5em',
    marginBottom: '20px',
    textAlign: 'center'
  },
  scannerLineWrapper: {
    width: '100%',
    height: '4px',
    backgroundColor: 'rgba(255,255,255,0.05)',
    borderRadius: '4px',
    overflow: 'hidden',
    marginBottom: '30px',
    position: 'relative'
  },
  scannerLine: {
    position: 'absolute',
    top: 0,
    left: 0,
    height: '100%',
    width: '30%',
    background: 'linear-gradient(90deg, transparent, var(--primary), transparent)',
    animation: 'scanX 1.5s ease-in-out infinite alternate'
  },
  consoleText: {
    backgroundColor: 'rgba(0,0,0,0.6)',
    padding: '20px',
    borderRadius: '12px',
    border: '1px solid var(--glass-border)'
  }
};

if (typeof document !== 'undefined') {
  const style = document.createElement('style');
  style.innerHTML = `
    @keyframes pulse {
      0% { opacity: 0.4; }
      50% { opacity: 1; box-shadow: 0 0 12px #00e5ff; }
      100% { opacity: 0.4; }
    }
    @keyframes scanX {
      0% { left: -30%; }
      100% { left: 100%; }
    }
    @keyframes fadeIn {
      to { opacity: 1; }
    }
    .consoleText p {
      color: #00e5ff;
      font-size: 0.85em;
      margin: 8px 0;
      opacity: 0;
    }
  `;
  document.head.appendChild(style);
}

export default App;
