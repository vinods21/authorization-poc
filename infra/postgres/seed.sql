insert into tenants (id, code, name) values
  ('11111111-1111-1111-1111-111111111111', 'school-a', 'School A'),
  ('22222222-2222-2222-2222-222222222222', 'school-b', 'School B')
on conflict (code) do nothing;

insert into user_profiles (id, keycloak_user_id, tenant_id, username, email, display_name, role) values
  ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1', 'admin-a',   '11111111-1111-1111-1111-111111111111', 'admin-a',   'admin-a@school-a.local',   'Admin A',   'admin'),
  ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa2', 'teacher-a', '11111111-1111-1111-1111-111111111111', 'teacher-a', 'teacher-a@school-a.local', 'Teacher A', 'teacher'),
  ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa3', 'student-a', '11111111-1111-1111-1111-111111111111', 'student-a', 'student-a@school-a.local', 'Student A', 'student'),
  ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa4', 'parent-a',  '11111111-1111-1111-1111-111111111111', 'parent-a',  'parent-a@school-a.local',  'Parent A',  'parent'),
  ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb5', 'teacher-b', '22222222-2222-2222-2222-222222222222', 'teacher-b', 'teacher-b@school-b.local', 'Teacher B', 'teacher')
on conflict (keycloak_user_id) do nothing;

insert into school_classes (id, tenant_id, code, name, section, created_by) values
  ('33333333-3333-3333-3333-333333333331', '11111111-1111-1111-1111-111111111111', 'class-10a', 'Class 10A', 'A', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1'),
  ('33333333-3333-3333-3333-333333333332', '22222222-2222-2222-2222-222222222222', 'class-10b', 'Class 10B', 'B', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb5')
on conflict (tenant_id, code) do nothing;

insert into students (id, tenant_id, code, class_id, name, owner_user_id) values
  ('44444444-4444-4444-4444-444444444441', '11111111-1111-1111-1111-111111111111', 'student-a', '33333333-3333-3333-3333-333333333331', 'Student A', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa3')
on conflict (tenant_id, code) do nothing;

insert into assignments (id, tenant_id, code, class_id, title, description, created_by) values
  ('55555555-5555-5555-5555-555555555551', '11111111-1111-1111-1111-111111111111', 'assignment-1', '33333333-3333-3333-3333-333333333331', 'Assignment 1', 'Introductory assignment', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa2')
on conflict (tenant_id, code) do nothing;

insert into reports (id, tenant_id, code, student_id, report_type, content) values
  ('66666666-6666-6666-6666-666666666661', '11111111-1111-1111-1111-111111111111', 'report-1', '44444444-4444-4444-4444-444444444441', 'progress', 'Student A progress report')
on conflict (tenant_id, code) do nothing;

