import { error } from "console";

const BASE_URL = 'http://localhost:8080';

const LOGIN_URL = `${BASE_URL}/login`;

export async function loginUser(username: string, password: string) {
    try {
        const response = await fetch(LOGIN_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username, 
                password,
            }),
        });

        if (!response.ok) {
            const errorBodyText = await response.text();
            let errorMessage = `Fallo al iniciar sesión. Código: ${response.status} ${response.statusText}`;

            try {
                const errorData = JSON.parse(errorBodyText);
                errorMessage = errorData.message || errorMessage;
            } catch (e) {
            }
            
            throw new Error(errorMessage);
        }

        const data = await response.json(); 

        const jwtToken = data.token || data.jwt; 
        if (!jwtToken) {
            throw new Error("Token de autenticación no encontrado en la respuesta del backend.");
        }

        localStorage.setItem('authToken', jwtToken);
        localStorage.setItem('currentUserId', data.userId || '1'); 

        return jwtToken;

    } catch (error) {
        console.error("Error en la llamada a loginUser:", error);
        throw error;
    }
}

// obtener lista de usuarios en linea
export async function getOnlineUsers() {
    const token = localStorage.getItem('authToken');
    if (!token) {
        console.log("No hay token, la llamada a getOnlineUsers fue omitida.");
        return []; 
    }

    try{
        const response = await fetch(`${BASE_URL}/api/users/online`, {
           // headers: {
             //   'Authorization': `Bearer ${token}`,
            //},
        });
        
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

// obtener la conversación entre dos usuarios
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