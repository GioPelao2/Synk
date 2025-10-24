import React, { useState} from 'react';
import styles from '@/styles/auth.module.css'
import Link from 'next/link';

interface LoginViewProps {
    onSwitchToRegister: () => void;
}

const LoginView: React.FC<LoginViewProps> = ({ onSwitchToRegister }) => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        console.log('Login attempt: ', {email,password});

    };

    return(
        <form className={styles.authForm} onSubmit={handleSubmit}>
            <input
            type="email"
            placeholder="Email o Nombre de Usuario"
            className={styles.inputField}
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            />

            <input
            type="password"
            placeholder="Contraseña"
            className={styles.inputField}
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            />

            <div className={styles.optionsRow}>
                <label className={styles.checkboxContainer}>
                    <input type="checkbox" />
                    <span className={styles.checkboxLabel}>Recordarme</span>
                </label>

                <Link href="#" className={styles.forgotPassword}>
                </Link>
            </div>

            <button type="submit" className={styles.authButton}>
                INICIAR SESIÓN
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