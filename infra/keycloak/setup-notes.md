# Keycloak Setup Notes

Realm: `saas-platform`

Clients:
- `react-ui` for the browser app
- `spring-api` for backend token validation

Users:
- `admin-a` / `password`
- `teacher-a` / `password`
- `student-a` / `password`
- `parent-a` / `password`
- `teacher-b` / `password`

Required user attributes:
- `tenant_id`
- `tenant_code`
- `app_user_id`
- `role`

Token claims expected by the backend:
- `sub`
- `preferred_username`
- `email`
- `tenant_id`
- `tenant_code`
- `app_user_id`
- `role`

For the PoC, realm import is expected to be handled manually or by a later bootstrap script once the container wiring is in place.
