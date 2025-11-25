import React, { useState} from 'react';
import styles from '@/styles/auth.module.css';
import { registerUser } from '@/services/api'; 
import Link from "next/link";

interface RegisterViewProps {
    onRegistrationSuccess: () => void;
    onSwitchToLogin: () => void;
}

const RegisterView: React.FC<RegisterViewProps> = ({ onRegistrationSuccess, onSwitchToLogin}) => {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');

    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault(); // Detiene el comportamiento por defecto del formulario

        // Resetear estado de error y activar carga
        setError(null);
        setIsLoading(true);

        try {
            await registerUser(username, email); 

            setUsername('');
            setEmail('');
            onRegistrationSuccess(); 

        } catch (err: any) {
            // Maneja el error y lo muestra al usuario
            const errorMessage = err.message || "Ocurrió un error desconocido al registrar.";
            setError(errorMessage);
        } finally {
            setIsLoading(false); // Siempre detiene la carga
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
            disabled={isLoading} // Deshabilitar durante la petición
            />

            <input
            type="email" 
            placeholder="Correo Electrónico" 
            className={styles.inputField}
            value={email}
            onChange={(e) => setEmail(e.target.value)} 
            required
            disabled={isLoading} // Deshabilitar durante la petición
            />

            <button 
                type="submit" 
                className={styles.authButton}
                disabled={isLoading} // Deshabilitar si está cargando
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