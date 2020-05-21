CREATE SEQUENCE public.category_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 10
  CACHE 1;
ALTER TABLE public.category_id_seq
  OWNER TO postgres;

CREATE SEQUENCE public.recipe_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.recipe_id_seq
  OWNER TO postgres;

CREATE SEQUENCE public.share_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.share_id_seq
  OWNER TO postgres;

CREATE SEQUENCE public.user_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 2
  CACHE 1;
ALTER TABLE public.user_id_seq
  OWNER TO postgres;

CREATE TABLE public.share
(
  id bigint NOT NULL DEFAULT nextval('share_id_seq'::regclass),
  name character varying(255),
  recipe_id bigint,
  CONSTRAINT share_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.share
  OWNER TO postgres;

CREATE TABLE public.recipe
(
  id bigint NOT NULL DEFAULT nextval('recipe_id_seq'::regclass),
  category_id bigint,
  creation_date timestamp without time zone,
  description text,
  image_path character varying(255),
  name character varying(255),
  short_link character varying(255),
  status character varying(255),
  user_id bigint,
  recipe_id bigint,
  CONSTRAINT recipe_pkey PRIMARY KEY (id),
  CONSTRAINT fk9f8c1b6x937hecc3loc3tyfd1 FOREIGN KEY (recipe_id)
      REFERENCES public.share (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.recipe
  OWNER TO postgres;

CREATE TABLE public.category
(
  id bigint NOT NULL DEFAULT nextval('category_id_seq'::regclass),
  name character varying(255),
  category_id bigint,
  CONSTRAINT category_pkey PRIMARY KEY (id),
  CONSTRAINT fklhnesutcmo89sikmwkxmq19k0 FOREIGN KEY (category_id)
      REFERENCES public.recipe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.category
  OWNER TO postgres;

CREATE TABLE public."user"
(
  id bigint NOT NULL DEFAULT nextval('user_id_seq'::regclass),
  email character varying(255),
  first_name character varying(255),
  last_name character varying(255),
  password character varying(255),
  username character varying(255),
  user_id bigint,
  name bigint,
  CONSTRAINT user_pkey PRIMARY KEY (id),
  CONSTRAINT fk24g0770i90ictj349psrlmpiu FOREIGN KEY (name)
      REFERENCES public.share (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fkhcaum7neqwsxn50ymmirynl1s FOREIGN KEY (user_id)
      REFERENCES public.recipe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public."user"
  OWNER TO postgres;

CREATE TABLE public.user_role
(
  user_id bigint NOT NULL,
  roles character varying(255),
  CONSTRAINT fkfgsgxvihks805qcq8sq26ab7c FOREIGN KEY (user_id)
      REFERENCES public."user" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.user_role
  OWNER TO postgres;

INSERT INTO public.category(name) VALUES ('Salads');
INSERT INTO public.category(name) VALUES ('Soups');
INSERT INTO public.category(name) VALUES ('Snacks');
INSERT INTO public.category(name) VALUES ('Main');
INSERT INTO public.category(name) VALUES ('Meat');
INSERT INTO public.category(name) VALUES ('Fish');
INSERT INTO public.category(name) VALUES ('Vegetarian');
INSERT INTO public.category(name) VALUES ('Sauces');
INSERT INTO public.category(name) VALUES ('Deserts');
INSERT INTO public.category(name) VALUES ('Italian');
