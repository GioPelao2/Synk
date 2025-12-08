'use client';

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

const ChatWindow: React.FC<ChatWindowProps> = ({
  activeContact,
  messages,
  onSendMessage,
  isLoading,
  currentUserId
}) => {
  if (!activeContact) {
    return (
      <div className={styles.placeholder}>
        <p>Selecciona un chat para ver los mensajes</p>
      </div>
    );
  }

  return (
    <div className={styles.ChatWindowContainer}>
      <ChatHeader contact={activeContact} />

      <div className={styles.messageList}>
        {isLoading ? (
          <div className={styles.loadingMessages}>Cargando mensajes...</div>
        ) : messages.length > 0 ? (
          messages.map((msg) => (
            <Message
              key={msg.id}
              text={msg.content}
              sender={msg.senderId === currentUserId ? "user" : "other"}
              timestamp={msg.timestamp}
            />
          ))
        ) : (
          <div className={styles.noMessages}>No hay mensajes aún</div>
        )}
      </div>

      <MessageInput onSendMessage={onSendMessage} />
    </div>
  );
}

export default ChatWindow;
