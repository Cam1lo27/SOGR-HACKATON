# Evidencias · Prueba Google

- **Participante:** Andres Camilo Vivas Baquero
- **Código de la prueba:** `GOOGLE-03`
- **Lenguaje:** Java
- **Ejecución** (desde la carpeta de cada ejercicio): `cmd /c "java Main.java < entrada_oficial.txt"`

| Ej. | Problema | Tema | Resultado |
|---|---|---|---|
| A | d1000000 | Ordenamiento y voraz | ✅ CORRECTA |
| B | Moons and Umbrellas | Programación dinámica | ✅ CORRECTA |
| C | Parenting Partnering Returns | Intervalos y voraz | ✅ CORRECTA |
| D | Chain Reactions | Árboles y DFS | ✅ CORRECTA |
| E | Bathroom Stalls | Eficiencia con números enormes | ✅ CORRECTA |

## Ingreso con nombre y código

![Ingreso con nombre y código GOOGLE-03](evidencias/google/01-sesion-nombre-codigo.png)

## A · d1000000

Solución: [google/A-d1000000/Main.java](google/A-d1000000/Main.java). Se ordenan los dados y cada uno alarga la escalera si tiene más caras que el largo actual.

**Entrada oficial**

![Entrada oficial A](evidencias/google/02-A-entrada-oficial.png)

**Salida**

![Salida A](evidencias/google/03-A-salida-correcta.png)

## B · Moons and Umbrellas

Solución: [google/B-moons-and-umbrellas/Main.java](google/B-moons-and-umbrellas/Main.java). Programación dinámica con el costo mínimo si el mural termina en C o en J.

**Entrada oficial**

![Entrada oficial B](evidencias/google/04-B-entrada-oficial.png)

**Salida**

![Salida B](evidencias/google/05-B-salida-correcta.png)

## C · Parenting Partnering Returns

Solución: [google/C-parenting-partnering-returns/Main.java](google/C-parenting-partnering-returns/Main.java). Se ordenan las actividades por inicio y cada una va a quien esté libre; si nadie lo está, `IMPOSSIBLE`. Se acepta cualquier asignación válida.

**Entrada oficial**

![Entrada oficial C](evidencias/google/06-C-entrada-oficial.png)

**Salida**

![Salida C](evidencias/google/07-C-salida-correcta.png)

## D · Chain Reactions

Solución: [google/D-chain-reactions/Main.java](google/D-chain-reactions/Main.java). Se recorre el árbol de las hojas a la raíz: la cadena de menor valor sube y las demás se cobran. Usa enteros de 64 bits.

**Entrada oficial**

![Entrada oficial D](evidencias/google/08-D-entrada-oficial.png)

**Salida**

![Salida D](evidencias/google/09-D-salida-correcta.png)

## E · Bathroom Stalls

Solución: [google/E-bathroom-stalls/Main.java](google/E-bathroom-stalls/Main.java). Los tramos vacíos se agrupan por tamaño en lugar de simular cubículo por cubículo, así funciona con N hasta 10¹⁸.

**Entrada oficial**

![Entrada oficial E](evidencias/google/10-E-entrada-oficial.png)

**Salida**

![Salida E](evidencias/google/11-E-salida-correcta.png)
