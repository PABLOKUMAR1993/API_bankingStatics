# Banking Statistics API - Configuración AWS

## 📋 Cambios realizados

### 1. Archivos de configuración por entorno

**Estructura creada:**
```
src/main/resources/
├── application.yml              # Configuración común
├── application-local.yml        # Desarrollo local
└── application-prod.yml         # Producción AWS
```

**Ya NO necesitas** `application.properties` (puedes eliminarlo)

### 2. Dependencias añadidas al pom.xml

- ✅ Spring Cloud Function (para Lambda)
- ✅ AWS Lambda Java Core
- ✅ AWS Lambda Java Events
- ✅ Maven Shade Plugin (empaquetado para Lambda)

### 3. Handler para Lambda

Archivo creado: `StreamLambdaHandler.java` en el paquete raíz

---

## 🚀 Cómo usar los perfiles

### Desarrollo local (como siempre)

**Opción A: En IntelliJ IDEA**
1. Edit Configurations → Spring Boot
2. Active profiles: `local`

**Opción B: Línea de comandos**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

**Opción C: Variable de entorno**
```bash
export SPRING_PROFILES_ACTIVE=local
mvn spring-boot:run
```

### Producción AWS

Lambda automáticamente usará el perfil `prod` mediante variable de entorno.

---

## 📦 Empaquetar para AWS Lambda

### Build del proyecto

```bash
# Limpiar y compilar
mvn clean package

# Esto genera 2 JARs:
# 1. API_bankingStatistics-1.0-SNAPSHOT.jar (normal)
# 2. API_bankingStatistics-1.0-SNAPSHOT-aws.jar (para Lambda) ⭐
```

El JAR con sufijo `-aws` es el que subirás a Lambda.

---

## 🔐 Variables de entorno necesarias en AWS Lambda

Cuando crees la función Lambda, debes configurar estas variables:

### Variables REQUERIDAS:

```bash
SPRING_PROFILES_ACTIVE=prod

# Base de datos RDS
DB_URL=jdbc:mysql://TU-RDS-ENDPOINT:3306/movimientos_db
DB_USERNAME=admin
DB_PASSWORD=tu-password-rds

# JWT (genera uno nuevo para producción)
JWT_SECRET=GENERA-UN-SECRET-SEGURO-DE-64-CARACTERES-O-MAS
```

### Generar JWT_SECRET seguro

```bash
# En Linux/Mac
openssl rand -base64 64

# En Windows PowerShell
[Convert]::ToBase64String((1..64 | ForEach-Object { Get-Random -Maximum 256 }))
```

---

## 🔧 Configuración de Lambda en AWS

### Configuración básica:

- **Runtime**: Java 21
- **Handler**: `com.banking.statistics.StreamLambdaHandler::handleRequest`
- **Memory**: 1024 MB (mínimo recomendado para Spring Boot)
- **Timeout**: 30 segundos
- **Environment variables**: Las mencionadas arriba

### VPC Configuration (para conectar con RDS):

- **VPC**: La misma VPC donde está tu RDS
- **Subnets**: Selecciona al menos 2 subnets privadas
- **Security Group**: Crea uno nuevo que permita tráfico hacia RDS

---

## 🌐 Configuración de API Gateway

API Gateway se conectará a tu Lambda y expondrá los endpoints REST.

### Configuración:

1. **Tipo**: REST API (no HTTP API)
2. **Integración**: Lambda Function (Proxy integration)
3. **Path**: `/{proxy+}` (captura todas las rutas)
4. **Method**: ANY (permite GET, POST, PUT, DELETE, etc.)

### CORS:

Si tu frontend está en otro dominio (S3/CloudFront), necesitas configurar CORS:

```json
{
  "Access-Control-Allow-Origin": "*",
  "Access-Control-Allow-Headers": "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token",
  "Access-Control-Allow-Methods": "GET,POST,PUT,DELETE,OPTIONS"
}
```

