import styles from "@/styles/ChatBlock.module.css";
import Image from "next/image";
interface ChatBlockProps {
  avatarSrc: string;
  name: string;
  lastMessage: string;
  time: string;
  unreadCount?: number;
  onlineStatus: 'online' | 'offline';
}

export default function ChatBlock({ avatarSrc, name, lastMessage, time, unreadCount, onlineStatus}: ChatBlockProps) {
    return (
        <div className={styles.container}>
      <div className={`${styles.statusPill} ${styles[onlineStatus]}`}></div>

      <div className={styles.avatar}>
        <Image src={avatarSrc} alt={`${name}'s avatar`} width={48} height={48} />
      </div>

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
