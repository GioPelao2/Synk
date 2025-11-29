import styles from "@/styles/Message.module.css";

interface MessageProps {
    text: string;
    sender: "user" | "other";
    timestamp?: string;
}

export default function Message({ text, sender, timestamp}: MessageProps) {
    const messageClass = sender === "user" ? styles.userMessage : styles.otherMessage;
   
    const formatTime = (timestamp?: string) => {
        if (!timestamp) return '';
        
        const date = new Date(timestamp);
        return date.toLocaleTimeString('es-ES', { 
            hour: '2-digit', 
            minute: '2-digit' 
        });
    };

    return (
        <div className={`${styles.messageBubble} ${messageClass}`}>
            <p>{text}</p>
            {timestamp && (
                <span className={styles.timestamp}>
                    {formatTime(timestamp)}
                </span>
            )}
        </div>
    );
}