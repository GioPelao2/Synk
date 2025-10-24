'use client';
import React, { useState } from 'react';
import styles from '@/app/login/page.module.css';
import LoginView from '@/components/auth/LoginView';
import RegisterView from '@/components/auth/RegisterView';
import SuccessView from '@/components/auth/SuccessView';

type AuthView = 'Login' | 'Register' | 'Success';

export default function LoginPage(){
    const [currentView, setCurrentView] = useState<AuthView>('Login');

    const renderView = () => {
        switch (currentView) {
            case 'Login':
                return <LoginView onSwitchToRegister={() => setCurrentView('Register')} />;
          
            case 'Register':
                return (
                    <RegisterView
                    onRegistrationSuccess={() => setCurrentView('Success')}
                    onSwitchToLogin={() => setCurrentView('Login')}
                    />
                );
            case 'Success':
                return <SuccessView onRedirectToHome={() => window.location.href = '/'} />;
                default:
                    return <LoginView onSwitchToRegister={() => setCurrentView('Register')} />;   
        }
    };

    const getSubtitle = () => {
        if (currentView === 'Login') return 'Inicia sesión para continuar';
        if (currentView === 'Register') return 'Crea tu cuenta de SYNK';
        return 'Proceso Realizado Correctamente'
    };

    return (
        <div className={styles.maincontainer}>
            <div className={styles.loginBox}>
                <h1 className={styles.tittle}>SYNK</h1>
                <p className={styles.subtittle}>{getSubtitle()}</p>

                {renderView()}
            </div>
        </div>
    );
}