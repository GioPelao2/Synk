import React from "react";
import styles from '@/styles/auth.module.css';
import Link from "next/link";

interface RegisterViewProps {
    onRegistrationSuccess: () => void;
    onSwitchToLogin: () => void;
}

const RegisterView: React.FC<RegisterViewProps> = ({ onRegistrationSuccess, onSwitchToLogin}) => {

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onRegistrationSuccess();
    };

    return (
        <form className={styles.authForm} onSubmit={handleSubmit}>
            <h2 style={{ color: 'white' }}>Vista de Registro (Pendiente)</h2>
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