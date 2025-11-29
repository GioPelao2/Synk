'use client'
import React, { useState, useEffect} from "react";
import Header from "@/components/layout/Header"
import NavBar from "@/components/layout/NavBar";
import Sidebar from "@/components/layout/Sidebar";
import ChatWindow from "@/components/chat/ChatWindow";
import { ContactData, MessageData, User } from "@/types";
import { getOnlineUsers, getConversationHistory, markMessagesAsRead, sendMessage } from "@/services/api";
import styles from "./page.module.css";

const mapUserToContactData = (user: User): ContactData => ({
    id: user.id,
    name: user.username, 
    status: user.status,
    avatarUrl: `/images/avatars/${user.id}.png`, 
});

export default function Home() {
  const logoUrl = "/images/logo_SYNK.png"
  const [onlineUsers, setOnlineUsers] = useState<ContactData[]>([]);
  const [activeContact, setActiveContact] = useState<ContactData | null>(null);
  const [messages, setMessages] = useState<MessageData[]>([]);
  const [isLoadingMessages, setIsLoadingMessages] = useState(false);
  


  const myUserId = parseInt(localStorage.getItem('currentUserId') || '1'); 

  const handleContactSelect = async (user: ContactData) => {
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
      const users: User[] = await getOnlineUsers(); // Llama a http://localhost:8080/api/users/online
      
      const contactUsers = users.map(mapUserToContactData);
      setOnlineUsers(contactUsers);
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
