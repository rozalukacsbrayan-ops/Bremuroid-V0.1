# Bremuroid V0.1

Frontend Android de biblioteca multi-sistema, con interfaz oscura, acentos rojos e icono original de mando rojo.

## Importante sobre el estado de V0.1

Esta versión es una **base de frontend y biblioteca**, no un emulador completo todavía. Permite importar archivos y consultar el catálogo de sistemas, pero todavía no ejecuta juegos. Para jugar hay que integrar núcleos de emulación compatibles, uno por sistema, y conectar sus APIs de ejecución, controles y guardado.

### Sistemas en el catálogo

- Nintendo: NES, SNES, Game Boy, Game Boy Color, Game Boy Advance, Nintendo 64, Nintendo DS, GameCube y Wii.
- PlayStation: PS1, PS2 y PSP.
- Xbox: Xbox original y Xbox 360 (compatibilidad en Android muy limitada; no garantizada).
- SEGA: Master System, Mega Drive/Genesis, Saturn y Dreamcast.

La presencia de un sistema en el catálogo **no significa que ya sea compatible para jugar**.

## BIOS y firmware

**No se incluyen BIOS ni firmware en este ZIP.** Muchos archivos BIOS/firmware son propiedad intelectual de sus fabricantes y no se pueden redistribuir libremente. La app incluye una pantalla de gestión preparada para que el usuario importe sus propios archivos obtenidos legalmente. La app no descarga BIOS de Internet ni contiene archivos de terceros.

No subas BIOS, firmware ni ROM comerciales a GitHub. Si integras un núcleo, revisa sus licencias y sus requisitos exactos de BIOS.

## Requisitos y compilación

- Android Studio reciente
- JDK 17
- Android SDK Platform 35

1. Extrae el ZIP.
2. Abre la carpeta `Bremuroid-V0.1` en Android Studio.
3. Espera a que Gradle sincronice.
4. Ejecuta la configuración `app` en un móvil Android o emulador.

- Nombre: **Bremuroid**
- Versión: **0.1**
- Application ID: `com.bremuroid.app`
- SDK mínimo: Android 8.0 (API 26)

## GitHub

Crea un repositorio vacío llamado `Bremuroid`. Después, desde esta carpeta:

```bash
git init
git add .
git commit -m "Bremuroid V0.1 initial project"
git branch -M main
git remote add origin https://github.com/TU-USUARIO/Bremuroid.git
git push -u origin main
```

Sustituye `TU-USUARIO` por tu usuario de GitHub.

## Marca y recursos

Bremuroid es un proyecto independiente, no afiliado a Lemuroid. La interfaz toma como referencia la facilidad de uso de los frontends multi-sistema, pero utiliza recursos gráficos originales. El icono incluido es un mando rojo original.
