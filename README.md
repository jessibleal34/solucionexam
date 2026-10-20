# Solución de referencia — Sistema de Gestión de Citas Médicas MediSalud

Material docente. Código completo y ya ensamblado del proyecto final del Curso de Java
(ver la guía del estudiante en [`../guia/guia-proyecto-final.md`](../guia/guia-proyecto-final.md)
y la especificación en [`../../../specs/022-proyecto-final/spec.md`](../../../specs/022-proyecto-final/spec.md)).

## Un solo proyecto, un solo punto de entrada

Toda la aplicación es **un único código**: `com.medisalud.Principal` es la única clase
`main`. `Principal` delega en `VistaConsola.mostrarMenu()`, que ofrece un **menú
interactivo real** (con `java.util.Scanner`, la única clase del proyecto que lee la
consola) en un bucle hasta que el usuario elige "Salir".

También hay una cuarta excepción propia, `TransicionInvalidaException`, además de las
tres (`PacienteNoEncontradoException`/`MedicoNoEncontradoException`/
`CitaNoEncontradaException`) mencionadas como ejemplo en `spec.md` FR-012: la regla de
validación V5 de `data-model.md` exige que una transición de `EstadoCita` fuera de las
permitidas se rechace "lanzando una excepción propia", así que `ServicioCitas` la usa en
vez de una excepción genérica de Java.

## Alcance del proyecto

- Aplicación de consola (sin interfaz gráfica) para la gestión de citas médicas de
  MediSalud: pacientes, médicos, citas, historias clínicas y facturación.
- Persistencia final en MySQL vía JDBC (los paquetes `persistencia.archivo` y las
  implementaciones en memoria de `repository` siguen presentes como el código que el
  proyecto tenía antes de migrar a base de datos — ver Pasos 8-17 de la guía — pero el
  punto de entrada final solo usa los DAOs de `persistencia.jdbc`).
- Sin frameworks externos al temario del curso (sin Spring, sin un ORM): JDBC puro.
- Un único dominio de negocio (MediSalud); sin Biblioteca Universitaria en este
  proyecto.

## Funcionalidades (menú de `VistaConsola`)

1. **Registrar paciente**: nombre, código y edad; opcionalmente antecedentes y alergias
   (construidos con el patrón Builder, `ConstructorHistoriaClinica`).
2. **Registrar médico**: nombre, código y especialidad.
3. **Agendar cita**: código de cita, código de paciente, código de médico y fecha;
   permite confirmarla y registrar su atención (con diagnóstico) en el mismo flujo.
4. **Consultar historia clínica**: por código de paciente: antecedentes, alergias y
   consultas registradas.
5. **Facturar consulta**: por código de cita, elige estrategia de costo (consulta
   general o de especialista) y permite aplicar descuento de afiliado y/o recargo
   nocturno (patrón Decorator).
6. **Generar reporte**: citas de un médico, total facturado por médico (Stream API) y
   pacientes a partir de una edad mínima (lambda + Stream API).
7. **Salir**: detiene de forma ordenada el hilo de notificaciones y cierra el programa.

## Reglas de negocio

- La edad de un paciente no puede ser negativa; el nombre completo y la especialidad no
  pueden estar vacíos (`IllegalArgumentException`, validado en el `set` de cada clase).
- Una `Cita` nace en estado `PENDIENTE` y solo puede seguir las transiciones de la tabla
  de `EstadoCita` (`PENDIENTE→CONFIRMADA/CANCELADA`, `CONFIRMADA→ATENDIDA/CANCELADA`);
  cualquier otra transición se rechaza con `TransicionInvalidaException`.
- Buscar un paciente, médico o cita por un código que no existe lanza su excepción
  propia (`PacienteNoEncontradoException`/`MedicoNoEncontradoException`/
  `CitaNoEncontradaException`) con un mensaje claro, sin detener el programa.
- El código de cada paciente/médico/cita lo asigna quien usa el menú (no hay un
  generador automático en la versión final interactiva; `ContadorCodigos` —
  `AtomicLong` — se usa en los Pasos 16-17 de la guía, antes de que el menú
  interactivo pida el código directamente al usuario).
- El acceso al repositorio en memoria (vigente en los Pasos 8-17) está sincronizado
  (`synchronized`) para el caso de hilos concurrentes; las notificaciones de cambio de
  estado de una cita se procesan en un hilo separado (`HiloNotificaciones`).
- El total facturado por médico y el filtro de pacientes por edad se calculan con
  `Stream`/`Predicate` (Módulos 19-20), nunca con `Scanner` ni con conceptos fuera del
  temario del curso, salvo la única excepción documentada de `VistaConsola`.

## Cómo ejecutar

Requiere Docker (para MySQL) y el conector `mysql-connector-j` en el *classpath* (el mismo
requisito que ya exige el Módulo 18 del curso). La guía del estudiante explica cada paso
en las secciones «Base de datos MySQL con Docker Compose» y «Compilar y ejecutar el
proyecto».

- `docker-compose.yml`: servidor MySQL 8.0 (`root`/`root`, puerto `3306`, los mismos datos
  que usa `ConexionBD`), con los datos en el volumen `medisalud-datos`.
- `sql/medisalud.sql`: crea la base `medisalud`, las tablas `pacientes`, `medicos` y
  `citas` (con claves foráneas `CONSTRAINT … FOREIGN KEY`) y carga datos de ejemplo (10 pacientes, 10 médicos, 30 citas). Se ejecuta solo
  en el primer arranque del contenedor, porque la carpeta `sql` se monta en
  `/docker-entrypoint-initdb.d`.

