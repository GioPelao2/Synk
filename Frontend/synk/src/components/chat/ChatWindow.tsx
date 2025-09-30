import React from "react";
import ChatHeader from "./ChatHeader";
import { ContactData } from "@/types/chat";
import Message from "@/components/chat/Message";
import MessageInput from "@/components/chat/MessageInput";
import styles from "@/styles/ChatWindow.module.css";

interface ChatWindowProps {
    activeContact: ContactData | null;
}

const ChatWindow: React.FC<ChatWindowProps> = ({ activeContact }) => {
    if (!activeContact){
        return (
            <div className="placeholder">
                Selecciona un chat para ver los mensajes
            </div>
        );
    }
    
    return (
        <div className={styles.ChatWindowContainer}>
            <ChatHeader contact={activeContact} />

        <div className={styles.messageList}>
        <Message text="hola hrno" sender="other" />
        <Message text="como estai brother?" sender="user" />
        </div>

        <MessageInput />
        </div>
    );
}

export default ChatWindow