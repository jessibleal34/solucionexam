package com.medisalud.notificacion;

import java.util.ArrayList;
import java.util.List;

/** Concurrencia: procesa notificaciones en un hilo separado para no bloquear el hilo principal. */
public class HiloNotificaciones extends Thread {

    private final List<String> pendientes = new ArrayList<>();
    private volatile boolean activo = true;

    public HiloNotificaciones() {
        super("hilo-notificaciones");
    }

    public synchronized void encolar(String mensaje) {
        pendientes.add(mensaje);
    }

    public void detener() {
        activo = false;
    }

    @Override
    public void run() {
        while (activo) {
            procesarPendientes();
            try {
                Thread.sleep(200);
            } catch (InterruptedException excepcion) {
                Thread.currentThread().interrupt();
                activo = false;
            }
        }
        procesarPendientes();
    }

    private synchronized void procesarPendientes() {
        for (String mensaje : pendientes) {
            System.out.println("[notificacion] " + mensaje);
        }
        pendientes.clear();
    }
}