```bash
cd curso-java/proyecto-final/solucion
docker compose up -d                       # esperar a "running (healthy)" en: docker compose ps
docker compose exec mysql mysql -uroot -proot -e "SHOW TABLES FROM medisalud;"

# Solo si las tablas no aparecieron (o para volver a cargar el script):
docker compose exec -T mysql mysql -uroot -proot < sql/medisalud.sql

javac -d out $(find src/main/java -name "*.java")
java -cp "out:lib/mysql-connector-j-8.3.0.jar" com.medisalud.Principal
```

Se muestra el menú y queda esperando una opción por teclado (`1` a `7`) en un bucle real
hasta elegir "Salir". Los datos quedan en las tablas `pacientes`/`medicos`/`citas` y se
conservan entre ejecuciones (y aunque se borre el contenedor con `docker compose down`;
`docker compose down -v` borra también los datos).

## Verificación mapeada a los Success Criteria de `spec.md`

| Success Criteria | Cómo se verificó |
|---|---|
| **SC-001** (18-22 pasos) | La guía tiene 20 pasos numerados, uno por módulo (`grep -c '^### Paso '`). |
| **SC-002** (la guía reproduce exactamente el código de `solucion/`) | Comparación automática: para cada archivo `.java`, la última versión mostrada en la guía es idéntica al archivo final de `solucion/` (quickstart.md §3). |
| **SC-003** (el menú ejecuta sin errores) | Ejecución interactiva de `Principal`/`VistaConsola` con entrada simulada por `stdin` (`echo "1\n...\n7\n" \| java ...`): las 7 operaciones de FR-008 se ejecutan sin lanzar ninguna excepción no controlada, incluida una opción de menú inválida. |
| **SC-004** (persistencia entre ejecuciones) | Ejecutar `Principal` dos veces contra las mismas tablas MySQL: la segunda ejecución consulta (opción 4/6) los datos registrados en la primera. |
| **SC-005** (Edge Cases) | Primer arranque sin tablas con filas: las consultas devuelven listas vacías en vez de fallar; búsquedas por código inexistente lanzan `PacienteNoEncontradoException`/`MedicoNoEncontradoException`/`CitaNoEncontradaException`; transiciones de estado inválidas lanzan `TransicionInvalidaException`; el repositorio en memoria (vigente en los Pasos 8-17) sincroniza su acceso con `synchronized` para el caso de hilos concurrentes. |
| **SC-006** (16 resultados esperados, evidencia de los 20 módulos) | Cada paquete de la solución corresponde a un módulo o grupo de módulos específico (ver tabla de paquetes en `research.md` D3); la guía dedica un paso a cada uno. |
| **SC-007** (lambdas/Stream con el mismo resultado) | `ServicioPacientes.listarMayoresDeEdad` (`Predicate`/`Stream.filter`) y `ServicioFacturacion.totalFacturadoPorMedico` (`Stream`/`collect`/`groupingBy`) reemplazan los recorridos manuales de los Pasos 8-9; la salida impresa (lista de pacientes, mapa de totales por médico) es la misma que la versión anterior con bucles, documentada paso a paso en la guía (Pasos 19-20). |

## Alcance verificado

- Compila el árbol completo sin el conector de MySQL en el *classpath*: **sin errores**
  (solo se usan tipos de `java.sql`, parte del JDK; el conector solo es necesario en
  tiempo de ejecución, para que `DriverManager` encuentre el controlador real).
- El menú interactivo se probó de punta a punta (las 7 opciones, incluida una opción
  inválida) conectado a tres repositorios en memoria equivalentes a los DAOs (mismas
  interfaces de `com.medisalud.repository`), confirmando que `VistaConsola` funciona
  correctamente — ver detalle en el historial de este proyecto.
- Ejecución real contra MySQL 8.0.39 en Docker (`docker compose up -d`, 2026-10-18): el
  script se ejecutó solo en el primer arranque; la carga manual (`docker compose exec -T …
  < sql/medisalud.sql` y `source /docker-entrypoint-initdb.d/medisalud.sql`) funciona y se
  puede repetir sin errores. El programa, compilado con JDK 25 y `mysql-connector-j-8.3.0`,
  registró un paciente, un médico y una cita, y los datos seguían en las tablas en la
  segunda ejecución.
- **Dos fallos encontrados en esa prueba (ya corregidos):**
  1. Confirmar o atender una cita desde la opción 3 no se guardaba en MySQL, porque con
     JDBC cada búsqueda crea un objeto `Cita` nuevo y `ServicioCitas.cambiarEstado` no
     volvía a guardarlo. Ahora llama a `repositorioCitas.guardar(cita)` y `VistaConsola`
     muestra el estado releído de la base (`ControladorMediSalud.buscarCita`).
  2. Registrar un paciente (o médico) con un código repetido detenía el programa, porque
     el DAO lanzaba una `RuntimeException` que la vista no captura. Ahora `PacienteDAO`/
     `MedicoDAO` convierten la clave duplicada (`SQLIntegrityConstraintViolationException`)
     en `IllegalArgumentException("Ya existe un … con el codigo …")`, que la vista ya
     muestra como mensaje.
- **Limitación conocida:** los diagnósticos de `atenderCita` se agregan a la historia
  clínica en memoria; no hay tabla que los guarde, así que no persisten entre ejecuciones.
- Si al ejecutar aparece `No suitable driver found for jdbc:mysql://...`, falta el
  conector en el *classpath*: crear `lib/` y copiar allí `mysql-connector-j-8.3.0.jar`.
