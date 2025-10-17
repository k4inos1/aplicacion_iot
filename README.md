<!-- README generado/actualizado por el asistente -->
# Aplicación IoT — Simulador de riego

Breve: Android app (Jetpack Compose) que simula un sensor de humedad de suelo y un actuador (bomba). Incluye un cliente MQTT simulado (`MockMqttClient`) para pruebas locales sin necesidad de broker.

## Estado del repositorio (resumen rápido)

- Proyecto: `aplicacion_iot` (Android, Jetpack Compose)
- Objetivo: Simulador de sensor de humedad y actuador (bomba).
- Nota importante: En entornos Windows pueden aparecer bloqueos de archivos en `app/build` que impidan `clean` si algún proceso mantiene un handle abierto (IDE, language servers, herramientas). Si ves errores de "Unable to delete" o archivos bloqueados, cierra VSCode/Android Studio y vuelve a intentar o reinicia el equipo.

## Cambios recientes (por el asistente)

- Añadido `app/src/main/assets/db.json` con un estado inicial demostrativo.
- MainActivity se actualizó para leer `db.json` al iniciar y mostrar el estado en la UI.
- `app/build.gradle.kts` actualizado para habilitar Compose y usar toolchain Java 17.
- `REPORT_Control_Riego_IoT.md` añadido como informe técnico.

## Archivos clave

- `app/src/main/assets/db.json` — estado inicial de la bomba/sensor.
- `app/src/main/java/com/example/aplicacion_iot/MainActivity.kt` — lógica y UI principal.
- `app/build.gradle.kts` — configuración de Compose y JVM toolchain.

## Cómo compilar y ejecutar (Windows - PowerShell)

1. Abre PowerShell en la raíz del proyecto:

```powershell
cd C:\Users\Ricardo\AndroidStudioProjects\aplicacion_iot
```

2. (Opcional) Forzar Gradle a usar JDK 17 si tienes varios JDKs instalados:

```powershell
setx JAVA_HOME "C:\Program Files\Eclipse Adoptium\jdk-17.0.16.8-hotspot"
# Cierra y vuelve a abrir la terminal después de setx
```

3. Limpiar y compilar usando el wrapper (recomendado):

```powershell
.\gradlew.bat --no-daemon clean assembleDebug --console=plain
```

Si recibes errores de bloqueo de archivos durante `clean`, cierra IDEs y procesos que usen Java o reinicia Windows.

## Notas sobre `db.json`

La app lee `app/src/main/assets/db.json` al iniciarse. Edita este archivo para simular distintos estados (bomba encendida/apagada, valores de humedad). Si no existe, la aplicación usa valores por defecto.

Ejemplo de `db.json` usado en pruebas:

```json
{
  "pumpCommand": "on",
  "soil": {
    "id": "sensor-01",
    "humidity": 45.3
  }
}
```

## Git: commit local y push remoto

Realiza un commit local con los cambios:

```powershell
git add -A
git commit -m "Mejora: README, assets (db.json) y ajustes de Compose/toolchain"
```

Crear un repo remoto en GitHub (si usas `gh`):

```powershell
gh repo create tu-usuario/aplicacion_iot --public --source=. --remote=origin --push
```

Si no tienes `gh`, crea el repo en https://github.com/new y luego:

```powershell
git remote add origin https://github.com/tu-usuario/aplicacion_iot.git
git push -u origin main
```

## Estado actual y próximos pasos recomendados

- Estado del build: pendiente de limpieza completa en Windows (puede requerir cerrar procesos o reiniciar).
- Próximo paso: ejecutar `clean assembleDebug` y, si aparecen errores de KAPT/annotation processing, revisar versiones de Kotlin y de los procesadores (Hilt, etc.).

Si quieres que haga el commit ahora desde este entorno, lo puedo hacer localmente. Para crear y empujar el repo remoto necesitaré que ejecutes los comandos con tus credenciales o que me autorices a usar la CLI `gh` autenticada si está instalada.

----

Para más detalles técnicos y recomendaciones, revisa `REPORT_Control_Riego_IoT.md`.
Informe y guía rápida — Aplicación IoT (Simulador de riego)

Resumen rápido
- Proyecto: aplicacion_iot (Android, Jetpack Compose)
- Objetivo: Simulador de sensor de humedad y actuador (bomba), con lógica automática de control y un cliente MQTT "mock" para pruebas sin dependencias externas.

Cambios realizados (resumen técnico)
- Reemplazado cliente MQTT real por `MockMqttClient` para evitar dependencias/manifest durante desarrollo.
- Renombradas variables a nombres de componentes: `sensorHumidity`, `waterPumpOn`, `brokerConnected`, `autoSendEnabled`.
- Implementado control automático de bomba con:
  - Umbral ajustable (`pumpHumidityThreshold`).
  - Histéresis (`pumpHysteresis`) para evitar oscilaciones.
  - Seguridad: `pumpMaxRunMs` (tiempo máximo de funcionamiento de la bomba).
- Throttle de logs para auto-envío (evita recomposiciones/ANR) y límite de tamaño del historial.
- Reemplazo de `java.time` por `SimpleDateFormat` (compatibilidad min SDK 24).
- UI mejorada: controles claros para sensor, auto-envío, parámetros de bomba y historial.

