//package com.saki.citasPeluqueria;
//
//import com.saki.citasPeluqueria.modelo.Cita;
//import com.saki.citasPeluqueria.modelo.Cliente;
//import com.saki.citasPeluqueria.modelo.Corte;
//import com.saki.citasPeluqueria.repositorio.CitaRepository;
//import com.saki.citasPeluqueria.repositorio.ClienteRepository;
//import com.saki.citasPeluqueria.repositorio.CorteRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.HashSet;
//import java.util.Set;
//
//
//@Component
//public class TestConnection implements CommandLineRunner {
//
//    @Autowired
//    private CitaRepository citaRepository;
//
//    @Autowired
//    private CorteRepository corteRepository;
//
//    @Autowired
//    private ClienteRepository clienteRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        citaRepository.deleteAll();
//        clienteRepository.deleteAll();
//
//        Cita cita = new Cita();
//        cita.setFecha(LocalDate.now());
//        cita.setHora(LocalTime.now());
//        cita.setCortes(crearCortes());
//        cita.setCliente(crearCLiente());
//        cita.setAtendida(true);
//        citaRepository.save(cita);
//
//    }
//
//    private Cliente crearCLiente() {
//        Cliente cliente = new Cliente();
//
//        cliente.setNombre("Husnain");
//
//        return cliente;
//
//    }
//
//    private Set<Corte> crearCortes(){
//        Set<Corte> cortes = new HashSet<>(corteRepository.findAll());
//
//        if(! cortes.isEmpty()) {
//            return cortes;
//        }
//
//        cortes = new HashSet<>();
//
//        Corte corte = new Corte();
//        corte.setNombre("Corte Pelo");
//        corte.setPrecio(new BigDecimal("10.00"));
//        corteRepository.save(corte);
//
//        cortes.add(corte);
//
//        corte = new Corte();
//        corte.setNombre("Barba");
//        corte.setPrecio(new BigDecimal("5.00"));
//        corteRepository.save(corte);
//
//        cortes.add(corte);
//
//        return cortes;
//    }
//}
