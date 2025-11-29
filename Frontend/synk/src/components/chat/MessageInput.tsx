import React, {useState} from "react";
import { IoMdSend } from "react-icons/io";
import styles from "@/styles/MessageInput.module.css"

interface MessageInputProps {
    onSendMessage: (content: string) => void;
}

export default function MessageInput({ onSendMessage }: MessageInputProps) {
  const [message, setMessage] = useState('');
  
  const handleSend = () => {
        if (message.trim()) {
            onSendMessage(message.trim());
            setMessage(''); // Limpiar el input
        }
    };

    const handleKeyPress = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            handleSend();
        }
    };

  return (
      <div className={styles.inputContainer}>

        <div className={styles.actionIcons}>
          <button className={styles.iconButton} aria-label="Enviar enlace">🔗</button>
          <button className={styles.iconButton} aria-label="Adjuntar archivo">📎</button>
        </div>

        <input
            type="text"
            placeholder="Escribe un mensaje..."
            className={styles.messageInput}
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            onKeyPress={handleKeyPress}
        />
        <button className={styles.sendButton} aria-label="Enviar Mensaje" onClick={handleSend} disabled={!message.trim()}>
                <IoMdSend />
            </button>
      </div>
    );
}