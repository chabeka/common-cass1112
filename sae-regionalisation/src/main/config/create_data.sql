--
-- PostgreSQL database dump
--

-- Dumped from database version 9.0.4
-- Dumped by pg_dump version 9.0.4
-- Started on 2012-07-17 13:33:09

SET statement_timeout = 0;
SET client_encoding = 'SQL_ASCII';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET search_path = public, pg_catalog;

--
-- TOC entry 1819 (class 0 OID 0)
-- Dependencies: 1511
-- Name: CRITERES_ID_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('"CRITERES_ID_seq"', 5, true);


--
-- TOC entry 1820 (class 0 OID 0)
-- Dependencies: 1513
-- Name: METADONNEES_ID_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('"METADONNEES_ID_seq"', 1, false);


--
-- TOC entry 1821 (class 0 OID 0)
-- Dependencies: 1515
-- Name: TRACE_MAJ_ID_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('"TRACE_MAJ_ID_seq"', 1, false);


--
-- TOC entry 1822 (class 0 OID 0)
-- Dependencies: 1517
-- Name: TRACE_REC_ID_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('"TRACE_REC_ID_seq"', 1, false);


--
-- TOC entry 1813 (class 0 OID 36518)
-- Dependencies: 1512
-- Data for Name: criteres; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO criteres (id, lucene, traite) VALUES (0, 'lucene0', false);
INSERT INTO criteres (id, lucene, traite) VALUES (1, 'lucene1', true);
INSERT INTO criteres (id, lucene, traite) VALUES (2, 'lucene2', false);
INSERT INTO criteres (id, lucene, traite) VALUES (3, 'lucene3', false);
INSERT INTO criteres (id, lucene, traite) VALUES (4, 'lucene4', false);


--
-- TOC entry 1814 (class 0 OID 36530)
-- Dependencies: 1514 1813
-- Data for Name: metadonnees; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO metadonnees (id, id_critere, nne, nne_flag, npe, npe_flag, den, den_flag, cv2, cv2_flag, scv, scv_flag, nci, nci_flag, nce, nce_flag, srt, srt_flag, psi, psi_flag, nst, nst_flag, nre, nre_flag, nic, nic_flag, dre, dre_flag, apr, apr_flag, atr, atr_flag, cop, cop_flag, cog, cog_flag, sac, sac_flag, nbp, nbp_flag) VALUES (0, 0, '148032541101648', false, '123854', true, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2007-07-12', true, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'CER69', false, 4, true);
INSERT INTO metadonnees (id, id_critere, nne, nne_flag, npe, npe_flag, den, den_flag, cv2, cv2_flag, scv, scv_flag, nci, nci_flag, nce, nce_flag, srt, srt_flag, psi, psi_flag, nst, nst_flag, nre, nre_flag, nic, nic_flag, dre, dre_flag, apr, apr_flag, atr, atr_flag, cop, cop_flag, cog, cog_flag, sac, sac_flag, nbp, nbp_flag) VALUES (1, 2, '148032541101649', false, '123855', true, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2007-07-13', true, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'UR42', false, 5, true);
INSERT INTO metadonnees (id, id_critere, nne, nne_flag, npe, npe_flag, den, den_flag, cv2, cv2_flag, scv, scv_flag, nci, nci_flag, nce, nce_flag, srt, srt_flag, psi, psi_flag, nst, nst_flag, nre, nre_flag, nic, nic_flag, dre, dre_flag, apr, apr_flag, atr, atr_flag, cop, cop_flag, cog, cog_flag, sac, sac_flag, nbp, nbp_flag) VALUES (2, 3, '148032541101650', true, '123856', true, 'COUTURIER GINETTE', true, '4', true, '11', true, '719900', true, '30148032541101600', true, '12345678912345', true, '4914736610005', true, '000050221', true, '20080798', true, '57377', true, '2007-07-14', true, 'ADELAIDE', true, 'ATTESTATIONS', true, 'UR750', true, 'UR42', true, 'CER69', true, 6, true);


--
-- TOC entry 1815 (class 0 OID 36556)
-- Dependencies: 1516 1813
-- Data for Name: trace_maj; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 1816 (class 0 OID 36569)
-- Dependencies: 1518 1813
-- Data for Name: trace_rec; Type: TABLE DATA; Schema: public; Owner: postgres
--



-- Completed on 2012-07-17 13:33:10

--
-- PostgreSQL database dump complete
--

