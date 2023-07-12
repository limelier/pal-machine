create table public.sessions (
    id serial primary key not null,
    user_id  bigint not null,
    start timestamptz not null,
    "end" timestamptz
)