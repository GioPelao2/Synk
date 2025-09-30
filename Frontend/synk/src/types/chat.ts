export interface ContactData {
    id: number;
    name: string;
    status: string;
    avatarUrl: string;
}

export interface ChatHeaderProps {
    contact: ContactData | null;
}