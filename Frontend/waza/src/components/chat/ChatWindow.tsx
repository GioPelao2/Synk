import Message from "@/components/chat/Message"
import MessageInput from "@/components/chat/MessageInput";
import styles from "@/styles/ChatWindow.module.css"

export default function ChatWindow () {
    return (
        <div className={styles.ChatWindowContainer}>
            <div className={styles.messageList}>
                <Message text="increible" sender="other" />
                <Message text="maxi mili ANO" sender="user" />
            
            </div>
            <MessageInput />
        </div>
    )
}
 