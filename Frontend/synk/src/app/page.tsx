'use client'
import React, { useState} from "react";
import Header from "@/components/layout/Header"
import NavBar from "@/components/layout/NavBar";
import Sidebar from "@/components/layout/Sidebar";
import ChatWindow from "@/components/chat/ChatWindow";
import { ContactData } from "@/types/chat";
import styles from "./page.module.css";

const MOCK_CONTACTS: ContactData[] = [
  { id: 1, name: 'JuaKo', status: 'pero hermano sakjfkj', avatarUrl: '/images/avatar1.jpg' },
  { id: 2, name: 'Hermano', status: 'Online', avatarUrl: '/images/avatar1.jpg' },
];

export default function Home() {
  const logoUrl = "/images/logo_SYNK.png"
  const [activeContact, setActiveContact] = useState<ContactData | null>(null);
 {/*clik contacto */}
  const handleContactSelect = (contact: ContactData) => {
    setActiveContact(contact);
  };

  return (
    <div className={styles.mainContainer}>
        <Header logoSrc={logoUrl} />

        <div className={styles.Columncontainer}>
            <Sidebar />
    
            <NavBar
                contacts={MOCK_CONTACTS}
                onContactClick={handleContactSelect}
            />

            <ChatWindow activeContact={activeContact} />
        </div>
    </div>
  );
}
