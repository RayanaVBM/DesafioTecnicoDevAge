CREATE TABLE funcionario (
    rowid bigint auto_increment PRIMARY KEY, 
    nm_funcionario VARCHAR(255) NOT NULL
);

CREATE TABLE agenda (
    rowid bigint auto_increment PRIMARY KEY, 
    nm_agenda VARCHAR(255) NOT NULL,
    periodo_disponivel VARCHAR(20) NOT NULL
);

CREATE TABLE compromisso (
    rowid bigint auto_increment PRIMARY KEY, 
    id_funcionario bigint NOT NULL, 
    id_agenda bigint NOT NULL, 
    dt_compromisso DATE NOT NULL, 
    hr_compromisso TIME NOT NULL,
    FOREIGN KEY(id_funcionario) REFERENCES funcionario(rowid) ON DELETE CASCADE,
    FOREIGN KEY(id_agenda) REFERENCES agenda(rowid) ON DELETE RESTRICT
);

INSERT INTO funcionario (nm_funcionario) VALUES ('João'), ('Maria'), ('José'), ('Joana'), ('Lucas'), ('Marcia'), ('Cristian'), ('Antonio');

INSERT INTO agenda (nm_agenda, periodo_disponivel) VALUES 
('Agenda Manhã', 'Manhã'),
('Agenda Tarde', 'Tarde'),
('Agenda Geral', 'Ambos');

INSERT INTO compromisso (id_funcionario, id_agenda, dt_compromisso, hr_compromisso) VALUES 
(1, 1, '2026-11-16', '09:00:00'),
(2, 2, '2026-11-16', '14:30:00'),
(3, 3, '2026-11-17', '10:00:00'),
(4, 3, '2026-11-17', '16:00:00');
