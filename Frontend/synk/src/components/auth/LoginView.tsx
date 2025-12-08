'use client';

import React, { useState } from 'react';
import styles from '@/styles/auth.module.css';
import { useRouter } from 'next/navigation';
import { loginUser } from '@/services/api';

interface LoginViewProps {
  onSwitchToRegister: () => void;
}

const LoginView: React.FC<LoginViewProps> = ({ onSwitchToRegister }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const router = useRouter();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setIsLoading(true);

    try {
      const response = await loginUser(username, password);

      if (response.token) {
        console.log("Login exitoso. Token JWT recibido.");
        router.push('/index');
      }
    } catch (err: any) {
      console.error("Fallo de autenticación:", err.message);
      setError('Credenciales inválidas. Por favor, intenta nuevamente.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <form className={styles.authForm} onSubmit={handleSubmit}>
      <input
        type="text"
        placeholder="Nombre de Usuario"
        className={styles.inputField}
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        required
        disabled={isLoading}
      />

      <input
        type="password"
        placeholder="Contraseña"
        className={styles.inputField}
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        required
        disabled={isLoading}
      />

      {error && <p className={styles.errorMessage}>{error}</p>}

      <div className={styles.optionsRow}>
        <label className={styles.checkboxContainer}>
          <input type="checkbox" />
          <span className={styles.checkboxLabel}>Recordarme</span>
        </label>
      </div>

      <button type="submit" className={styles.authButton} disabled={isLoading}>
        {isLoading ? 'CARGANDO...' : 'INICIAR SESIÓN'}
      </button>

      <div className={styles.footerLinks}>
        <p>¿No tienes una cuenta?</p>
        <button type="button" className={styles.switchButton} onClick={onSwitchToRegister}>
          Registrarte
        </button>
      </div>
    </form>
  );
};

export default LoginView;
