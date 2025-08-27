package com.saki.citasPeluqueria.interfaces;

import com.saki.citasPeluqueria.modelo.Corte;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

/**
 * @author husnain
 */
public interface ICita {

    public LocalDate getFecha();

    public void setFecha(LocalDate fecha);

    public LocalTime getHora();

    public void setHora(LocalTime hora);

    public Set<Corte> getCortes();

    public void setCortes(Set<Corte> cortes);
}
