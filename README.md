# SOGR-HACKATON

Evidencias de la hackatón individual (2026-2): soluciones en **Java**, entradas oficiales, salidas y capturas.

- **Participante:** Andres Camilo Vivas Baquero
- **Prueba Google:** código `GOOGLE-03`
- **Prueba Anthropic:** código `ANTHROPIC-03`

## Cómo se ejecuta cada ejercicio

Desde la carpeta del ejercicio (Java 11 o superior; `java Main.java` compila y ejecuta en un paso):

```bash
cmd /c "java Main.java < entrada_oficial.txt"
```

En PowerShell el operador `<` no existe, por eso se usa `cmd /c`.

## Prueba Google · `GOOGLE-03`

Ingreso con nombre y código: [evidencias/google/01-sesion-nombre-codigo.png](evidencias/google/01-sesion-nombre-codigo.png)

| Ej. | Problema | Tema | Solución | Entrada oficial | Salida correcta | Verificación |
|---|---|---|---|---|---|---|
| A | d1000000 | Ordenamiento y voraz | [Main.java](google/A-d1000000/Main.java) | [captura](evidencias/google/02-A-entrada-oficial.png) | [captura](evidencias/google/03-A-salida-correcta.png) | ✅ CORRECTA |
| B | Moons and Umbrellas | Programación dinámica | [Main.java](google/B-moons-and-umbrellas/Main.java) | [captura](evidencias/google/04-B-entrada-oficial.png) | [captura](evidencias/google/05-B-salida-correcta.png) | ✅ CORRECTA |
| C | Parenting Partnering Returns | Intervalos y voraz | [Main.java](google/C-parenting-partnering-returns/Main.java) | [captura](evidencias/google/06-C-entrada-oficial.png) | [captura](evidencias/google/07-C-salida-correcta.png) | ✅ CORRECTA |
| D | Chain Reactions | Árboles y DFS | [Main.java](google/D-chain-reactions/Main.java) | [captura](evidencias/google/08-D-entrada-oficial.png) | [captura](evidencias/google/09-D-salida-correcta.png) | ✅ CORRECTA |
| E | Bathroom Stalls | Eficiencia con números enormes | [Main.java](google/E-bathroom-stalls/Main.java) | [captura](evidencias/google/10-E-entrada-oficial.png) | [captura](evidencias/google/11-E-salida-correcta.png) | ✅ CORRECTA |

Cada carpeta contiene:

- `Main.java`: la solución.
- `ejemplo_entrada.txt`, `ejemplo_salida_esperada.txt` y `ejemplo_salida.txt`: el ejemplo de la página y lo que da la solución.
- `entrada_oficial.txt` y `salida_oficial.txt`: la entrada generada para el participante y la salida del programa.
- `verificacion.txt`: el resultado del verificador.

En C se acepta cualquier asignación válida. En el ejemplo, el caso 3 da `JCCJJ`, que es la misma asignación que `CJJCC` con C y J intercambiados.

## Prueba Anthropic · `ANTHROPIC-03`

Pendiente.

## Verificador

[herramientas/verificar.js](herramientas/verificar.js) usa el mismo motor de la página de la prueba, que debe estar en la carpeta que contiene este repositorio. Sirve para comprobar que una salida es la correcta:

```bash
node herramientas/verificar.js g1 "Andres Camilo Vivas Baquero" GOOGLE-03 google/A-d1000000/salida_oficial.txt
```

Los ids son `g1`…`g5` para Google A…E y `b1`…`b5` para Anthropic A…E.
