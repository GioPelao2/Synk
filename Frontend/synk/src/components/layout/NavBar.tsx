import React, { useState } from "react";
import styles from "@/styles/NavBar.module.css"
import ChatBlock from "@/components/chat/ChatBlock";
import { User } from "@/types";

interface NavBarProps {
    users: User[];
    onContactClick: (user: User) => void;
    activeContact: User | null;
}

const NavBar: React.FC<NavBarProps> = ({ users, onContactClick, activeContact }) => {
    const [searchQuery, setSearchQuery] = useState('');
  
    const filteredUsers = users.filter(user => 
        user.username.toLowerCase().includes(searchQuery.toLowerCase())
    );

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
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                />
            </div>

            <div className={styles.chatListSection}>
                {filteredUsers.length > 0 ? (
                    filteredUsers.map((user) => (
                        <div
                            key={user.id}
                            onClick={() => onContactClick(user)}
                            className={activeContact?.id === user.id ? styles.activeChat : ''}
                        >
                            <ChatBlock
                                name={user.username}
                                lastMessage={user.status}
                                time="9:45 AM"
                                unreadCount={2}
                                onlineStatus="online"
                            />
                        </div>
                    ))
                ) : (
                    <div className={styles.noResults}>
                        <p>No se encontraron usuarios con "{searchQuery}"</p>
                    </div>
                )}
            </div>
        </div>
    );
};

export default NavBar;