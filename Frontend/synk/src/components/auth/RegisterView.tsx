import React, { useState } from 'react';
import styles from '@/styles/auth.module.css';
import { registerUser } from '@/services/api'; 
import Link from "next/link";

interface RegisterViewProps {
    onRegistrationSuccess: () => void;
    onSwitchToLogin: () => void;
}

const RegisterView: React.FC<RegisterViewProps> = ({ onRegistrationSuccess, onSwitchToLogin }) => {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');

    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        // Resetear estado de error
        setError(null);

        // Validaciones del frontend
        if (username.trim().length === 0) {
            setError("El nombre de usuario es requerido");
            return;
        }

        if (email.trim().length === 0) {
            setError("El correo electrónico es requerido");
            return;
        }

        if (password.length < 8) {
            setError("La contraseña debe tener al menos 8 caracteres");
            return;
        }

        if (password !== confirmPassword) {
            setError("Las contraseñas no coinciden");
            return;
        }

        setIsLoading(true);

        try {
            await registerUser(username, email, password); 

            // Limpiar campos en caso de éxito
            setUsername('');
            setEmail('');
            setPassword('');
            setConfirmPassword('');
            
            onRegistrationSuccess(); 

        } catch (err: any) {
            const errorMessage = err.message || "Ocurrió un error desconocido al registrar.";
            setError(errorMessage);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <form className={styles.authForm} onSubmit={handleSubmit}>
            {error && <p className={styles.errorMessage}>{error}</p>} 
            
            <input
                type="text"
                placeholder="Nombre de Usuario" 
                className={styles.inputField}
                value={username}
                onChange={(e) => setUsername(e.target.value)} 
                required
                disabled={isLoading}
                minLength={1}
            />

            <input
                type="email" 
                placeholder="Correo Electrónico" 
                className={styles.inputField}
                value={email}
                onChange={(e) => setEmail(e.target.value)} 
                required
                disabled={isLoading}
            />

            <input
                type="password"
                placeholder="Contraseña (mínimo 8 caracteres)" 
                className={styles.inputField}
                value={password}
                onChange={(e) => setPassword(e.target.value)} 
                required
                disabled={isLoading}
                minLength={8}
            />

            <input
                type="password"
                placeholder="Confirmar Contraseña" 
                className={styles.inputField}
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)} 
                required
                disabled={isLoading}
                minLength={8}
            />

            <button 
                type="submit" 
                className={styles.authButton}
                disabled={isLoading}
            >
                {isLoading ? 'REGISTRANDO...' : 'REGISTRAR CUENTA'}
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