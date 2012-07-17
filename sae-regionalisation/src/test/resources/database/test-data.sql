INSERT INTO criteres (id, lucene, traite) VALUES (0, 'lucene0', false);
INSERT INTO criteres (id, lucene, traite) VALUES (1, 'lucene1', true);
INSERT INTO criteres (id, lucene, traite) VALUES (2, 'lucene2', false);
INSERT INTO criteres (id, lucene, traite) VALUES (3, 'lucene3', false);
INSERT INTO criteres (id, lucene, traite) VALUES (4, 'lucene4', false);

INSERT INTO metadonnees (id, id_critere, nne, nne_flag, npe, npe_flag, den, den_flag, cv2, cv2_flag, scv, scv_flag, nci, nci_flag, nce, nce_flag, srt, srt_flag, psi, psi_flag, nst, nst_flag, nre, nre_flag, nic, nic_flag, dre, dre_flag, apr, apr_flag, atr, atr_flag, cop, cop_flag, cog, cog_flag, sac, sac_flag, nbp, nbp_flag) VALUES (0, 0, '148032541101648', false, '123854', true, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2007-07-12', true, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'CER69', false, 4, true);
INSERT INTO metadonnees (id, id_critere, nne, nne_flag, npe, npe_flag, den, den_flag, cv2, cv2_flag, scv, scv_flag, nci, nci_flag, nce, nce_flag, srt, srt_flag, psi, psi_flag, nst, nst_flag, nre, nre_flag, nic, nic_flag, dre, dre_flag, apr, apr_flag, atr, atr_flag, cop, cop_flag, cog, cog_flag, sac, sac_flag, nbp, nbp_flag) VALUES (1, 2, '148032541101649', false, '123855', true, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2007-07-13', true, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'UR42', false, 5, true);
INSERT INTO metadonnees (id, id_critere, nne, nne_flag, npe, npe_flag, den, den_flag, cv2, cv2_flag, scv, scv_flag, nci, nci_flag, nce, nce_flag, srt, srt_flag, psi, psi_flag, nst, nst_flag, nre, nre_flag, nic, nic_flag, dre, dre_flag, apr, apr_flag, atr, atr_flag, cop, cop_flag, cog, cog_flag, sac, sac_flag, nbp, nbp_flag) VALUES (2, 3, '148032541101650', true, '123856', true, 'COUTURIER GINETTE', true, '4', true, '11', true, '719900', true, '30148032541101600', true, '12345678912345', true, '4914736610005', true, '000050221', true, '20080798', true, '57377', true, '2007-07-14', true, 'ADELAIDE', true, 'ATTESTATIONS', true, 'UR750', true, 'UR42', true, 'CER69', true, 6, true);

INSERT INTO trace_maj (id, id_critere, id_document, nom_metadata, ancienne_valeur, nouvelle_valeur) VALUES (0, 0, '59fba7b7-63bd-4543-8638-11e620c127df', 'nne', '123854', '123856');
INSERT INTO trace_maj (id, id_critere, id_document, nom_metadata, ancienne_valeur, nouvelle_valeur) VALUES (1, 0, '48276db0-63c1-4201-a46f-e06b608bd624', 'nne', '123852', '123856');
INSERT INTO trace_maj (id, id_critere, id_document, nom_metadata, ancienne_valeur, nouvelle_valeur) VALUES (2, 1, '2577a9e1-5592-4883-bebf-623ac52b3731', 'dre', '2007-05-12', '2009-12-31');

INSERT INTO trace_rec (id, id_critere, nbre_doc) VALUES (0, 0, 2);
INSERT INTO trace_rec (id, id_critere, nbre_doc) VALUES (1, 1, 1);