'use client';

import React, { useState } from 'react';
import styles from '@/styles/AddContactModal.module.css';
import { searchUserByUsername } from '@/services/api';

interface AddContactModalProps {
  isOpen: boolean;
  onClose: () => void;
  onContactAdded: (userId: number) => void;
  currentUserId: number;
}

const AddContactModal: React.FC<AddContactModalProps> = ({
  isOpen,
  onClose,
  onContactAdded,
  currentUserId
}) => {
  const [searchQuery, setSearchQuery] = useState('');
  const [searchResult, setSearchResult] = useState<any>(null);
  const [isSearching, setIsSearching] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSearch = async () => {
    if (!searchQuery.trim()) {
      setError('Ingresa un nombre de usuario');
      return;
    }

    setIsSearching(true);
    setError(null);
    setSearchResult(null);

    try {
      const user = await searchUserByUsername(searchQuery.trim());

      if (!user) {
        setError('Usuario no encontrado');
      } else if (user.id === currentUserId) {
        setError('No puedes agregarte a ti mismo');
      } else {
        setSearchResult(user);
      }
    } catch (err) {
      setError('Error al buscar usuario');
    } finally {
      setIsSearching(false);
    }
  };

  const handleAddContact = () => {
    if (searchResult) {
      onContactAdded(searchResult.id);
      handleClose();
    }
  };

  const handleClose = () => {
    setSearchQuery('');
    setSearchResult(null);
    setError(null);
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div className={styles.modalOverlay} onClick={handleClose}>
      <div className={styles.modalContent} onClick={(e) => e.stopPropagation()}>
        <div className={styles.modalHeader}>
          <h2>Agregar Contacto</h2>
          <button className={styles.closeButton} onClick={handleClose}>×</button>
        </div>

        <div className={styles.modalBody}>
          <div className={styles.searchSection}>
            <input
              type="text"
              placeholder="Buscar por nombre de usuario"
              className={styles.searchInput}
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
            />
            <button
              className={styles.searchButton}
              onClick={handleSearch}
              disabled={isSearching}
            >
              {isSearching ? 'Buscando...' : 'Buscar'}
            </button>
          </div>

          {error && <p className={styles.errorMessage}>{error}</p>}

          {searchResult && (
            <div className={styles.resultCard}>
              <div className={styles.userInfo}>
                <div className={styles.avatar}>
                  <img
                    src={searchResult.avatarUrl || '/images/avatars/default.png'}
                    alt={searchResult.username}
                  />
                </div>
                <div className={styles.userDetails}>
                  <h3>{searchResult.username}</h3>
                  <p className={styles.status}>{searchResult.status}</p>
                </div>
              </div>
              <button
                className={styles.addButton}
                onClick={handleAddContact}
              >
                Iniciar Chat
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default AddContactModal;
