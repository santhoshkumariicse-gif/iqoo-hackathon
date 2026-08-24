import React, { useRef, useState, useCallback, useEffect } from 'react';

export default function CameraView({ onSnap }) {
  const videoRef = useRef(null);
  const canvasRef = useRef(null);
  const [hasPermission, setHasPermission] = useState(false);
  const [error, setError] = useState('');

  const startCamera = useCallback(async () => {
    try {
      const stream = await navigator.mediaDevices.getUserMedia({ 
        video: { facingMode: 'environment' }
      });
      if (videoRef.current) {
        videoRef.current.srcObject = stream;
        setHasPermission(true);
      }
    } catch (err) {
      console.error("Error accessing camera:", err);
      setError("Camera access denied.");
    }
  }, []);

  useEffect(() => {
    startCamera();
    return () => {
      if (videoRef.current && videoRef.current.srcObject) {
        const tracks = videoRef.current.srcObject.getTracks();
        tracks.forEach(track => track.stop());
      }
    };
  }, [startCamera]);

  const handleSnap = () => {
    if (videoRef.current && canvasRef.current) {
      const video = videoRef.current;
      const canvas = canvasRef.current;
      canvas.width = video.videoWidth;
      canvas.height = video.videoHeight;
      const ctx = canvas.getContext('2d');
      ctx.drawImage(video, 0, 0, canvas.width, canvas.height);
      onSnap(canvas.toDataURL('image/jpeg'));
    }
  };

  return (
    <div className="glass-panel" style={styles.container}>
      {error ? (
        <div style={styles.errorBox}>
          <p>{error}</p>
          <button onClick={startCamera}>Retry</button>
        </div>
      ) : (
        <div style={styles.videoWrapper}>
          <video ref={videoRef} autoPlay playsInline style={styles.video} />
          <canvas ref={canvasRef} style={{ display: 'none' }} />
          
          {hasPermission && (
            <div style={styles.overlay}>
              {/* Sci-Fi Targeting Brackets */}
              <div style={{...styles.bracket, top: '20%', left: '10%', borderTop: '3px solid var(--primary)', borderLeft: '3px solid var(--primary)'}}></div>
              <div style={{...styles.bracket, top: '20%', right: '10%', borderTop: '3px solid var(--primary)', borderRight: '3px solid var(--primary)'}}></div>
              <div style={{...styles.bracket, bottom: '20%', left: '10%', borderBottom: '3px solid var(--primary)', borderLeft: '3px solid var(--primary)'}}></div>
              <div style={{...styles.bracket, bottom: '20%', right: '10%', borderBottom: '3px solid var(--primary)', borderRight: '3px solid var(--primary)'}}></div>
              
              {/* Laser Scanner */}
              <div style={styles.laser}></div>
              
              <div style={styles.hudGroup}>
                <div style={styles.hudText} className="code-font">[ REC ]</div>
                <div style={styles.hudText} className="code-font">NPU READY</div>
              </div>
            </div>
          )}
        </div>
      )}
      
      {hasPermission && (
        <div style={styles.controls}>
          <button onClick={handleSnap} className="primary" style={styles.snapButton}>
            <div style={styles.snapInner}></div>
          </button>
        </div>
      )}
    </div>
  );
}

const styles = {
  container: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    width: '100%',
    maxWidth: '450px',
    margin: '0 auto',
    padding: '16px',
    backgroundColor: '#000',
    borderColor: 'var(--primary-glow)'
  },
  videoWrapper: {
    position: 'relative',
    width: '100%',
    borderRadius: '16px',
    overflow: 'hidden',
    backgroundColor: '#111',
    aspectRatio: '3/4',
    boxShadow: 'inset 0 0 50px rgba(0,0,0,1)',
    animation: 'pulse-glow 4s infinite'
  },
  video: {
    width: '100%',
    height: '100%',
    objectFit: 'cover',
    opacity: 0.8
  },
  overlay: {
    position: 'absolute',
    inset: 0,
    pointerEvents: 'none',
    boxShadow: 'inset 0 0 100px rgba(0,0,0,0.8)'
  },
  bracket: {
    position: 'absolute',
    width: '40px',
    height: '40px',
    borderColor: 'var(--primary)',
    boxShadow: '0 0 15px var(--primary-glow)',
    transition: 'all 0.3s'
  },
  laser: {
    position: 'absolute',
    top: '20%',
    left: '10%',
    width: '80%',
    height: '2px',
    backgroundColor: 'var(--primary)',
    boxShadow: '0 0 15px 2px var(--primary)',
    animation: 'scanY 3s ease-in-out infinite alternate',
    opacity: 0.7
  },
  hudGroup: {
    position: 'absolute',
    bottom: '20px',
    left: '20px',
    right: '20px',
    display: 'flex',
    justifyContent: 'space-between'
  },
  hudText: {
    color: 'var(--primary)',
    fontSize: '0.7em',
    letterSpacing: '2px',
    textShadow: '0 0 5px var(--primary-glow)'
  },
  controls: {
    marginTop: '20px',
    display: 'flex',
    justifyContent: 'center'
  },
  snapButton: {
    width: '70px',
    height: '70px',
    borderRadius: '50%',
    padding: '5px',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'transparent',
    border: '2px solid var(--primary)',
  },
  snapInner: {
    width: '100%',
    height: '100%',
    backgroundColor: 'var(--primary)',
    borderRadius: '50%',
    transition: 'transform 0.2s'
  },
  errorBox: { padding: '40px 20px', textAlign: 'center' }
};

if (typeof document !== 'undefined') {
  const style = document.createElement('style');
  style.innerHTML = `
    @keyframes scanY {
      0% { top: 20%; opacity: 0.3; }
      50% { opacity: 1; }
      100% { top: 80%; opacity: 0.3; }
    }
    @keyframes pulse-glow {
      0% { box-shadow: 0 0 10px var(--primary-glow); }
      50% { box-shadow: 0 0 30px var(--primary-glow); }
      100% { box-shadow: 0 0 10px var(--primary-glow); }
    }
  `;
  document.head.appendChild(style);
}
