'use client';

import React, { useState, useEffect } from "react";
import Header from "@/components/layout/Header";
import NavBar from "@/components/layout/NavBar";
import Sidebar from "@/components/layout/Sidebar";
import ChatWindow from "@/components/chat/ChatWindow";
import AddContactModal from "@/components/ui/AddContactModal";
import { ContactData, MessageData, User } from "@/types";
import {
  getOnlineUsers,
  getConversationHistory,
  markMessagesAsRead,
  sendMessage,
  getUserById
} from "@/services/api";
import styles from "./page.module.css";

const mapUserToContactData = (user: User): ContactData => ({
  id: user.id,
  name: user.username,
  status: user.status,
  avatarUrl: "/images/avatars/default.png",
});

export default function Home() {
  const logoUrl = "/images/logo_SYNK.png";
  const [onlineUsers, setOnlineUsers] = useState<ContactData[]>([]);
  const [activeContact, setActiveContact] = useState<ContactData | null>(null);
  const [messages, setMessages] = useState<MessageData[]>([]);
  const [isLoadingMessages, setIsLoadingMessages] = useState(false);
  const [myUserId, setMyUserId] = useState<number>(1);
  const [isModalOpen, setIsModalOpen] = useState(false);

  useEffect(() => {
    if (typeof window !== 'undefined') {
      const userId = parseInt(sessionStorage.getItem('currentUserId') || '1');
      setMyUserId(userId);
    }
  }, []);

  const handleContactSelect = async (user: ContactData) => {
    setActiveContact(user);
    setIsLoadingMessages(true);

    try {
      const history = await getConversationHistory(myUserId, user.id);
      setMessages(history);
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
      setMessages(prev => [...prev, newMessage]);
    } catch (error) {
      console.error("Error al enviar mensaje:", error);
      alert("No se pudo enviar el mensaje");
    }
  };

  const handleContactAdded = async (userId: number) => {
    try {
      const user = await getUserById(userId);
      if (user) {
        const newContact = mapUserToContactData(user);

        const exists = onlineUsers.some(u => u.id === newContact.id);
        if (!exists) {
          setOnlineUsers(prev => [...prev, newContact]);
        }

        handleContactSelect(newContact);
      }
    } catch (error) {
      console.error("Error al agregar contacto:", error);
    }
  };

  useEffect(() => {
    const loadUsers = async () => {
      const users: User[] = await getOnlineUsers();
      const contactUsers = users.map(mapUserToContactData);
      setOnlineUsers(contactUsers);
    };

    loadUsers();
  }, []);

  return (
    <div className={styles.mainContainer}>
      <Header logoSrc={logoUrl} />

      <div className={styles.Columncontainer}>
        <Sidebar onAddContact={() => setIsModalOpen(true)} />

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

      <AddContactModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onContactAdded={handleContactAdded}
        currentUserId={myUserId}
      />
    </div>
  );
}
