# 🔐 Keycloak Setup — driver-service

Guía de configuración de Keycloak para el proyecto `driver-service`.

> **Keycloak:** `http://localhost:8081`  
> **Consola de administración:** `http://localhost:8081` → usuario `admin`

---

## 1. Crear el Realm `mini-uber`

1. Accede a la consola de administración
2. Click en el selector de realm (arriba a la izquierda) → **`Create realm`**
3. Rellena los datos:

| Campo | Valor |
|---|---|
| Realm name | `mini-uber` |
| Enabled | `ON` |

4. Click **`Create`**

---

## 2. Crear los Realm Roles

Dentro del realm `mini-uber`:

**`Realm roles` → `Create role`**

| Rol | Descripción |
|---|---|
| `read` | Permite leer/consultar recursos |
| `write` | Permite escribir/actualizar recursos (actualizar ubicación del conductor) |

> El endpoint `POST /drivers/{driverId}/location` requiere el rol **`write`**.

---

## 3. Crear el Client `driver-client`

**`Clients` → `Create client`**

### General Settings
| Campo | Valor |
|---|---|
| Client ID | `driver-client` |
| Client type | `OpenID Connect` |

### Capability Config
| Opción | Valor |
|---|---|
| Client authentication | ✅ `ON` |
| Authorization | ❌ `OFF` |
| Standard flow | ❌ `OFF` |
| Direct access grants | ✅ `ON` |
| Implicit flow | ❌ `OFF` |
| Service accounts roles | ❌ `OFF` |
| OAuth 2.0 Device Authorization Grant | ❌ `OFF` |
| OIDC CIBA Grant | ❌ `OFF` |

> Las URLs (redirect URIs, web origins, etc.) se pueden dejar vacías ya que se usa `Direct access grants` sin redirecciones.

### Obtener el Client Secret

Una vez creado el client:  
`Clients → driver-client → Credentials → Client secret → Copiar`

---

## 4. Crear el usuario `driver1`

**`Users` → `Add user`**

| Campo | Valor |
|---|---|
| Username | `driver1` |
| Email | `antonio.garcia1989@gmail.com` |
| Email verified | `ON` |
| First name | `Antonio` |
| Last name | `Martin` |

### Establecer contraseña

`Users → driver1 → Credentials → Set password`

| Campo | Valor |
|---|---|
| Password | `******` |
| Temporary | ❌ `OFF` ← importante, si está ON el login fallará |

### Asignar roles al usuario

`Users → driver1 → Role mapping → Assign role`

| Rol asignado |
|---|
| `read` |
| `write` |

---

## 5. Obtener un token JWT (Postman / curl)

```bash
curl --location 'http://localhost:8081/realms/mini-uber/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'client_id=driver-client' \
--data-urlencode 'client_secret=<TU_CLIENT_SECRET>' \
--data-urlencode 'username=driver1' \
--data-urlencode 'password=<TU_PASSWORD>'
```

**Respuesta:**
```json
{
  "access_token": "eyJhbGci...",
  "token_type": "Bearer",
  "expires_in": 300
}
```

El payload del JWT contiene los roles del usuario:
```json
{
  "realm_access": {
    "roles": ["read", "write", "offline_access", "uma_authorization", "default-roles-mini-uber"]
  }
}
```

> Puedes decodificar el token en **[https://jwt.io](https://jwt.io)** para verificar los roles.

---

## 6. Usar el token en la API

```bash
curl --location 'http://localhost:8082/drivers/abc123/location' \
--header 'Authorization: Bearer <access_token>' \
--header 'Content-Type: application/json' \
--data '{
  "lat": 40.4168,
  "lng": -3.7038
}'
```

---

## 7. Resumen del flujo de seguridad

```
[Postman]
    │
    ▼  POST /realms/mini-uber/protocol/openid-connect/token
[Keycloak :8081]
    │
    ▼  JWT con roles [read, write]
[Postman]
    │
    ▼  POST /drivers/{driverId}/location  +  Authorization: Bearer <JWT>
[driver-service :8082]
    │
    ├── Valida JWT contra Keycloak (issuer-uri)
    ├── Extrae roles de realm_access.roles
    ├── Comprueba rol "write" → ✅ 200 OK
    │
    ▼
[Kafka] → topic: driver-location-topic
```

---

## 8. Reglas de seguridad configuradas en Spring Boot

| Endpoint | Método | Rol requerido |
|---|---|---|
| `/swagger-ui/**` | ANY | Público |
| `/v3/api-docs/**` | ANY | Público |
| `/drivers/*/location` | `POST` | `write` |
| Cualquier otro | ANY | Autenticado |

> Configuración en: `infrastructure/src/main/java/com/driver/config/SecurityConfig.java`

