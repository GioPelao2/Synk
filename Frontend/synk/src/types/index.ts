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
    content: string; 
    timestamp: string; 
    read: boolean;
}