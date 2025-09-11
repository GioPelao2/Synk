import styles from "@/styles/NavBar.module.css"
import { FcSearch } from "react-icons/fc";
import { MdFavoriteBorder } from "react-icons/md";

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
                    <FcSearch />   
                </button>
                <button className={styles.favoriteButton}>
                    <MdFavoriteBorder />
                </button>
            </div>

            <div className={styles.chatListSection}>
                <p>chat chat chat chat chat chat y mas chats...</p>
            </div>
        </div>
    );
}