import React from "react";
import styles from "@/styles/NavBar.module.css"
import ChatBlock from "@/components/chat/ChatBlock";
import { User } from "@/types";

interface NavBarProps {
    users: User[];
    onContactClick: (user: User) => void;
    activeContact: User | null;
}

const NavBar: React.FC<NavBarProps> = ({ users, onContactClick, activeContact }) => {
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
                {users.map((user) => (
                    <div
                        key={user.id}
                        onClick={() => onContactClick(user)}
                        //posibles futuros estilos para el hover activo
                    >
                        <ChatBlock
                        name={user.username}
                        lastMessage={user.status}
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