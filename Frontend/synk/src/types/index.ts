export interface User {
    id: number;
    username: string;
    email: string;
    status: string;
    lastSeen: string; 
}

export interface Message {
    id: number;
    senderId: number; 
    receiverId: number;
    senderUsername?: string;
    receiverUsername?: string;
    content: string; 
    timestamp: string; 
    read: boolean;
}

export interface ContactData {
    id: number;
    name: string;
    status: string;
    avatarUrl?: string;
}

export type MessageData = Message;