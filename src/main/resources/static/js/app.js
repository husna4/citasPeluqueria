const uriApi = 'http://localhost:8080/api/citas';

let allAppointments = [];
let filteredAppointments = [];
let currentFilter = 'today';
let currentCustomDate = null;

async function loadAppointments() {
    showLoading(true);
    
    try {
        const response = await fetch(`${uriApi}/sin-atender`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if(!response.ok) {
            throw new Error('Error al cargar las citas: HTTP Error Status' + response.statusText);
        }

        allAppointments = await response.json();
        
        // Aplicar filtro inicial (hoy)
        applyCurrentFilter();
        
    } catch (error) {
        console.error('Error cargando citas:', error);
        showError('Error al cargar las citas');
    } finally {
        showLoading(false);
    }
}

function applyCurrentFilter() {
    switch(currentFilter) {
        case 'today':
            filteredAppointments = filterByDate(getTodayDate());
            updateFilterLabel('Hoy');
            break;
        case 'tomorrow':
            filteredAppointments = filterByDate(getTomorrowDate());
            updateFilterLabel('Mañana');
            break;
        case 'custom':
            if (currentCustomDate) {
                filteredAppointments = filterByDate(currentCustomDate);
                updateFilterLabel(formatDateLabel(currentCustomDate));
            }
            break;
        case 'all':
            filteredAppointments = [...allAppointments];
            updateFilterLabel('Todas las citas sin atender');
            break;
        default:
            filteredAppointments = filterByDate(getTodayDate());
            updateFilterLabel('Hoy');
    }
    
    renderAppointments(filteredAppointments);
}

function filterAppointments(filterType) {
    currentFilter = filterType;

    // Actualizar botones activos
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    
    const activeBtn = document.querySelector(`[data-filter="${filterType}"]`);
    if (activeBtn && filterType !== 'date' && filterType !== 'custom') { //Para la fecha custom, el boton se activa al abrir el datepicker, en la funcion: showDatePicker()
        activeBtn.classList.add('active');
        document.getElementById('datePickerContainer').style.display = 'none'; // Ocultar date picker si estaba abierto
    }
    
    applyCurrentFilter();

    // Guardar el filtro actual
    saveFilterState();
}

function filterByDate(targetDate) {
    return allAppointments.filter(appointment => {
        return appointment.fecha === targetDate;
    });
}

function getTodayDate() {
    const today = new Date();
    return formatDateISO(today);
}

function getTomorrowDate() {
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    return formatDateISO(tomorrow);
}

function formatDateISO(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

function formatDateLabel(dateString) {
    const date = new Date(dateString + 'T00:00:00');
    const today = new Date();
    const tomorrow = new Date(today);
    tomorrow.setDate(tomorrow.getDate() + 1);
    
    today.setHours(0, 0, 0, 0);
    tomorrow.setHours(0, 0, 0, 0);
    date.setHours(0, 0, 0, 0);
    
    if (date.getTime() === today.getTime()) {
        return 'Hoy';
    } else if (date.getTime() === tomorrow.getTime()) {
        return 'Mañana';
    } else {
        return date.toLocaleDateString('es-ES', { 
            weekday: 'long', 
            year: 'numeric', 
            month: 'long', 
            day: 'numeric' 
        });
    }
}

function updateFilterLabel(label) {
    const filterLabel = document.getElementById('currentFilterLabel');
    filterLabel.innerHTML = `<span>📅 Mostrando citas de: <strong>${label}</strong></span>`;
}

function showDatePicker() {
    const container = document.getElementById('datePickerContainer');
    const input = document.getElementById('customDateInput');
    
    // Establecer fecha mínima como hoy
    input.min = getTodayDate();
    input.value = getTodayDate();
    
    container.style.display = 'flex';
    input.focus();
    
    // Marcar el botón de fecha como activo
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    document.querySelector('[data-filter="date"]').classList.add('active');
}

function closeDatePicker() {
    const container = document.getElementById('datePickerContainer');
    container.style.display = 'none';
}

// Función para agrupar citas por fecha
function groupAppointmentsByDate(appointments) {
    const grouped = {};
    
    appointments.forEach(appointment => {
        if (!grouped[appointment.fecha]) {
            grouped[appointment.fecha] = [];
        }
        grouped[appointment.fecha].push(appointment);
    });
    
    // Ordenar fechas
    const sortedDates = Object.keys(grouped).sort();
    
    return sortedDates.map(date => ({
        fecha: date,
        appointments: grouped[date].sort((a, b) => a.hora.localeCompare(b.hora))
    }));
}

// Función para renderizar las citas
function renderAppointments(appointmentsToRender) {
    const container = document.getElementById('appointmentsContainer');
    const emptyState = document.getElementById('emptyState');
    const emptyMessage = document.getElementById('emptyStateMessage');

    if (appointmentsToRender.length === 0) {
        container.innerHTML = '';
        
        // Personalizar mensaje según el filtro
        if (currentFilter === 'today') {
            emptyMessage.textContent = 'No hay citas programadas para hoy';
        } else if (currentFilter === 'tomorrow') {
            emptyMessage.textContent = 'No hay citas programadas para mañana';
        } else if (currentFilter === 'custom') {
            emptyMessage.textContent = 'No hay citas para la fecha seleccionada';
        } else {
            emptyMessage.textContent = 'No hay citas sin atender';
        }
        
        emptyState.style.display = 'block';
        return;
    }

    emptyState.style.display = 'none';
    
    // Si es el filtro "all", agrupar por fechas
    if (currentFilter === 'all') {
        const groupedByDate = groupAppointmentsByDate(appointmentsToRender);
        
        container.innerHTML = groupedByDate.map(group => `
            <div class="date-group">
                <div class="date-separator">
                    <div class="date-separator-line"></div>
                    <div class="date-separator-label">
                        <span class="date-icon">📅</span>
                        <span class="date-text">${formatDateLabel(group.fecha)}</span>
                        <span class="date-count">${group.appointments.length} cita${group.appointments.length !== 1 ? 's' : ''}</span>
                    </div>
                    <div class="date-separator-line"></div>
                </div>
                
                <div class="appointments-group">
                    ${group.appointments.map(appointment => renderAppointmentCard(appointment)).join('')}
                </div>
            </div>
        `).join('');
    } else {
        // Para otros filtros, mostrar normalmente
        container.innerHTML = appointmentsToRender
            .sort((a, b) => a.hora.localeCompare(b.hora))
            .map(appointment => renderAppointmentCard(appointment))
            .join('');
    }
}

// Función auxiliar para renderizar una tarjeta de cita
function renderAppointmentCard(appointment) {
    return `
        <div class="appointment-card" data-id="${appointment.id}">
            <div class="appointment-header">
                <div class="datetime-block">
                    <div class="time-display">${formatHour(appointment.hora)}</div>
                </div>
            </div>

            <div class="client-block">
                <div class="client-name">${getNombreCliente(appointment.cliente)}</div>
                <div class="client-phone">${getTfnoCliente(appointment.cliente)}</div>
            </div>

             <div class="service-block">
                <div class="service-name">${formatCuttingNames(appointment.cortes)}</div>
                <div class="service-duration">${getSumDuration(appointment.cortes) === 0 ? '' : getSumDuration(appointment.cortes) + ' min'}</div>
                ${appointment.observaciones ? `<div class="observations">📝 ${escapeHtml(appointment.observaciones)}</div>` : ''}
            </div>

            <div class="price-status-block">
                <div class="price">${getSumCuttingPrices(appointment.cortes)} €</div>
            </div>
            
            <div class="appointment-actions">
                <button class="btn btn-attend" onclick="attendAppointment('${appointment.id}', '${escapeForAttribute(getNombreCliente(appointment.cliente))}', '${escapeForAttribute(formatCuttingNames(appointment.cortes))}', ${getSumCuttingPrices(appointment.cortes)})">
                    ✅ Atender
                </button>
                <button class="btn btn-edit" onclick="editAppointment('${appointment.id}')">
                    ✏️ Modificar
                </button>
                <button class="btn btn-delete" onclick="confirmDeleteAppointment('${appointment.id}')">
                    🗑️ Eliminar
                </button>
            </div>
        </div>
    `;
}

// Funciones auxiliares
function getStatusText(status) {
    const statusMap = {
        'true': 'Atendida',
        'false': 'Pendiente',
    };
    return statusMap[status];
}

function formatDate(dateString) {
    const date = new Date(dateString + 'T00:00:00');
    return date.toLocaleDateString('es-ES', { 
        weekday: 'short', 
        year: 'numeric', 
        month: 'short', 
        day: 'numeric' 
    });
}

function formatHour(hourString) {
    const [hours, minutes, seconds] = hourString.split(':');
    return `${hours}:${minutes}`;
}

function getSumCuttingPrices(cortes) {
    let suma = 0

    for(const corte of cortes) {
        suma += corte.precio
    }

    return suma.toFixed(2);
}

function getSumDuration(cortes) {
    let suma = 0

    for(const corte of cortes) {
        suma += corte.duracion
    }

    return suma;
}

function getNombreCliente(cliente) {
    if(cliente && cliente.nombre) {
        return cliente.nombre;
    }

    return "Sin nombre";
}

function getTfnoCliente(cliente) {
    if(cliente) {
        return cliente.tfno ?? 'Sin teléfono';
    }

    return "Sin teléfono";
}

function formatCuttingNames(cortes) {
    return cortes.map(corte => corte.nombre).join(', ');
}

function escapeForAttribute(text) {
    return text.replace(/'/g, "\\'").replace(/"/g, '&quot;');
}

function showLoading(show) {
    document.getElementById('loadingState').style.display = show ? 'block' : 'none';
}

// Función de búsqueda
function searchAppointments() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    
    if (searchTerm === '') {
        renderAppointments(filteredAppointments);
        return;
    }
    
    const searchResults = filteredAppointments.filter(appointment => {
        const clientName = getNombreCliente(appointment.cliente).toLowerCase();
        const clientPhone = getTfnoCliente(appointment.cliente).toLowerCase();
        const services = formatCuttingNames(appointment.cortes).toLowerCase();
        
        return clientName.includes(searchTerm) ||
               clientPhone.includes(searchTerm) ||
               services.includes(searchTerm);
    });
    
    renderAppointments(searchResults);
}

// Funciones para manejar citas
function showNewAppointmentForm() {
    window.location.href = 'html/nuevaCita.html';
}

function editAppointment(id) {
    window.location.href = `html/modificarCita.html?id=${id}`;
}

function confirmDeleteAppointment(id) {
    showConfirmDialog(
        '¿Estás seguro de que deseas eliminar esta cita? Esta acción no se puede deshacer.',
        () => deleteAppointment(id),
        null,
        {
            type: 'danger',
            title: '⚠️ Eliminar cita',
            confirmText: 'Eliminar',
            cancelText: 'Cancelar'
        }
    );
}

async function deleteAppointment(id) {
    try {
        showLoading(true);
        
        const response = await fetch(`${uriApi}/${id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('Error al eliminar la cita: ' + response.statusText);
        }

        await loadAppointments();
        showSuccess('Cita eliminada exitosamente');
        
    } catch (error) {
        console.error('Error eliminando cita:', error);
        showError('Error al eliminar la cita. Por favor, inténtalo de nuevo.');
    } finally {
        showLoading(false);
    }
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Función para atender una cita
function attendAppointment(id, clientName, services, price) {
    showPriceDialog(
        {
            clientName: clientName,
            services: services,
            price: price
        },
        (finalPrice) => confirmAttendAppointment(id, finalPrice)
    );
}

// Confirmar atención de cita con el precio final
async function confirmAttendAppointment(id, finalPrice) {
    try {
        showLoading(true);
        
        const response = await fetch(`${uriApi}/${id}/atender`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                precio: parseFloat(finalPrice)
            })
        });

        if (!response.ok) {
            throw new Error('Error al atender la cita: ' + response.statusText);
        }

        await loadAppointments();
        showSuccess(`Cita atendida correctamente. Precio final: ${finalPrice} €`);
        
    } catch (error) {
        console.error('Error atendiendo cita:', error);
        showError('Error al atender la cita. Por favor, inténtalo de nuevo.');
    } finally {
        showLoading(false);
    }
}

/**
 * Guarda el estado actual del filtro en sessionStorage
 */
function saveFilterState() {
    const filterState = {
        filter: currentFilter,
        customDate: currentCustomDate,
        timestamp: new Date().getTime()
    };
    sessionStorage.setItem('appointmentFilterState', JSON.stringify(filterState));
}

/**
 * Carga el estado guardado del filtro desde sessionStorage
 */
function loadFilterState() {
    const saved = sessionStorage.getItem('appointmentFilterState');
    
    if (saved) {
        const filterState = JSON.parse(saved);
        
        // Solo restaurar si es reciente (menos de 30 minutos)
        const age = new Date().getTime() - filterState.timestamp;
        if (age < 30 * 60 * 1000) {
            currentFilter = filterState.filter;
            currentCustomDate = filterState.customDate;
            
            // Limpiar después de restaurar
            sessionStorage.removeItem('appointmentFilterState');
            
            return true;
        }
    }
    
    return false;
}

/**
 * Restaura la UI del filtro guardado
 */
function restoreFilterUI() {
    // Actualizar botones activos
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    
    if (currentFilter === 'today' || currentFilter === 'tomorrow' || currentFilter === 'all') {
        const activeBtn = document.querySelector(`[data-filter="${currentFilter}"]`);
        if (activeBtn) {
            activeBtn.classList.add('active');
        }
    } else if (currentFilter === 'custom' && currentCustomDate) {
        const dateInput = document.getElementById('customDateInput');
        if (dateInput) {
            dateInput.value = currentCustomDate;
        }
        
        const container = document.getElementById('datePickerContainer');
        if (container) {
            container.style.display = 'flex';
        }
        
        document.querySelector('[data-filter="date"]').classList.add('active');
    }
}

// Inicializar la aplicación
document.addEventListener('DOMContentLoaded', function() {
    // Cargar el filtro guardado antes de cargar las citas
    const hasFilterState = loadFilterState();
    
    // Cargar citas
    loadAppointments();
    
    // Si había un filtro guardado, restaurar la UI de forma robusta
    if (hasFilterState) {
        waitForFilterElementsAndRestoreUI();
        saveFilterState();
    }

    // Espera a que los elementos del filtro existan antes de restaurar la UI
    function waitForFilterElementsAndRestoreUI() {
        const maxWait = 2000; // ms
        const interval = 50; // ms
        let waited = 0;
        function check() {
            // Comprobar si los elementos clave existen
            const filterBtns = document.querySelectorAll('.filter-btn');
            const filterLabel = document.getElementById('currentFilterLabel');
            if (filterBtns.length > 0 && filterLabel) {
                restoreFilterUI();
            } else if (waited < maxWait) {
                waited += interval;
                setTimeout(check, interval);
            } // Si no, no hace nada
        }
        check();
    }
    
    // Listener para el date picker personalizado
    const customDateInput = document.getElementById('customDateInput');
    if (customDateInput) {
        customDateInput.addEventListener('change', function() {
            currentCustomDate = this.value;
            filterAppointments('custom');
            
            document.querySelectorAll('.filter-btn').forEach(btn => {
                btn.classList.remove('active');
            });
            document.querySelector('[data-filter="date"]').classList.add('active');
        });
    }
});