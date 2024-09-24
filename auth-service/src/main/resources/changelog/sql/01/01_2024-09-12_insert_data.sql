insert into users(id, username, password)
values (1, 'dbuser', '$2y$10$Ce4L3LI78U0x9Qvr9VUUn.tN0U3OQOQAXUwt83nuzTmqKlGsfqLEe'),
       (2, 'premium', '$2y$10$Ce4L3LI78U0x9Qvr9VUUn.tN0U3OQOQAXUwt83nuzTmqKlGsfqLEe'),
       (3, 'admin', '$2y$10$Ce4L3LI78U0x9Qvr9VUUn.tN0U3OQOQAXUwt83nuzTmqKlGsfqLEe'),
       (4, 'moderator', '$2y$10$Ce4L3LI78U0x9Qvr9VUUn.tN0U3OQOQAXUwt83nuzTmqKlGsfqLEe');

insert into roles(id, name)
values (1, 'ROLE_USER'),
       (2, 'ROLE_PREMIUM_USER'),
       (3, 'ROLE_MODERATOR'),
       (4, 'ROLE_ADMIN');

insert into user_roles(user_id, role_id)
values (1, 1),
       (2, 2),
       (3, 1),
       (3, 4),
       (4, 1),
       (4, 3);