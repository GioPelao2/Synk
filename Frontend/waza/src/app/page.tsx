import NavBar from "@/components/layout/NavBar";
import Sidebar from "@/components/layout/Sidebar";
import ChatWindow from "@/components/chat/ChatWindow";
import styles from "@/styles/Chat.module.css"

export default function Home() {
  return (
    <div className={styles.page}>
      <NavBar />
      <Sidebar />
      <ChatWindow />
    </div>
  );
}
