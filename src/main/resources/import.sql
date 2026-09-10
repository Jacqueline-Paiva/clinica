-- PACIENTES

INSERT INTO paciente (nome, cpf, telefone) VALUES ('Maria Silva', '111.111.111-11', '(63) 99999-1111');
INSERT INTO paciente (nome, cpf, telefone) VALUES ('Joao Santos', '222.222.222-22', '(63) 99999-2222');
INSERT INTO paciente (nome, cpf, telefone) VALUES ('Ana Costa', '333.333.333-33', '(63) 99999-3333');
INSERT INTO paciente (nome, cpf, telefone) VALUES ('Pedro Oliveira', '444.444.444-44', '(63) 99999-4444');
INSERT INTO paciente (nome, cpf, telefone) VALUES ('Mariana Souza', '555.555.555-55', '(63) 99999-5555');
INSERT INTO paciente (nome, cpf, telefone) VALUES ('Lucas Ferreira', '666.666.666-66', '(63) 99999-6666');
INSERT INTO paciente (nome, cpf, telefone) VALUES ('Carla Rodrigues', '777.777.777-77', '(63) 99999-7777');
INSERT INTO paciente (nome, cpf, telefone) VALUES ('Rafael Martins', '888.888.888-88', '(63) 99999-8888');
INSERT INTO paciente (nome, cpf, telefone) VALUES ('Juliana Alves', '999.999.999-99', '(63) 99999-9999');
INSERT INTO paciente (nome, cpf, telefone) VALUES ('Fernando Lima', '101.101.101-10', '(63) 98888-1010');
INSERT INTO paciente (nome, cpf, telefone) VALUES ('Beatriz Ribeiro', '202.202.202-20', '(63) 98888-2020');
INSERT INTO paciente (nome, cpf, telefone) VALUES ('Gabriel Gomes', '303.303.303-30', '(63) 98888-3030');
INSERT INTO paciente (nome, cpf, telefone) VALUES ('Camila Rocha', '404.404.404-40', '(63) 98888-4040');
INSERT INTO paciente (nome, cpf, telefone) VALUES ('Bruno Carvalho', '505.505.505-50', '(63) 98888-5050');
INSERT INTO paciente (nome, cpf, telefone) VALUES ('Larissa Almeida', '606.606.606-60', '(63) 98888-6060');

-- MEDICOS

INSERT INTO medico (nome, crm) VALUES ('Carlos Oliveira', 'CRM-TO 1234');
INSERT INTO medico (nome, crm) VALUES ('Ana Pereira', 'CRM-TO 5678');
INSERT INTO medico (nome, crm) VALUES ('Ricardo Mendes', 'CRM-TO 2345');
INSERT INTO medico (nome, crm) VALUES ('Fernanda Castro', 'CRM-TO 3456');
INSERT INTO medico (nome, crm) VALUES ('Marcelo Ribeiro', 'CRM-TO 4567');
INSERT INTO medico (nome, crm) VALUES ('Patricia Gomes', 'CRM-TO 6789');
INSERT INTO medico (nome, crm) VALUES ('Eduardo Martins', 'CRM-TO 7890');
INSERT INTO medico (nome, crm) VALUES ('Renata Alves', 'CRM-TO 8901');

-- CONSULTAS

INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-01 08:00:00', 150.00, 'Consulta de rotina', 1, 1);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-01 09:30:00', 180.00, 'Primeira consulta', 2, 2);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-01 14:00:00', 200.00, 'Avaliacao clinica', 3, 3);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-02 08:30:00', 150.00, 'Retorno', 1, 1);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-02 10:00:00', 220.00, 'Consulta inicial', 4, 4);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-02 15:30:00', 180.00, 'Acompanhamento', 5, 2);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-03 08:00:00', 200.00, 'Consulta de rotina', 6, 3);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-03 09:00:00', 150.00, 'Retorno', 7, 1);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-03 11:30:00', 250.00, 'Avaliacao especializada', 8, 5);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-03 16:00:00', 180.00, 'Primeira consulta', 9, 4);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-04 08:30:00', 150.00, 'Consulta de rotina', 10, 2);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-04 10:30:00', 200.00, 'Acompanhamento', 2, 3);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-04 14:30:00', 180.00, 'Retorno', 3, 4);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-05 09:00:00', 220.00, 'Avaliacao clinica', 4, 5);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-05 11:00:00', 150.00, 'Consulta de rotina', 5, 1);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-08 08:00:00', 180.00, 'Retorno', 6, 2);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-08 09:30:00', 200.00, 'Primeira consulta', 7, 3);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-08 13:30:00', 250.00, 'Avaliacao especializada', 8, 5);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-09 08:30:00', 150.00, 'Acompanhamento', 9, 1);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-09 10:00:00', 180.00, 'Consulta de rotina', 10, 4);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-10 09:00:00', 200.00, 'Retorno', 11, 3);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-10 14:00:00', 220.00, 'Primeira consulta', 12, 5);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-11 08:00:00', 150.00, 'Consulta de rotina', 1, 2);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-11 10:30:00', 180.00, 'Acompanhamento', 4, 3);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-11 15:00:00', 200.00, 'Retorno', 7, 4);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-12 09:00:00', 250.00, 'Avaliacao especializada', 10, 5);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-12 11:00:00', 150.00, 'Consulta de rotina', 11, 1);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-09-15 08:30:00', 180.00, 'Retorno', 12, 2);