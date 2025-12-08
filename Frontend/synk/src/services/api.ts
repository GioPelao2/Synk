const BASE_URL = 'https://backend-synk.giovanny.cl';

const LOGIN_URL = `${BASE_URL}/login`;
const REGISTER_URL = `${BASE_URL}/api/users/register`;

// Helper para verificar si estamos en el cliente
const isBrowser = typeof window !== 'undefined';

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
      let errorMessage = `Fallo al iniciar sesiÃ³n. CÃ³digo: ${response.status} ${response.statusText}`;

      try {
        const errorData = JSON.parse(errorBodyText);
        errorMessage = errorData.message || errorMessage;
      } catch (e) {
        // Ignorar error de parse
      }

      throw new Error(errorMessage);
    }

    const data = await response.json();

    // El AuthController devuelve token, userId y username
    const jwtToken = data.token || data.jwt;
    if (!jwtToken) {
      throw new Error("Token de autenticaciÃ³n no encontrado en la respuesta del backend.");
    }

    if (isBrowser) {
      sessionStorage.setItem('authToken', jwtToken);
      sessionStorage.setItem('currentUserId', String(data.userId));
      sessionStorage.setItem('currentUsername', data.username);
    }

    return {
      token: jwtToken,
      userId: data.userId,
      username: data.username
    };

  } catch (error) {
    console.error("Error en la llamada a loginUser:", error);
    throw error;
  }
}

// Obtener lista de todos los usuarios
export async function getAllUsers() {
  if (!isBrowser) return [];

  const token = sessionStorage.getItem('authToken');

  try {
    const response = await fetch(`${BASE_URL}/api/users`, {
      headers: token ? {
        'Authorization': `Bearer ${token}`,
      } : {},
    });

    if (!response.ok) {
      throw new Error(`Error al obtener usuarios: ${response.statusText}`);
    }

    const users = await response.json();
    return users;
  } catch (error) {
    console.error("Error en la llamada a getAllUsers:", error);
    return [];
  }
}

// Obtener lista de usuarios en lÃ­nea
export async function getOnlineUsers() {
  if (!isBrowser) return [];

  const token = sessionStorage.getItem('authToken');

  try {
    const response = await fetch(`${BASE_URL}/api/users/online`, {
      headers: token ? {
        'Authorization': `Bearer ${token}`,
      } : {},
    });

    if (!response.ok) {
      throw new Error(`Error al obtener usuarios online: ${response.statusText}`);
    }

    const users = await response.json();
    return users;
  } catch (error) {
    console.error("Error en la llamada a getOnlineUsers:", error);
    return [];
  }
}

// Obtener la conversaciÃ³n entre dos usuarios
export async function getConversationHistory(userId1: number, userId2: number) {
  if (!isBrowser) return [];

  const token = sessionStorage.getItem('authToken');

  if (!token) {
    console.warn("Token no encontrado para conversaciÃ³n");
    return [];
  }

  try {
    const url = `${BASE_URL}/api/messages/conversation/${userId1}/${userId2}`;
    const response = await fetch(url, {
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      if (response.status === 404) {
        console.log("No hay conversaciÃ³n previa entre estos usuarios");
        return [];
      }
      throw new Error(`Error al obtener conversaciÃ³n: ${response.statusText}`);
    }

    const messages = await response.json();
    return messages;
  } catch (error) {
    console.error("Error en getConversationHistory:", error);
    return [];
  }
}

// Enviar mensaje
export async function sendMessage(senderId: number, receiverId: number, content: string) {
  if (!isBrowser) {
    throw new Error("No disponible en el servidor");
  }

  const token = sessionStorage.getItem('authToken');

  if (!token) {
    throw new Error("Token no encontrado");
  }

  try {
    const response = await fetch(`${BASE_URL}/api/messages/send`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify({
        senderId,
        receiverId,
        content
      }),
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || 'Error al enviar mensaje');
    }

    const message = await response.json();
    return message;
  } catch (error) {
    console.error("Error al enviar mensaje:", error);
    throw error;
  }
}

// Marcar mensajes como leÃ­dos
export async function markMessagesAsRead(userId1: number, userId2: number) {
  if (!isBrowser) return;

  const token = sessionStorage.getItem('authToken');

  if (!token) {
    console.warn("Token no encontrado");
    return;
  }

  try {
    const response = await fetch(
      `${BASE_URL}/api/messages/conversation/${userId1}/${userId2}/read-all`,
      {
        method: 'PUT',
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      }
    );

    if (!response.ok) {
      console.error("Error al marcar mensajes como leÃ­dos");
    }
  } catch (error) {
    console.error("Error en markMessagesAsRead:", error);
  }
}

