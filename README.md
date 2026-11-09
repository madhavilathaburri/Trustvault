# TrustVault — Secure Document Sharing

Java 21 + Spring Boot + MySQL application starter.

## Features
- Secure login and registration with BCrypt password hashing.
- Roles: ADMIN, MANAGER, EMPLOYEE. Public registration always creates EMPLOYEE.
- Document upload (10 MB maximum) and private local file storage.
- Admin can view all documents and grant access to registered users.
- Owners and explicitly authorised users can download documents.
- Backend authorization check on every download request.
- Audit records for uploads, allowed/denied downloads and grants.
- Audit page restricted to ADMIN.

## Setup
1. Install Java 21, Maven 3.9+ and MySQL 8.
2. Create database: `CREATE DATABASE trustvault_db;`
3. In `src/main/resources/application.properties`, replace `YOUR_EXISTING_MYSQL_PASSWORD` with your current MySQL password. Keep username `root`; do not change your MySQL password.
4. Open this folder in VS Code and run `mvn spring-boot:run`.
5. Visit `http://localhost:8080`, register an account, and log in.

## Promote your first account to Admin
Registration does not allow users to choose their own role. After registering your own account, run this SQL in MySQL:

```sql
UPDATE app_users SET role='ADMIN' WHERE email='your-email@example.com';
```

Sign out and sign in again. Admin can view `/audit` and grant access from the Documents page.

## Run tests
`mvn test`

## Access rules
- ADMIN: all documents, audit page, grant access.
- MANAGER and EMPLOYEE: documents they own or that have been explicitly shared with them.
- All download permissions are checked on the server, not just in the user interface.

## Before using real confidential files in production
Add HTTPS, cloud secret management, file-type validation and malware scanning, rate limiting,
account recovery, backups, and private cloud object storage. Do not commit passwords or uploaded files to GitHub.
