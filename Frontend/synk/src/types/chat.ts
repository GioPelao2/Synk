export interface ContactData {
    id: number;
    name: string;
    status: string;
    avatarUrl: string;
}

export interface MessageData {
    id: number;
    text: string;
    timestamp: string;
    senderId: number;
}

export interface ConversationData {
    contactId: number;
    messages: MessageData[];
}

export interface ChatHeaderProps {
    contact: ContactData | null;
}