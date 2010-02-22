--
-- PostgreSQL database dump
--

SET client_encoding = 'LATIN1';
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: onp
--

COMMENT ON SCHEMA public IS 'Standard public schema';


--
-- Name: plpgsql; Type: PROCEDURAL LANGUAGE; Schema: -; Owner: 
--

CREATE PROCEDURAL LANGUAGE plpgsql;


SET search_path = public, pg_catalog;

--
-- Name: gtsvector_in(cstring); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION gtsvector_in(cstring) RETURNS gtsvector
    AS '$libdir/tsearch2', 'gtsvector_in'
    LANGUAGE c STRICT;


ALTER FUNCTION public.gtsvector_in(cstring) OWNER TO onp;

--
-- Name: gtsvector_out(gtsvector); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION gtsvector_out(gtsvector) RETURNS cstring
    AS '$libdir/tsearch2', 'gtsvector_out'
    LANGUAGE c STRICT;


ALTER FUNCTION public.gtsvector_out(gtsvector) OWNER TO onp;

--
-- Name: gtsvector; Type: TYPE; Schema: public; Owner: onp
--

CREATE TYPE gtsvector (
    INTERNALLENGTH = variable,
    INPUT = gtsvector_in,
    OUTPUT = gtsvector_out,
    ALIGNMENT = int4,
    STORAGE = plain
);


ALTER TYPE public.gtsvector OWNER TO onp;

--
-- Name: tsquery_in(cstring); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION tsquery_in(cstring) RETURNS tsquery
    AS '$libdir/tsearch2', 'tsquery_in'
    LANGUAGE c STRICT;


ALTER FUNCTION public.tsquery_in(cstring) OWNER TO onp;

--
-- Name: tsquery_out(tsquery); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION tsquery_out(tsquery) RETURNS cstring
    AS '$libdir/tsearch2', 'tsquery_out'
    LANGUAGE c STRICT;


ALTER FUNCTION public.tsquery_out(tsquery) OWNER TO onp;

--
-- Name: tsquery; Type: TYPE; Schema: public; Owner: onp
--

CREATE TYPE tsquery (
    INTERNALLENGTH = variable,
    INPUT = tsquery_in,
    OUTPUT = tsquery_out,
    ALIGNMENT = int4,
    STORAGE = plain
);


ALTER TYPE public.tsquery OWNER TO onp;

--
-- Name: tsvector_in(cstring); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION tsvector_in(cstring) RETURNS tsvector
    AS '$libdir/tsearch2', 'tsvector_in'
    LANGUAGE c STRICT;


ALTER FUNCTION public.tsvector_in(cstring) OWNER TO onp;

--
-- Name: tsvector_out(tsvector); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION tsvector_out(tsvector) RETURNS cstring
    AS '$libdir/tsearch2', 'tsvector_out'
    LANGUAGE c STRICT;


ALTER FUNCTION public.tsvector_out(tsvector) OWNER TO onp;

--
-- Name: tsvector; Type: TYPE; Schema: public; Owner: onp
--

CREATE TYPE tsvector (
    INTERNALLENGTH = variable,
    INPUT = tsvector_in,
    OUTPUT = tsvector_out,
    ALIGNMENT = int4,
    STORAGE = extended
);


ALTER TYPE public.tsvector OWNER TO onp;

--
-- Name: statinfo; Type: TYPE; Schema: public; Owner: onp
--

CREATE TYPE statinfo AS (
	word text,
	ndoc integer,
	nentry integer
);


ALTER TYPE public.statinfo OWNER TO onp;

--
-- Name: tokenout; Type: TYPE; Schema: public; Owner: onp
--

CREATE TYPE tokenout AS (
	tokid integer,
	token text
);


ALTER TYPE public.tokenout OWNER TO onp;

--
-- Name: tokentype; Type: TYPE; Schema: public; Owner: onp
--

CREATE TYPE tokentype AS (
	tokid integer,
	alias text,
	descr text
);


ALTER TYPE public.tokentype OWNER TO onp;

--
-- Name: tsdebug; Type: TYPE; Schema: public; Owner: onp
--

CREATE TYPE tsdebug AS (
	ts_name text,
	tok_type text,
	description text,
	token text,
	dict_name text[],
	tsvector tsvector
);


ALTER TYPE public.tsdebug OWNER TO onp;

--
-- Name: _get_parser_from_curcfg(); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION _get_parser_from_curcfg() RETURNS text
    AS $$ select prs_name from pg_ts_cfg where oid = show_curcfg() $$
    LANGUAGE sql IMMUTABLE STRICT;


ALTER FUNCTION public._get_parser_from_curcfg() OWNER TO onp;

--
-- Name: add_page_read_access_tf(); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION add_page_read_access_tf() RETURNS "trigger"
    AS $$
    BEGIN
	insert into on_access (segment_id, content_id, contenttype_id, role_name, permission_p_key) values(new.segment_id, new.id, (SELECT id FROM on_contenttype WHERE description='on_template_impl'), 'User', 'READ');
	RETURN new;
    END;
$$
    LANGUAGE plpgsql;


ALTER FUNCTION public.add_page_read_access_tf() OWNER TO onp;

--
-- Name: check_user_exists_tf(); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION check_user_exists_tf() RETURNS "trigger"
    AS $$
    BEGIN
IF NOT NEW.username ISNULL THEN
   IF (select count(*) from on_user where username =
   NEW.username) > 0 THEN
   RAISE EXCEPTION 'Username ''%'' in use!', NEW.username;
   END IF;
END IF;
EXECUTE 'DELETE FROM pending_user_request WHERE expires < ''now''';
RETURN NEW;
    END;
$$
    LANGUAGE plpgsql;


ALTER FUNCTION public.check_user_exists_tf() OWNER TO onp;

--
-- Name: concat(tsvector, tsvector); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION concat(tsvector, tsvector) RETURNS tsvector
    AS '$libdir/tsearch2', 'concat'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.concat(tsvector, tsvector) OWNER TO onp;

--
-- Name: dex_init(internal); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION dex_init(internal) RETURNS internal
    AS '$libdir/tsearch2', 'dex_init'
    LANGUAGE c;


ALTER FUNCTION public.dex_init(internal) OWNER TO onp;

--
-- Name: dex_lexize(internal, internal, integer); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION dex_lexize(internal, internal, integer) RETURNS internal
    AS '$libdir/tsearch2', 'dex_lexize'
    LANGUAGE c STRICT;


ALTER FUNCTION public.dex_lexize(internal, internal, integer) OWNER TO onp;

--
-- Name: exectsq(tsvector, tsquery); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION exectsq(tsvector, tsquery) RETURNS boolean
    AS '$libdir/tsearch2', 'exectsq'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.exectsq(tsvector, tsquery) OWNER TO onp;

--
-- Name: FUNCTION exectsq(tsvector, tsquery); Type: COMMENT; Schema: public; Owner: onp
--

COMMENT ON FUNCTION exectsq(tsvector, tsquery) IS 'boolean operation with text index';


--
-- Name: get_covers(tsvector, tsquery); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION get_covers(tsvector, tsquery) RETURNS text
    AS '$libdir/tsearch2', 'get_covers'
    LANGUAGE c STRICT;


ALTER FUNCTION public.get_covers(tsvector, tsquery) OWNER TO onp;

--
-- Name: gtsvector_compress(internal); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION gtsvector_compress(internal) RETURNS internal
    AS '$libdir/tsearch2', 'gtsvector_compress'
    LANGUAGE c;


ALTER FUNCTION public.gtsvector_compress(internal) OWNER TO onp;

--
-- Name: gtsvector_consistent(gtsvector, internal, integer); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION gtsvector_consistent(gtsvector, internal, integer) RETURNS boolean
    AS '$libdir/tsearch2', 'gtsvector_consistent'
    LANGUAGE c;


ALTER FUNCTION public.gtsvector_consistent(gtsvector, internal, integer) OWNER TO onp;

--
-- Name: gtsvector_decompress(internal); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION gtsvector_decompress(internal) RETURNS internal
    AS '$libdir/tsearch2', 'gtsvector_decompress'
    LANGUAGE c;


ALTER FUNCTION public.gtsvector_decompress(internal) OWNER TO onp;

--
-- Name: gtsvector_penalty(internal, internal, internal); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION gtsvector_penalty(internal, internal, internal) RETURNS internal
    AS '$libdir/tsearch2', 'gtsvector_penalty'
    LANGUAGE c STRICT;


ALTER FUNCTION public.gtsvector_penalty(internal, internal, internal) OWNER TO onp;

--
-- Name: gtsvector_picksplit(internal, internal); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION gtsvector_picksplit(internal, internal) RETURNS internal
    AS '$libdir/tsearch2', 'gtsvector_picksplit'
    LANGUAGE c;


ALTER FUNCTION public.gtsvector_picksplit(internal, internal) OWNER TO onp;

--
-- Name: gtsvector_same(gtsvector, gtsvector, internal); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION gtsvector_same(gtsvector, gtsvector, internal) RETURNS internal
    AS '$libdir/tsearch2', 'gtsvector_same'
    LANGUAGE c;


ALTER FUNCTION public.gtsvector_same(gtsvector, gtsvector, internal) OWNER TO onp;

--
-- Name: gtsvector_union(internal, internal); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION gtsvector_union(internal, internal) RETURNS integer[]
    AS '$libdir/tsearch2', 'gtsvector_union'
    LANGUAGE c;


ALTER FUNCTION public.gtsvector_union(internal, internal) OWNER TO onp;

--
-- Name: gtsvector_union(bytea, internal); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION gtsvector_union(bytea, internal) RETURNS integer[]
    AS '$libdir/tsearch2', 'gtsvector_union'
    LANGUAGE c;


ALTER FUNCTION public.gtsvector_union(bytea, internal) OWNER TO onp;

--
-- Name: headline(oid, text, tsquery, text); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION headline(oid, text, tsquery, text) RETURNS text
    AS '$libdir/tsearch2', 'headline'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.headline(oid, text, tsquery, text) OWNER TO onp;

--
-- Name: headline(oid, text, tsquery); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION headline(oid, text, tsquery) RETURNS text
    AS '$libdir/tsearch2', 'headline'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.headline(oid, text, tsquery) OWNER TO onp;

--
-- Name: headline(text, text, tsquery, text); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION headline(text, text, tsquery, text) RETURNS text
    AS '$libdir/tsearch2', 'headline_byname'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.headline(text, text, tsquery, text) OWNER TO onp;

--
-- Name: headline(text, text, tsquery); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION headline(text, text, tsquery) RETURNS text
    AS '$libdir/tsearch2', 'headline_byname'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.headline(text, text, tsquery) OWNER TO onp;

--
-- Name: headline(text, tsquery, text); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION headline(text, tsquery, text) RETURNS text
    AS '$libdir/tsearch2', 'headline_current'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.headline(text, tsquery, text) OWNER TO onp;

--
-- Name: headline(text, tsquery); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION headline(text, tsquery) RETURNS text
    AS '$libdir/tsearch2', 'headline_current'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.headline(text, tsquery) OWNER TO onp;

--
-- Name: instr(character varying, character varying); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION instr(character varying, character varying) RETURNS integer
    AS $_$
    DECLARE
        pos integer;
    BEGIN
        pos:= instr($1,$2,1);
        RETURN pos;
    END;
    $_$
    LANGUAGE plpgsql;


ALTER FUNCTION public.instr(character varying, character varying) OWNER TO onp;

--
-- Name: instr(character varying, character varying, integer); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION instr(character varying, character varying, integer) RETURNS integer
    AS $_$
    DECLARE
        string ALIAS FOR $1;
        string_to_search ALIAS FOR $2;
        beg_index ALIAS FOR $3;
        pos integer NOT NULL DEFAULT 0;
        temp_str VARCHAR;
        beg INTEGER;
        length INTEGER;
        ss_length INTEGER;
    BEGIN
        IF beg_index > 0 THEN
    
           temp_str := substring(string FROM beg_index);
           pos := position(string_to_search IN temp_str);
    
           IF pos = 0 THEN
                 RETURN 0;
             ELSE
                 RETURN pos + beg_index - 1;
             END IF;
        ELSE
           ss_length := char_length(string_to_search);
           length := char_length(string);
           beg := length + beg_index - ss_length + 2;
    
           WHILE beg > 0 LOOP
               temp_str := substring(string FROM beg FOR ss_length);
                 pos := position(string_to_search IN temp_str);
    
                 IF pos > 0 THEN
                           RETURN beg;
                 END IF;
    
                 beg := beg - 1;
           END LOOP;
           RETURN 0;
        END IF;
    END;
    $_$
    LANGUAGE plpgsql;


