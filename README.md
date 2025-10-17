# Aplicación IoT — Simulador de riego

Android app (Jetpack Compose) que simula un sensor de humedad de suelo y un actuador (bomba). Incluye un cliente MQTT simulado (`MockMqttClient`) para pruebas locales sin necesidad de broker.

---

## Tabla de contenido

- [Estado del repositorio](#estado-del-repositorio)
- [Archivos clave](#archivos-clave)
- [Cómo compilar y ejecutar (Windows)](#cómo-compilar-y-ejecutar-windows)
- [Notas sobre `db.json`](#notas-sobre-dbjson)
- [Problemas conocidos / advertencias](#problemas-conocidos--advertencias)
- [Comandos Git útiles](#comandos-git-útiles)
- [Resumen rápido](#resumen-rápido)

---

## Estado del repositorio

- Proyecto: `aplicacion_iot` (Android, Jetpack Compose)
- Objetivo: Simulador de sensor de humedad y actuador (bomba).
- Nota importante: En entornos Windows pueden aparecer bloqueos de archivos en `app/build` que impidan `clean` si algún proceso mantiene un handle abierto (IDE, language servers, herramientas). Si ves errores de "Unable to delete" o archivos bloqueados, cierra VSCode/Android Studio y vuelve a intentar o reinicia el equipo.

---

## Archivos clave

- `app/src/main/assets/db.json` — estado inicial de la bomba/sensor.
- `app/src/main/java/com/example/aplicacion_iot/MainActivity.kt` — lógica y UI principal.
- `app/build.gradle.kts` — configuración de Compose y JVM toolchain.

---

## Cómo compilar y ejecutar (Windows)

1. Abre PowerShell o cmd.exe en la raíz del proyecto:

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

4. Ejecutar unit tests (todos):

  ```powershell
  .\gradlew.bat testDebugUnitTest --console=plain
  ```

5. Ejecutar un test en particular (ejemplo):

  ```powershell
  .\gradlew.bat testDebugUnitTest --tests "com.example.aplicacion_iot.MockMqttClientTest" --console=plain
  ```

> Si recibes errores de bloqueo de archivos durante `clean`, cierra IDEs y procesos que usen Java o reinicia Windows.

---

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

---

## Problemas conocidos / advertencias

- Uso experimental de Material3: `@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)` — es seguro para desarrollo, pero puede cambiar.
- Algunos iconos (`Icons.Default.Send`) muestran deprecaciones menores; no impiden el build.
- Solución si la terminal remota del IDE falla al ejecutar gradle: ejecuta los comandos anteriores en tu terminal local (PowerShell o cmd).

---

## Comandos Git útiles

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

---

## Resumen rápido

- Proyecto: aplicacion_iot (Android, Jetpack Compose)
- Objetivo: Simulador de sensor de humedad y actuador (bomba), con lógica automática de control y un cliente MQTT "mock" para pruebas sin dependencias externas.

