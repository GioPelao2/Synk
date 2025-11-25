import React, { useState} from 'react';
import styles from '@/styles/auth.module.css';
import Link from "next/link";

interface RegisterViewProps {
    onRegistrationSuccess: () => void;
    onSwitchToLogin: () => void;
}

const RegisterView: React.FC<RegisterViewProps> = ({ onRegistrationSuccess, onSwitchToLogin}) => {
    const [usernameOrEmail, setUsernameOrEmail] = useState('');
    const [password, setPassword] = useState('');
    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onRegistrationSuccess();
    };

    return (
        <form className={styles.authForm} onSubmit={handleSubmit}>
            <input
            type="text"
            placeholder="Email o Nombre de Usuario"
            className={styles.inputField}
            value={usernameOrEmail}
            onChange={(e) => setUsernameOrEmail(e.target.value)}
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
            <button type="submit" className={styles.authButton}>
                REGISTRAR CUENTA
            </button>

            <div className={styles.footerLinks}>
                <p>¿Ya tienes una cuenta?</p>
                <button type="button" className={styles.switchButton} onClick={onSwitchToLogin}>
                    Iniciar Sesión
                </button>
            </div>
        </form>
    );
};

export default RegisterView;