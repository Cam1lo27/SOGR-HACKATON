// Verifica una salida (o genera la entrada oficial) con el mismo motor de la página de la prueba.
// Busca la página HTML de la prueba en la carpeta que contiene el repositorio.
// Uso:
//   node verificar.js <id> "<nombre>" <codigo> salida_oficial.txt   -> dice si la salida es CORRECTA
//   node verificar.js <id> "<nombre>" <codigo>                      -> imprime la entrada oficial
// id: g1..g5 (Google A..E) o b1..b5 (Anthropic A..E)
const fs = require('fs');
const path = require('path');
const [id, nombre, codigo, salida] = process.argv.slice(2);
if (!/^[gb][1-5]$/.test(id || '') || !nombre || !codigo) {
  console.error('Uso: node verificar.js <g1..g5|b1..b5> "<nombre>" <codigo> [salida.txt]');
  process.exit(2);
}
const kind = id[0] === 'g' ? 'google' : 'anthropic';
const carpeta = path.resolve(__dirname, '..', '..');
const prefijo = kind === 'google' ? 'Prueba Google' : 'Prueba Anthropic';
const html = fs.readdirSync(carpeta).find(f => f.startsWith(prefijo) && f.endsWith('.html'));
if (!html) { console.error(`No encontré "${prefijo}...html" en ${carpeta}`); process.exit(2); }
const src = fs.readFileSync(path.join(carpeta, html), 'utf8');
const m = src.match(/<script>(window\.LIB=\(function\(\)\{[\s\S]*?\}\)\(\);)\s*<\/script>/);
if (!m) { console.error('No se encontró el motor de la prueba en la página'); process.exit(2); }
const window = {}; eval(m[1]); const LIB = window.LIB;
if (!salida) { process.stdout.write(LIB.inst(kind, id, nombre, codigo).input + '\n'); process.exit(0); }
const buf = fs.readFileSync(salida);
const txt = (buf[0] === 0xFF && buf[1] === 0xFE ? buf.toString('utf16le') : buf.toString('utf8')).replace(/^﻿/, '');
const errs = LIB.check(kind, id, txt, nombre, codigo).filter(e => e);
const letra = 'ABCDE'[+id[1] - 1];
if (errs.length) { console.log(`INCORRECTA: ${prefijo} ${letra}`); errs.forEach(e => console.log(' - ' + e)); process.exit(1); }
console.log(`CORRECTA: ${prefijo} · ejercicio ${letra} · ${nombre} · código ${codigo}`);
