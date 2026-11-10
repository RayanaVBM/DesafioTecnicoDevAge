CREATE TABLE funcionario (
    rowid bigint auto_increment PRIMARY KEY, 
    nm_funcionario VARCHAR(255)
);

CREATE TABLE agenda (
    rowid bigint auto_increment PRIMARY KEY, 
    nm_agenda VARCHAR(255),
    periodo_disponivel VARCHAR(20) 
);

CREATE TABLE compromisso (
    rowid bigint auto_increment PRIMARY KEY, 
    id_funcionario bigint, 
    id_agenda bigint, 
    dt_compromisso DATE, 
    hr_compromisso TIME,
    FOREIGN KEY(id_funcionario) REFERENCES funcionario(rowid) ON DELETE CASCADE,
    FOREIGN KEY(id_agenda) REFERENCES agenda(rowid) ON DELETE RESTRICT
);

INSERT INTO funcionario (nm_funcionario) VALUES ('João'), ('Maria'), ('José'), ('Joana');