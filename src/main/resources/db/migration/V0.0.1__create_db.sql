CREATE TABLE Pessoa
(
  id varchar(20) NOT NULL,
  dataCriacao timestamp NOT NULL,
  nome varchar(255) NOT NULL,
  tipoPessoa varchar(50) DEFAULT 'FISICA',
  CONSTRAINT pessoa_pkey PRIMARY KEY (id)
);
