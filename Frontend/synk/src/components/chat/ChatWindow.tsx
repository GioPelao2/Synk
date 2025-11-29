import React from "react";
import ChatHeader from "./ChatHeader";
import { ContactData, MessageData } from "@/types";
import Message from "@/components/chat/Message";
import MessageInput from "@/components/chat/MessageInput";
import styles from "@/styles/ChatWindow.module.css";

interface ChatWindowProps {
    activeContact: ContactData | null;
    messages: MessageData[];
    onSendMessage: (content: string) => void;
    isLoading?: boolean;
    currentUserId: number;
}

const ChatWindow: React.FC<ChatWindowProps> = ({ activeContact, messages, onSendMessage, isLoading, currentUserId }) => {
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
        {messages.map((msg) => (
            <Message
            key={msg.id}
            text={msg.content}
            sender={msg.senderId === currentUserId ? "other" : "user"}
            timestamp={msg.timestamp}
            />
        ))}
        </div>

        <MessageInput onSendMessage={onSendMessage} />
        </div>
    );
}

export default ChatWindow