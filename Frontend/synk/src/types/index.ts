export interface User {
    id: number;
    username: string;
    email: string;
    status: string;
    lastSeen: string; 
}

export interface Message {
    id: number;
    senderUsername: string; 
    receiverUsername: string;
    senderId: number; 
    receiverId: number;
    content: string; 
    timestamp: string; 
    text: string;
    read: boolean;
}

export interface ChatHeaderProps {
    contact: ContactData | null;
}

export interface ContactData {
    id: number;
    name: string;
    status: string;
    avatarUrl?: string;
}

export type MessageData = Message;