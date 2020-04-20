USE dfce;

CREATE TABLE dictionary_terms (
  dictionary_id TEXT,
  dictionary_term TEXT,
  PRIMARY KEY ((dictionary_id), dictionary_term)
)
WITH COMPACTION = { 'class': 'LeveledCompactionStrategy', 'sstable_size_in_mb' : 256 };

DROP TABLE dictionary;

CREATE TABLE dictionary (
  dictionary_id TEXT,
  dictionary_type TEXT,
  dictionary_options MAP<TEXT, TEXT>,
  PRIMARY KEY (dictionary_id)
)
WITH COMPACTION = { 'class': 'LeveledCompactionStrategy', 'sstable_size_in_mb' : 256 };

CREATE TABLE dynamic_dictionary_terms (
  dictionary_id TEXT,
  source TEXT,
  bucket VARINT,
  normalized_value_prefix TEXT,
  normalized_value TEXT,
  terms SET<TEXT>,
  PRIMARY KEY ((dictionary_id, source, bucket, normalized_value_prefix), normalized_value)
)
WITH COMPACTION = { 'class': 'LeveledCompactionStrategy', 'sstable_size_in_mb' : 256 };

CREATE TABLE overlays (
  name TEXT,
  chunk_id INT,
  digest TEXT static,
  digest_algorithme TEXT static,
  size BIGINT static,
  archivage_date TIMESTAMP static,
  chunk_size INT static,
  chunk_nb BIGINT static,
  chunk_value BLOB,
  PRIMARY KEY ((name), chunk_id)
)
WITH COMPACTION = { 'class': 'LeveledCompactionStrategy', 'sstable_size_in_mb' : 256 };

INSERT INTO dictionary (dictionary_id, dictionary_type, dictionary_options) VALUES ('DYNAMIC_DICTIONARY', 'DYNAMIC_DICTIONARY_TYPE', {});  

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_document_overlay_name','DEFAULT','STRING','SM_DOCUMENT_OVERLAY_NAME','INDEXABLE_DOCUMENT','SYSTEM', null);

INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_document_overlay_name','INDEXABLE_DOCUMENT','SYSTEM', 'STRING', 'SM_DOCUMENT_OVERLAY_NAME', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_user_time_zone','DEFAULT','STRING','SM_USER_TIME_ZONE','USER','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_user_locale','DEFAULT','STRING','SM_USER_LOCALE','USER','SYSTEM', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_user_organization_unit','DEFAULT','STRING','SM_USER_ORGANIZATION_UNIT','USER','SYSTEM', null);

INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_user_organization_unit','USER','SYSTEM', 'STRING', 'SM_USER_ORGANIZATION_UNIT', null);

INSERT INTO metadata (name, translation_code, data_type, default_name, domain, scope, translation)
values ('sm_document_overlay_name','DEFAULT','STRING','SM_DOCUMENT_OVERLAY_NAME','INDEXABLE_DOCUMENT','SYSTEM', null);

INSERT INTO metadata_translation (code, metadata_name, domain, scope, data_type, default_name, translation)
values ('DEFAULT', 'sm_document_overlay_name','INDEXABLE_DOCUMENT','SYSTEM', 'STRING', 'SM_DOCUMENT_OVERLAY_NAME', null);

