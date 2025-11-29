import React from "react";
import { IoMdSend } from "react-icons/io";
import styles from "@/styles/MessageInput.module.css"

export default function MessageInput() {
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
        />
        <button className={styles.sendButton} aria-label="Enviar Mensaje">
                <IoMdSend />
            </button>
      </div>
    );
}