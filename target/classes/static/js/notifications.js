// notifications.js
// Sistema de notificaciones reutilizable

/**
 * Muestra una notificación de éxito
 * @param {string} message - Mensaje a mostrar
 * @param {number} duration - Duración en milisegundos (default: 3000)
 */
function showSuccess(message, duration = 3000) {
    showNotification(message, 'success', duration);
}

/**
 * Muestra una notificación de error
 * @param {string} message - Mensaje a mostrar
 * @param {number} duration - Duración en milisegundos (default: 3000)
 */
function showError(message, duration = 3000) {
    showNotification(message, 'error', duration);
}

/**
 * Muestra una notificación de información
 * @param {string} message - Mensaje a mostrar
 * @param {number} duration - Duración en milisegundos (default: 3000)
 */
function showInfo(message, duration = 3000) {
    showNotification(message, 'info', duration);
}

/**
 * Muestra una notificación de advertencia
 * @param {string} message - Mensaje a mostrar
 * @param {number} duration - Duración en milisegundos (default: 3000)
 */
function showWarning(message, duration = 3000) {
    showNotification(message, 'warning', duration);
}

/**
 * Función interna para crear y mostrar notificaciones
 * @param {string} message - Mensaje a mostrar
 * @param {string} type - Tipo de notificación (success, error, info, warning)
 * @param {number} duration - Duración en milisegundos
 */
function showNotification(message, type = 'info', duration = 3000) {
    // Iconos según el tipo
    const icons = {
        success: '✅',
        error: '❌',
        info: 'ℹ️',
        warning: '⚠️'
    };
    
    // Crear elemento de notificación
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.innerHTML = `
        <div class="notification-content">
            <span class="notification-icon">${icons[type] || icons.info}</span>
            <span class="notification-message">${message}</span>
            <button class="notification-close" onclick="closeNotification(this)">×</button>
        </div>
    `;
    
    // Añadir al body
    document.body.appendChild(notification);
    
    // Mostrar con animación
    setTimeout(() => notification.classList.add('show'), 10);
    
    // Auto-cerrar después de la duración especificada
    setTimeout(() => {
        closeNotificationElement(notification);
    }, duration);
}

/**
 * Cierra una notificación específica
 * @param {HTMLElement} button - Botón de cerrar clickeado
 */
function closeNotification(button) {
    const notification = button.closest('.notification');
    closeNotificationElement(notification);
}

/**
 * Cierra el elemento de notificación con animación
 * @param {HTMLElement} notification - Elemento de notificación a cerrar
 */
function closeNotificationElement(notification) {
    if (!notification) return;
    
    notification.classList.remove('show');
    setTimeout(() => {
        if (notification.parentNode) {
            notification.remove();
        }
    }, 300);
}

/**
 * Muestra un diálogo de confirmación personalizado
 * @param {string} message - Mensaje de confirmación
 * @param {Function} onConfirm - Callback si el usuario confirma
 * @param {Function} onCancel - Callback si el usuario cancela (opcional)
 * @param {Object} options - Opciones adicionales (title, confirmText, cancelText, type)
 */
function showConfirmDialog(message, onConfirm, onCancel = null, options = {}) {
    const defaults = {
        title: '⚠️ Confirmar acción',
        confirmText: 'Confirmar',
        cancelText: 'Cancelar',
        type: 'warning' // warning, danger, info
    };
    
    const config = { ...defaults, ...options };
    
    // Crear overlay
    const overlay = document.createElement('div');
    overlay.className = 'confirm-overlay';
    
    // Crear modal
    const modal = document.createElement('div');
    modal.className = `confirm-modal confirm-modal-${config.type}`;
    modal.innerHTML = `
        <div class="confirm-header">
            <h3>${config.title}</h3>
        </div>
        <div class="confirm-body">
            <p>${message}</p>
        </div>
        <div class="confirm-footer">
            <button class="btn-confirm-cancel">${config.cancelText}</button>
            <button class="btn-confirm-accept">${config.confirmText}</button>
        </div>
    `;
    
    overlay.appendChild(modal);
    document.body.appendChild(overlay);
    
    // Mostrar con animación
    setTimeout(() => {
        overlay.classList.add('show');
        modal.classList.add('show');
    }, 10);
    
    // Función para cerrar el modal
    const closeModal = () => {
        overlay.classList.remove('show');
        modal.classList.remove('show');
        setTimeout(() => overlay.remove(), 300);
    };
    
    // Event listeners
    const cancelBtn = modal.querySelector('.btn-confirm-cancel');
    const acceptBtn = modal.querySelector('.btn-confirm-accept');
    
    cancelBtn.addEventListener('click', () => {
        closeModal();
        if (typeof onCancel === 'function') {
            onCancel();
        }
    });
    
    acceptBtn.addEventListener('click', () => {
        closeModal();
        if (typeof onConfirm === 'function') {
            onConfirm();
        }
    });
    
    // Cerrar al hacer clic fuera del modal
    overlay.addEventListener('click', (e) => {
        if (e.target === overlay) {
            closeModal();
            if (typeof onCancel === 'function') {
                onCancel();
            }
        }
    });
    
    // Cerrar con ESC
    const handleEsc = (e) => {
        if (e.key === 'Escape') {
            closeModal();
            if (typeof onCancel === 'function') {
                onCancel();
            }
            document.removeEventListener('keydown', handleEsc);
        }
    };
    document.addEventListener('keydown', handleEsc);
}

