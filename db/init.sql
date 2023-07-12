create table public.sessions (
     user_id  bigint primary key not null,
     start timestamp not null,
     "end" timestamp
)