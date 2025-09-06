import NavBar from "@/components/layout/NavBar";
import Sidebar from "@/components/layout/Sidebar";
import ChatWindow from "@/components/chat/ChatWindow";
import styles from "./page.module.css";

export default function Home() {
  return (
    <div className={styles.container}>
      <Sidebar />
      <ChatWindow />
      <NavBar />
    </div>
  );
}
