import React from "react";
import styles from "@/styles/ChatBlock.module.css";
import Image from "next/image";

interface ChatBlockProps {
  name: string;
  lastMessage: string;
  time: string;
  unreadCount?: number;
  onlineStatus: 'online' | 'offline';
}

const ChatBlock: React.FC<ChatBlockProps> = ({
    name,
    lastMessage,
    time,
    unreadCount,
    onlineStatus
}) => {
    return (
    <div className={styles.container}>
        <div className={`${styles.statusBar} ${styles[onlineStatus]}`}></div>

    <div className={styles.info}>
        <div className={styles.name}>{name}</div>
        <div className={styles.lastMessage}>{lastMessage}</div>
    </div>

    <div className={styles.details}>
        <div className={styles.time}>{time}</div>
            {unreadCount && unreadCount > 0 && (
            <div className={styles.unreadCount}>{unreadCount}</div>
            )}
    </div>
    </div>
  );
}

export default ChatBlock;
