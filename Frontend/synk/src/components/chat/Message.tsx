import styles from "@/styles/Message.module.css";

interface MessageProps {
    text: string;
    sender: "user" | "other";
}

export default function Message({ text, sender}: MessageProps) {
    const messageClass = sender === "user" ? styles.userMessage : styles.otherMessage;
   
    return (
        <div className={`${styles.messageBubble} ${messageClass}`}>
            <p>{text}</p>
        </div>
    );
}