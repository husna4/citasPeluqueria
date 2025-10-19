// nueva-cita.js
// Funcionalidad para el formulario de nueva cita

const uriApiCortes = 'http://localhost:8080/api/cortes';
const uriApiCitas = 'http://localhost:8080/api/citas';

document.addEventListener('DOMContentLoaded', function() {
    
    initializeForm(); 
    loadCortes().then(() => {
        setupEventListeners();  
    });
});

function initializeForm() {
    
    // Establecer fecha mínima como hoy
    const today = new Date().toISOString().split('T')[0];
    const fechaInput = document.getElementById('fecha');
    fechaInput.value = today;
    fechaInput.min = today; // Evitar fechas pasadas en HTML
    
    // Establecer hora actual + 30 minutos como predeterminada
    const now = new Date();
    now.setMinutes(now.getMinutes() + 30);
    document.getElementById('hora').value = formatHour(now);
    
    // Validar formulario inicial
    validateForm();
}

async function loadCortes() {
    try {
        const response = await fetch(uriApiCortes);
        if (!response.ok) {
            throw new Error('Error al cargar los cortes: ' + response.statusText);
        }
        const data = await response.json();
        renderCortes(data);
    } catch (error) {
        console.error('Error fetching cortes:', error);
    }
}

function renderCortes(cortes) {
    const servicesContainer = document.getElementById('services-container');
    servicesContainer.innerHTML = cortes.map(corte => `
        <div class="service-card">
            <div class="service-header">
                <input type="checkbox" id="${corte.nombre.toLowerCase()}" name="servicios" value="${corte.nombre.toLowerCase()}" data-precio="${corte.precio.toFixed(2)}" data-duracion="${corte.duracion}" data-id="${corte.id}">
                <label for="${corte.nombre.toLowerCase()}">${corte.nombre}</label>
            </div>
            <div class="service-details">
                <span class="service-price">${corte.precio.toFixed(2)} €</span>
                <span class="service-duration">~${corte.duracion} min</span>        
            </div>
        </div>
    `).join('');
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
        // Permitir solo letras, espacios y acentos
        this.value = this.value.replace(/[^A-Za-zÀ-ÿ\s]/g, '');
        // Limitar longitud visual
        if (this.value.length > 50) {
            this.value = this.value.substring(0, 50);
        }
    });
    
    // Validación del teléfono
    const telefonoField = document.getElementById('telefonoCliente');
    telefonoField.addEventListener('input', function() {
        // Permitir solo números, +, espacios y guiones
        this.value = this.value.replace(/[^0-9+\s\-]/g, '');
        // Limitar longitud
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
        
        // Cambiar color cuando se acerca al límite
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
    
    // Remover clases de error previas
    field.classList.remove('error');
    hideFieldError(field);
    
    // Validaciones específicas
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
        // Verificar que solo contenga letras y espacios
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

function validateFecha(field){
    const value = field.value.trim();
    const selectedDate = new Date(value);
    const today = getTodayDate();

    if(isNaN(selectedDate.getTime())) {
        field.classList.add('error');
        showFieldError(field, 'Selecciona una fecha válida');
        return false;
    }

    if (selectedDate < today) {
        field.classList.add('error');
        showFieldError(field, 'No puedes seleccionar una fecha en el pasado');
        return false;
    }

    fieldHora = document.getElementById('hora');
    fieldHora.classList.remove('error');
    hideFieldError(fieldHora);

    validateHora(fieldHora);

    return true;
}

function validateHora(field){
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

function removeHour(date){
    date.setHours(0,0,0,0);
    return date;
}

function formatHour(date) {
    return date.toTimeString().slice(0,5);
}

function showFieldError(field, message) {
    hideFieldError(field); // Limpiar error previo
    
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

async function saveAppointment() {
    if (!validateForm()) {
        showError('Por favor, completa todos los campos obligatorios.');
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
        showError('No puedes crear una cita en el pasado.');
        return;
    }
    
    const serviciosSeleccionados = document.querySelectorAll('input[name="servicios"]:checked');
    
    // Construir objeto de cita
    const newAppointment = {
        fecha: fecha,
        hora: hora + ':00',
        atendida: false,
        cliente: {
            nombre: formData.get('nombreCliente')?.trim() || null,
            tfno: formData.get('telefonoCliente')?.trim() || null
        },
        idsCorte: Array.from(serviciosSeleccionados).map(checkbox => checkbox.dataset.id),
        observaciones: formData.get('observaciones')?.trim() || null,
        precio: Array.from(serviciosSeleccionados).reduce((total, checkbox) => {
            return total + parseFloat(checkbox.dataset.precio);
        }, 0).toFixed(2)
    };

    console.log('Nueva cita a enviar:', newAppointment);
    
    try {
        showLoading(true);
        
        // Llamada a la API
        const response = await fetch(uriApiCitas, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newAppointment)
        });
        
        if (!response.ok) {
            throw new Error('Error al crear la cita: ' + response.statusText);
        }
        
        const createdAppointment = await response.json();
        
        showLoading(false);
        showSuccess();
        
    } catch (error) {
        console.error('Error creando cita:', error);
        showLoading(false);
        showError('Error al crear la cita. Por favor, inténtalo de nuevo.');

    }
}

function showLoading(show) {
    document.getElementById('loadingOverlay').style.display = show ? 'flex' : 'none';
}

function showSuccess() {
    document.getElementById('successMessage').style.display = 'flex';
}

function showError(message) {
    alert(message); // Podrías reemplazar esto con un modal de error más elegante
}

function goBack() {
    // Confirmar si hay cambios sin guardar
    const form = document.getElementById('appointmentForm');
    const formData = new FormData(form);
    const hasChanges = formData.get('nombreCliente') || 
                      formData.get('telefonoCliente') || 
                      formData.get('observaciones') ||
                      document.querySelectorAll('input[name="servicios"]:checked').length > 0;
    
    
    window.location.href = '../index.html';
}