'use client';

import React, { useEffect } from "react";
import styles from '@/styles/auth.module.css';

interface SuccessViewProps {
  onRedirectToLogin: () => void;
}

const SuccessView: React.FC<SuccessViewProps> = ({ onRedirectToLogin }) => {
  useEffect(() => {
    const timer = setTimeout(() => {
      onRedirectToLogin();
    }, 3000);

    return () => clearTimeout(timer);
  }, [onRedirectToLogin]);

  return (
    <div className={styles.SuccessContainer}>
      <h2 style={{ color: 'white', marginTop: '15px' }}>¡Bienvenido a SYNK!</h2>
      <p className={styles.subtittle} style={{ color: '#a0a0a0' }}>
        Registro exitoso. Redirigiendo a Inicio de Sesión
      </p>
    </div>
  );
};

export default SuccessView;
