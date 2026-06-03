# ⚙️ CI Pipeline — GitHub Actions

Pipeline de integración continua que se ejecuta automáticamente en cada push o pull request a cualquier rama.

---

## 📋 Qué hace el pipeline

```
Push / PR a cualquier rama
        │
        ▼
1. 📥 Checkout del código
2. ☕ Setup Java 21
3. 📦 Cache de dependencias Maven
4. 🧪 mvn clean install  (compila + tests)
5. 🔍 Sonar Analysis     (calidad de código)
6. 📊 Publicar resultados de tests
```

---

## 🔧 Configuración necesaria en GitHub

Antes de que el pipeline funcione hay que añadir dos **Secrets** en el repositorio:

`GitHub → Settings → Secrets and variables → Actions → New repository secret`

| Secret | Valor | Descripción |
|---|---|---|
| `SONAR_HOST_URL` | `http://tu-sonar:9000` | URL de tu servidor SonarQube |
| `SONAR_TOKEN` | `sqa_xxxxxxxxxxxx` | Token generado en SonarQube |

### Generar el token en SonarQube:
```
SonarQube → My Account → Security → Generate Token
→ Tipo: Project Analysis Token
→ Proyecto: driver-service
```

---

## 📁 Archivo del workflow

```
.github/
  workflows/
    ci.yml    ← pipeline principal
```

---

## 🧪 Tests y cobertura

El pipeline ejecuta `mvn clean install` que incluye:
- **Compilación** de los 3 módulos (`domain`, `application`, `infrastructure`)
- **Tests unitarios** con JUnit 5 y Mockito
- **Cobertura** con JaCoCo → enviada a SonarQube

Los resultados de los tests se publican como **artifact** en cada ejecución del workflow y se conservan **7 días**.

---

## ✅ Resultado esperado

| Paso | Estado esperado |
|---|---|
| Build | ✅ `BUILD SUCCESS` |
| Tests | ✅ Todos los tests pasan |
| Sonar | ✅ Quality Gate passed |

---

## ⚠️ Notas importantes

- El pipeline usa **Java 21** (igual que el proyecto)
- Sonar necesita `fetch-depth: 0` en el checkout para analizar el historial de cambios
- Si SonarQube no está disponible, el paso de Sonar fallará pero el build ya habrá pasado

