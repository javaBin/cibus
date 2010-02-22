rsync --delete -d -v www4.java.no:/pool0/global/data/*.dump src/test/resources/

psql -U onp onp < src/test/resources/onp.ddl

psql -U onp onp -c "delete from on_status_current";
psql -U onp onp -c "delete from on_status_value";
psql -U onp onp -c "delete from on_article";
psql -U onp onp -c "delete from on_article_template";
psql -U onp onp -c "delete from on_contentrelation";
psql -U onp onp -c "delete from on_contentrelationtype";
psql -U onp onp -c "delete from on_article_active";
psql -U onp onp -c "delete from on_content";
psql -U onp onp -c "delete from on_contenttype";
psql -U onp onp -c "delete from code";
psql -U onp onp -c "delete from code_group";

pg_restore -U onp -d onp -c -F c src/test/resources/code_group.dump
pg_restore -U onp -d onp -c -F c src/test/resources/code.dump
pg_restore -U onp -d onp -c -F c src/test/resources/on_contenttype.dump
pg_restore -U onp -d onp -c -F c src/test/resources/on_content.dump
pg_restore -U onp -d onp -c -F c src/test/resources/on_article_active.dump
pg_restore -U onp -d onp -c -F c src/test/resources/on_contentrelationtype.dump
pg_restore -U onp -d onp -c -F c src/test/resources/on_contentrelation.dump
pg_restore -U onp -d onp -c -F c src/test/resources/on_article_template.dump
pg_restore -U onp -d onp -c -F c src/test/resources/on_article.dump
pg_restore -U onp -d onp -c -F c src/test/resources/on_status_value.dump
pg_restore -U onp -d onp -c -F c src/test/resources/on_status_current.dump
psql -U onp onp -c "vacuum full analyze";
