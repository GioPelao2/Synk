import React from "react";
import styles from "@/styles/Sidebar.module.css";

export default function Sidebar() {
    return (
        <div className={styles.container}>
            <div className={styles.topIcons}>
                <button className={styles.iconButton} aria-label="Perfil">👤</button>
                <button className={styles.iconButton} aria-label="Contactos">👥</button>
                <button className={styles.iconButton} aria-label="Notificaciones">🔔</button>
            </div>

            <div className={styles.underIcons}>
                <button className={styles.iconButton} aria-label="Ajustes">⚙️</button>
                <button className={styles.iconButton} aria-label="Cerrar Sesión">🚪</button>
            </div>
        </div>
    )

}