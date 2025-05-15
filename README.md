# Veterinary Registry Application

## Database Configuration

The application connects to a PostgreSQL database. By default, it uses the following connection parameters:
- URL: `jdbc:postgresql://localhost:5432/vet_registry_2`
- Username: `postgres`
- Password: `root`

### Environment Variables

You can override these default database connection parameters by setting the following environment variables:

- `VET_REGISTRY_DB_URL` - The JDBC URL for the database
- `VET_REGISTRY_DB_USER` - The database username
- `VET_REGISTRY_DB_PASSWORD` - The database password

Example (Windows):
```
set VET_REGISTRY_DB_URL=jdbc:postgresql://myserver:5432/my_database
set VET_REGISTRY_DB_USER=myuser
set VET_REGISTRY_DB_PASSWORD=mypassword
```

Example (Linux/macOS):
```
export VET_REGISTRY_DB_URL=jdbc:postgresql://myserver:5432/my_database
export VET_REGISTRY_DB_USER=myuser
export VET_REGISTRY_DB_PASSWORD=mypassword
```

If any of these environment variables are not set, the application will fall back to using the default values.