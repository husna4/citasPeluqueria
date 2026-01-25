// modificarCita.js
// Funcionalidad para modificar una cita existente

const uriApiCortes = 'http://localhost:8080/api/cortes';
const uriApiCitas = 'http://localhost:8080/api/citas';

let appointmentId = null;
let originalAppointment = null;

document.addEventListener('DOMContentLoaded', function() {
    // Obtener ID de la cita desde la URL
    const urlParams = new URLSearchParams(window.location.search);
    appointmentId = urlParams.get('id');
    
    if (!appointmentId) {
        showError('No se especificó el ID de la cita');
        setTimeout(() => goBack(), 2000);
        return;
    }
    
    initializeForm();
});

async function initializeForm() {
    try {
        showLoading(true);
        
        // Cargar servicios y cita en paralelo
        const [cortesData, citaData] = await Promise.all([
            loadCortes(),
            loadAppointment(appointmentId)
        ]);
        
        originalAppointment = citaData;
        
        // Renderizar servicios
        renderCortes(cortesData);
        
        // Prellenar formulario con datos de la cita
        fillFormWithAppointmentData(citaData);
        
        // Configurar listeners después de cargar todo
        setupEventListeners();
        
        showLoading(false);
        
    } catch (error) {
        console.error('Error inicializando formulario:', error);
        showLoading(false);
        showError('Error al cargar los datos de la cita');
    }
}

async function loadCortes() {
    const response = await fetch(uriApiCortes);
    if (!response.ok) {
        throw new Error('Error al cargar los cortes: ' + response.statusText);
    }
    return await response.json();
}

async function loadAppointment(id) {
    const response = await fetch(`${uriApiCitas}/${id}`);
    if (!response.ok) {
        throw new Error('Error al cargar la cita: ' + response.statusText);
    }
    return await response.json();
}

function renderCortes(cortes) {
    const servicesContainer = document.getElementById('services-container');
    servicesContainer.innerHTML = cortes.map(corte => `
        <div class="service-card">
            <div class="service-header">
                <input type="checkbox" id="${corte.nombre.toLowerCase()}" 
                       name="servicios" 
                       value="${corte.nombre.toLowerCase()}" 
                       data-precio="${corte.precio.toFixed(2)}" 
                       data-duracion="${corte.duracion}" 
                       data-id="${corte.id}">
                <label for="${corte.nombre.toLowerCase()}" class="service-label">
                    <span class="service-name">${corte.nombre}</span>
                </label>
            </div>
            <div class="service-details">
                <span class="service-price">${corte.precio.toFixed(2)} €</span>
                <span class="service-duration">~${corte.duracion} min</span>        
            </div>
        </div>
    `).join('');
}

function fillFormWithAppointmentData(cita) {
    // Llenar fecha y hora
    document.getElementById('fecha').value = cita.fecha;
    document.getElementById('hora').value = cita.hora.substring(0, 5); // HH:MM (quitar segundos)
    
    // Llenar datos del cliente
    if (cita.cliente) {
        document.getElementById('nombreCliente').value = cita.cliente.nombre || '';
        document.getElementById('telefonoCliente').value = cita.cliente.tfno || '';
    }
    
    // Seleccionar servicios
    if (cita.cortes && cita.cortes.length > 0) {
        cita.cortes.forEach(corte => {
            const checkbox = document.querySelector(`input[data-id="${corte.id}"]`);
            if (checkbox) {
                checkbox.checked = true;
                updateServiceCard(checkbox);
            }
        });
        updateServiceSummary();
    }
    
    // Llenar observaciones
    if (cita.observaciones) {
        const observacionesField = document.getElementById('observaciones');
        observacionesField.value = cita.observaciones;
        
        // Actualizar contador de caracteres
        const charCounter = document.querySelector('.char-counter');
        charCounter.textContent = `${cita.observaciones.length}/500 caracteres`;
    }
    
    // Validar formulario inicial
    validateForm();
}

