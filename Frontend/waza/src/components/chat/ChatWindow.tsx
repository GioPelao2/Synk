import Message from "@/components/chat/Message"
import MessageInput from "@/components/chat/MessageImput";
import styles from "@/styles/ChatWindow.module.css"

export default function ChatWindow () {
    return (
        <div className={styles.chatWindowContainer}> 

        <div className={styles.messageList}>
            
            </div>
            <MessageInput />
        </div>
    )
}