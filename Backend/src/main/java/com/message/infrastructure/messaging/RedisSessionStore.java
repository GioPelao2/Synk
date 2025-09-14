package com.message.infrastructure.messaging;

import org.springframework.stereotype.Component;
import com.message.domain.valueobjects.UserId;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class RedisSessionStore {

    // Mapas en memoria que simulan Redis
    private final Map<String, SessionData> sessions = new ConcurrentHashMap<>(); // sessionId -> SessionData
    private final Map<Long, String> userSessions = new ConcurrentHashMap<>(); // userId -> sessionId
    private final Set<String> activeSessions = ConcurrentHashMap.newKeySet();
    private final Map<Long, Set<String>> userConnections = new ConcurrentHashMap<>(); // userId -> Set<connectionId>

    private static final long DEFAULT_SESSION_TIMEOUT_MINUTES = 30;

    private final ObjectMapper objectMapper;
    private final ScheduledExecutorService scheduler;

    public RedisSessionStore() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());

        // Programar limpieza automática cada 5 minutos
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.scheduler.scheduleAtFixedRate(this::cleanupExpiredSessions, 5, 5, TimeUnit.MINUTES);
    }

    // Crear nueva sesión para un usuario
    public String createSession(UserId userId) {
        String sessionId = generateSessionId();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusMinutes(DEFAULT_SESSION_TIMEOUT_MINUTES);

        // Invalidar sesión anterior si existe
        invalidateUserSessions(userId);

        // Crear nueva sesión
        UserSession session = new UserSession(sessionId, userId, now);
        SessionData sessionData = new SessionData(session, expiresAt);

        // Guardar en mapas
        sessions.put(sessionId, sessionData);
        userSessions.put(userId.value(), sessionId);
        activeSessions.add(sessionId);

        return sessionId;
    }

    // Obtener sesión por ID de sesión
    public Optional<UserSession> getSession(String sessionId) {
        SessionData sessionData = sessions.get(sessionId);

        if (sessionData == null || isExpired(sessionData)) {
            if (sessionData != null) {
                invalidateSession(sessionId);
            }
            return Optional.empty();
        }

        // Extender la sesión al accederla
        extendSession(sessionId);
        return Optional.of(sessionData.getSession());
    }

    // Obtener sesión por ID de usuario
    public Optional<String> getSessionByUserId(UserId userId) {
        String sessionId = userSessions.get(userId.value());

        if (sessionId != null) {
            // Verificar que la sesión aún existe y no ha expirado
            if (sessionExists(sessionId)) {
                extendSession(sessionId);
                return Optional.of(sessionId);
            } else {
                // Limpiar referencia huérfana
                userSessions.remove(userId.value());
            }
        }

        return Optional.empty();
    }

    // Verificar si una sesión existe y no ha expirado
    public boolean sessionExists(String sessionId) {
        SessionData sessionData = sessions.get(sessionId);
        return sessionData != null && !isExpired(sessionData);
    }

    // Verificar si un usuario tiene sesión activa
    public boolean hasActiveSession(UserId userId) {
        return getSessionByUserId(userId).isPresent();
    }

    // Extender tiempo de vida de una sesión
    public void extendSession(String sessionId) {
        SessionData sessionData = sessions.get(sessionId);
        if (sessionData != null && !isExpired(sessionData)) {
            LocalDateTime newExpiresAt = LocalDateTime.now().plusMinutes(DEFAULT_SESSION_TIMEOUT_MINUTES);
            sessionData.setExpiresAt(newExpiresAt);
            sessionData.getSession().setLastAccessedAt(LocalDateTime.now());
        }
    }

    // Invalidar sesión
    public void invalidateSession(String sessionId) {
        SessionData sessionData = sessions.remove(sessionId);

        if (sessionData != null) {
            UserId userId = sessionData.getSession().getUserId();

            // Limpiar mapeos
            userSessions.remove(userId.value());
            activeSessions.remove(sessionId);

            // Limpiar conexiones del usuario
            cleanupUserConnections(userId);
        }
    }

    // Invalidar todas las sesiones de un usuario
    public void invalidateUserSessions(UserId userId) {
        Optional<String> sessionIdOpt = getSessionByUserId(userId);
        if (sessionIdOpt.isPresent()) {
            invalidateSession(sessionIdOpt.get());
        }
    }

    // Obtener todas las sesiones activas
    public Set<String> getActiveSessions() {
        // Filtrar solo las sesiones no expiradas
        Set<String> validSessions = new HashSet<>();
        for (String sessionId : activeSessions) {
            if (sessionExists(sessionId)) {
                validSessions.add(sessionId);
            }
        }
        return validSessions;
    }

    // Limpiar sesiones expiradas
    public void cleanupExpiredSessions() {
        LocalDateTime now = LocalDateTime.now();
        List<String> expiredSessions = new ArrayList<>();

        for (Map.Entry<String, SessionData> entry : sessions.entrySet()) {
            if (isExpired(entry.getValue())) {
                expiredSessions.add(entry.getKey());
            }
        }

        // Limpiar sesiones expiradas
        for (String sessionId : expiredSessions) {
            invalidateSession(sessionId);
        }

        System.out.println("Cleaned up " + expiredSessions.size() + " expired sessions");
    }

    // Gestión de conexiones WebSocket por usuario
    public void addUserConnection(UserId userId, String connectionId) {
        userConnections.computeIfAbsent(userId.value(), k -> ConcurrentHashMap.newKeySet())
                      .add(connectionId);
    }

    public void removeUserConnection(UserId userId, String connectionId) {
        Set<String> connections = userConnections.get(userId.value());
        if (connections != null) {
            connections.remove(connectionId);
            if (connections.isEmpty()) {
                userConnections.remove(userId.value());
            }
        }
    }

    public Set<String> getUserConnections(UserId userId) {
        Set<String> connections = userConnections.get(userId.value());
        return connections != null ? new HashSet<>(connections) : new HashSet<>();
    }

    public void cleanupUserConnections(UserId userId) {
        userConnections.remove(userId.value());
    }

    // Métodos utilitarios
    private String generateSessionId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private boolean isExpired(SessionData sessionData) {
        return sessionData.getExpiresAt().isBefore(LocalDateTime.now());
    }

    // Configurar timeout personalizado para una sesión específica
    public void setSessionTimeout(String sessionId, long timeoutMinutes) {
        SessionData sessionData = sessions.get(sessionId);
        if (sessionData != null) {
            LocalDateTime newExpiresAt = LocalDateTime.now().plusMinutes(timeoutMinutes);
            sessionData.setExpiresAt(newExpiresAt);
        }
    }

    // Obtener información de la sesión para debug/monitoreo
    public SessionInfo getSessionInfo(String sessionId) {
        SessionData sessionData = sessions.get(sessionId);
        if (sessionData == null) {
            return null;
        }

        UserSession session = sessionData.getSession();
        long ttlSeconds = Duration.between(LocalDateTime.now(), sessionData.getExpiresAt()).getSeconds();
        Set<String> connections = getUserConnections(session.getUserId());

        return new SessionInfo(
            session.getSessionId(),
            session.getUserId(),
            session.getCreatedAt(),
            Math.max(0, ttlSeconds),
            connections.size()
        );
    }

    // Obtener estadísticas generales
    public SessionStatistics getStatistics() {
        cleanupExpiredSessions(); // Limpiar antes de contar

        int totalSessions = sessions.size();
        int totalConnections = userConnections.values().stream()
                                             .mapToInt(Set::size)
                                             .sum();

        return new SessionStatistics(totalSessions, totalConnections);
    }

    // Shutdown del servicio
    public void shutdown() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    // Clase para datos de sesión con expiración
    private static class SessionData {
        private final UserSession session;
        private LocalDateTime expiresAt;

        public SessionData(UserSession session, LocalDateTime expiresAt) {
            this.session = session;
            this.expiresAt = expiresAt;
        }

        public UserSession getSession() { return session; }
        public LocalDateTime getExpiresAt() { return expiresAt; }
        public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    }

    // Clase para sesión de usuario
    public static class UserSession {
        private String sessionId;
        private UserId userId;
        private LocalDateTime createdAt;
        private LocalDateTime lastAccessedAt;

        public UserSession() {} // Para Jackson

        public UserSession(String sessionId, UserId userId, LocalDateTime createdAt) {
            this.sessionId = sessionId;
            this.userId = userId;
            this.createdAt = createdAt;
            this.lastAccessedAt = createdAt;
        }

        // Getters y Setters
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }

        public UserId getUserId() { return userId; }
        public void setUserId(UserId userId) { this.userId = userId; }

        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

        public LocalDateTime getLastAccessedAt() { return lastAccessedAt; }
        public void setLastAccessedAt(LocalDateTime lastAccessedAt) { this.lastAccessedAt = lastAccessedAt; }

        @Override
        public String toString() {
            return "UserSession{" +
                    "sessionId='" + sessionId + '\'' +
                    ", userId=" + userId +
                    ", createdAt=" + createdAt +
                    ", lastAccessedAt=" + lastAccessedAt +
                    '}';
        }
    }

    // Clase para información de sesión (monitoreo)
    public static class SessionInfo {
        private final String sessionId;
        private final UserId userId;
        private final LocalDateTime createdAt;
        private final long ttlSeconds;
        private final int activeConnections;

        public SessionInfo(String sessionId, UserId userId, LocalDateTime createdAt,
                          long ttlSeconds, int activeConnections) {
            this.sessionId = sessionId;
            this.userId = userId;
            this.createdAt = createdAt;
            this.ttlSeconds = ttlSeconds;
            this.activeConnections = activeConnections;
        }

        // Getters
        public String getSessionId() { return sessionId; }
        public UserId getUserId() { return userId; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public long getTtlSeconds() { return ttlSeconds; }
        public int getActiveConnections() { return activeConnections; }
        public boolean isExpired() { return ttlSeconds <= 0; }

        @Override
        public String toString() {
            return "SessionInfo{" +
                    "sessionId='" + sessionId + '\'' +
                    ", userId=" + userId +
                    ", createdAt=" + createdAt +
                    ", ttlSeconds=" + ttlSeconds +
                    ", activeConnections=" + activeConnections +
                    ", expired=" + isExpired() +
                    '}';
        }
    }

    // Clase para estadísticas del sistema
    public static class SessionStatistics {
        private final int totalActiveSessions;
        private final int totalActiveConnections;

        public SessionStatistics(int totalActiveSessions, int totalActiveConnections) {
            this.totalActiveSessions = totalActiveSessions;
            this.totalActiveConnections = totalActiveConnections;
        }

        public int getTotalActiveSessions() { return totalActiveSessions; }
        public int getTotalActiveConnections() { return totalActiveConnections; }
        public double getAverageConnectionsPerSession() {
            return totalActiveSessions > 0 ? (double) totalActiveConnections / totalActiveSessions : 0.0;
        }

        @Override
        public String toString() {
            return "SessionStatistics{" +
                    "totalActiveSessions=" + totalActiveSessions +
                    ", totalActiveConnections=" + totalActiveConnections +
                    ", avgConnectionsPerSession=" + String.format("%.2f", getAverageConnectionsPerSession()) +
                    '}';
        }
    }
}
