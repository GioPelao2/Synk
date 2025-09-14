import Message from "@/components/chat/Message"
import MessageInput from "@/components/chat/MessageInput";
import styles from "@/styles/ChatWindow.module.css"

export default function ChatWindow () {
    return (
        <div className={styles.ChatWindowContainer}>
            <div className={styles.messageList}>
                <Message text="hola hrno" sender="other" />
                <Message text="como estai brother?" sender="user" />
            
            </div>
            <MessageInput />
        </div>
    )
}
 