---

## 📊 Endpoints de tu API

Una vez desplegado, tus endpoints serán:

```
https://tu-api-id.execute-api.eu-west-1.amazonaws.com/prod/api/auth/login
https://tu-api-id.execute-api.eu-west-1.amazonaws.com/prod/api/auth/register
https://tu-api-id.execute-api.eu-west-1.amazonaws.com/prod/api/transactions
https://tu-api-id.execute-api.eu-west-1.amazonaws.com/prod/api/categories
...
```

Esta URL es la que pondrás en el `environment.ts` de tu frontend Angular.

---

## ⚠️ IMPORTANTE: Diferencias RDS vs Local

### Connection string:

**Local:**
```
jdbc:mysql://localhost:3306/banking_statistics
```

**RDS (producción):**
```
jdbc:mysql://movimientos-bancarios-db.xxxxx.eu-west-1.rds.amazonaws.com:3306/movimientos_db
```

### Nombre de base de datos:

- Local: `banking_statistics`
- RDS: `movimientos_db`

**¿Tienes datos en local que quieres migrar a RDS?**

Si sí, puedes hacer un dump y restaurarlo:

```bash
# Exportar de local
mysqldump -u root -p banking_statistics > backup.sql

# Importar a RDS
mysql -h movimientos-bancarios-db.xxxxx.eu-west-1.rds.amazonaws.com \
      -P 3306 \
      -u admin \
      -p movimientos_db < backup.sql
```

---

## 🧪 Probar localmente antes de desplegar

### Con perfil local:

```bash
# Asegúrate de tener MySQL local corriendo
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### Con perfil prod (simulando AWS):

```bash
# Configura las variables de entorno
export SPRING_PROFILES_ACTIVE=prod
export DB_URL=jdbc:mysql://TU-RDS-ENDPOINT:3306/movimientos_db
export DB_USERNAME=admin
export DB_PASSWORD=tu-password
export JWT_SECRET=tu-secret-seguro

# Ejecuta
mvn spring-boot:run
```

Esto te permite probar la conexión a RDS antes de desplegar a Lambda.

---

## 📝 Checklist antes de desplegar

- [ ] RDS MySQL creado y accesible
- [ ] Schema ejecutado en RDS (tablas creadas)
- [ ] Security Group de RDS permite conexiones desde Lambda
- [ ] Variables de entorno configuradas correctamente
- [ ] JWT_SECRET generado y guardado de forma segura
- [ ] JAR con sufijo `-aws` generado correctamente
- [ ] Probado localmente con perfil `prod` conectando a RDS

---

## 🐛 Troubleshooting

### Error: "Communications link failure"

- Verifica que el Security Group de RDS permita tráfico desde Lambda
- Verifica que Lambda esté en la misma VPC que RDS
- Verifica que el endpoint de RDS sea correcto

### Error: "Access denied for user"

- Verifica usuario y contraseña en variables de entorno
- Verifica que el usuario tenga permisos en la base de datos

### Cold start muy lento (>10 segundos)

- Aumenta la memoria de Lambda a 1536 MB o 2048 MB
- Considera usar Provisioned Concurrency (cuesta dinero pero elimina cold start)

### Error 502 Bad Gateway

- Revisa los logs de Lambda en CloudWatch
- Probablemente un error de configuración o timeout

---

## 📚 Próximos pasos

1. ✅ Configuración local lista
2. ⏳ Crear función Lambda
3. ⏳ Configurar API Gateway
4. ⏳ Desplegar y probar
5. ⏳ Configurar frontend con URL de API Gateway
6. ⏳ CI/CD con GitHub Actions

---

¿Dudas? Consulta la documentación oficial:
- [Spring Cloud Function](https://spring.io/projects/spring-cloud-function)
- [AWS Lambda Java](https://docs.aws.amazon.com/lambda/latest/dg/lambda-java.html)