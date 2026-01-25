// historial.js
const uriApi = 'http://localhost:8080/api/citas/atendidas';

let allAppointments = [];
let filteredAppointments = [];
let dateFrom = null;
let dateTo = null;

async function loadHistorial() {
    showLoading(true);
    
    try {
        const response = await fetch(`${uriApi}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if(!response.ok) {
            throw new Error('Error al cargar el historial: ' + response.statusText);
        }

        allAppointments = await response.json();
        
        // Ordenar por fecha descendente (más reciente primero)
        allAppointments.sort((a, b) => {
            const dateA = new Date(a.fecha + 'T' + a.hora);
            const dateB = new Date(b.fecha + 'T' + b.hora);
            return dateB - dateA;
        });
        
        filteredAppointments = [...allAppointments];
        
        renderAppointments(filteredAppointments);
        updateStats(filteredAppointments);
        
    } catch (error) {
        console.error('Error cargando historial:', error);
        showError('Error al cargar el historial');
    } finally {
        showLoading(false);
    }
}

function filterByDateRange() {
    const rangeFilter = document.getElementById('dateRangeFilter').value;
    const customDateRange = document.getElementById('customDateRange');
    
    if (rangeFilter === 'custom') {
        customDateRange.style.display = 'block';
        return;
    } else {
        customDateRange.style.display = 'none';
    }
    
    const today = new Date();
    let filterDate = null;
    
    switch(rangeFilter) {
        case 'today':
            filterDate = new Date();
            filteredAppointments = allAppointments.filter(app => {
                return app.fecha === formatDateISO(filterDate);
            });
            break;
            
        case 'week':
            filterDate = new Date();
            filterDate.setDate(filterDate.getDate() - 7);
            filteredAppointments = allAppointments.filter(app => {
                const appDate = new Date(app.fecha);
                return appDate >= filterDate;
            });
            break;
            
        case 'month':
            filterDate = new Date();
            filterDate.setMonth(filterDate.getMonth() - 1);
            filteredAppointments = allAppointments.filter(app => {
                const appDate = new Date(app.fecha);
                return appDate >= filterDate;
            });
            break;
            
        case 'all':
        default:
            filteredAppointments = [...allAppointments];
            break;
    }
    
    renderAppointments(filteredAppointments);
    updateStats(filteredAppointments);
}

function applyCustomDateRange() {
    const dateFromInput = document.getElementById('dateFrom').value;
    const dateToInput = document.getElementById('dateTo').value;
    
    if (!dateFromInput || !dateToInput) {
        return;
    }
    
    filteredAppointments = allAppointments.filter(app => {
        return app.fecha >= dateFromInput && app.fecha <= dateToInput;
    });
    
    renderAppointments(filteredAppointments);
    updateStats(filteredAppointments);
}

function clearCustomRange() {
    document.getElementById('dateFrom').value = '';
    document.getElementById('dateTo').value = '';
    document.getElementById('dateRangeFilter').value = 'all';
    document.getElementById('customDateRange').style.display = 'none';
    
    filteredAppointments = [...allAppointments];
    renderAppointments(filteredAppointments);
    updateStats(filteredAppointments);
}

function updateStats(appointments) {
    const statsContainer = document.getElementById('statsContainer');
    
    if (appointments.length === 0) {
        statsContainer.style.display = 'none';
        return;
    }
    
    statsContainer.style.display = 'grid';
    
    // Total de citas
    document.getElementById('totalCitas').textContent = appointments.length;
    
    // Total de ingresos
    const totalIngresos = appointments.reduce((sum, app) => sum + parseFloat(app.precio || 0), 0);
    document.getElementById('totalIngresos').textContent = totalIngresos.toFixed(2) + ' €';
    
    // Precio promedio
    const promedio = appointments.length > 0 ? totalIngresos / appointments.length : 0;
    document.getElementById('promedioPrecio').textContent = promedio.toFixed(2) + ' €';
}

function groupAppointmentsByDate(appointments) {
    const grouped = {};
    
    appointments.forEach(appointment => {
        if (!grouped[appointment.fecha]) {
            grouped[appointment.fecha] = [];
        }
        grouped[appointment.fecha].push(appointment);
    });
    
    const sortedDates = Object.keys(grouped).sort().reverse();
    
    return sortedDates.map(date => ({
        fecha: date,
        appointments: grouped[date].sort((a, b) => b.hora.localeCompare(a.hora))
    }));
}

function renderAppointments(appointmentsToRender) {
    const container = document.getElementById('appointmentsContainer');
    const emptyState = document.getElementById('emptyState');

    if (appointmentsToRender.length === 0) {
        container.innerHTML = '';
        emptyState.style.display = 'block';
        return;
    }

    emptyState.style.display = 'none';
    
    // Agrupar por fechas
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
}

function renderAppointmentCard(appointment, isAdmin = false) {
    return `
        <div class="appointment-card appointment-card-history" data-id="${appointment.id}">
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
                <div class="price">${parseFloat(appointment.precio).toFixed(2)} €</div>
                <div class="status status-atendida-true">✅ Atendida</div>
            </div>
            
            ${isAdmin ? `
                <div class="appointment-actions">
                    <button class="btn btn-edit" onclick="editAppointment('${appointment.id}')">✏️ Modificar</button>
                    <button class="btn btn-delete" onclick="confirmDeleteAppointment('${appointment.id}')">🗑️ Eliminar</button>
                </div>
            ` : ''}
        </div>
    `;
}

function formatDateLabel(dateString) {
    const date = new Date(dateString + 'T00:00:00');
    const today = new Date();
    const yesterday = new Date(today);
    yesterday.setDate(yesterday.getDate() - 1);
    
    today.setHours(0, 0, 0, 0);
    yesterday.setHours(0, 0, 0, 0);
    date.setHours(0, 0, 0, 0);
    
    if (date.getTime() === today.getTime()) {
        return 'Hoy';
    } else if (date.getTime() === yesterday.getTime()) {
        return 'Ayer';
    } else {
        return date.toLocaleDateString('es-ES', { 
            weekday: 'long', 
            year: 'numeric', 
            month: 'long', 
            day: 'numeric' 
        });
    }
}

function formatDateISO(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

function formatHour(hourString) {
    const [hours, minutes] = hourString.split(':');
    return `${hours}:${minutes}`;
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

function getSumDuration(cortes) {
    let suma = 0;
    for(const corte of cortes) {
        suma += corte.duracion;
    }
    return suma;
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function escapeForAttribute(text) {
    return text.replace(/'/g, "\\'").replace(/"/g, '&quot;');
}

function showLoading(show) {
    document.getElementById('loadingState').style.display = show ? 'block' : 'none';
}

function searchAppointments() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    
    if (searchTerm === '') {
        renderAppointments(filteredAppointments);
        updateStats(filteredAppointments);
        return;
    }
    
    const searchResults = filteredAppointments.filter(appointment => {
        const clientName = getNombreCliente(appointment.cliente).toLowerCase();
        const clientPhone = getTfnoCliente(appointment.cliente).toLowerCase();
        const services = formatCuttingNames(appointment.cortes).toLowerCase();
        const fecha = formatDateLabel(appointment.fecha).toLowerCase();
        
        return clientName.includes(searchTerm) ||
               clientPhone.includes(searchTerm) ||
               services.includes(searchTerm) ||
               fecha.includes(searchTerm);
    });
    
    renderAppointments(searchResults);
    updateStats(searchResults);
}

function editAppointment(id) {
    window.location.href = `modificarCita.html?id=${id}`;
}

function confirmDeleteAppointment(id) {
    showConfirmDialog(
        '¿Estás seguro de que deseas eliminar esta cita del historial? Esta acción no se puede deshacer.',
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

        await loadHistorial();
        showSuccess('Cita eliminada exitosamente del historial');
        
    } catch (error) {
        console.error('Error eliminando cita:', error);
        showError('Error al eliminar la cita. Por favor, inténtalo de nuevo.');
    } finally {
        showLoading(false);
    }
}

// Inicializar
document.addEventListener('DOMContentLoaded', function() {
    loadHistorial();
});