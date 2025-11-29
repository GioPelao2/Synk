import React from "react";
import ChatHeader from "./ChatHeader";
import Message from "@/components/chat/Message";
import MessageInput from "@/components/chat/MessageInput";
import styles from "@/styles/ChatWindow.module.css";


interface User {
    id: number;
    username: string;
    email: string;
    status: string;
    lastSeen: string;
}

interface MessageData {
    id: number;
    senderId: number;
    receiverId: number;
    content: string;
    timestamp: string;
    read: boolean;
}

interface ChatWindowProps {
    activeContact: User | null;
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
                {isLoading ? (
                    <div className={styles.loadingMessages}>
                        <p>Cargando mensajes...</p>
                    </div>
                ) : messages.length === 0 ? (
                    <div className={styles.noMessages}>
                        <p>No hay mensajes aún. ¡Inicia la conversación!</p>
                    </div>
                ) : (
                    messages.map((msg) => (
                        <Message
                            key={msg.id}
                            text={msg.content}
                            sender={msg.senderId === currentUserId ? "user" : "other"}
                            timestamp={msg.timestamp}
                        />
                    ))
                )}
            </div>

            <MessageInput onSendMessage={onSendMessage} />
        </div>
    );
}

export default ChatWindow;