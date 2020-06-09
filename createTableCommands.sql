CREATE TABLE public.die_user
(
    username text COLLATE pg_catalog."default" NOT NULL,
    password text COLLATE pg_catalog."default" NOT NULL,
    role text COLLATE pg_catalog."default" NOT NULL DEFAULT false,
    uid integer NOT NULL DEFAULT nextval('employee_id_seq'::regclass),
    CONSTRAINT employee_pkey PRIMARY KEY (uid)
)

CREATE TABLE public.die_team
(
    player_1 integer NOT NULL,
    player_2 integer NOT NULL,
    p_1_confirmed boolean NOT NULL,
    p_2_confirmed boolean NOT NULL,
    name text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT die_team_pk PRIMARY KEY (player_1, player_2),
    CONSTRAINT name UNIQUE (name)
)

CREATE TABLE public.die_game
(
    id integer NOT NULL DEFAULT nextval('die_game_id_seq'::regclass),
    status text COLLATE pg_catalog."default" NOT NULL,
    start_time date,
    end_time date,
    duration date,
    t_1_confirmed boolean NOT NULL,
    t_2_confirmed boolean NOT NULL,
    team_1 text COLLATE pg_catalog."default" NOT NULL,
    team_2 text COLLATE pg_catalog."default" NOT NULL,
    team_1_score integer,
    team_2_score integer,
    CONSTRAINT die_game_pkey PRIMARY KEY (id),
    CONSTRAINT team_one_name FOREIGN KEY (team_1)
        REFERENCES public.die_team (name) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT team_two_name FOREIGN KEY (team_2)
        REFERENCES public.die_team (name) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

CREATE TABLE public.die_event
(
    "eventType" text COLLATE pg_catalog."default" NOT NULL,
    id integer NOT NULL,
    uid integer,
    "gameID" integer,
    CONSTRAINT die_event_pkey PRIMARY KEY (id),
    CONSTRAINT "die_event_gameID_fkey" FOREIGN KEY ("gameID")
        REFERENCES public.die_game (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT die_event_uid_fkey FOREIGN KEY (uid)
        REFERENCES public.die_user (uid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
