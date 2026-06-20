create extension if not exists pgcrypto;

create table if not exists tenants (
    id uuid primary key,
    code varchar(100) unique not null,
    name varchar(255) not null
);

create table if not exists user_profiles (
    id uuid primary key,
    keycloak_user_id varchar(255) unique not null,
    tenant_id uuid not null references tenants(id),
    username varchar(100) not null,
    email varchar(255),
    display_name varchar(255),
    role varchar(100) not null
);

create table if not exists school_classes (
    id uuid primary key,
    tenant_id uuid not null references tenants(id),
    code varchar(100) not null,
    name varchar(255) not null,
    section varchar(100),
    created_by uuid references user_profiles(id),
    unique (tenant_id, code)
);

create table if not exists students (
    id uuid primary key,
    tenant_id uuid not null references tenants(id),
    code varchar(100) not null,
    class_id uuid references school_classes(id),
    name varchar(255) not null,
    owner_user_id uuid references user_profiles(id),
    unique (tenant_id, code)
);

create table if not exists assignments (
    id uuid primary key,
    tenant_id uuid not null references tenants(id),
    code varchar(100) not null,
    class_id uuid not null references school_classes(id),
    title varchar(255) not null,
    description text,
    created_by uuid references user_profiles(id),
    unique (tenant_id, code)
);

create table if not exists reports (
    id uuid primary key,
    tenant_id uuid not null references tenants(id),
    code varchar(100) not null,
    student_id uuid not null references students(id),
    report_type varchar(100) not null,
    content text,
    unique (tenant_id, code)
);

create table if not exists audit_logs (
    id uuid primary key,
    tenant_id uuid,
    user_id uuid,
    action varchar(255) not null,
    resource_type varchar(100),
    resource_id varchar(255),
    allowed boolean,
    reason text,
    created_at timestamp not null
);

