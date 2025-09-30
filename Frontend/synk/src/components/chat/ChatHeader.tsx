import React from "react";
import styles from '@/styles/ChatHeader.module.css'
import { ChatHeaderProps } from "@/types/chat";

const ChatHeader: React.FC<ChatHeaderProps> = ({ contact }) => {
    if (!contact){
        return (
            <header className={styles.header}>
                <p>Selecciona un chat para comenzar</p>
            </header>
        );
    }

    const { name, status, avatarUrl } = contact;

    return (
        <header className={styles.header}>
            <div className={styles.contactInfo}>
                <div className={styles.avatar}>
                    <img src={avatarUrl} alt={`${name}'s avatar`} />
                </div>

                <div className={styles.textDetails}>
                    <h2 className={styles.name}>{name}</h2> 
                    <p className={styles.status}>{status}</p>
                </div>
            </div>
    {/*-----------agregar boton de react icons----------*/}
             <div className={styles.actionButtons}>
                <button className={styles.iconButton}>🔍</button>
                <button className={styles.iconButton}>⋮</button>
            </div>
        </header>
    );
}

export default ChatHeader;
