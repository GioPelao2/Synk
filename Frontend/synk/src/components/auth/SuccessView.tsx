import React, { useEffect } from "react";
import styles from '@/styles/auth.module.css';

interface SuccessViewProps {
    onRedirectToHome: () => void;
}

const SuccessView: React.FC<SuccessViewProps> = ({ onRedirectToHome }) => {

    useEffect(() => {
        const timer = setTimeout(() => {
            onRedirectToHome();
        }, 3000);

            return () => clearTimeout(timer);
    }, [onRedirectToHome]);

    return (
        <div className={styles.SuccessContainer}>
            <h2 style={{ color: 'white', marginTop: '15px' }}>¡Bienvenido a SYNK!</h2>
            <p className={styles.subtittle} style={{ color: '#a0a0a0' }}>
                Inicio de sesión exitoso. Redirigiendo a la pantalla principal...
            </p>
        </div>
    );
};

export default SuccessView;