function setupEventListeners() {
    // Campos obligatorios
    const requiredFields = ['fecha', 'hora', 'nombreCliente', 'telefonoCliente'];
    requiredFields.forEach(fieldId => {
        const field = document.getElementById(fieldId);
        if (field) {
            field.addEventListener('input', validateForm);
            field.addEventListener('blur', validateField);
        }
    });
    
    // Checkboxes de servicios
    const serviceCheckboxes = document.querySelectorAll('input[name="servicios"]');
    serviceCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function() {
            updateServiceCard(this);
            updateServiceSummary();
            validateForm();
        });
    });
    
    // Validación en tiempo real del nombre
    const nombreField = document.getElementById('nombreCliente');
    nombreField.addEventListener('input', function() {
        this.value = this.value.replace(/[^A-Za-zÀ-ÿ\s]/g, '');
        if (this.value.length > 50) {
            this.value = this.value.substring(0, 50);
        }
    });
    
    // Validación del teléfono
    const telefonoField = document.getElementById('telefonoCliente');
    telefonoField.addEventListener('input', function() {
        this.value = this.value.replace(/[^0-9+\s\-]/g, '');
        if (this.value.length > 15) {
            this.value = this.value.substring(0, 15);
        }
    });
    
    // Contador de caracteres para observaciones
    const observacionesField = document.getElementById('observaciones');
    const charCounter = document.querySelector('.char-counter');
    
    observacionesField.addEventListener('input', function() {
        const length = this.value.length;
        charCounter.textContent = `${length}/500 caracteres`;
        
        if (length > 450) {
            charCounter.style.color = '#dc3545';
        } else if (length > 400) {
            charCounter.style.color = '#ffc107';
        } else {
            charCounter.style.color = '#6c757d';
        }
    });
}

function updateServiceCard(checkbox) {
    const serviceCard = checkbox.closest('.service-card');
    if (checkbox.checked) {
        serviceCard.classList.add('selected');
    } else {
        serviceCard.classList.remove('selected');
    }
}

function updateServiceSummary() {
    const selectedServices = document.querySelectorAll('input[name="servicios"]:checked');
    const summaryDiv = document.getElementById('serviceSummary');
    
    if (selectedServices.length === 0) {
        summaryDiv.style.display = 'none';
        return;
    }
    
    let totalPrice = 0;
    let totalDuration = 0;
    
    selectedServices.forEach(checkbox => {
        totalPrice += parseFloat(checkbox.dataset.precio);
        totalDuration += parseInt(checkbox.dataset.duracion);
    });
    
    document.getElementById('totalServices').textContent = selectedServices.length;
    document.getElementById('totalDuration').textContent = totalDuration + ' min';
    document.getElementById('totalPrice').textContent = totalPrice.toFixed(2) + ' €';
    
    summaryDiv.style.display = 'block';
}

function validateField(event) {
    const field = event.target;
    const value = field.value.trim();
    
    field.classList.remove('error');
    hideFieldError(field);
    
    if (field.id === 'fecha') {
        return validateFecha(field);
    }

    if (field.id === 'hora') {
        return validateHora(field);
    }

    if (field.id === 'nombreCliente' && value) {
        if (value.length > 50) {
            field.classList.add('error');
            showFieldError(field, 'El nombre no puede tener más de 50 caracteres');
            return false;
        }
        if (!/^[A-Za-zÀ-ÿ\s]+$/.test(value)) {
            field.classList.add('error');
            showFieldError(field, 'El nombre solo puede contener letras y espacios');
            return false;
        }
    }
    
    if (field.id === 'telefonoCliente' && value) {
        if (value.length < 9) {
            field.classList.add('error');
            showFieldError(field, 'El teléfono debe tener al menos 9 caracteres');
            return false;
        }
        if (!/^[0-9+\s\-]+$/.test(value)) {
            field.classList.add('error');
            showFieldError(field, 'El teléfono solo puede contener números, +, espacios y guiones');
            return false;
        }
    }
    
    if (field.id === 'observaciones' && value.length > 500) {
        field.classList.add('error');
        showFieldError(field, 'Las observaciones no pueden tener más de 500 caracteres');
        return false;
    }
    
    return true;
}

function validateFecha(field) {
    const value = field.value.trim();
    const selectedDate = new Date(value);
    const today = getTodayDate();

    if (isNaN(selectedDate.getTime())) {
        field.classList.add('error');
        showFieldError(field, 'Selecciona una fecha válida');
        return false;
    }

    if (selectedDate < today) {
        field.classList.add('error');
        showFieldError(field, 'No puedes seleccionar una fecha en el pasado');
        return false;
    }

    const fieldHora = document.getElementById('hora');
    fieldHora.classList.remove('error');
    hideFieldError(fieldHora);
    validateHora(fieldHora);

    return true;
}