ALTER FUNCTION public.instr(character varying, character varying, integer) OWNER TO onp;

--
-- Name: instr(character varying, character varying, integer, integer); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION instr(character varying, character varying, integer, integer) RETURNS integer
    AS $_$
    DECLARE
        string ALIAS FOR $1;
        string_to_search ALIAS FOR $2;
        beg_index ALIAS FOR $3;
        occur_index ALIAS FOR $4;
        pos integer NOT NULL DEFAULT 0;
        occur_number INTEGER NOT NULL DEFAULT 0;
        temp_str VARCHAR;
        beg INTEGER;
        i INTEGER;
        length INTEGER;
        ss_length INTEGER;
    BEGIN
        IF beg_index > 0 THEN
            beg := beg_index;
            temp_str := substring(string FROM beg_index);
    
            FOR i IN 1..occur_index LOOP
                pos := position(string_to_search IN temp_str);
    
                IF i = 1 THEN
                    beg := beg + pos - 1;
                ELSE
                    beg := beg + pos;
                END IF;
    
                temp_str := substring(string FROM beg + 1);
            END LOOP;
    
            IF pos = 0 THEN
                RETURN 0;
            ELSE
                RETURN beg;
            END IF;
        ELSE
            ss_length := char_length(string_to_search);
            length := char_length(string);
            beg := length + beg_index - ss_length + 2;
    
            WHILE beg > 0 LOOP
                temp_str := substring(string FROM beg FOR ss_length);
                pos := position(string_to_search IN temp_str);
    
                IF pos > 0 THEN
                    occur_number := occur_number + 1;
    
                    IF occur_number = occur_index THEN
                        RETURN beg;
                    END IF;
                END IF;
    
                beg := beg - 1;
            END LOOP;
    
            RETURN 0;
        END IF;
    END;
    $_$
    LANGUAGE plpgsql;


ALTER FUNCTION public.instr(character varying, character varying, integer, integer) OWNER TO onp;

--
-- Name: length(tsvector); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION length(tsvector) RETURNS integer
    AS '$libdir/tsearch2', 'tsvector_length'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.length(tsvector) OWNER TO onp;

--
-- Name: lexize(oid, text); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION lexize(oid, text) RETURNS text[]
    AS '$libdir/tsearch2', 'lexize'
    LANGUAGE c STRICT;


ALTER FUNCTION public.lexize(oid, text) OWNER TO onp;

--
-- Name: lexize(text, text); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION lexize(text, text) RETURNS text[]
    AS '$libdir/tsearch2', 'lexize_byname'
    LANGUAGE c STRICT;


ALTER FUNCTION public.lexize(text, text) OWNER TO onp;

--
-- Name: lexize(text); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION lexize(text) RETURNS text[]
    AS '$libdir/tsearch2', 'lexize_bycurrent'
    LANGUAGE c STRICT;


ALTER FUNCTION public.lexize(text) OWNER TO onp;

--
-- Name: parse(oid, text); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION parse(oid, text) RETURNS SETOF tokenout
    AS '$libdir/tsearch2', 'parse'
    LANGUAGE c STRICT;


ALTER FUNCTION public.parse(oid, text) OWNER TO onp;

--
-- Name: parse(text, text); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION parse(text, text) RETURNS SETOF tokenout
    AS '$libdir/tsearch2', 'parse_byname'
    LANGUAGE c STRICT;


ALTER FUNCTION public.parse(text, text) OWNER TO onp;

--
-- Name: parse(text); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION parse(text) RETURNS SETOF tokenout
    AS '$libdir/tsearch2', 'parse_current'
    LANGUAGE c STRICT;


ALTER FUNCTION public.parse(text) OWNER TO onp;

--
-- Name: plpgsql_call_handler(); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION plpgsql_call_handler() RETURNS language_handler
    AS '$libdir/plpgsql', 'plpgsql_call_handler'
    LANGUAGE c;


ALTER FUNCTION public.plpgsql_call_handler() OWNER TO onp;

--
-- Name: prsd_end(internal); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION prsd_end(internal) RETURNS void
    AS '$libdir/tsearch2', 'prsd_end'
    LANGUAGE c;


ALTER FUNCTION public.prsd_end(internal) OWNER TO onp;

--
-- Name: prsd_getlexeme(internal, internal, internal); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION prsd_getlexeme(internal, internal, internal) RETURNS integer
    AS '$libdir/tsearch2', 'prsd_getlexeme'
    LANGUAGE c;


ALTER FUNCTION public.prsd_getlexeme(internal, internal, internal) OWNER TO onp;

--
-- Name: prsd_headline(internal, internal, internal); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION prsd_headline(internal, internal, internal) RETURNS internal
    AS '$libdir/tsearch2', 'prsd_headline'
    LANGUAGE c;


ALTER FUNCTION public.prsd_headline(internal, internal, internal) OWNER TO onp;

--
-- Name: prsd_lextype(internal); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION prsd_lextype(internal) RETURNS internal
    AS '$libdir/tsearch2', 'prsd_lextype'
    LANGUAGE c;


ALTER FUNCTION public.prsd_lextype(internal) OWNER TO onp;

--
-- Name: prsd_start(internal, integer); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION prsd_start(internal, integer) RETURNS internal
    AS '$libdir/tsearch2', 'prsd_start'
    LANGUAGE c;


ALTER FUNCTION public.prsd_start(internal, integer) OWNER TO onp;

--
-- Name: querytree(tsquery); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION querytree(tsquery) RETURNS text
    AS '$libdir/tsearch2', 'tsquerytree'
    LANGUAGE c STRICT;


ALTER FUNCTION public.querytree(tsquery) OWNER TO onp;

--
-- Name: rank(real[], tsvector, tsquery); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION rank(real[], tsvector, tsquery) RETURNS real
    AS '$libdir/tsearch2', 'rank'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.rank(real[], tsvector, tsquery) OWNER TO onp;

--
-- Name: rank(real[], tsvector, tsquery, integer); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION rank(real[], tsvector, tsquery, integer) RETURNS real
    AS '$libdir/tsearch2', 'rank'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.rank(real[], tsvector, tsquery, integer) OWNER TO onp;

--
-- Name: rank(tsvector, tsquery); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION rank(tsvector, tsquery) RETURNS real
    AS '$libdir/tsearch2', 'rank_def'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.rank(tsvector, tsquery) OWNER TO onp;

--
-- Name: rank(tsvector, tsquery, integer); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION rank(tsvector, tsquery, integer) RETURNS real
    AS '$libdir/tsearch2', 'rank_def'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.rank(tsvector, tsquery, integer) OWNER TO onp;

--
-- Name: rank_cd(integer, tsvector, tsquery); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION rank_cd(integer, tsvector, tsquery) RETURNS real
    AS '$libdir/tsearch2', 'rank_cd'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.rank_cd(integer, tsvector, tsquery) OWNER TO onp;

--
-- Name: rank_cd(integer, tsvector, tsquery, integer); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION rank_cd(integer, tsvector, tsquery, integer) RETURNS real
    AS '$libdir/tsearch2', 'rank_cd'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.rank_cd(integer, tsvector, tsquery, integer) OWNER TO onp;

--
-- Name: rank_cd(tsvector, tsquery); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION rank_cd(tsvector, tsquery) RETURNS real
    AS '$libdir/tsearch2', 'rank_cd_def'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.rank_cd(tsvector, tsquery) OWNER TO onp;

--
-- Name: rank_cd(tsvector, tsquery, integer); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION rank_cd(tsvector, tsquery, integer) RETURNS real
    AS '$libdir/tsearch2', 'rank_cd_def'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.rank_cd(tsvector, tsquery, integer) OWNER TO onp;

--
-- Name: reset_tsearch(); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION reset_tsearch() RETURNS void
    AS '$libdir/tsearch2', 'reset_tsearch'
    LANGUAGE c STRICT;


ALTER FUNCTION public.reset_tsearch() OWNER TO onp;

--
-- Name: rexectsq(tsquery, tsvector); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION rexectsq(tsquery, tsvector) RETURNS boolean
    AS '$libdir/tsearch2', 'rexectsq'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.rexectsq(tsquery, tsvector) OWNER TO onp;

--
-- Name: FUNCTION rexectsq(tsquery, tsvector); Type: COMMENT; Schema: public; Owner: onp
--

COMMENT ON FUNCTION rexectsq(tsquery, tsvector) IS 'boolean operation with text index';


--
-- Name: set_curcfg(integer); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION set_curcfg(integer) RETURNS void
    AS '$libdir/tsearch2', 'set_curcfg'
    LANGUAGE c STRICT;


ALTER FUNCTION public.set_curcfg(integer) OWNER TO onp;

--
-- Name: set_curcfg(text); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION set_curcfg(text) RETURNS void
    AS '$libdir/tsearch2', 'set_curcfg_byname'
    LANGUAGE c STRICT;


ALTER FUNCTION public.set_curcfg(text) OWNER TO onp;

--
-- Name: set_curdict(integer); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION set_curdict(integer) RETURNS void
    AS '$libdir/tsearch2', 'set_curdict'
    LANGUAGE c STRICT;


ALTER FUNCTION public.set_curdict(integer) OWNER TO onp;

--
-- Name: set_curdict(text); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION set_curdict(text) RETURNS void
    AS '$libdir/tsearch2', 'set_curdict_byname'
    LANGUAGE c STRICT;


ALTER FUNCTION public.set_curdict(text) OWNER TO onp;

--
-- Name: set_curprs(integer); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION set_curprs(integer) RETURNS void
    AS '$libdir/tsearch2', 'set_curprs'
    LANGUAGE c STRICT;


ALTER FUNCTION public.set_curprs(integer) OWNER TO onp;

--
-- Name: set_curprs(text); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION set_curprs(text) RETURNS void
    AS '$libdir/tsearch2', 'set_curprs_byname'
    LANGUAGE c STRICT;


ALTER FUNCTION public.set_curprs(text) OWNER TO onp;

--
-- Name: setweight(tsvector, "char"); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION setweight(tsvector, "char") RETURNS tsvector
    AS '$libdir/tsearch2', 'setweight'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.setweight(tsvector, "char") OWNER TO onp;

--
-- Name: show_curcfg(); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION show_curcfg() RETURNS oid
    AS '$libdir/tsearch2', 'show_curcfg'
    LANGUAGE c STRICT;


ALTER FUNCTION public.show_curcfg() OWNER TO onp;

--
-- Name: snb_en_init(internal); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION snb_en_init(internal) RETURNS internal
    AS '$libdir/tsearch2', 'snb_en_init'
    LANGUAGE c;


ALTER FUNCTION public.snb_en_init(internal) OWNER TO onp;

--
-- Name: snb_lexize(internal, internal, integer); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION snb_lexize(internal, internal, integer) RETURNS internal
    AS '$libdir/tsearch2', 'snb_lexize'
    LANGUAGE c STRICT;


ALTER FUNCTION public.snb_lexize(internal, internal, integer) OWNER TO onp;

--
-- Name: snb_ru_init(internal); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION snb_ru_init(internal) RETURNS internal
    AS '$libdir/tsearch2', 'snb_ru_init'
    LANGUAGE c;


ALTER FUNCTION public.snb_ru_init(internal) OWNER TO onp;

--
-- Name: spell_init(internal); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION spell_init(internal) RETURNS internal
    AS '$libdir/tsearch2', 'spell_init'
    LANGUAGE c;


ALTER FUNCTION public.spell_init(internal) OWNER TO onp;

