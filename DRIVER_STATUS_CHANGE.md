# Cambio de Estado de Drivers

## Descripción
Esta funcionalidad permite cambiar el estado de un driver y publica un evento a Kafka cuando ocurre el cambio.

## Estados Disponibles
Los estados posibles de un driver están definidos en `DriverStatus`:
- `AVAILABLE`: Conectado y listo para recibir viajes
- `OFFLINE`: Desconectado
- `BUSY`: En viaje
- `BLOCKED`: Bloqueado por administrador

## Arquitectura

### Componentes Creados

#### 1. Capa de Dominio (`domain/`)
- **UpdateDriverStatusCommand**: Record que encapsula el comando de cambio de estado
- **DriverStatusChangedEvent**: Evento que se publica cuando cambia el estado
- **UpdateDriverStatusUseCase**: Puerto de entrada (interfaz) para el caso de uso
- **DriverProducer** (actualizado): Ahora incluye método para enviar eventos de cambio de estado

#### 2. Capa de Aplicación (`application/`)
- **UpdateDriverStatusService**: Servicio que implementa la lógica de negocio del cambio de estado

#### 3. Capa de Infraestructura (`infrastructure/`)
- **UpdateDriverStatusRequest**: DTO para la petición HTTP
- **DriverController** (actualizado): Ahora incluye endpoint PATCH para cambiar el estado
- **DriverProducerImpl** (actualizado): Implementa el envío del evento a Kafka
- **ApplicationConfig**: Configura los beans de los servicios

## Uso

### Endpoint REST

**PATCH** `/drivers/{driverId}/status`

**Request Body:**
```json
{
  "status": "AVAILABLE"
}
```

**Respuesta:**
- **200 OK**: Estado actualizado correctamente

### Ejemplos de Uso

#### Cambiar driver a AVAILABLE
```bash
curl -X PATCH http://localhost:8082/drivers/driver-123/status \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "status": "AVAILABLE"
  }'
```

#### Cambiar driver a BUSY
```bash
curl -X PATCH http://localhost:8082/drivers/driver-123/status \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "status": "BUSY"
  }'
```

#### Cambiar driver a OFFLINE
```bash
curl -X PATCH http://localhost:8082/drivers/driver-123/status \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "status": "OFFLINE"
  }'
```

#### Cambiar driver a BLOCKED
```bash
curl -X PATCH http://localhost:8082/drivers/driver-123/status \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "status": "BLOCKED"
  }'
```

## Evento Kafka

Cuando se cambia el estado de un driver, se publica un evento al topic `driver-status-changed-topic` con la siguiente estructura:

```json
{
  "driverId": "driver-123",
  "previousStatus": "OFFLINE",
  "newStatus": "AVAILABLE",
  "timestamp": 1719087600000
}
```

### Topics de Kafka
- **driver-location-topic**: Eventos de actualización de ubicación (existente)
- **driver-status-changed-topic**: Eventos de cambio de estado (nuevo)

## Flujo de Ejecución

1. El cliente envía una petición PATCH al endpoint `/drivers/{driverId}/status`
2. El `DriverController` recibe la petición y valida el payload
3. Se crea un `UpdateDriverStatusCommand` con el nuevo estado
4. El `UpdateDriverStatusService` procesa el comando
5. Se construye un `DriverStatusChangedEvent` con la información del cambio
6. El evento se publica al topic de Kafka `driver-status-changed-topic`
7. Otros servicios pueden consumir este evento para reaccionar al cambio de estado

## Mejoras Futuras

### Persistencia
Actualmente, el servicio no persiste el estado del driver en base de datos. Se recomienda:

1. Crear una entidad JPA `DriverEntity`
2. Crear un repositorio `DriverRepository`
3. Implementar un puerto `DriverRepository` en el dominio
4. Guardar el estado anterior antes de actualizarlo
5. Validar transiciones de estado válidas

### Validaciones de Negocio
Se pueden agregar validaciones como:
- No permitir cambiar a BUSY si no hay un viaje asignado
- No permitir cambiar a AVAILABLE si el driver está bloqueado
- Registrar el historial de cambios de estado

### Notificaciones
- Enviar notificaciones push al driver cuando cambia su estado
- Alertar a administradores cuando un driver es bloqueado

## Configuración

Asegúrate de que Kafka esté corriendo en `localhost:9092` o configura la URL en `application.yml`:

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
```
