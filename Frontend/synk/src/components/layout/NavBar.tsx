import styles from "@/styles/NavBar.module.css"
import { FcSearch } from "react-icons/fc";
import { MdOutlineFavorite } from "react-icons/md";
import ChatBlock from "@/components/chat/ChatBlock";

export default function NavBar () {
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
                <button className={styles.searchButton}>
                    <FcSearch  />
                </button>
                <button className={styles.favoriteButton}>
                    <MdOutlineFavorite />
                </button>
            </div>

            <div className={styles.chatListSection}>
                <ChatBlock
                    avatarSrc="/images/avatar1.jpg"
                    name="JuaKo"
                    lastMessage= "pero hermano ksaldj"
                    time="9:45 AM"
                    unreadCount={2}
                    onlineStatus="online"
                />
            </div>
        </div>
    );
}