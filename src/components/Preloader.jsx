import React, { useEffect, useState } from 'react';

const Preloader = ({ onComplete }) => {
  const [progress, setProgress] = useState(0);

  useEffect(() => {
    // Simulate loading progress
    const interval = setInterval(() => {
      setProgress((prev) => {
        const next = prev + (Math.random() * 5 + 1);
        if (next >= 100) {
          clearInterval(interval);
          setTimeout(onComplete, 500); // Wait a beat before dismissing
          return 100;
        }
        return next;
      });
    }, 100);
    
    return () => clearInterval(interval);
  }, [onComplete]);

  return (
    <div className="preloader-container">
      {/* Background Grid Lines & Tech Elements */}
      <div className="hud-line hud-line-1"></div>
      <div className="hud-line hud-line-2"></div>
      
      {/* Top Right HUD */}
      <div className="hud-top-right">
        <div>DATA ANALYSIS</div>
        <div className="hud-small-text">10/01/2023</div>
        <div className="hud-small-text" style={{marginTop: '4px'}}>2.45</div>
      </div>
      
      {/* Middle Left HUD */}
      <div className="hud-mid-left">
        <div>OPTICAL LENS</div>
        <div className="hud-small-text">MACL NB-5A.3</div>
        <div className="hud-line-graph"></div>
      </div>
      
      {/* Central Emblem */}
      <div className="emblem-container">
        <div className="emblem-outer-ring"></div>
        <div className="emblem-inner-ring">
          <div className="emblem-glow"></div>
          {/* Stylized Brain & M Logo */}
          <svg className="emblem-svg" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
            {/* Neural pathways / lightning */}
            <path d="M 20 50 Q 30 20 50 20 Q 70 20 80 50 Q 70 80 50 80 Q 30 80 20 50" fill="none" stroke="rgba(247, 164, 0, 0.4)" strokeWidth="1" />
            <path d="M 30 50 Q 40 30 50 30 Q 60 30 70 50 Q 60 70 50 70 Q 40 70 30 50" fill="none" stroke="rgba(247, 164, 0, 0.6)" strokeWidth="1" />
            <path d="M 50 20 L 50 80 M 20 50 L 80 50 M 30 30 L 70 70 M 30 70 L 70 30" stroke="rgba(247, 164, 0, 0.3)" strokeWidth="0.5" />
            
            {/* The M */}
            <path d="M 35 70 L 35 35 L 50 55 L 65 35 L 65 70" fill="none" stroke="#f7a400" strokeWidth="4" strokeLinejoin="round" />
            <path d="M 35 70 L 35 35 L 50 55 L 65 35 L 65 70" fill="none" stroke="#ffffff" strokeWidth="1.5" strokeLinejoin="round" />
          </svg>
        </div>
      </div>
      
      {/* Main Title */}
      <h1 className="preloader-title">INSIDEME</h1>
      
      {/* Progress Section */}
      <div className="progress-section">
        <div className="progress-text">PROGRESS: {Math.floor(progress)}%</div>
        <div className="progress-bar-container">
          <div className="progress-bar-fill" style={{ width: `${progress}%` }}>
            <div className="progress-bar-glow"></div>
          </div>
        </div>
        <div className="progress-subtext">SYSTEM RECALL: ANALYZING TEMPORAL SEGMENTS...</div>
      </div>
    </div>
  );
};

export default Preloader;
