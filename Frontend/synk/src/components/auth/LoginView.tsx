import React, { useState} from 'react';
import styles from '@/styles/auth.module.css'
import Link from 'next/link';
import { useRouter } from 'next/navigation'; 
import { loginUser } from '@/services/api';

interface LoginViewProps {
    onSwitchToRegister: () => void;
}

const LoginView: React.FC<LoginViewProps> = ({ onSwitchToRegister }) => {
    const [usernameOrEmail, setUsernameOrEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(false); 
    const router = useRouter();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);
        setIsLoading(true);

    try {
            const USER_TO_TEST = 'admin'; 
            const PASS_TO_TEST = '1234';  

           const token = await loginUser(USER_TO_TEST, PASS_TO_TEST);
            
            if (token) {
                console.log("Login Exitoso. Token JWT recibido y almacenado.");
                router.push('/index'); 
            }
        } catch (err: any) {
            console.error("Fallo de autenticación:", err.message);
            setError('Error al iniciar sesión. Por favor, verifica el servidor y las credenciales (admin/1234).');
        } finally {
            setIsLoading(false);
        }
    };

    return(
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
            disabled={isLoading}
            />
            {error && <p className={styles.errorMessage} style={{color: 'red', marginTop: '10px'}}>{error}</p>}

            <div className={styles.optionsRow}>
                <label className={styles.checkboxContainer}>
                    <input type="checkbox" />
                    <span className={styles.checkboxLabel}>Recordarme</span>
                </label>

                <Link href="#" className={styles.forgotPassword}>
                </Link>
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