'use client';

import React from "react";
import { useRouter } from "next/navigation";
import styles from "@/styles/Sidebar.module.css";
import { setUserOffline } from "@/services/api";

interface SidebarProps {
  onAddContact: () => void;
}

export default function Sidebar({ onAddContact }: SidebarProps) {
  const router = useRouter();

  const handleLogout = async () => {
    const userId = sessionStorage.getItem('currentUserId');

    if (userId) {
      await setUserOffline(parseInt(userId, 10));
    }

    sessionStorage.removeItem('authToken');
    sessionStorage.removeItem('currentUserId');
    sessionStorage.removeItem('currentUsername');

    router.push('/login');
  };

  return (
    <div className={styles.container}>
      <div className={styles.topIcons}>
        <button className={styles.iconButton} aria-label="Perfil">👤</button>
        <button
          className={styles.iconButton}
          aria-label="Agregar Contacto"
          onClick={onAddContact}
        >
          ➕
        </button>
        <button className={styles.iconButton} aria-label="Notificaciones">🔔</button>
      </div>

      <div className={styles.underIcons}>
        <button className={styles.iconButton} aria-label="Ajustes">⚙️</button>
        <button
          className={styles.iconButton}
          aria-label="Cerrar Sesión"
          onClick={handleLogout}
        >
          🚪
        </button>
      </div>
    </div>
  );
}
