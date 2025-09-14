import styles from "@/styles/HeaderBar.module.css"
import Logo from "@/components/ui/Logo"

interface HeaderProps {
    logoSrc: string;
}

export default function Header({ logoSrc }: HeaderProps) {
    return (
        <header className={styles.header}>
        <div className={styles.logoContainer}>
        <Logo src={logoSrc} alt="Logo App" />
        </div>
        </header>
    );
}
