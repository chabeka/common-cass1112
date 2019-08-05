
CREATE TABLE tracedestinatairecql (
    codeevt text PRIMARY KEY,
    destinataires map<text, frozen<list<text>>>
); 
