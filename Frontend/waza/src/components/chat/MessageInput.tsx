import styles from "@/styles/MessageInput.module.css"

export default function MessageInput() {
    return (
      <div className={styles.inputContainer}>
        <input
            type="text"
            placeholder="Escribe un mensaje..."
            className={styles.messageInput}
        />
        <button className={styles.sendButton}>
            Enviar
        </button>    
      </div>
    );
}