Archivos modificados
- `app/src/main/java/com/example/aplicacion_iot/MainActivity.kt` — lógica y UI principal.

Problemas conocidos / advertencias
- Uso experimental de Material3: `@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)` — es seguro para desarrollo, pero puede cambiar.
- Algunos iconos (`Icons.Default.Send`) muestran deprecaciones menores; no impiden el build.

Instrucciones para compilar y probar (en Windows)
1) Abrir PowerShell o cmd.exe en la raíz del proyecto:

```powershell
cd C:\Users\Ricardo\AndroidStudioProjects\aplicacion_iot
```

1) (Opcional) Forzar Gradle a usar tu JDK 17 si tienes problemas:

```powershell
setx JAVA_HOME "C:\Program Files\Eclipse Adoptium\jdk-17.0.16.8-hotspot"
# cerrar y reabrir la terminal después de setx
```

1) Limpiar y compilar:

```powershell
.\gradlew.bat clean assembleDebug --console=plain
```

1) Ejecutar unit tests (todos):

```powershell
.\gradlew.bat testDebugUnitTest --console=plain
```

1) Ejecutar un test en particular (ejemplo):

```powershell
.\gradlew.bat testDebugUnitTest --tests "com.example.aplicacion_iot.MockMqttClientTest" --console=plain
```

Solución si la terminal remota del IDE falla cuando yo intento ejecutar gradle: ejecuta los comandos anteriores en tu terminal local (PowerShell o cmd) — yo no puedo abrir una consola real desde aquí.

Comandos Git listos para usar (hazlos en la raíz del repo)
- Crear branch, añadir cambios y push:

```bash
# crea una rama nueva
git checkout -b improve/ui-pump-logic
# agrega todos los cambios
git add -A
# commit con mensaje claro
git commit -m "Mejora: lógica de bomba automática, mock MQTT y mejoras UI"
# subir a remoto (asume origin ya configurado)
git push -u origin improve/ui-pump-logic
```

- Si no tienes remoto configurado, agrega el origin (reemplaza URL):

```bash
git remote add origin https://github.com/tu-usuario/aplicacion_iot.git
git push -u origin improve/ui-pump-logic
```

Recomendaciones finales y next steps
- Ejecuta los comandos de build y tests en tu máquina (PowerShell). Si algo falla, copia aquí la salida y lo corrijo.
- Si quieres que yo cree un branch y haga commit/Push desde aquí necesitaría acceso al remoto (no disponible). Te doy los comandos exactos para que lo hagas.
- Siguientes mejoras recomendadas:
  - Añadir tests unitarios para `MockMqttClient` y la lógica `evaluateAutoPumpControl`.
  - Extraer la lógica de control de bomba a una clase `PumpController` para facilitar pruebas.
  - Reemplazar Snackbar frecuentes por un pequeño Toast/Indicator para no saturar la UI en auto-send.

Plantilla de Informe (para entregar - editable)

1. Información General y Definición de la Problemática
- Nombre de la Aplicación (APK): aplicacion_iot
- Problemática Abordada: Control automático de riego por humedad de suelo entre dos dispositivos móviles (simulador), con actuador (bomba) y sensor de humedad. Se simula la comunicación vía MQTT para pruebas.
- Tecnología de Interconexión: MQTT (simulado en `MockMqttClient`) — en despliegue real usar un broker MQTT accesible por ambos móviles (ej. Mosquitto).
- Nombre: [Tu nombre]
- Fecha de Entrega: [Fecha]

1. Diseño y Estándares de la Aplicación
- Mockup de Interfaz: (incluir capturas de pantalla de `app/src/main/res` o exportar pantallas)
- Justificación de Diseño: Colores sobrios para facilitar lectura; tarjetas para separar sensor/actuador; controles directos para pruebas; historial limitado para evitar sobrecarga.

1. Implementación de Funcionalidades Clave y Código
- Conectividad Inalámbrica: En desarrollo se usó `MockMqttClient` para simular un cliente MQTT. Para conexión real se recomienda `org.eclipse.paho.android.service` o `paho.mqtt.java` con un broker (Mosquitto).
- Código clave de interconexión: ver `MockMqttClient.publish(...)` y `setOnMessageListener` en `MainActivity.kt`.
- Lógica de negocio: `evaluateAutoPumpControl(currentHumidity: Float)` decide encender/apagar la bomba usando umbral y histéresis.

1. Medidas de Seguridad y Estándares
- Validación de mensajes recibidos: el mock acepta solo payloads controlados; en producción validar mensaje y autenticidad.
- Recomendación: usar broker autenticado (TLS + usuario/clave) y permisos runtime para red.

1. Evidencias de Funcionamiento y Prototipo
- Incluye capturas de pantalla del dispositivo/emulador con la app en ejecución (Dashboard con lectura y estado de bomba).

---

Si quieres, ahora:
- Puedo crear un branch y preparar commits locales (no puedo hacer push remoto sin tus credenciales), o
- Te doy un ZIP del repo modificado (si lo deseas) o
- Te explico paso a paso cómo ejecutar los builds y resolver cualquier error que aparezca.

¿Qué prefieres que haga ahora?"
