To create a clean PostgreSQL database run this from the root of the project:

  initdb -D ../cibus-postgres

This will create a postgresql "cluster" outside the main project structure.

To run the database, run this:

  postgres -d 5 -D ../cibus-postgres

To create the "cibus" user and load the initial schema run this:

  createuser -D -R -S cibus
  createdb -E UTF8 -O cibus cibus
  psql -U cibus cibus -f cibus-domain/src/main/resources/sql/register.sql

If you already have a "cibus" database set up, you can clean out the
database by running clean.sql instead of register.sql.
