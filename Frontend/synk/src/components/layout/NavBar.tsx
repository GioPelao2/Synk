import React from "react";
import styles from "@/styles/NavBar.module.css"
import ChatBlock from "@/components/chat/ChatBlock";
import { ContactData } from "@/types/chat";

interface NavBarProps {
    contacts: ContactData[];
    onContactClick: (contact: ContactData) => void;
}

const NavBar: React.FC<NavBarProps> = ({ contacts, onContactClick }) => {
    return (
        <div className={styles.navContainer}>
            <div className={styles.topSection}> 
            <button className={styles.groupChatsButton}>Grupos</button>
            <button className={styles.groupChatsButton}>Chats</button>
            </div>   

            <div className={styles.searchSection}>
                <input
                    type="text"
                    placeholder="Buscar Contactos"
                    className={styles.searchInput}
                />
            </div>

            <div className={styles.chatListSection}>
                {contacts.map((contacts) => (
                    <div
                        key={contacts.id}
                        onClick={() => onContactClick(contacts)}
                        //posibles futuros estilos para el hover activo
                    >
                        <ChatBlock
                        avatarSrc={contacts.avatarUrl}
                        name={contacts.name}
                        lastMessage={contacts.status}
                        time="9:45 AM"
                        unreadCount={2}
                        onlineStatus="online"
                        />
                    </div>
                ))}
            </div>
        </div>
    );
};

export default NavBar;