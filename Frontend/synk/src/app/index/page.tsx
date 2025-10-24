'use client'
import React, { useState, useEffect} from "react";
import Header from "@/components/layout/Header"
import NavBar from "@/components/layout/NavBar";
import Sidebar from "@/components/layout/Sidebar";
import ChatWindow from "@/components/chat/ChatWindow";
import { getOnlineUsers, getConversationHistory } from "@/services/api";
import styles from "./page.module.css";

interface User{
  id: number;
  username: string;
  email: string;
  status: string;
  lastSeen: string;
}

interface Message {
  id: number;
  senderUsername: string;
  receiverUsername: string;
  content: string;
  timestamp: string;
}

export default function Home() {
  const logoUrl = "/images/logo_SYNK.png"
  const [onlineUsers, setOnlineUsers] = useState<User[]>([]);
  const [activeContact, setActiveContact] = useState<User | null>(null);
  const [messages, setMessages] = useState<Message[]>([]);
  const myUserId = 1;

  const handleContactSelect = (user: User) => {
    setActiveContact(user);
  };

  useEffect(() => {
    const loadUsers = async () => {
      const users = await getOnlineUsers(); // Llama a http://localhost:8080/api/users/online
      setOnlineUsers(users);
    };

      loadUsers();
  }, []);

  useEffect(() => {
    if (activeContact) {
      const loadConversation = async () => {
        const history = await getConversationHistory(myUserId, activeContact.id);
        setMessages(history);
      };
      loadConversation();
    } else {
      setMessages([]);
    }
  }, [activeContact]) 

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
            />
        </div>
    </div>
  );
}
