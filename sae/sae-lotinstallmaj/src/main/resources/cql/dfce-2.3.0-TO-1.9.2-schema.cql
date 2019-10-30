USE dfce;

/* 2.3.1 TO 2.1.0 */

USE dfce;

alter table action_by_day drop errors;
alter table action_by_day drop retry_count;
alter table action_by_day drop last_retry_date;
alter table action_by_day drop creation_date;

DROP TABLE suggestion;

DROP TABLE suggestor;

/* 2.1.0 TO 2.0.0 */

DROP TABLE dictionary_terms;

DROP TABLE dictionary;

CREATE TABLE dictionary (
  metadata_name TEXT,
  base_id TEXT,
  terms SET<TEXT>,
  PRIMARY KEY ((metadata_name, base_id))
)
WITH COMPACTION = { 'class': 'LeveledCompactionStrategy', 'sstable_size_in_mb' : 256 };

DROP TABLE dynamic_dictionary_terms;

DROP TABLE overlays;

DELETE FROM metadata
WHERE name='sm_document_overlay_name';

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_document_overlay_name' AND domain='INDEXABLE_DOCUMENT' AND scope='SYSTEM';

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_user_time_zone','DEFAULT','UUID','SM_USER_TIME_ZONE','USER','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_user_locale','DEFAULT','UUID','SM_USER_LOCALE','USER','SYSTEM', null);

DELETE FROM metadata
WHERE name='sm_user_organization_unit';

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_user_organization_unit' AND domain='USER' AND scope='SYSTEM';

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_document_overlay_name' AND domain='INDEXABLE_DOCUMENT' AND scope='SYSTEM';

/* 2.0.0 TO 1.9.2 */

alter table life_cycle_step_history drop pattern;

DROP TABLE doc_text;

alter table base drop full_text;
alter table base drop es_index;

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_file_uuid','DEFAULT','STRING','SM_FILE_UUID','INDEXABLE','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_version','DEFAULT','STRING','SM_VERSION','INDEXABLE','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_final_date','DEFAULT','DATETIME','SM_FINAL_DATE','INDEXABLE','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_title','DEFAULT','STRING','SM_TITLE','INDEXABLE','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_digest','DEFAULT','STRING','SM_DIGEST','INDEXABLE','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_digest_algorithm','DEFAULT','STRING','SM_DIGEST_ALGORITHM','INDEXABLE','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_filename','DEFAULT','STRING','SM_FILENAME','INDEXABLE','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_extension','DEFAULT','STRING','SM_EXTENSION','INDEXABLE','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_virtual','DEFAULT','STRING','SM_VIRTUAL','INDEXABLE','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_size','DEFAULT','LONG','SM_SIZE','INDEXABLE','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_start_page','DEFAULT','INTEGER','SM_START_PAGE','INDEXABLE','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_end_page','DEFAULT','INTEGER','SM_END_PAGE','INDEXABLE','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_document_type','DEFAULT','STRING','SM_DOCUMENT_TYPE','INDEXABLE','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_life_cycle_reference_date','DEFAULT','DATETIME','SM_LIFE_CYCLE_REFERENCE_DATE','INDEXABLE','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_disposal_date','DEFAULT','DATETIME','SM_DISPOSAL_DATE','INDEXABLE','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_repository_name','DEFAULT','STRING','SM_REPOSITORY_NAME','INDEXABLE','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_key_reference_uuid','DEFAULT','UUID','SM_KEY_REFERENCE_UUID','INDEXABLE','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_encrypted_key_uuid','DEFAULT','UUID','SM_ENCRYPTED_KEY_UUID','INDEXABLE','SYSTEM', null);

DELETE FROM metadata
WHERE name='sm_user_time_zone';

DELETE FROM metadata
WHERE name='sm_user_locale';

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_user_time_zone' AND domain='USER' AND scope='SYSTEM';

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_user_locale' AND domain='USER' AND scope='SYSTEM';

/*If other translation code than DEFAULT, execute following statement replacing DEFAULT by other code*/
DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_file_uuid' AND domain='INDEXABLE_DOCUMENT' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_file_uuid','INDEXABLE','SYSTEM', 'STRING', 'SM_FILE_UUID', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_version' AND domain='INDEXABLE_DOCUMENT' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_version','INDEXABLE','SYSTEM', 'STRING', 'SM_VERSION', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_final_date' AND domain='INDEXABLE_DOCUMENT' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_final_date','INDEXABLE','SYSTEM', 'DATETIME', 'SM_FINAL_DATE', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_title' AND domain='INDEXABLE_DOCUMENT' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_title','INDEXABLE','SYSTEM', 'STRING', 'SM_TITLE', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_digest' AND domain='INDEXABLE_DOCUMENT' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_digest','INDEXABLE','SYSTEM', 'STRING', 'SM_DIGEST', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_digest_algorithm' AND domain='INDEXABLE_DOCUMENT' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_digest_algorithm','INDEXABLE','SYSTEM', 'STRING', 'SM_DIGEST_ALGORITHM', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_filename' AND domain='INDEXABLE_DOCUMENT' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_filename','INDEXABLE','SYSTEM', 'STRING', 'SM_FILENAME', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_extension' AND domain='INDEXABLE_DOCUMENT' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_extension','INDEXABLE','SYSTEM', 'STRING', 'SM_EXTENSION', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_virtual' AND domain='INDEXABLE_DOCUMENT' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_virtual','INDEXABLE','SYSTEM', 'STRING', 'SM_VIRTUAL', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_size' AND domain='INDEXABLE_DOCUMENT' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_size','INDEXABLE','SYSTEM', 'LONG', 'SM_SIZE', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_start_page' AND domain='INDEXABLE_DOCUMENT' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_start_page','INDEXABLE','SYSTEM', 'INTEGER', 'SM_START_PAGE', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_end_page' AND domain='INDEXABLE_DOCUMENT' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_end_page','INDEXABLE','SYSTEM', 'INTEGER', 'SM_END_PAGE', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_document_type' AND domain='INDEXABLE_DOCUMENT' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_document_type','INDEXABLE','SYSTEM', 'STRING', 'SM_DOCUMENT_TYPE', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_life_cycle_reference_date' AND domain='INDEXABLE_DOCUMENT' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_life_cycle_reference_date','INDEXABLE','SYSTEM', 'DATETIME', 'SM_LIFE_CYCLE_REFERENCE_DATE', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_disposal_date' AND domain='INDEXABLE_DOCUMENT' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_disposal_date','INDEXABLE','SYSTEM', 'DATETIME', 'SM_DISPOSAL_DATE', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_repository_name' AND domain='INDEXABLE_DOCUMENT' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_repository_name','INDEXABLE','SYSTEM', 'STRING', 'SM_REPOSITORY_NAME', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_key_reference_uuid' AND domain='INDEXABLE_DOCUMENT' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_key_reference_uuid','INDEXABLE','SYSTEM', 'UUID', 'SM_KEY_REFERENCE_UUID', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_encrypted_key_uuid' AND domain='INDEXABLE_DOCUMENT' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_encrypted_key_uuid','INDEXABLE','SYSTEM', 'UUID', 'SM_ENCRYPTED_KEY_UUID', null);