// Obtener conteo de mensajes no leÃ­dos
export async function getUnreadMessagesCount(userId: number) {
  if (!isBrowser) return 0;

  const token = sessionStorage.getItem('authToken');

  if (!token) {
    return 0;
  }

  try {
    const response = await fetch(`${BASE_URL}/api/messages/unread-count/${userId}`, {
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      return 0;
    }

    const count = await response.json();
    return count;
  } catch (error) {
    console.error("Error al obtener mensajes no leÃ­dos:", error);
    return 0;
  }
}

// Registrar usuario
export async function registerUser(username: string, email: string, password: string) {
  try {
    const response = await fetch(REGISTER_URL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        username,
        email,
        password,
      }),
    });

    if (!response.ok) {
      const errorBodyText = await response.text();
      let errorMessage = `Fallo al registrar la cuenta. CÃ³digo: ${response.status} ${response.statusText}`;

      try {
        const errorData = JSON.parse(errorBodyText);
        errorMessage = typeof errorData === 'string' ? errorData : (errorData.message || errorMessage);
      } catch (e) {
        errorMessage = errorBodyText || errorMessage;
      }

      throw new Error(errorMessage);
    }

    const data = await response.json();

    return { success: true, data };

  } catch (error) {
    console.error("Error en la llamada a registerUser:", error);
    throw error;
  }
}

// Buscar usuario por username
export async function searchUserByUsername(username: string) {
  if (!isBrowser) return null;

  const token = sessionStorage.getItem('authToken');

  if (!token) {
    console.warn("Token no encontrado para bÃºsqueda");
    return null;
  }

  if (!username || username.trim() === '') {
    return null;
  }

  try {
    const response = await fetch(`${BASE_URL}/api/users/search?username=${encodeURIComponent(username)}`, {
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      if (response.status === 404) {
        return null;
      }
      throw new Error(`Error al buscar usuario: ${response.statusText}`);
    }

    const user = await response.json();
    return user;
  } catch (error) {
    console.error("Error en la llamada a searchUserByUsername:", error);
    return null;
  }
}

// Obtener usuario por ID
export async function getUserById(userId: number) {
  if (!isBrowser) return null;

  const token = sessionStorage.getItem('authToken');

  if (!token) {
    console.warn("Token no encontrado");
    return null;
  }

  try {
    const response = await fetch(`${BASE_URL}/api/users/${userId}`, {
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      if (response.status === 404) {
        return null;
      }
      throw new Error(`Error al obtener usuario: ${response.statusText}`);
    }

    const user = await response.json();
    return user;
  } catch (error) {
    console.error("Error en getUserById:", error);
    return null;
  }
}

// Cambiar estado del usuario a online
export async function setUserOnline(userId: number) {
  if (!isBrowser) return null;

  const token = sessionStorage.getItem('authToken');

  if (!token) return null;

  try {
    const response = await fetch(`${BASE_URL}/api/users/${userId}/online`, {
      method: 'PUT',
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      throw new Error(`Error al cambiar estado a online: ${response.statusText}`);
    }

    const user = await response.json();
    return user;
  } catch (error) {
    console.error("Error en setUserOnline:", error);
    return null;
  }
}

// Cambiar estado del usuario a offline
export async function setUserOffline(userId: number) {
  if (!isBrowser) return null;

  const token = sessionStorage.getItem('authToken');

  if (!token) return null;

  try {
    const response = await fetch(`${BASE_URL}/api/users/${userId}/offline`, {
      method: 'PUT',
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      throw new Error(`Error al cambiar estado a offline: ${response.statusText}`);
    }

    const user = await response.json();
    return user;
  } catch (error) {
    console.error("Error en setUserOffline:", error);
    return null;
  }
}

// Cambiar estado del usuario a away
export async function setUserAway(userId: number) {
  if (!isBrowser) return null;

  const token = sessionStorage.getItem('authToken');

  if (!token) return null;

  try {
    const response = await fetch(`${BASE_URL}/api/users/${userId}/away`, {
      method: 'PUT',
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      throw new Error(`Error al cambiar estado a away: ${response.statusText}`);
    }

    const user = await response.json();
    return user;
  } catch (error) {
    console.error("Error en setUserAway:", error);
    return null;
  }
}

// Verificar disponibilidad de username
export async function checkUsernameAvailability(username: string) {
  if (!isBrowser) return false;

  try {
    const response = await fetch(`${BASE_URL}/api/users/check-username?username=${encodeURIComponent(username)}`);

    if (!response.ok) {
      return false;
    }

    const available = await response.json();
    return available;
  } catch (error) {
    console.error("Error en checkUsernameAvailability:", error);
    return false;
  }
}

// Verificar disponibilidad de email
export async function checkEmailAvailability(email: string) {
  if (!isBrowser) return false;

  try {
    const response = await fetch(`${BASE_URL}/api/users/check-email?email=${encodeURIComponent(email)}`);

    if (!response.ok) {
      return false;
    }

    const available = await response.json();
    return available;
  } catch (error) {
    console.error("Error en checkEmailAvailability:", error);
    return false;
  }
}