--
-- Name: spell_lexize(internal, internal, integer); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION spell_lexize(internal, internal, integer) RETURNS internal
    AS '$libdir/tsearch2', 'spell_lexize'
    LANGUAGE c STRICT;


ALTER FUNCTION public.spell_lexize(internal, internal, integer) OWNER TO onp;

--
-- Name: stat(text); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION stat(text) RETURNS SETOF statinfo
    AS '$libdir/tsearch2', 'ts_stat'
    LANGUAGE c STRICT;


ALTER FUNCTION public.stat(text) OWNER TO onp;

--
-- Name: stat(text, text); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION stat(text, text) RETURNS SETOF statinfo
    AS '$libdir/tsearch2', 'ts_stat'
    LANGUAGE c STRICT;


ALTER FUNCTION public.stat(text, text) OWNER TO onp;

--
-- Name: strip(tsvector); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION strip(tsvector) RETURNS tsvector
    AS '$libdir/tsearch2', 'strip'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.strip(tsvector) OWNER TO onp;

--
-- Name: syn_init(internal); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION syn_init(internal) RETURNS internal
    AS '$libdir/tsearch2', 'syn_init'
    LANGUAGE c;


ALTER FUNCTION public.syn_init(internal) OWNER TO onp;

--
-- Name: syn_lexize(internal, internal, integer); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION syn_lexize(internal, internal, integer) RETURNS internal
    AS '$libdir/tsearch2', 'syn_lexize'
    LANGUAGE c STRICT;


ALTER FUNCTION public.syn_lexize(internal, internal, integer) OWNER TO onp;

--
-- Name: to_day(timestamp without time zone); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION to_day(timestamp without time zone) RETURNS integer
    AS $_$
    BEGIN RETURN cast(to_char($1, 'DD') as int); END;
$_$
    LANGUAGE plpgsql IMMUTABLE STRICT;


ALTER FUNCTION public.to_day(timestamp without time zone) OWNER TO onp;

--
-- Name: to_month(timestamp without time zone); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION to_month(timestamp without time zone) RETURNS integer
    AS $_$
    BEGIN RETURN cast(to_char($1, 'MM') as int); END;
$_$
    LANGUAGE plpgsql IMMUTABLE STRICT;


ALTER FUNCTION public.to_month(timestamp without time zone) OWNER TO onp;

--
-- Name: to_tsquery(oid, text); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION to_tsquery(oid, text) RETURNS tsquery
    AS '$libdir/tsearch2', 'to_tsquery'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.to_tsquery(oid, text) OWNER TO onp;

--
-- Name: to_tsquery(text, text); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION to_tsquery(text, text) RETURNS tsquery
    AS '$libdir/tsearch2', 'to_tsquery_name'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.to_tsquery(text, text) OWNER TO onp;

--
-- Name: to_tsquery(text); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION to_tsquery(text) RETURNS tsquery
    AS '$libdir/tsearch2', 'to_tsquery_current'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.to_tsquery(text) OWNER TO onp;

--
-- Name: to_tsvector(oid, text); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION to_tsvector(oid, text) RETURNS tsvector
    AS '$libdir/tsearch2', 'to_tsvector'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.to_tsvector(oid, text) OWNER TO onp;

--
-- Name: to_tsvector(text, text); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION to_tsvector(text, text) RETURNS tsvector
    AS '$libdir/tsearch2', 'to_tsvector_name'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.to_tsvector(text, text) OWNER TO onp;

--
-- Name: to_tsvector(text); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION to_tsvector(text) RETURNS tsvector
    AS '$libdir/tsearch2', 'to_tsvector_current'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.to_tsvector(text) OWNER TO onp;

--
-- Name: to_week(timestamp without time zone); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION to_week(timestamp without time zone) RETURNS integer
    AS $_$
    BEGIN RETURN cast(to_char($1, 'IW') as int); END;
$_$
    LANGUAGE plpgsql IMMUTABLE STRICT;


ALTER FUNCTION public.to_week(timestamp without time zone) OWNER TO onp;

--
-- Name: to_year(timestamp without time zone); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION to_year(timestamp without time zone) RETURNS integer
    AS $_$
    BEGIN RETURN cast(to_char($1, 'YYYY') as int); END;
$_$
    LANGUAGE plpgsql IMMUTABLE STRICT;


ALTER FUNCTION public.to_year(timestamp without time zone) OWNER TO onp;

--
-- Name: token_type(integer); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION token_type(integer) RETURNS SETOF tokentype
    AS '$libdir/tsearch2', 'token_type'
    LANGUAGE c STRICT;


ALTER FUNCTION public.token_type(integer) OWNER TO onp;

--
-- Name: token_type(text); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION token_type(text) RETURNS SETOF tokentype
    AS '$libdir/tsearch2', 'token_type_byname'
    LANGUAGE c STRICT;


ALTER FUNCTION public.token_type(text) OWNER TO onp;

--
-- Name: token_type(); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION token_type() RETURNS SETOF tokentype
    AS '$libdir/tsearch2', 'token_type_current'
    LANGUAGE c STRICT;


ALTER FUNCTION public.token_type() OWNER TO onp;

--
-- Name: ts_debug(text); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION ts_debug(text) RETURNS SETOF tsdebug
    AS $_$
select 
        m.ts_name,
        t.alias as tok_type,
        t.descr as description,
        p.token,
        m.dict_name,
        strip(to_tsvector(p.token)) as tsvector
from
        parse( _get_parser_from_curcfg(), $1 ) as p,
        token_type() as t,
        pg_ts_cfgmap as m,
        pg_ts_cfg as c
where
        t.tokid=p.tokid and
        t.alias = m.tok_alias and 
        m.ts_name=c.ts_name and 
        c.oid=show_curcfg() 
$_$
    LANGUAGE sql STRICT;


ALTER FUNCTION public.ts_debug(text) OWNER TO onp;

--
-- Name: tsearch2(); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION tsearch2() RETURNS "trigger"
    AS '$libdir/tsearch2', 'tsearch2'
    LANGUAGE c;


ALTER FUNCTION public.tsearch2() OWNER TO onp;

--
-- Name: tsvector_cmp(tsvector, tsvector); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION tsvector_cmp(tsvector, tsvector) RETURNS integer
    AS '$libdir/tsearch2', 'tsvector_cmp'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.tsvector_cmp(tsvector, tsvector) OWNER TO onp;

--
-- Name: tsvector_eq(tsvector, tsvector); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION tsvector_eq(tsvector, tsvector) RETURNS boolean
    AS '$libdir/tsearch2', 'tsvector_eq'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.tsvector_eq(tsvector, tsvector) OWNER TO onp;

--
-- Name: tsvector_ge(tsvector, tsvector); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION tsvector_ge(tsvector, tsvector) RETURNS boolean
    AS '$libdir/tsearch2', 'tsvector_ge'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.tsvector_ge(tsvector, tsvector) OWNER TO onp;

--
-- Name: tsvector_gt(tsvector, tsvector); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION tsvector_gt(tsvector, tsvector) RETURNS boolean
    AS '$libdir/tsearch2', 'tsvector_gt'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.tsvector_gt(tsvector, tsvector) OWNER TO onp;

--
-- Name: tsvector_le(tsvector, tsvector); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION tsvector_le(tsvector, tsvector) RETURNS boolean
    AS '$libdir/tsearch2', 'tsvector_le'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.tsvector_le(tsvector, tsvector) OWNER TO onp;

--
-- Name: tsvector_lt(tsvector, tsvector); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION tsvector_lt(tsvector, tsvector) RETURNS boolean
    AS '$libdir/tsearch2', 'tsvector_lt'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.tsvector_lt(tsvector, tsvector) OWNER TO onp;

--
-- Name: tsvector_ne(tsvector, tsvector); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION tsvector_ne(tsvector, tsvector) RETURNS boolean
    AS '$libdir/tsearch2', 'tsvector_ne'
    LANGUAGE c IMMUTABLE STRICT;


ALTER FUNCTION public.tsvector_ne(tsvector, tsvector) OWNER TO onp;

--
-- Name: update_document_search_text_tf(); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION update_document_search_text_tf() RETURNS "trigger"
    AS $$
