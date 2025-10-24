import { error } from "console";

const BASE_URL = 'http://localhost:8080/api';

//obtener lista de usuarios en linea
export async function getOnlineUsers() {
    try{
        const response = await fetch(`${BASE_URL}/users/online`);

        if (!response.ok) {
            throw new Error(`Error al obtener usuarios online: ${response.statusText}`);
        }

        // el backend devuelve una lista<UserDTO>
        const users = await response.json();
        return users;
    } catch (error) {
        console.error("Error en la llamada a getOnlineUsers:", error);
        // devolvemos un array vacio en caso de fallo para evitar romper la UI
        return [];  
    }
}

//obtener la conversación entre dos usuarios
export async function getConversationHistory(userId1: number, userId2: number) {
    try {
        const response = await fetch(`${BASE_URL}/messages/conversation/${userId1}/${userId2}`);

        if (!response.ok) {
            throw new Error(`Error al obtener conversación: ${response.statusText}`);
        }

        // el backend devuelve un List<MessageDTO>
        const messages = await response.json();
        return messages; 
    } catch (error) {
        console.error("Error en la llamada a getConversationHistory", error);
        return [];
    }
}

