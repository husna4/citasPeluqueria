package com.saki.citasPeluqueria.modelo;


import com.saki.citasPeluqueria.interfaces.ICliente;
import jakarta.persistence.Entity;

/**
 * @author husnain
 */
@Entity
public class Cliente extends Persona implements ICliente {
}
