CREATE VIEW jb_active_articles AS
(SELECT a.id, a.valid_from, a.valid_to, a.created, a.created_by, CASE WHEN a.modified is null THEN a.created ELSE a.modified END AS modified, a.modified_by, a.aknowledged_by, a.is_ready, a.current_version, a.on_article_template_id, a.priority
FROM on_article a, on_status_current s
WHERE (a.valid_from IS NULL OR now() > a.valid_from) AND (a.valid_to IS NULL OR a.valid_to > now()) AND a.id = s.content_id AND s.s_key::text = 'ACT'::text);
