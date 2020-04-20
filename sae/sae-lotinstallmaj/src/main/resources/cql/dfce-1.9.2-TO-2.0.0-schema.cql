USE dfce;

alter table life_cycle_step_history add pattern TEXT ;

CREATE TABLE doc_text (
  doc_uuid UUID,
  full_text TEXT,
  PRIMARY KEY (doc_uuid)
);

alter table base add full_text BOOLEAN;
alter table base add es_index TEXT;

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_file_uuid','DEFAULT','STRING','SM_FILE_UUID','INDEXABLE_DOCUMENT','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_version','DEFAULT','STRING','SM_VERSION','INDEXABLE_DOCUMENT','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_final_date','DEFAULT','DATETIME','SM_FINAL_DATE','INDEXABLE_DOCUMENT','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_title','DEFAULT','STRING','SM_TITLE','INDEXABLE_DOCUMENT','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_digest','DEFAULT','STRING','SM_DIGEST','INDEXABLE_DOCUMENT','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_digest_algorithm','DEFAULT','STRING','SM_DIGEST_ALGORITHM','INDEXABLE_DOCUMENT','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_filename','DEFAULT','STRING','SM_FILENAME','INDEXABLE_DOCUMENT','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_extension','DEFAULT','STRING','SM_EXTENSION','INDEXABLE_DOCUMENT','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_virtual','DEFAULT','BOOLEAN','SM_VIRTUAL','INDEXABLE_DOCUMENT','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_size','DEFAULT','LONG','SM_SIZE','INDEXABLE_DOCUMENT','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_start_page','DEFAULT','INTEGER','SM_START_PAGE','INDEXABLE_DOCUMENT','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_end_page','DEFAULT','INTEGER','SM_END_PAGE','INDEXABLE_DOCUMENT','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_document_type','DEFAULT','STRING','SM_DOCUMENT_TYPE','INDEXABLE_DOCUMENT','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_life_cycle_reference_date','DEFAULT','DATETIME','SM_LIFE_CYCLE_REFERENCE_DATE','INDEXABLE_DOCUMENT','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_disposal_date','DEFAULT','DATETIME','SM_DISPOSAL_DATE','INDEXABLE_DOCUMENT','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_repository_name','DEFAULT','STRING','SM_REPOSITORY_NAME','INDEXABLE_DOCUMENT','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_key_reference_uuid','DEFAULT','UUID','SM_KEY_REFERENCE_UUID','INDEXABLE_DOCUMENT','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_encrypted_key_uuid','DEFAULT','UUID','SM_ENCRYPTED_KEY_UUID','INDEXABLE_DOCUMENT','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_user_time_zone','DEFAULT','UUID','SM_USER_TIME_ZONE','USER','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_user_locale','DEFAULT','UUID','SM_USER_LOCALE','USER','SYSTEM', null);


INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_user_time_zone','USER','SYSTEM', 'STRING', 'SM_USER_TIME_ZONE', null);

INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_user_locale','USER','SYSTEM', 'STRING', 'SM_USER_LOCALE', null);

/*If other translation code than DEFAULT, execute following statement replacing DEFAULT by other code*/
DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_file_uuid' AND domain='INDEXABLE' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_file_uuid','INDEXABLE_DOCUMENT','SYSTEM', 'STRING', 'SM_FILE_UUID', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_version' AND domain='INDEXABLE' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_version','INDEXABLE_DOCUMENT','SYSTEM', 'STRING', 'SM_VERSION', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_final_date' AND domain='INDEXABLE' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_final_date','INDEXABLE_DOCUMENT','SYSTEM', 'DATETIME', 'SM_FINAL_DATE', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_title' AND domain='INDEXABLE' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_title','INDEXABLE_DOCUMENT','SYSTEM', 'STRING', 'SM_TITLE', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_digest' AND domain='INDEXABLE' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_digest','INDEXABLE_DOCUMENT','SYSTEM', 'STRING', 'SM_DIGEST', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_digest_algorithm' AND domain='INDEXABLE' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_digest_algorithm','INDEXABLE_DOCUMENT','SYSTEM', 'STRING', 'SM_DIGEST_ALGORITHM', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_filename' AND domain='INDEXABLE' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_filename','INDEXABLE_DOCUMENT','SYSTEM', 'STRING', 'SM_FILENAME', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_extension' AND domain='INDEXABLE' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_extension','INDEXABLE_DOCUMENT','SYSTEM', 'STRING', 'SM_EXTENSION', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_virtual' AND domain='INDEXABLE' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_virtual','INDEXABLE_DOCUMENT','SYSTEM', 'BOOLEAN', 'SM_VIRTUAL', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_size' AND domain='INDEXABLE' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_size','INDEXABLE_DOCUMENT','SYSTEM', 'LONG', 'SM_SIZE', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_start_page' AND domain='INDEXABLE' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_start_page','INDEXABLE_DOCUMENT','SYSTEM', 'INTEGER', 'SM_START_PAGE', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_end_page' AND domain='INDEXABLE' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_end_page','INDEXABLE_DOCUMENT','SYSTEM', 'INTEGER', 'SM_END_PAGE', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_document_type' AND domain='INDEXABLE' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_document_type','INDEXABLE_DOCUMENT','SYSTEM', 'STRING', 'SM_DOCUMENT_TYPE', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_life_cycle_reference_date' AND domain='INDEXABLE' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_life_cycle_reference_date','INDEXABLE_DOCUMENT','SYSTEM', 'DATETIME', 'SM_LIFE_CYCLE_REFERENCE_DATE', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_disposal_date' AND domain='INDEXABLE' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_disposal_date','INDEXABLE_DOCUMENT','SYSTEM', 'DATETIME', 'SM_DISPOSAL_DATE', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_repository_name' AND domain='INDEXABLE' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_repository_name','INDEXABLE_DOCUMENT','SYSTEM', 'STRING', 'SM_REPOSITORY_NAME', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_key_reference_uuid' AND domain='INDEXABLE' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_key_reference_uuid','INDEXABLE_DOCUMENT','SYSTEM', 'UUID', 'SM_KEY_REFERENCE_UUID', null);

DELETE FROM metadata_translation
WHERE code='DEFAULT' AND metadata_name='sm_encrypted_key_uuid' AND domain='INDEXABLE' AND scope='SYSTEM';
INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_encrypted_key_uuid','INDEXABLE_DOCUMENT','SYSTEM', 'UUID', 'SM_ENCRYPTED_KEY_UUID', null);
