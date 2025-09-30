'use client'
import React, { useState} from "react";
import Header from "@/components/layout/Header"
import NavBar from "@/components/layout/NavBar";
import Sidebar from "@/components/layout/Sidebar";
import ChatWindow from "@/components/chat/ChatWindow";
import { ContactData, ConversationData } from "@/types/chat";
import styles from "./page.module.css";

const MOCK_CONTACTS: ContactData[] = [
  { id: 1, name: 'JuaKo', status: 'pero hermano sakjfkj', avatarUrl: '/images/avatar1.jpg' },
  { id: 2, name: 'Hermano', status: 'Online', avatarUrl: '/images/avatar1.jpg' },
];

const MOCK_CONVERSATIONS: ConversationData[] = [
  {
    contactId: 1,
    messages: [
      { id: 101, text: "pero brother kasfjkas", timestamp: "9:45 AM", senderId: 1},
      { id: 102, text: "Como estai hrno?", timestamp: "12:21 PM", senderId: 999},
    ]
  },
  {
    contactId: 2,
    messages: [
      { id: 101, text: "llegaste a la casa?", timestamp: "14:27 PM", senderId: 2},
      { id: 102, text: "o sigues en la Ufro?", timestamp: "14:27 PM", senderId: 2},
      { id: 103, text: "ya llegué hrno, salí antes de clase", timestamp: "14:29 PM", senderId: 999},
    ]
  },
];

export default function Home() {
  const logoUrl = "/images/logo_SYNK.png"
  const [activeContact, setActiveContact] = useState<ContactData | null>(null);
 {/*clik contacto */}
  const handleContactSelect = (contact: ContactData) => {
    setActiveContact(contact);
  };

  //si no existe una conversación, se utilizará un array vacío
  const activeMessages = activeContact
  ? MOCK_CONVERSATIONS.find(c => c.contactId === activeContact.id)?.messages || []
  : [];

  return (
    <div className={styles.mainContainer}>
        <Header logoSrc={logoUrl} />

        <div className={styles.Columncontainer}>
            <Sidebar />
    
            <NavBar
                contacts={MOCK_CONTACTS}
                onContactClick={handleContactSelect}
            />

            <ChatWindow 
                activeContact={activeContact}
                messages={activeMessages}
            />
        </div>
    </div>
  );
}
