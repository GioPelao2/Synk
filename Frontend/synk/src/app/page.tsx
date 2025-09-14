import Header from "@/components/layout/Header"
import NavBar from "@/components/layout/NavBar";
import Sidebar from "@/components/layout/Sidebar";
import ChatWindow from "@/components/chat/ChatWindow";
import styles from "./page.module.css";

export default function Home() {
  const logoUrl = "/images/logo_SYNK.png"

  return (
    <div className={styles.mainContainer}>
    <Header logoSrc={logoUrl} />

    <div className={styles.Columncontainer}>
    <Sidebar />
    <NavBar />
    <ChatWindow />
    </div>
    </div>
  );
}
