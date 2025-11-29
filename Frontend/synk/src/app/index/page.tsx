'use client'
import React, { useState, useEffect} from "react";
import { User, MessageData } from "@/types";
import Header from "@/components/layout/Header"
import NavBar from "@/components/layout/NavBar";
import Sidebar from "@/components/layout/Sidebar";
import ChatWindow from "@/components/chat/ChatWindow";
import { getOnlineUsers, getConversationHistory, sendMessage, markMessagesAsRead } from "@/services/api";
import styles from "./page.module.css";

export default function Home() {
  const logoUrl = "/images/logo_SYNK.png"
  const [onlineUsers, setOnlineUsers] = useState<User[]>([]);
  const [activeContact, setActiveContact] = useState<User | null>(null);
  const [messages, setMessages] = useState<MessageData[]>([]);
  const [isLoadingMessages, setIsLoadingMessages] = useState(false);

  const storedId = localStorage.getItem('currentUserId');
  const parsedId = storedId ? parseInt(storedId, 10) : 1;

  const myUserId = !isNaN(parsedId) && parsedId > 0 ? parsedId : 1;

  const handleContactSelect = async (user: User) => {
    setActiveContact(user);
    setIsLoadingMessages(true);
  

  try {
            // Cargar la conversación
            const history = await getConversationHistory(myUserId, user.id);
            setMessages(history);

            // Marcar mensajes como leídos
            await markMessagesAsRead(myUserId, user.id);
        } catch (error) {
            console.error("Error al cargar conversación:", error);
            setMessages([]);
        } finally {
            setIsLoadingMessages(false);
        }
    };

    const handleSendMessage = async (content: string) => {
        if (!activeContact || !content.trim()) {
            return;
        }

        if (myUserId <= 0 || !activeContact.id || activeContact.id <= 0) {
            console.error("Error: ID de emisor o receptor no válido.", { myUserId, activeContactId: activeContact.id });
            alert("No se pudo enviar el mensaje: ID de usuario inválido.");
            return;
        }
        
        try {
            const newMessage = await sendMessage(myUserId, activeContact.id, content);
            
            // Agregar el mensaje a la lista local
            setMessages(prev => [...prev, newMessage]);
        } catch (error) {
            console.error("Error al enviar mensaje:", error);
            alert("No se pudo enviar el mensaje");
        }
    };

  useEffect(() => {
    const loadUsers = async () => {
      const users = await getOnlineUsers(); // Llama a http://localhost:8080/api/users/online
      setOnlineUsers(users);
    };

      loadUsers();
  }, []);

  return (
    <div className={styles.mainContainer}>
        <Header logoSrc={logoUrl} />

        <div className={styles.Columncontainer}>
            <Sidebar />
    
            <NavBar
                users={onlineUsers}
                onContactClick={handleContactSelect}
                activeContact={activeContact}
            />

            <ChatWindow 
                activeContact={activeContact}
                messages={messages}
                onSendMessage={handleSendMessage}
                isLoading={isLoadingMessages}
                currentUserId={myUserId}
            />
        </div>
    </div>
  );
}