function validateHora(field) {
    const value = field.value.trim();
    const now = new Date();
    const selectedDate = new Date(document.getElementById('fecha').value.trim());

    if (isNaN(selectedDate.getTime())) {
        field.classList.add('error');
        showFieldError(field, 'Selecciona una fecha válida primero');
        return false;
    }

    if (value < formatHour(now) && removeHour(selectedDate).getTime() === getTodayDate().getTime()) {
        field.classList.add('error');
        showFieldError(field, 'La hora no puede ser en el pasado');
        return false;
    }

    return true;
}

function getTodayDate() {
    const today = new Date();
    return removeHour(today);
}

function removeHour(date) {
    date.setHours(0, 0, 0, 0);
    return date;
}

function formatHour(date) {
    return date.toTimeString().slice(0, 5);
}

function showFieldError(field, message) {
    hideFieldError(field);
    
    const errorDiv = document.createElement('div');
    errorDiv.className = 'field-error';
    errorDiv.textContent = message;
    
    field.parentNode.appendChild(errorDiv);
}

function hideFieldError(field) {
    const existingError = field.parentNode.querySelector('.field-error');
    if (existingError) {
        existingError.remove();
    }
}

function validateForm() {
    const form = document.getElementById('appointmentForm');
    const fecha = document.getElementById('fecha').value;
    const hora = document.getElementById('hora').value;
    
    const saveButton = document.getElementById('saveButton');
    const isValid = fecha && hora;

    saveButton.disabled = !isValid || !form.checkValidity();
    saveButton.style.opacity = isValid && form.checkValidity() ? '1' : '0.5';
    
    return isValid;
}

async function updateAppointment() {
    if (!validateForm()) {
        showErrorAlert('Por favor, completa todos los campos obligatorios.');
        return;
    }
    
    const form = document.getElementById('appointmentForm');
    const formData = new FormData(form);
    
    // Validaciones adicionales
    const fecha = formData.get('fecha');
    const hora = formData.get('hora');
    const selectedDateTime = new Date(fecha + 'T' + hora);
    const now = new Date();
    
    if (selectedDateTime < now) {
        showErrorAlert('No puedes modificar una cita con fecha/hora en el pasado.');
        return;
    }
    
    const serviciosSeleccionados = document.querySelectorAll('input[name="servicios"]:checked');
    
    // Construir objeto de cita actualizada
    const updatedAppointment = {
        fecha: fecha,
        hora: hora + ':00',
        atendida: originalAppointment.atendida, // Mantener el estado original
        cliente: {
            nombre: formData.get('nombreCliente').trim(),
            tfno: formData.get('telefonoCliente')?.trim() || null
        },
        idsCorte: Array.from(serviciosSeleccionados).map(checkbox => checkbox.dataset.id),
        observaciones: formData.get('observaciones')?.trim() || null,
        precio: Array.from(serviciosSeleccionados).reduce((total, checkbox) => {
            return total + parseFloat(checkbox.dataset.precio);
        }, 0).toFixed(2)
    };

    console.log('Cita actualizada a enviar:', updatedAppointment);
    
    try {
        showLoading(true, 'Guardando cambios...');
        
        // Llamada a la API con PUT
        const response = await fetch(`${uriApiCitas}/${appointmentId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedAppointment)
        });
        
        if (!response.ok) {
            throw new Error('Error al actualizar la cita: ' + response.statusText);
        }
        
        const updatedData = await response.json();
        
        showLoading(false);
        showSuccess();
        
    } catch (error) {
        console.error('Error actualizando cita:', error);
        showLoading(false);
        showErrorAlert('Error al actualizar la cita. Por favor, inténtalo de nuevo.');
    }
}

function showLoading(show, message = 'Cargando datos...') {
    const overlay = document.getElementById('loadingOverlay');
    overlay.style.display = show ? 'flex' : 'none';
    
    if (show) {
        const loadingText = overlay.querySelector('p');
        loadingText.textContent = message;
    }
}

function showSuccess() {
    document.getElementById('successMessage').style.display = 'flex';
}

function showError(message) {
    const errorDiv = document.getElementById('errorMessage');
    const errorText = document.getElementById('errorText');
    errorText.textContent = message;
    errorDiv.style.display = 'flex';
}

function showErrorAlert(message) {
    alert(message); // Podrías reemplazar esto con un modal más elegante
}

function getBackUrl() {
    const referrer = document.referrer;
    
    if (referrer.includes('historial.html')) {
        return './historial.html';
    }
    
    return '../index.html';
}

// Modificar la función goBack() existente
function goBack() {
    const backUrl = getBackUrl();
    window.location.href = backUrl;
}