BEGIN
EXECUTE 'delete from onp_document_search_text where id = ' || NEW.id;
EXECUTE 'INSERT INTO onp_document_search_text (id, search_text) values (' || NEW.id || ',
(select coalesce(oi.title, '''') || '' '' || coalesce(oi.description, '''') from onp_document oi WHERE oi.id = ' || NEW.id || '))';
RETURN NEW;
    END;
$$
    LANGUAGE plpgsql;


ALTER FUNCTION public.update_document_search_text_tf() OWNER TO onp;

--
-- Name: update_image_search_text_tf(); Type: FUNCTION; Schema: public; Owner: onp
--

CREATE FUNCTION update_image_search_text_tf() RETURNS "trigger"
    AS $$
BEGIN
EXECUTE 'delete from onp_image_search_text where id = ' || NEW.id;
EXECUTE 'INSERT INTO onp_image_search_text (id, search_text) values (' || NEW.id || ',
(select coalesce(oi.title, '''') || '' '' || coalesce(oi.description, '''') || '' ''
|| coalesce(oi.source, '''') || '' '' || coalesce(oi.email, '''') || '' '' ||
coalesce(oi.copyright, '''') from onp_image oi WHERE oi.id = ' || NEW.id || '))';
RETURN NEW;
    END;
$$
    LANGUAGE plpgsql;


ALTER FUNCTION public.update_image_search_text_tf() OWNER TO onp;

--
-- Name: <; Type: OPERATOR; Schema: public; Owner: onp
--

CREATE OPERATOR < (
    PROCEDURE = tsvector_lt,
    LEFTARG = tsvector,
    RIGHTARG = tsvector,
    COMMUTATOR = >,
    NEGATOR = >=,
    RESTRICT = contsel,
    JOIN = contjoinsel
);


ALTER OPERATOR public.< (tsvector, tsvector) OWNER TO onp;

--
-- Name: <=; Type: OPERATOR; Schema: public; Owner: onp
--

CREATE OPERATOR <= (
    PROCEDURE = tsvector_le,
    LEFTARG = tsvector,
    RIGHTARG = tsvector,
    COMMUTATOR = >=,
    NEGATOR = >,
    RESTRICT = contsel,
    JOIN = contjoinsel
);


ALTER OPERATOR public.<= (tsvector, tsvector) OWNER TO onp;

--
-- Name: <>; Type: OPERATOR; Schema: public; Owner: onp
--

CREATE OPERATOR <> (
    PROCEDURE = tsvector_ne,
    LEFTARG = tsvector,
    RIGHTARG = tsvector,
    COMMUTATOR = <>,
    NEGATOR = =,
    RESTRICT = neqsel,
    JOIN = neqjoinsel
);


ALTER OPERATOR public.<> (tsvector, tsvector) OWNER TO onp;

--
-- Name: =; Type: OPERATOR; Schema: public; Owner: onp
--

CREATE OPERATOR = (
    PROCEDURE = tsvector_eq,
    LEFTARG = tsvector,
    RIGHTARG = tsvector,
    COMMUTATOR = =,
    NEGATOR = <>,
    RESTRICT = eqsel,
    JOIN = eqjoinsel,
    SORT1 = <,
    SORT2 = <,
    LTCMP = <,
    GTCMP = >
);


ALTER OPERATOR public.= (tsvector, tsvector) OWNER TO onp;

--
-- Name: >; Type: OPERATOR; Schema: public; Owner: onp
--

CREATE OPERATOR > (
    PROCEDURE = tsvector_gt,
    LEFTARG = tsvector,
    RIGHTARG = tsvector,
    COMMUTATOR = <,
    NEGATOR = <=,
    RESTRICT = contsel,
    JOIN = contjoinsel
);


ALTER OPERATOR public.> (tsvector, tsvector) OWNER TO onp;

--
-- Name: >=; Type: OPERATOR; Schema: public; Owner: onp
--

CREATE OPERATOR >= (
    PROCEDURE = tsvector_ge,
    LEFTARG = tsvector,
    RIGHTARG = tsvector,
    COMMUTATOR = <=,
    NEGATOR = <,
    RESTRICT = contsel,
    JOIN = contjoinsel
);


ALTER OPERATOR public.>= (tsvector, tsvector) OWNER TO onp;

--
-- Name: @@; Type: OPERATOR; Schema: public; Owner: onp
--

CREATE OPERATOR @@ (
    PROCEDURE = rexectsq,
    LEFTARG = tsquery,
    RIGHTARG = tsvector,
    COMMUTATOR = @@,
    RESTRICT = contsel,
    JOIN = contjoinsel
);


ALTER OPERATOR public.@@ (tsquery, tsvector) OWNER TO onp;

--
-- Name: @@; Type: OPERATOR; Schema: public; Owner: onp
--

CREATE OPERATOR @@ (
    PROCEDURE = exectsq,
    LEFTARG = tsvector,
    RIGHTARG = tsquery,
    COMMUTATOR = @@,
    RESTRICT = contsel,
    JOIN = contjoinsel
);


ALTER OPERATOR public.@@ (tsvector, tsquery) OWNER TO onp;

--
-- Name: ||; Type: OPERATOR; Schema: public; Owner: onp
--

CREATE OPERATOR || (
    PROCEDURE = concat,
    LEFTARG = tsvector,
    RIGHTARG = tsvector
);


ALTER OPERATOR public.|| (tsvector, tsvector) OWNER TO onp;

--
-- Name: gist_tsvector_ops; Type: OPERATOR CLASS; Schema: public; Owner: onp
--

CREATE OPERATOR CLASS gist_tsvector_ops
    DEFAULT FOR TYPE tsvector USING gist AS
    STORAGE gtsvector ,
    OPERATOR 1 @@(tsvector,tsquery) RECHECK ,
    FUNCTION 1 gtsvector_consistent(gtsvector,internal,integer) ,
    FUNCTION 2 gtsvector_union(internal,internal) ,
    FUNCTION 3 gtsvector_compress(internal) ,
    FUNCTION 4 gtsvector_decompress(internal) ,
    FUNCTION 5 gtsvector_penalty(internal,internal,internal) ,
    FUNCTION 6 gtsvector_picksplit(internal,internal) ,
    FUNCTION 7 gtsvector_same(gtsvector,gtsvector,internal);


ALTER OPERATOR CLASS public.gist_tsvector_ops USING gist OWNER TO onp;

--
-- Name: tsvector_ops; Type: OPERATOR CLASS; Schema: public; Owner: onp
--

CREATE OPERATOR CLASS tsvector_ops
    DEFAULT FOR TYPE tsvector USING btree AS
    OPERATOR 1 <(tsvector,tsvector) ,
    OPERATOR 2 <=(tsvector,tsvector) ,
    OPERATOR 3 =(tsvector,tsvector) ,
    OPERATOR 4 >=(tsvector,tsvector) ,
    OPERATOR 5 >(tsvector,tsvector) ,
    FUNCTION 1 tsvector_cmp(tsvector,tsvector);


ALTER OPERATOR CLASS public.tsvector_ops USING btree OWNER TO onp;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: code; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE code (
    id serial NOT NULL,
    code_key character varying NOT NULL,
    value character varying,
    code_group_id integer NOT NULL
);


ALTER TABLE public.code OWNER TO onp;

--
-- Name: code_group; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE code_group (
    id serial NOT NULL,
    description character varying NOT NULL
);


ALTER TABLE public.code_group OWNER TO onp;

--
-- Name: group_roles; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE group_roles (
    segment_id integer NOT NULL,
    groupname character varying NOT NULL,
    role_name character varying(15) NOT NULL
);


ALTER TABLE public.group_roles OWNER TO onp;

--
-- Name: on_access; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_access (
    segment_id integer,
    content_id integer,
    contenttype_id integer NOT NULL,
    role_name character varying(512) NOT NULL,
    permission_p_key character varying(15) NOT NULL
);


ALTER TABLE public.on_access OWNER TO onp;

--
-- Name: on_article; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_article (
    id integer NOT NULL,
    valid_from timestamp without time zone,
    valid_to timestamp without time zone,
    created timestamp without time zone DEFAULT now() NOT NULL,
    created_by character varying NOT NULL,
    modified timestamp without time zone,
    modified_by character varying,
    aknowledged_by character varying,
    is_ready boolean DEFAULT false,
    current_version integer DEFAULT 1,
    on_article_template_id integer NOT NULL,
    priority integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.on_article OWNER TO onp;

--
-- Name: on_article_active; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_article_active (
    id integer NOT NULL,
    lang_id integer NOT NULL,
    text_value text NOT NULL,
    name character varying NOT NULL,
    serial_id bigint DEFAULT nextval(('on_article_active_serial_id_seq'::text)::regclass) NOT NULL
);


ALTER TABLE public.on_article_active OWNER TO onp;

--
-- Name: on_article_active_serial_id_seq; Type: SEQUENCE; Schema: public; Owner: onp
--

CREATE SEQUENCE on_article_active_serial_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.on_article_active_serial_id_seq OWNER TO onp;

--
-- Name: on_article_presentation; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_article_presentation (
    id serial NOT NULL,
    on_article_template_id integer NOT NULL,
    format_id integer NOT NULL,
    description character varying NOT NULL,
    value text NOT NULL
);


ALTER TABLE public.on_article_presentation OWNER TO onp;

--
-- Name: on_article_reply; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_article_reply (
    reply_id integer NOT NULL,
    article_id integer NOT NULL,
    replyto_id integer
);


ALTER TABLE public.on_article_reply OWNER TO onp;

--
-- Name: on_article_template; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_article_template (
    id serial NOT NULL,
    description character varying NOT NULL
);


ALTER TABLE public.on_article_template OWNER TO onp;

--
-- Name: on_article_template_values; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_article_template_values (
    on_article_template_id integer NOT NULL,
    field_id integer NOT NULL,
    seq integer NOT NULL,
    required boolean DEFAULT false,
    validation_regexp character varying,
    name character varying NOT NULL,
    description character varying NOT NULL,
    "rows" integer DEFAULT 50,
    cols integer DEFAULT 600,
    flags integer DEFAULT 0 NOT NULL,
    id bigint DEFAULT nextval(('on_article_template_values_id_seq'::text)::regclass) NOT NULL
);


ALTER TABLE public.on_article_template_values OWNER TO onp;

--
-- Name: on_article_template_values_id_seq; Type: SEQUENCE; Schema: public; Owner: onp
--

CREATE SEQUENCE on_article_template_values_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.on_article_template_values_id_seq OWNER TO onp;

--
-- Name: on_article_title; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_article_title (
    id integer NOT NULL,
    lang_id integer NOT NULL,
    title character varying NOT NULL
);


ALTER TABLE public.on_article_title OWNER TO onp;

--
-- Name: on_content; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_content (
    id serial NOT NULL,
    contenttype_id integer,
    created timestamp without time zone DEFAULT now() NOT NULL,
    created_by character varying NOT NULL,
    valid_from timestamp without time zone,
    valid_to timestamp without time zone,
    modified timestamp without time zone,
    modified_by character varying,
    content_classification_id integer
);


ALTER TABLE public.on_content OWNER TO onp;

--
-- Name: on_content_classification; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_content_classification (
    id serial NOT NULL,
    code character varying NOT NULL
);


ALTER TABLE public.on_content_classification OWNER TO onp;

--
-- Name: on_content_field; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_content_field (
    id serial NOT NULL,
    contenttype_id integer NOT NULL,
    f_name character varying NOT NULL,
    f_type_id integer NOT NULL
);


ALTER TABLE public.on_content_field OWNER TO onp;

--
-- Name: on_content_field_type; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_content_field_type (
    id serial NOT NULL,
    description character varying NOT NULL
);


ALTER TABLE public.on_content_field_type OWNER TO onp;

--
-- Name: on_content_format_mapping; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_content_format_mapping (
    id serial NOT NULL,
    contenttype_id integer NOT NULL,
    content_id integer NOT NULL,
    format_id integer NOT NULL
);


ALTER TABLE public.on_content_format_mapping OWNER TO onp;

--
-- Name: on_content_metadata; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_content_metadata (
    content_id integer NOT NULL,
    lang_id integer DEFAULT 0 NOT NULL,
    title character varying,
    description character varying,
    keywords character varying
);


ALTER TABLE public.on_content_metadata OWNER TO onp;

--
-- Name: on_contentrelation; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_contentrelation (
    relater integer NOT NULL,
    relater_contenttype_id integer NOT NULL,
    relatee integer NOT NULL,
    relatee_contenttype_id integer NOT NULL,
    reltype_id integer NOT NULL
);


ALTER TABLE public.on_contentrelation OWNER TO onp;

--
-- Name: on_contentrelationtype; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_contentrelationtype (
    id serial NOT NULL,
    description character varying NOT NULL
);


ALTER TABLE public.on_contentrelationtype OWNER TO onp;

--
-- Name: on_contenttype; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_contenttype (
    id serial NOT NULL,
    description character varying NOT NULL
);


ALTER TABLE public.on_contenttype OWNER TO onp;

--
-- Name: on_document; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_document (
    id integer NOT NULL,
    folderitem_id integer NOT NULL,
    path character varying NOT NULL,
    filename character varying NOT NULL
);


ALTER TABLE public.on_document OWNER TO onp;

--
-- Name: on_element_module; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_element_module (
    id serial NOT NULL,
    element_id integer NOT NULL,
    module_id integer NOT NULL,
    seq integer NOT NULL,
    flags integer DEFAULT 1 NOT NULL,
    description character varying
);


ALTER TABLE public.on_element_module OWNER TO onp;

--
-- Name: on_field_type; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_field_type (
    field_type_id bigint NOT NULL,
    name character varying NOT NULL
);


ALTER TABLE public.on_field_type OWNER TO onp;

--
-- Name: on_field_type_seq; Type: SEQUENCE; Schema: public; Owner: onp
--

CREATE SEQUENCE on_field_type_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.on_field_type_seq OWNER TO onp;

--
-- Name: on_field_value; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_field_value (
    content_id bigint NOT NULL,
    field_type_id bigint NOT NULL,
    lang_id integer DEFAULT 0 NOT NULL,
    value character varying
);


ALTER TABLE public.on_field_value OWNER TO onp;

--
-- Name: on_form; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_form (
    id integer NOT NULL,
    impl_id integer NOT NULL,
    formclass character varying(256),
    description character varying(2048) NOT NULL,
    scope character varying(16) DEFAULT 'session'::character varying,
    actionclass character varying(256) NOT NULL,
    validate boolean DEFAULT false
);


ALTER TABLE public.on_form OWNER TO onp;

--
-- Name: on_form_forwards; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_form_forwards (
    id serial NOT NULL,
    form_id integer NOT NULL,
    "forward" character varying(256) NOT NULL,
    implid integer NOT NULL,
    redirect boolean DEFAULT false,
    description character varying(2048) NOT NULL
);


ALTER TABLE public.on_form_forwards OWNER TO onp;

--
-- Name: on_forum; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_forum (
    id serial NOT NULL,
    description character varying NOT NULL,
    created timestamp without time zone DEFAULT now() NOT NULL,
    created_by character varying NOT NULL,
    is_moderated_posting boolean NOT NULL,
    is_moderated_reply boolean NOT NULL,
    is_open boolean NOT NULL,
    article_template_id integer NOT NULL,
    article_presentation_id integer NOT NULL,
    reply_template_id integer NOT NULL,
    reply_presentation_id integer NOT NULL,
    list_limit integer NOT NULL
);


ALTER TABLE public.on_forum OWNER TO onp;

--
-- Name: on_forum_article; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_forum_article (
    forum_id integer NOT NULL,
    article_id integer NOT NULL
);


ALTER TABLE public.on_forum_article OWNER TO onp;

--
-- Name: on_group; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_group (
    id serial NOT NULL,
    p_id integer NOT NULL,
    groupname character varying NOT NULL
);


ALTER TABLE public.on_group OWNER TO onp;

--
-- Name: on_image; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_image (
    id integer NOT NULL,
    imgbagitem_id integer NOT NULL,
    path character varying NOT NULL,
    filename character varying NOT NULL,
    thm_url character varying,
    img_url character varying,
    description character varying
);


ALTER TABLE public.on_image OWNER TO onp;

--
-- Name: on_metadata_key; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_metadata_key (
    id serial NOT NULL,
    content_id integer NOT NULL,
    md_key character varying NOT NULL,
    md_name character varying NOT NULL,
    md_desc character varying NOT NULL,
    contenttype_id integer NOT NULL
);


ALTER TABLE public.on_metadata_key OWNER TO onp;

--
-- Name: on_metadata_value; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_metadata_value (
    id serial NOT NULL,
    md_key_id integer NOT NULL,
    md_value character varying NOT NULL,
    is_valid boolean DEFAULT false,
    format_id integer NOT NULL
);


ALTER TABLE public.on_metadata_value OWNER TO onp;

--
-- Name: on_mime_type; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_mime_type (
    id serial NOT NULL,
    extension character varying NOT NULL,
    mime_type character varying NOT NULL,
    description character varying NOT NULL,
    icon character varying
);


ALTER TABLE public.on_mime_type OWNER TO onp;

--
-- Name: on_module; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_module (
    id serial NOT NULL,
    description character varying NOT NULL,
    module_config character varying,
    metadata_helper character varying
);


ALTER TABLE public.on_module OWNER TO onp;

--
-- Name: on_module_format; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_module_format (
    id serial NOT NULL,
    module_id integer NOT NULL,
    contenttype_id integer NOT NULL,
    format_id integer NOT NULL,
    filename character varying NOT NULL
);


ALTER TABLE public.on_module_format OWNER TO onp;

--
-- Name: on_oem_header; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_oem_header (
    id serial NOT NULL,
    element_module_id integer NOT NULL,
    lang_id integer NOT NULL,
    header_text character varying
);


ALTER TABLE public.on_oem_header OWNER TO onp;

--
-- Name: on_oem_sm; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_oem_sm (
    id serial NOT NULL,
    element_module_id integer NOT NULL,
    format_id integer NOT NULL,
    surrounding_markup character varying
);


ALTER TABLE public.on_oem_sm OWNER TO onp;

--
-- Name: on_oem_static; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_oem_static (
    oem_id integer NOT NULL,
    new_id integer NOT NULL
);


ALTER TABLE public.on_oem_static OWNER TO onp;

--
-- Name: on_permission; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_permission (
    p_key character varying(15) NOT NULL,
    description character varying(256) NOT NULL
);


ALTER TABLE public.on_permission OWNER TO onp;

--
-- Name: on_status_current; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_status_current (
    id serial NOT NULL,
    p_id integer NOT NULL,
    content_id integer NOT NULL,
    contenttype_id integer NOT NULL,
    modified timestamp without time zone NOT NULL,
    modified_by character varying NOT NULL,
    s_key character varying(3) NOT NULL,
    is_current boolean
);


ALTER TABLE public.on_status_current OWNER TO onp;

--
-- Name: on_status_history; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_status_history (
    id serial NOT NULL,
    p_id integer NOT NULL,
    content_id integer NOT NULL,
    contenttype_id integer NOT NULL,
    modified timestamp without time zone NOT NULL,
    modified_by character varying NOT NULL,
    s_key character varying(3) NOT NULL,
    is_current boolean
);


ALTER TABLE public.on_status_history OWNER TO onp;

--
-- Name: on_status_value; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_status_value (
    id serial NOT NULL,
    s_key character varying(3) NOT NULL,
    value character varying(64)
);


ALTER TABLE public.on_status_value OWNER TO onp;

--
-- Name: on_template_def; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_template_def (
    id serial NOT NULL,
    on_template_def_key character varying NOT NULL,
    on_template_def_val character varying NOT NULL,
    on_template_def_element_count integer
);


ALTER TABLE public.on_template_def OWNER TO onp;

--
-- Name: on_template_elements; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_template_elements (
    id serial NOT NULL,
    impl_id integer NOT NULL,
    value character varying NOT NULL,
    element_key character varying NOT NULL
);


ALTER TABLE public.on_template_elements OWNER TO onp;

--
-- Name: on_template_impl; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_template_impl (
    id serial NOT NULL,
    def_id integer NOT NULL,
    segment_id integer NOT NULL,
    description character varying NOT NULL,
    flags integer DEFAULT 1 NOT NULL
);


ALTER TABLE public.on_template_impl OWNER TO onp;

--
-- Name: on_text; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_text (
    id serial NOT NULL,
    field_id integer NOT NULL,
    content_id integer,
    version integer DEFAULT 1,
    seq integer DEFAULT 1,
    lang_id integer NOT NULL,
    text_value text NOT NULL
);


ALTER TABLE public.on_text OWNER TO onp;

--
-- Name: on_text_key; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_text_key (
    id serial NOT NULL,
    text_key character varying NOT NULL
);


ALTER TABLE public.on_text_key OWNER TO onp;

--
-- Name: on_url; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_url (
    id integer NOT NULL,
    url character varying(2048) NOT NULL
);


ALTER TABLE public.on_url OWNER TO onp;

--
-- Name: on_user; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_user (
    id serial NOT NULL,
    username character varying NOT NULL,
    "password" character varying NOT NULL,
    props character varying
);


ALTER TABLE public.on_user OWNER TO onp;

--
-- Name: on_user_props; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE on_user_props (
    on_user_id integer NOT NULL,
    prop_key character varying NOT NULL,
    prop_value character varying NOT NULL
);


ALTER TABLE public.on_user_props OWNER TO onp;

--
-- Name: onp_document; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE onp_document (
    id serial NOT NULL,
    documentfolder_id integer NOT NULL,
    path character varying NOT NULL,
    filename character varying NOT NULL,
    title character varying,
    description character varying,
    flags integer,
    format character varying,
    size bigint NOT NULL,
    created timestamp without time zone DEFAULT now() NOT NULL,
    modified timestamp without time zone,
    created_by character varying NOT NULL,
    modified_by character varying
);


ALTER TABLE public.onp_document OWNER TO onp;

--
-- Name: onp_document_search_text; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE onp_document_search_text (
    id integer NOT NULL,
    search_text character varying NOT NULL
);


ALTER TABLE public.onp_document_search_text OWNER TO onp;

--
-- Name: onp_image; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE onp_image (
    id serial NOT NULL,
    imgbagitem_id integer NOT NULL,
    path character varying NOT NULL,
    filename character varying NOT NULL,
    title character varying,
    description character varying,
    source character varying,
    email character varying,
    copyright character varying,
    flags integer,
    orig_format character varying NOT NULL,
    format character varying NOT NULL,
    width integer NOT NULL,
    height integer NOT NULL,
    size bigint NOT NULL,
    transpose integer DEFAULT -1 NOT NULL,
    created timestamp without time zone DEFAULT now() NOT NULL,
    modified timestamp without time zone,
    created_by character varying NOT NULL,
    modified_by character varying,
    photographed timestamp without time zone DEFAULT now() NOT NULL,
    CONSTRAINT onp_image_height CHECK ((height <> 0)),
    CONSTRAINT onp_image_width CHECK ((width <> 0))
);


ALTER TABLE public.onp_image OWNER TO onp;

--
-- Name: onp_image_generated; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE onp_image_generated (
    id serial NOT NULL,
    orig_id integer NOT NULL,
    width integer NOT NULL,
    height integer NOT NULL,
    area integer NOT NULL,
    size bigint NOT NULL,
    orig_ratio numeric NOT NULL
);


ALTER TABLE public.onp_image_generated OWNER TO onp;

--
-- Name: onp_image_search_text; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE onp_image_search_text (
    id integer NOT NULL,
    search_text character varying NOT NULL
);


ALTER TABLE public.onp_image_search_text OWNER TO onp;

--
-- Name: onp_invalid_useragent; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE onp_invalid_useragent (
    id serial NOT NULL,
    useragent character varying NOT NULL
);


ALTER TABLE public.onp_invalid_useragent OWNER TO onp;

--
-- Name: onp_page_subtitle; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE onp_page_subtitle (
    id serial NOT NULL,
    page_id integer NOT NULL,
    lang_id integer NOT NULL,
    subtitle character varying NOT NULL
);


ALTER TABLE public.onp_page_subtitle OWNER TO onp;

--
-- Name: pending_user_request; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE pending_user_request (
    username character varying NOT NULL,
    "password" character varying NOT NULL,
    email character varying NOT NULL,
    userkey character varying NOT NULL,
    expires timestamp without time zone NOT NULL
);


ALTER TABLE public.pending_user_request OWNER TO onp;

SET default_with_oids = true;

--
-- Name: pg_ts_cfg; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE pg_ts_cfg (
    ts_name text NOT NULL,
    prs_name text NOT NULL,
    locale text
);


ALTER TABLE public.pg_ts_cfg OWNER TO onp;

--
-- Name: pg_ts_cfgmap; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE pg_ts_cfgmap (
    ts_name text NOT NULL,
    tok_alias text NOT NULL,
    dict_name text[]
);


ALTER TABLE public.pg_ts_cfgmap OWNER TO onp;

--
-- Name: pg_ts_dict; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE pg_ts_dict (
    dict_name text NOT NULL,
    dict_init regprocedure,
    dict_initoption text,
    dict_lexize regprocedure NOT NULL,
    dict_comment text
);


ALTER TABLE public.pg_ts_dict OWNER TO onp;

--
-- Name: pg_ts_parser; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE pg_ts_parser (
    prs_name text NOT NULL,
    prs_start regprocedure NOT NULL,
    prs_nexttoken regprocedure NOT NULL,
    prs_end regprocedure NOT NULL,
    prs_headline regprocedure NOT NULL,
    prs_lextype regprocedure NOT NULL,
    prs_comment text
);


ALTER TABLE public.pg_ts_parser OWNER TO onp;

SET default_with_oids = false;

--
-- Name: poll_main; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE poll_main (
    id serial NOT NULL,
    poll_name character varying NOT NULL,
    poll_created timestamp without time zone DEFAULT now() NOT NULL,
    poll_tod timestamp without time zone,
    is_active boolean NOT NULL,
    valid_from timestamp without time zone NOT NULL,
    valid_to timestamp without time zone NOT NULL,
    creator character varying NOT NULL
);


ALTER TABLE public.poll_main OWNER TO onp;

--
-- Name: poll_option; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE poll_option (
    poll_id integer NOT NULL,
    poll_value character varying NOT NULL
);


ALTER TABLE public.poll_option OWNER TO onp;

--
-- Name: poll_result; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE poll_result (
    poll_id integer NOT NULL,
    poll_value character varying NOT NULL,
    session_id character varying
);


ALTER TABLE public.poll_result OWNER TO onp;

--
-- Name: rfid; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE rfid (
    id character varying(20) NOT NULL,
    hotshot character varying(255)
);


ALTER TABLE public.rfid OWNER TO onp;

--
-- Name: rfidobservations; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE rfidobservations (
    observedtime bigint DEFAULT (0)::bigint NOT NULL,
    readerid bigint DEFAULT (0)::bigint NOT NULL,
    rfid bigint DEFAULT (0)::bigint NOT NULL
);


ALTER TABLE public.rfidobservations OWNER TO onp;

--
-- Name: roles; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE roles (
    role_name character varying(15) NOT NULL
);


ALTER TABLE public.roles OWNER TO onp;

--
-- Name: session; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE "session" (
    session_id character varying(256) NOT NULL,
    created timestamp without time zone DEFAULT now() NOT NULL,
    last_accessed timestamp without time zone NOT NULL,
    destroyed timestamp without time zone NOT NULL,
    username character varying,
    useragent character varying,
    remoteaddr character varying
);


ALTER TABLE public."session" OWNER TO onp;

--
-- Name: session_article; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE session_article (
    session_id character varying(256) NOT NULL,
    article_id integer NOT NULL,
    "timestamp" timestamp without time zone NOT NULL
);


ALTER TABLE public.session_article OWNER TO onp;

--
-- Name: session_page; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE session_page (
    session_id character varying(256) NOT NULL,
    impl_id integer NOT NULL,
    "timestamp" timestamp without time zone NOT NULL
);


ALTER TABLE public.session_page OWNER TO onp;

--
-- Name: session_stat_article_m_y_anon; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE session_stat_article_m_y_anon (
    lang_id integer,
    id integer,
    title character varying,
    num_hits bigint,
    "day" integer,
    "month" integer,
    "year" integer
);


ALTER TABLE public.session_stat_article_m_y_anon OWNER TO onp;

--
-- Name: session_stat_d_m_y; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE session_stat_d_m_y (
    "day" integer,
    "month" integer,
    "year" integer,
    num_hits_d_m_y bigint
);


ALTER TABLE public.session_stat_d_m_y OWNER TO onp;

--
-- Name: session_stat_d_m_y_anon; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE session_stat_d_m_y_anon (
    "day" integer,
    "month" integer,
    "year" integer,
    num_hits_d_m_y bigint
);


ALTER TABLE public.session_stat_d_m_y_anon OWNER TO onp;

--
-- Name: session_stat_d_m_y_user; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE session_stat_d_m_y_user (
    "day" integer,
    "month" integer,
    "year" integer,
    num_hits_d_m_y bigint
);


ALTER TABLE public.session_stat_d_m_y_user OWNER TO onp;

--
-- Name: session_stat_page_m_y_anon; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE session_stat_page_m_y_anon (
    descr character varying,
    num_hits bigint,
    "day" integer,
    "month" integer,
    "year" integer
);


ALTER TABLE public.session_stat_page_m_y_anon OWNER TO onp;

--
-- Name: session_stat_segment_m_y_anon; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE session_stat_segment_m_y_anon (
    "year" integer,
    "month" integer,
    num_hits bigint,
    impl_id integer,
    segment_id integer
);


ALTER TABLE public.session_stat_segment_m_y_anon OWNER TO onp;

--
-- Name: session_stat_w_y; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE session_stat_w_y (
    week integer,
    "year" integer,
    num_hits_w_y bigint
);


ALTER TABLE public.session_stat_w_y OWNER TO onp;

--
-- Name: session_stat_w_y_anon; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE session_stat_w_y_anon (
    week integer,
    "year" integer,
    num_hits_w_y bigint
);


ALTER TABLE public.session_stat_w_y_anon OWNER TO onp;

--
-- Name: session_stat_w_y_user; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE session_stat_w_y_user (
    week integer,
    "year" integer,
    num_hits_w_y bigint
);


ALTER TABLE public.session_stat_w_y_user OWNER TO onp;

--
-- Name: treeitem; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE treeitem (
    id serial NOT NULL,
    seq integer NOT NULL,
    p_id integer NOT NULL,
    contenttype_id integer NOT NULL,
    flags integer NOT NULL
);


ALTER TABLE public.treeitem OWNER TO onp;

--
-- Name: user_access; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE user_access (
    on_user_id character varying NOT NULL,
    access_type character varying NOT NULL,
    on_module_id integer NOT NULL
);


ALTER TABLE public.user_access OWNER TO onp;

--
-- Name: user_group; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE user_group (
    groupname character varying NOT NULL,
    username character varying NOT NULL
);


ALTER TABLE public.user_group OWNER TO onp;

--
-- Name: user_roles; Type: TABLE; Schema: public; Owner: onp; Tablespace: 
--

CREATE TABLE user_roles (
    segment_id integer NOT NULL,
    username character varying(15) NOT NULL,
    role_name character varying(15) NOT NULL
);


ALTER TABLE public.user_roles OWNER TO onp;

--
-- Name: code_code_key_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY code
    ADD CONSTRAINT code_code_key_key UNIQUE (code_key);


--
-- Name: code_group_description_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY code_group
    ADD CONSTRAINT code_group_description_key UNIQUE (description);


--
-- Name: code_group_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY code_group
    ADD CONSTRAINT code_group_pkey PRIMARY KEY (id);


--
-- Name: code_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY code
    ADD CONSTRAINT code_pkey PRIMARY KEY (id);


--
-- Name: group_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY group_roles
    ADD CONSTRAINT group_roles_pkey PRIMARY KEY (segment_id, groupname, role_name);


--
-- Name: on_access_segment_id_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_access
    ADD CONSTRAINT on_access_segment_id_key UNIQUE (segment_id, content_id, contenttype_id, role_name, permission_p_key);


--
-- Name: on_article_active_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_article_active
    ADD CONSTRAINT on_article_active_pkey PRIMARY KEY (id, lang_id, name);


--
-- Name: on_article_active_serial_id_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_article_active
    ADD CONSTRAINT on_article_active_serial_id_key UNIQUE (serial_id);


--
-- Name: on_article_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_article
    ADD CONSTRAINT on_article_pkey PRIMARY KEY (id);


--
-- Name: on_article_presentation_on_article_template_id_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_article_presentation
    ADD CONSTRAINT on_article_presentation_on_article_template_id_key UNIQUE (on_article_template_id, description);


--
-- Name: on_article_presentation_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_article_presentation
    ADD CONSTRAINT on_article_presentation_pkey PRIMARY KEY (id);


--
-- Name: on_article_reply_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_article_reply
    ADD CONSTRAINT on_article_reply_pkey PRIMARY KEY (article_id, reply_id);


--
-- Name: on_article_template_id_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_article_template
    ADD CONSTRAINT on_article_template_id_key UNIQUE (id);


--
-- Name: on_article_template_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_article_template
    ADD CONSTRAINT on_article_template_pkey PRIMARY KEY (description);


--
-- Name: on_article_template_values_id_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_article_template_values
    ADD CONSTRAINT on_article_template_values_id_key UNIQUE (id);


--
-- Name: on_article_template_values_on_article_template_id_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_article_template_values
    ADD CONSTRAINT on_article_template_values_on_article_template_id_key UNIQUE (on_article_template_id, name);


--
-- Name: on_article_template_values_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_article_template_values
    ADD CONSTRAINT on_article_template_values_pkey PRIMARY KEY (on_article_template_id, seq);


--
-- Name: on_article_title_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_article_title
    ADD CONSTRAINT on_article_title_pkey PRIMARY KEY (id, lang_id);


--
-- Name: on_content_classification_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_content_classification
    ADD CONSTRAINT on_content_classification_pkey PRIMARY KEY (id);


--
-- Name: on_content_field_contenttype_id_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_content_field
    ADD CONSTRAINT on_content_field_contenttype_id_key UNIQUE (contenttype_id, f_name);


--
-- Name: on_content_field_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_content_field
    ADD CONSTRAINT on_content_field_pkey PRIMARY KEY (id);


--
-- Name: on_content_field_type_description_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_content_field_type
    ADD CONSTRAINT on_content_field_type_description_key UNIQUE (description);


--
-- Name: on_content_field_type_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_content_field_type
    ADD CONSTRAINT on_content_field_type_pkey PRIMARY KEY (id);


--
-- Name: on_content_format_mapping_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_content_format_mapping
    ADD CONSTRAINT on_content_format_mapping_pkey PRIMARY KEY (id);


--
-- Name: on_content_metadata_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_content_metadata
    ADD CONSTRAINT on_content_metadata_pkey PRIMARY KEY (content_id, lang_id);


--
-- Name: on_content_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_content
    ADD CONSTRAINT on_content_pkey PRIMARY KEY (id);


--
-- Name: on_contentrelation_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_contentrelation
    ADD CONSTRAINT on_contentrelation_pkey PRIMARY KEY (relater, relater_contenttype_id, relatee, relatee_contenttype_id, reltype_id);


--
-- Name: on_contentrelationtype_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_contentrelationtype
    ADD CONSTRAINT on_contentrelationtype_pkey PRIMARY KEY (id);


--
-- Name: on_contenttype_description_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_contenttype
    ADD CONSTRAINT on_contenttype_description_key UNIQUE (description);


--
-- Name: on_contenttype_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_contenttype
    ADD CONSTRAINT on_contenttype_pkey PRIMARY KEY (id);


--
-- Name: on_document_path_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_document
    ADD CONSTRAINT on_document_path_key UNIQUE (path, filename);


--
-- Name: on_document_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_document
    ADD CONSTRAINT on_document_pkey PRIMARY KEY (id);


--
-- Name: on_element_module_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_element_module
    ADD CONSTRAINT on_element_module_pkey PRIMARY KEY (id);


--
-- Name: on_field_type_name_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_field_type
    ADD CONSTRAINT on_field_type_name_key UNIQUE (name);


--
-- Name: on_field_type_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_field_type
    ADD CONSTRAINT on_field_type_pkey PRIMARY KEY (field_type_id);


--
-- Name: on_field_value_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_field_value
    ADD CONSTRAINT on_field_value_pkey PRIMARY KEY (content_id, field_type_id, lang_id);


--
-- Name: on_form_forwards_form_id_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_form_forwards
    ADD CONSTRAINT on_form_forwards_form_id_key UNIQUE (form_id, "forward", implid);


--
-- Name: on_form_forwards_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_form_forwards
    ADD CONSTRAINT on_form_forwards_pkey PRIMARY KEY (id);


--
-- Name: on_form_impl_id_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_form
    ADD CONSTRAINT on_form_impl_id_key UNIQUE (impl_id);


--
-- Name: on_form_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_form
    ADD CONSTRAINT on_form_pkey PRIMARY KEY (id);


--
-- Name: on_forum_article_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_forum_article
    ADD CONSTRAINT on_forum_article_pkey PRIMARY KEY (article_id);


--
-- Name: on_forum_description_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_forum
    ADD CONSTRAINT on_forum_description_key UNIQUE (description);


--
-- Name: on_forum_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_forum
    ADD CONSTRAINT on_forum_pkey PRIMARY KEY (id);


--
-- Name: on_group_id_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_group
    ADD CONSTRAINT on_group_id_key UNIQUE (id);


--
-- Name: on_group_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_group
    ADD CONSTRAINT on_group_pkey PRIMARY KEY (groupname);


--
-- Name: on_image_img_url_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_image
    ADD CONSTRAINT on_image_img_url_key UNIQUE (img_url);


--
-- Name: on_image_path_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_image
    ADD CONSTRAINT on_image_path_key UNIQUE (path, filename);


--
-- Name: on_image_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_image
    ADD CONSTRAINT on_image_pkey PRIMARY KEY (id);


--
-- Name: on_image_thm_url_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_image
    ADD CONSTRAINT on_image_thm_url_key UNIQUE (thm_url);


--
-- Name: on_metadata_key_md_key_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_metadata_key
    ADD CONSTRAINT on_metadata_key_md_key_key UNIQUE (md_key, content_id, contenttype_id);


--
-- Name: on_metadata_key_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_metadata_key
    ADD CONSTRAINT on_metadata_key_pkey PRIMARY KEY (id);


--
-- Name: on_metadata_value_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_metadata_value
    ADD CONSTRAINT on_metadata_value_pkey PRIMARY KEY (id);


--
-- Name: on_mime_type_extension_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_mime_type
    ADD CONSTRAINT on_mime_type_extension_key UNIQUE (extension);


--
-- Name: on_mime_type_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_mime_type
    ADD CONSTRAINT on_mime_type_pkey PRIMARY KEY (id);


--
-- Name: on_module_description_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_module
    ADD CONSTRAINT on_module_description_key UNIQUE (description);


--
-- Name: on_module_format_format_id_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_module_format
    ADD CONSTRAINT on_module_format_format_id_key UNIQUE (format_id, module_id);


--
-- Name: on_module_format_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_module_format
    ADD CONSTRAINT on_module_format_pkey PRIMARY KEY (id);


--
-- Name: on_module_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_module
    ADD CONSTRAINT on_module_pkey PRIMARY KEY (id);


--
-- Name: on_oem_header_lang_id_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_oem_header
    ADD CONSTRAINT on_oem_header_lang_id_key UNIQUE (lang_id, element_module_id);


--
-- Name: on_oem_header_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_oem_header
    ADD CONSTRAINT on_oem_header_pkey PRIMARY KEY (id);


--
-- Name: on_oem_sm_format_id_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_oem_sm
    ADD CONSTRAINT on_oem_sm_format_id_key UNIQUE (format_id, element_module_id);


--
-- Name: on_oem_sm_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_oem_sm
    ADD CONSTRAINT on_oem_sm_pkey PRIMARY KEY (id);


--
-- Name: on_oem_static_oem_id_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_oem_static
    ADD CONSTRAINT on_oem_static_oem_id_key UNIQUE (oem_id, new_id);


--
-- Name: on_permission_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_permission
    ADD CONSTRAINT on_permission_pkey PRIMARY KEY (p_key);


--
-- Name: on_status_current_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_status_current
    ADD CONSTRAINT on_status_current_pkey PRIMARY KEY (id);


--
-- Name: on_status_history_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_status_history
    ADD CONSTRAINT on_status_history_pkey PRIMARY KEY (id);


--
-- Name: on_status_value_id_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_status_value
    ADD CONSTRAINT on_status_value_id_key UNIQUE (id);


--
-- Name: on_status_value_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_status_value
    ADD CONSTRAINT on_status_value_pkey PRIMARY KEY (s_key);


--
-- Name: on_template_def_on_template_def_key_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_template_def
    ADD CONSTRAINT on_template_def_on_template_def_key_key UNIQUE (on_template_def_key);


--
-- Name: on_template_def_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_template_def
    ADD CONSTRAINT on_template_def_pkey PRIMARY KEY (id);


--
-- Name: on_template_elements_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_template_elements
    ADD CONSTRAINT on_template_elements_pkey PRIMARY KEY (id);


--
-- Name: on_template_impl_description_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_template_impl
    ADD CONSTRAINT on_template_impl_description_key UNIQUE (description);


--
-- Name: on_template_impl_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_template_impl
    ADD CONSTRAINT on_template_impl_pkey PRIMARY KEY (id);


--
-- Name: on_text_field_id_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_text
    ADD CONSTRAINT on_text_field_id_key UNIQUE (field_id, content_id, version, seq, lang_id);


--
-- Name: on_text_key_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_text_key
    ADD CONSTRAINT on_text_key_pkey PRIMARY KEY (id);


--
-- Name: on_text_key_text_key_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_text_key
    ADD CONSTRAINT on_text_key_text_key_key UNIQUE (text_key);


--
-- Name: on_text_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_text
    ADD CONSTRAINT on_text_pkey PRIMARY KEY (id);


--
-- Name: on_url_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_url
    ADD CONSTRAINT on_url_pkey PRIMARY KEY (id);


--
-- Name: on_user_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_user
    ADD CONSTRAINT on_user_pkey PRIMARY KEY (id);


--
-- Name: on_user_props_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_user_props
    ADD CONSTRAINT on_user_props_pkey PRIMARY KEY (on_user_id, prop_key, prop_value);


--
-- Name: on_user_username_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_user
    ADD CONSTRAINT on_user_username_key UNIQUE (username);


--
-- Name: onp_document_path_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY onp_document
    ADD CONSTRAINT onp_document_path_key UNIQUE (path, filename);


--
-- Name: onp_document_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY onp_document
    ADD CONSTRAINT onp_document_pkey PRIMARY KEY (id);


--
-- Name: onp_document_search_text_id_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY onp_document_search_text
    ADD CONSTRAINT onp_document_search_text_id_key UNIQUE (id);


--
-- Name: onp_image_generated_orig_id_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY onp_image_generated
    ADD CONSTRAINT onp_image_generated_orig_id_key UNIQUE (orig_id, width, height);


--
-- Name: onp_image_generated_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY onp_image_generated
    ADD CONSTRAINT onp_image_generated_pkey PRIMARY KEY (id);


--
-- Name: onp_image_path_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY onp_image
    ADD CONSTRAINT onp_image_path_key UNIQUE (path, filename);


--
-- Name: onp_image_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY onp_image
    ADD CONSTRAINT onp_image_pkey PRIMARY KEY (id);


--
-- Name: onp_image_search_text_id_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY onp_image_search_text
    ADD CONSTRAINT onp_image_search_text_id_key UNIQUE (id);


--
-- Name: onp_invalid_useragent_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY onp_invalid_useragent
    ADD CONSTRAINT onp_invalid_useragent_pkey PRIMARY KEY (id);


--
-- Name: onp_invalid_useragent_useragent_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY onp_invalid_useragent
    ADD CONSTRAINT onp_invalid_useragent_useragent_key UNIQUE (useragent);


--
-- Name: onp_page_subtitle_page_id_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY onp_page_subtitle
    ADD CONSTRAINT onp_page_subtitle_page_id_key UNIQUE (page_id, lang_id);


--
-- Name: onp_page_subtitle_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY onp_page_subtitle
    ADD CONSTRAINT onp_page_subtitle_pkey PRIMARY KEY (id);


--
-- Name: pending_user_request_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY pending_user_request
    ADD CONSTRAINT pending_user_request_pkey PRIMARY KEY (username);


--
-- Name: pg_ts_cfg_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY pg_ts_cfg
    ADD CONSTRAINT pg_ts_cfg_pkey PRIMARY KEY (ts_name);


--
-- Name: pg_ts_cfgmap_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY pg_ts_cfgmap
    ADD CONSTRAINT pg_ts_cfgmap_pkey PRIMARY KEY (ts_name, tok_alias);


--
-- Name: pg_ts_dict_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY pg_ts_dict
    ADD CONSTRAINT pg_ts_dict_pkey PRIMARY KEY (dict_name);


--
-- Name: pg_ts_parser_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY pg_ts_parser
    ADD CONSTRAINT pg_ts_parser_pkey PRIMARY KEY (prs_name);


--
-- Name: poll_main_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY poll_main
    ADD CONSTRAINT poll_main_pkey PRIMARY KEY (id);


--
-- Name: poll_result_poll_id_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY poll_result
    ADD CONSTRAINT poll_result_poll_id_key UNIQUE (poll_id, session_id);


--
-- Name: roles_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (role_name);


--
-- Name: session_article_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY session_article
    ADD CONSTRAINT session_article_pkey PRIMARY KEY (session_id, article_id, "timestamp");


--
-- Name: session_page_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY session_page
    ADD CONSTRAINT session_page_pkey PRIMARY KEY (session_id, impl_id, "timestamp");


--
-- Name: session_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY "session"
    ADD CONSTRAINT session_pkey PRIMARY KEY (session_id);


--
-- Name: treeitem_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY treeitem
    ADD CONSTRAINT treeitem_pkey PRIMARY KEY (id);


--
-- Name: treeitem_seq_key; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY treeitem
    ADD CONSTRAINT treeitem_seq_key UNIQUE (seq, p_id);


--
-- Name: user_group_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY user_group
    ADD CONSTRAINT user_group_pkey PRIMARY KEY (groupname, username);


--
-- Name: user_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY user_roles
    ADD CONSTRAINT user_roles_pkey PRIMARY KEY (segment_id, username, role_name);


--
-- Name: username_unique; Type: CONSTRAINT; Schema: public; Owner: onp; Tablespace: 
--

ALTER TABLE ONLY on_user
    ADD CONSTRAINT username_unique UNIQUE (username);


--
-- Name: active_article_lang_id_idx; Type: INDEX; Schema: public; Owner: onp; Tablespace: 
--

CREATE INDEX active_article_lang_id_idx ON on_article_active USING btree (id, lang_id);


--
-- Name: key_id_contenttype_idx; Type: INDEX; Schema: public; Owner: onp; Tablespace: 
--

CREATE INDEX key_id_contenttype_idx ON on_metadata_key USING btree (md_key, id, contenttype_id);


--
-- Name: key_value_idx; Type: INDEX; Schema: public; Owner: onp; Tablespace: 
--

CREATE INDEX key_value_idx ON on_metadata_value USING btree (md_key_id, md_value);


--
-- Name: oem_static_new_id_idx; Type: INDEX; Schema: public; Owner: onp; Tablespace: 
--

CREATE INDEX oem_static_new_id_idx ON on_oem_static USING btree (new_id);


--
-- Name: on_forum_article_fid_idx; Type: INDEX; Schema: public; Owner: onp; Tablespace: 
--

CREATE INDEX on_forum_article_fid_idx ON on_forum_article USING btree (forum_id);


--
-- Name: on_user_prop_key_idx; Type: INDEX; Schema: public; Owner: onp; Tablespace: 
--

CREATE INDEX on_user_prop_key_idx ON on_user_props USING btree (on_user_id, prop_key);


--
-- Name: onarticle_created; Type: INDEX; Schema: public; Owner: onp; Tablespace: 
--

CREATE INDEX onarticle_created ON on_article USING btree (created);


--
-- Name: onarticle_template_idx; Type: INDEX; Schema: public; Owner: onp; Tablespace: 
--

CREATE INDEX onarticle_template_idx ON on_article USING btree (id, on_article_template_id);


--
-- Name: onarticletemplatevalues_idx; Type: INDEX; Schema: public; Owner: onp; Tablespace: 
--

CREATE INDEX onarticletemplatevalues_idx ON on_article_template_values USING btree (seq, on_article_template_id, field_id);


--
-- Name: oncontentf_type_id_idx; Type: INDEX; Schema: public; Owner: onp; Tablespace: 
--

CREATE INDEX oncontentf_type_id_idx ON on_content_field USING btree (id, f_type_id);


--
-- Name: oncontentfield_id_name_idx; Type: INDEX; Schema: public; Owner: onp; Tablespace: 
--

CREATE INDEX oncontentfield_id_name_idx ON on_content_field USING btree (id, f_name);


--
-- Name: onstatus_curr_idx; Type: INDEX; Schema: public; Owner: onp; Tablespace: 
--

CREATE INDEX onstatus_curr_idx ON on_status_current USING btree (content_id, contenttype_id);


--
-- Name: onstatus_curr_s_idx; Type: INDEX; Schema: public; Owner: onp; Tablespace: 
--

CREATE INDEX onstatus_curr_s_idx ON on_status_current USING btree (content_id, contenttype_id, s_key);


--
-- Name: ontext_idx; Type: INDEX; Schema: public; Owner: onp; Tablespace: 
--

CREATE INDEX ontext_idx ON on_text USING btree (field_id, seq, content_id, lang_id, version);


--
-- Name: ote_impl_id_element_key_idx; Type: INDEX; Schema: public; Owner: onp; Tablespace: 
--

CREATE UNIQUE INDEX ote_impl_id_element_key_idx ON on_template_elements USING btree (impl_id, element_key);


--
-- Name: add_page_read_access_t; Type: TRIGGER; Schema: public; Owner: onp
--

CREATE TRIGGER add_page_read_access_t
    AFTER INSERT ON on_template_impl
    FOR EACH ROW
    EXECUTE PROCEDURE add_page_read_access_tf();


--
-- Name: check_user_exists_t; Type: TRIGGER; Schema: public; Owner: onp
--

CREATE TRIGGER check_user_exists_t
    BEFORE INSERT ON pending_user_request
    FOR EACH ROW
    EXECUTE PROCEDURE check_user_exists_tf();


--
-- Name: update_document_search_text_t; Type: TRIGGER; Schema: public; Owner: onp
--

CREATE TRIGGER update_document_search_text_t
    AFTER INSERT OR UPDATE ON onp_document
    FOR EACH ROW
    EXECUTE PROCEDURE update_document_search_text_tf();


--
-- Name: update_image_search_text_t; Type: TRIGGER; Schema: public; Owner: onp
--

CREATE TRIGGER update_image_search_text_t
    AFTER INSERT OR UPDATE ON onp_image
    FOR EACH ROW
    EXECUTE PROCEDURE update_image_search_text_tf();


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_group
    ADD CONSTRAINT "$1" FOREIGN KEY (p_id) REFERENCES on_group(id) ON DELETE CASCADE;


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY user_group
    ADD CONSTRAINT "$1" FOREIGN KEY (groupname) REFERENCES on_group(groupname) ON DELETE CASCADE;


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY code
    ADD CONSTRAINT "$1" FOREIGN KEY (code_group_id) REFERENCES code_group(id);


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_content_field
    ADD CONSTRAINT "$1" FOREIGN KEY (contenttype_id) REFERENCES on_contenttype(id);


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_content
    ADD CONSTRAINT "$1" FOREIGN KEY (contenttype_id) REFERENCES on_contenttype(id);


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_text
    ADD CONSTRAINT "$1" FOREIGN KEY (field_id) REFERENCES on_content_field(id);


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_contentrelation
    ADD CONSTRAINT "$1" FOREIGN KEY (relater_contenttype_id) REFERENCES on_contenttype(id);


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_article_template_values
    ADD CONSTRAINT "$1" FOREIGN KEY (on_article_template_id) REFERENCES on_article_template(id) ON DELETE CASCADE;


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_article_presentation
    ADD CONSTRAINT "$1" FOREIGN KEY (on_article_template_id) REFERENCES on_article_template(id) ON DELETE CASCADE;


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_article
    ADD CONSTRAINT "$1" FOREIGN KEY (id) REFERENCES on_content(id) ON DELETE CASCADE;


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_article_active
    ADD CONSTRAINT "$1" FOREIGN KEY (id) REFERENCES on_content(id) ON DELETE CASCADE;


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY treeitem
    ADD CONSTRAINT "$1" FOREIGN KEY (contenttype_id) REFERENCES on_contenttype(id);


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY user_roles
    ADD CONSTRAINT "$1" FOREIGN KEY (segment_id) REFERENCES treeitem(id);


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY group_roles
    ADD CONSTRAINT "$1" FOREIGN KEY (segment_id) REFERENCES treeitem(id);


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_document
    ADD CONSTRAINT "$1" FOREIGN KEY (id) REFERENCES on_content(id) ON DELETE CASCADE;


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_module_format
    ADD CONSTRAINT "$1" FOREIGN KEY (module_id) REFERENCES on_module(id);


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_content_format_mapping
    ADD CONSTRAINT "$1" FOREIGN KEY (contenttype_id) REFERENCES on_contenttype(id);


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY user_access
    ADD CONSTRAINT "$1" FOREIGN KEY (access_type) REFERENCES code(id);


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_template_impl
    ADD CONSTRAINT "$1" FOREIGN KEY (def_id) REFERENCES on_template_def(id);


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_template_elements
    ADD CONSTRAINT "$1" FOREIGN KEY (impl_id) REFERENCES on_template_impl(id) ON DELETE CASCADE;


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_image
    ADD CONSTRAINT "$1" FOREIGN KEY (id) REFERENCES on_content(id) ON DELETE CASCADE;


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_form
    ADD CONSTRAINT "$1" FOREIGN KEY (id) REFERENCES on_content(id) ON DELETE CASCADE;


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_form_forwards
    ADD CONSTRAINT "$1" FOREIGN KEY (form_id) REFERENCES on_form(id) ON DELETE CASCADE;


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_access
    ADD CONSTRAINT "$1" FOREIGN KEY (segment_id) REFERENCES treeitem(id);


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_status_current
    ADD CONSTRAINT "$1" FOREIGN KEY (contenttype_id) REFERENCES on_contenttype(id);


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_status_history
    ADD CONSTRAINT "$1" FOREIGN KEY (contenttype_id) REFERENCES on_contenttype(id);


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_element_module
    ADD CONSTRAINT "$1" FOREIGN KEY (element_id) REFERENCES on_template_elements(id) ON DELETE CASCADE;


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_metadata_key
    ADD CONSTRAINT "$1" FOREIGN KEY (contenttype_id) REFERENCES on_contenttype(id);


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_metadata_value
    ADD CONSTRAINT "$1" FOREIGN KEY (md_key_id) REFERENCES on_metadata_key(id) ON DELETE CASCADE;


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_url
    ADD CONSTRAINT "$1" FOREIGN KEY (id) REFERENCES on_content(id);


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_content_metadata
    ADD CONSTRAINT "$1" FOREIGN KEY (content_id) REFERENCES on_content(id) ON DELETE CASCADE;


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_field_value
    ADD CONSTRAINT "$1" FOREIGN KEY (content_id) REFERENCES on_content(id) ON DELETE CASCADE;


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_article_title
    ADD CONSTRAINT "$1" FOREIGN KEY (id) REFERENCES on_content(id) ON DELETE CASCADE;


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY poll_option
    ADD CONSTRAINT "$1" FOREIGN KEY (poll_id) REFERENCES poll_main(id);


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY poll_result
    ADD CONSTRAINT "$1" FOREIGN KEY (poll_id) REFERENCES poll_main(id);


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_forum
    ADD CONSTRAINT "$1" FOREIGN KEY (article_template_id) REFERENCES on_article_template(id);


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_forum_article
    ADD CONSTRAINT "$1" FOREIGN KEY (forum_id) REFERENCES on_forum(id);


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_article_reply
    ADD CONSTRAINT "$1" FOREIGN KEY (reply_id) REFERENCES on_article(id) ON DELETE CASCADE;


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_oem_sm
    ADD CONSTRAINT "$1" FOREIGN KEY (element_module_id) REFERENCES on_element_module(id) ON DELETE CASCADE;


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_oem_header
    ADD CONSTRAINT "$1" FOREIGN KEY (element_module_id) REFERENCES on_element_module(id) ON DELETE CASCADE;


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_oem_static
    ADD CONSTRAINT "$1" FOREIGN KEY (oem_id) REFERENCES on_element_module(id) ON DELETE CASCADE;


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_user_props
    ADD CONSTRAINT "$1" FOREIGN KEY (on_user_id) REFERENCES on_user(id) ON DELETE CASCADE;


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY onp_image
    ADD CONSTRAINT "$1" FOREIGN KEY (imgbagitem_id) REFERENCES treeitem(id);


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY onp_image_generated
    ADD CONSTRAINT "$1" FOREIGN KEY (orig_id) REFERENCES onp_image(id) ON DELETE CASCADE;


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY onp_image_search_text
    ADD CONSTRAINT "$1" FOREIGN KEY (id) REFERENCES onp_image(id) ON DELETE CASCADE;


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY onp_page_subtitle
    ADD CONSTRAINT "$1" FOREIGN KEY (page_id) REFERENCES on_template_impl(id) ON DELETE CASCADE;


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY onp_document
    ADD CONSTRAINT "$1" FOREIGN KEY (documentfolder_id) REFERENCES treeitem(id);


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY onp_document_search_text
    ADD CONSTRAINT "$1" FOREIGN KEY (id) REFERENCES onp_document(id) ON DELETE CASCADE;


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY session_page
    ADD CONSTRAINT "$1" FOREIGN KEY (session_id) REFERENCES "session"(session_id);


--
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY session_article
    ADD CONSTRAINT "$1" FOREIGN KEY (session_id) REFERENCES "session"(session_id);


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY user_group
    ADD CONSTRAINT "$2" FOREIGN KEY (username) REFERENCES on_user(username) ON DELETE CASCADE;


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_content_field
    ADD CONSTRAINT "$2" FOREIGN KEY (f_type_id) REFERENCES on_content_field_type(id);


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_content
    ADD CONSTRAINT "$2" FOREIGN KEY (content_classification_id) REFERENCES on_content_classification(id);


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_text
    ADD CONSTRAINT "$2" FOREIGN KEY (lang_id) REFERENCES code(id);


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_contentrelation
    ADD CONSTRAINT "$2" FOREIGN KEY (relatee_contenttype_id) REFERENCES on_contenttype(id);


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_article_template_values
    ADD CONSTRAINT "$2" FOREIGN KEY (field_id) REFERENCES on_content_field(id);


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_article_presentation
    ADD CONSTRAINT "$2" FOREIGN KEY (format_id) REFERENCES code(id);


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_article
    ADD CONSTRAINT "$2" FOREIGN KEY (on_article_template_id) REFERENCES on_article_template(id);


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_article_active
    ADD CONSTRAINT "$2" FOREIGN KEY (lang_id) REFERENCES code(id);


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_document
    ADD CONSTRAINT "$2" FOREIGN KEY (folderitem_id) REFERENCES treeitem(id);


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_module_format
    ADD CONSTRAINT "$2" FOREIGN KEY (contenttype_id) REFERENCES on_contenttype(id);


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_content_format_mapping
    ADD CONSTRAINT "$2" FOREIGN KEY (format_id) REFERENCES code(id);


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY user_access
    ADD CONSTRAINT "$2" FOREIGN KEY (on_module_id) REFERENCES on_module(id);


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_template_impl
    ADD CONSTRAINT "$2" FOREIGN KEY (segment_id) REFERENCES treeitem(id);


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_image
    ADD CONSTRAINT "$2" FOREIGN KEY (imgbagitem_id) REFERENCES treeitem(id);


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_form
    ADD CONSTRAINT "$2" FOREIGN KEY (impl_id) REFERENCES on_template_impl(id) ON DELETE CASCADE;


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_access
    ADD CONSTRAINT "$2" FOREIGN KEY (contenttype_id) REFERENCES on_contenttype(id);


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_status_current
    ADD CONSTRAINT "$2" FOREIGN KEY (s_key) REFERENCES on_status_value(s_key);


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_status_history
    ADD CONSTRAINT "$2" FOREIGN KEY (s_key) REFERENCES on_status_value(s_key);


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_element_module
    ADD CONSTRAINT "$2" FOREIGN KEY (module_id) REFERENCES on_module(id);


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_metadata_value
    ADD CONSTRAINT "$2" FOREIGN KEY (format_id) REFERENCES code(id);


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_field_value
    ADD CONSTRAINT "$2" FOREIGN KEY (field_type_id) REFERENCES on_field_type(field_type_id);


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_article_title
    ADD CONSTRAINT "$2" FOREIGN KEY (lang_id) REFERENCES code(id);


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_forum
    ADD CONSTRAINT "$2" FOREIGN KEY (article_presentation_id) REFERENCES on_article_presentation(id);


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_forum_article
    ADD CONSTRAINT "$2" FOREIGN KEY (article_id) REFERENCES on_article(id) ON DELETE CASCADE;


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_article_reply
    ADD CONSTRAINT "$2" FOREIGN KEY (article_id) REFERENCES on_article(id) ON DELETE CASCADE;


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_oem_sm
    ADD CONSTRAINT "$2" FOREIGN KEY (format_id) REFERENCES code(id);


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_oem_header
    ADD CONSTRAINT "$2" FOREIGN KEY (lang_id) REFERENCES code(id);


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_oem_static
    ADD CONSTRAINT "$2" FOREIGN KEY (new_id) REFERENCES on_element_module(id) ON DELETE CASCADE;


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY user_roles
    ADD CONSTRAINT "$2" FOREIGN KEY (username) REFERENCES on_user(username) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY group_roles
    ADD CONSTRAINT "$2" FOREIGN KEY (groupname) REFERENCES on_group(groupname) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: $2; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY onp_page_subtitle
    ADD CONSTRAINT "$2" FOREIGN KEY (lang_id) REFERENCES code(id);


--
-- Name: $3; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_contentrelation
    ADD CONSTRAINT "$3" FOREIGN KEY (reltype_id) REFERENCES on_contentrelationtype(id);


--
-- Name: $3; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY user_roles
    ADD CONSTRAINT "$3" FOREIGN KEY (role_name) REFERENCES roles(role_name);


--
-- Name: $3; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY group_roles
    ADD CONSTRAINT "$3" FOREIGN KEY (role_name) REFERENCES roles(role_name);


--
-- Name: $3; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_module_format
    ADD CONSTRAINT "$3" FOREIGN KEY (format_id) REFERENCES code(id);


--
-- Name: $3; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_access
    ADD CONSTRAINT "$3" FOREIGN KEY (permission_p_key) REFERENCES on_permission(p_key);


--
-- Name: $3; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_forum
    ADD CONSTRAINT "$3" FOREIGN KEY (reply_template_id) REFERENCES on_article_template(id);


--
-- Name: $3; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_article_reply
    ADD CONSTRAINT "$3" FOREIGN KEY (replyto_id) REFERENCES on_article(id) ON DELETE CASCADE;


--
-- Name: $4; Type: FK CONSTRAINT; Schema: public; Owner: onp
--

ALTER TABLE ONLY on_forum
    ADD CONSTRAINT "$4" FOREIGN KEY (reply_presentation_id) REFERENCES on_article_presentation(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: onp
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM onp;
GRANT ALL ON SCHEMA public TO onp;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