/**
 * Limpia todas las notificaciones activas
 */
function clearAllNotifications() {
    const notifications = document.querySelectorAll('.notification');
    notifications.forEach(notification => {
        closeNotificationElement(notification);
    });
}

/**
 * Muestra un diálogo con input para editar precio
 * @param {Object} appointmentData - Datos de la cita (cliente, servicios, precio)
 * @param {Function} onConfirm - Callback con el precio final
 * @param {Function} onCancel - Callback si cancela (opcional)
 */
function showPriceDialog(appointmentData, onConfirm, onCancel = null) {
    // Crear overlay
    const overlay = document.createElement('div');
    overlay.className = 'confirm-overlay';
    
    // Crear modal
    const modal = document.createElement('div');
    modal.className = 'confirm-modal price-dialog-modal';
    
    const clientName = appointmentData.clientName || 'Sin nombre';
    const services = appointmentData.services || 'Sin servicios';
    const suggestedPrice = parseFloat(appointmentData.price || 0).toFixed(2);
    
    modal.innerHTML = `
        <div class="confirm-header">
            <h3>💰 Atender cita</h3>
        </div>
        <div class="confirm-body">
            <div class="price-dialog-info">
                <div class="info-row">
                    <span class="info-label">Cliente:</span>
                    <span class="info-value">${clientName}</span>
                </div>
                <div class="info-row">
                    <span class="info-label">Servicios:</span>
                    <span class="info-value">${services}</span>
                </div>
            </div>
            
            <div class="price-input-group">
                <label for="finalPrice">Precio final:</label>
                <div class="price-input-wrapper">
                    <input type="number" 
                           id="finalPrice" 
                           step="0.01" 
                           min="0" 
                           value="${suggestedPrice}"
                           class="price-input">
                    <span class="price-currency">€</span>
                </div>
                <small class="price-hint">Ajusta el precio si es necesario</small>
            </div>
        </div>
        <div class="confirm-footer">
            <button class="btn-confirm-cancel">Cancelar</button>
            <button class="btn-confirm-accept">Confirmar</button>
        </div>
    `;
    
    overlay.appendChild(modal);
    document.body.appendChild(overlay);
    
    // Mostrar con animación
    setTimeout(() => {
        overlay.classList.add('show');
        modal.classList.add('show');
    }, 10);
    
    // Focus en el input
    setTimeout(() => {
        const priceInput = modal.querySelector('#finalPrice');
        priceInput.focus();
        priceInput.select();
    }, 100);
    
    // Función para cerrar el modal
    const closeModal = () => {
        overlay.classList.remove('show');
        modal.classList.remove('show');
        setTimeout(() => overlay.remove(), 300);
    };
    
    // Event listeners
    const cancelBtn = modal.querySelector('.btn-confirm-cancel');
    const acceptBtn = modal.querySelector('.btn-confirm-accept');
    const priceInput = modal.querySelector('#finalPrice');
    
    // Validar que el precio sea positivo
    priceInput.addEventListener('input', () => {
        if (parseFloat(priceInput.value) < 0) {
            priceInput.value = 0;
        }
    });
    
    cancelBtn.addEventListener('click', () => {
        closeModal();
        if (typeof onCancel === 'function') {
            onCancel();
        }
    });
    
    acceptBtn.addEventListener('click', () => {
        const finalPrice = parseFloat(priceInput.value);
        
        if (isNaN(finalPrice) || finalPrice < 0) {
            showError('Por favor, introduce un precio válido');
            return;
        }
        
        closeModal();
        if (typeof onConfirm === 'function') {
            onConfirm(finalPrice.toFixed(2));
        }
    });
    
    // Confirmar con Enter
    priceInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            acceptBtn.click();
        }
    });
    
    // Cerrar al hacer clic fuera del modal
    overlay.addEventListener('click', (e) => {
        if (e.target === overlay) {
            closeModal();
            if (typeof onCancel === 'function') {
                onCancel();
            }
        }
    });
    
    // Cerrar con ESC
    const handleEsc = (e) => {
        if (e.key === 'Escape') {
            closeModal();
            if (typeof onCancel === 'function') {
                onCancel();
            }
            document.removeEventListener('keydown', handleEsc);
        }
    };
    document.addEventListener('keydown', handleEsc);
}