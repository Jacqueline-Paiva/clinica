package br.edu.ifto.clinica.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.edu.ifto.clinica.model.Agenda;
import br.edu.ifto.clinica.model.Agendamento;
import br.edu.ifto.clinica.model.Consulta;
import br.edu.ifto.clinica.model.Especialidade;
import br.edu.ifto.clinica.model.Medico;
import br.edu.ifto.clinica.model.Paciente;
import br.edu.ifto.clinica.model.Perfil;
import br.edu.ifto.clinica.model.StatusAgenda;
import br.edu.ifto.clinica.model.StatusAgendamento;
import br.edu.ifto.clinica.model.TipoAgendamento;
import br.edu.ifto.clinica.model.Usuario;
import br.edu.ifto.clinica.repository.AgendaRepository;
import br.edu.ifto.clinica.repository.AgendamentoRepository;
import br.edu.ifto.clinica.repository.ConsultaRepository;
import br.edu.ifto.clinica.repository.EspecialidadeRepository;
import br.edu.ifto.clinica.repository.MedicoRepository;
import br.edu.ifto.clinica.repository.PacienteRepository;
import br.edu.ifto.clinica.repository.PerfilRepository;
import br.edu.ifto.clinica.repository.UsuarioRepository;

@Component
public class DadosIniciais implements CommandLineRunner {

    private final PerfilRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;
    private final EspecialidadeRepository especialidadeRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final AgendaRepository agendaRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final ConsultaRepository consultaRepository;
    private final PasswordEncoder passwordEncoder;

    public DadosIniciais(
            PerfilRepository perfilRepository,
            UsuarioRepository usuarioRepository,
            EspecialidadeRepository especialidadeRepository,
            MedicoRepository medicoRepository,
            PacienteRepository pacienteRepository,
            AgendaRepository agendaRepository,
            AgendamentoRepository agendamentoRepository,
            ConsultaRepository consultaRepository,
            PasswordEncoder passwordEncoder) {

        this.perfilRepository = perfilRepository;
        this.usuarioRepository = usuarioRepository;
        this.especialidadeRepository = especialidadeRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.agendaRepository = agendaRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.consultaRepository = consultaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        /*
         * PERFIS
         */

        Perfil perfilPaciente = new Perfil("PACIENTE");
        Perfil perfilMedico = new Perfil("MEDICO");
        Perfil perfilSecretaria = new Perfil("SECRETARIA");
        Perfil perfilAdmin = new Perfil("ADMIN");

        perfilRepository.save(perfilPaciente);
        perfilRepository.save(perfilMedico);
        perfilRepository.save(perfilSecretaria);
        perfilRepository.save(perfilAdmin);

        /*
         * ESPECIALIDADES
         */

        Especialidade cardiologia =
                new Especialidade("Cardiologia");

        Especialidade dermatologia =
                new Especialidade("Dermatologia");

        Especialidade oftalmologia =
                new Especialidade("Oftalmologia");

        Especialidade ortopedia =
                new Especialidade("Ortopedia");

        Especialidade pediatria =
                new Especialidade("Pediatria");

        especialidadeRepository.save(cardiologia);
        especialidadeRepository.save(dermatologia);
        especialidadeRepository.save(oftalmologia);
        especialidadeRepository.save(ortopedia);
        especialidadeRepository.save(pediatria);

        /*
         * ADMINISTRADOR
         */

        Usuario admin = criarUsuario(
                "admin",
                perfilAdmin
        );

        usuarioRepository.save(admin);

        /*
         * SECRETARIA
         */

        Usuario secretaria = criarUsuario(
                "secretaria",
                perfilSecretaria
        );

        usuarioRepository.save(secretaria);

        /*
         * MÉDICOS
         */

        Medico medico1 = new Medico();

        medico1.setNome("Ana Martins");
        medico1.setCpf("111.111.111-11");
        medico1.setEmail("ana@clinica.com");
        medico1.setTelefone("(63) 99999-1111");
        medico1.setCrm("CRM-TO 1001");
        medico1.setEspecialidade(cardiologia);
        medico1.setUsuario(
                criarUsuario("medico1", perfilMedico)
        );

        medicoRepository.save(medico1);


        Medico medico2 = new Medico();

        medico2.setNome("Carlos Oliveira");
        medico2.setCpf("222.222.222-22");
        medico2.setEmail("carlos@clinica.com");
        medico2.setTelefone("(63) 99999-2222");
        medico2.setCrm("CRM-TO 1002");
        medico2.setEspecialidade(oftalmologia);
        medico2.setUsuario(
                criarUsuario("medico2", perfilMedico)
        );

        medicoRepository.save(medico2);


        Medico medico3 = new Medico();

        medico3.setNome("Mariana Souza");
        medico3.setCpf("333.333.333-33");
        medico3.setEmail("mariana@clinica.com");
        medico3.setTelefone("(63) 99999-3333");
        medico3.setCrm("CRM-TO 1003");
        medico3.setEspecialidade(dermatologia);
        medico3.setUsuario(
                criarUsuario("medico3", perfilMedico)
        );

        medicoRepository.save(medico3);

        /*
         * PACIENTES
         */

        Paciente paciente1 = criarPaciente(
                "João Silva",
                "444.444.444-44",
                "joao@email.com",
                "(63) 99999-4444",
                "paciente1",
                perfilPaciente
        );

        pacienteRepository.save(paciente1);


        Paciente paciente2 = criarPaciente(
                "Maria Santos",
                "555.555.555-55",
                "maria@email.com",
                "(63) 99999-5555",
                "paciente2",
                perfilPaciente
        );

        pacienteRepository.save(paciente2);


        Paciente paciente3 = criarPaciente(
                "Pedro Almeida",
                "666.666.666-66",
                "pedro@email.com",
                "(63) 99999-6666",
                "paciente3",
                perfilPaciente
        );

        pacienteRepository.save(paciente3);


        Paciente paciente4 = criarPaciente(
                "Juliana Costa",
                "777.777.777-77",
                "juliana@email.com",
                "(63) 99999-7777",
                "paciente4",
                perfilPaciente
        );

        pacienteRepository.save(paciente4);


        Paciente paciente5 = criarPaciente(
                "Lucas Pereira",
                "888.888.888-88",
                "lucas@email.com",
                "(63) 99999-8888",
                "paciente5",
                perfilPaciente
        );

        pacienteRepository.save(paciente5);

        /*
         * DATAS FUTURAS
         *
         * São relativas à data em que o programa for executado.
         * Assim, a massa de teste não "vence".
         */

        LocalDate amanha =
                LocalDate.now().plusDays(1);

        LocalDate depoisDeAmanha =
                LocalDate.now().plusDays(2);

        LocalDate proximaSemana =
                LocalDate.now().plusDays(7);

        /*
         * AGENDA - HORÁRIOS DISPONÍVEIS
         */

        Agenda agenda1 = criarAgenda(
                medico1,
                amanha,
                LocalTime.of(8, 0),
                StatusAgenda.DISPONIVEL
        );

        agendaRepository.save(agenda1);


        Agenda agenda2 = criarAgenda(
                medico1,
                amanha,
                LocalTime.of(9, 0),
                StatusAgenda.DISPONIVEL
        );

        agendaRepository.save(agenda2);


        Agenda agenda3 = criarAgenda(
                medico2,
                depoisDeAmanha,
                LocalTime.of(14, 0),
                StatusAgenda.DISPONIVEL
        );

        agendaRepository.save(agenda3);


        Agenda agenda4 = criarAgenda(
                medico3,
                proximaSemana,
                LocalTime.of(10, 0),
                StatusAgenda.DISPONIVEL
        );

        agendaRepository.save(agenda4);

        /*
         * AGENDAMENTO ATIVO
         *
         * Maria possui uma CONSULTA agendada com Ana.
         */

        Agenda agendaAgendada = criarAgenda(
                medico1,
                depoisDeAmanha,
                LocalTime.of(10, 0),
                StatusAgenda.AGENDADO
        );

        agendaRepository.save(agendaAgendada);

        Agendamento agendamentoAtivo =
                criarAgendamento(
                        agendaAgendada,
                        paciente2,
                        TipoAgendamento.CONSULTA,
                        StatusAgendamento.AGENDADO
                );

        agendamentoRepository.save(agendamentoAtivo);

        /*
         * RETORNO AGENDADO
         *
         * Pedro possui um RETORNO com Carlos.
         */

        Agenda agendaRetorno = criarAgenda(
                medico2,
                proximaSemana,
                LocalTime.of(15, 0),
                StatusAgenda.AGENDADO
        );

        agendaRepository.save(agendaRetorno);

        Agendamento retorno =
                criarAgendamento(
                        agendaRetorno,
                        paciente3,
                        TipoAgendamento.RETORNO,
                        StatusAgendamento.AGENDADO
                );

        agendamentoRepository.save(retorno);

        /*
         * AGENDAMENTO CANCELADO
         *
         * O registro permanece no histórico.
         * Como foi cancelado, o horário volta a ficar DISPONÍVEL.
         */

        Agenda agendaCancelada = criarAgenda(
                medico3,
                proximaSemana,
                LocalTime.of(11, 0),
                StatusAgenda.DISPONIVEL
        );

        agendaRepository.save(agendaCancelada);

        Agendamento cancelado =
                criarAgendamento(
                        agendaCancelada,
                        paciente4,
                        TipoAgendamento.CONSULTA,
                        StatusAgendamento.CANCELADO
                );

        agendamentoRepository.save(cancelado);

        /*
         * CONSULTA REALIZADA
         *
         * João já teve presença confirmada.
         * Por isso existe Agendamento REALIZADO + Consulta.
         */

        LocalDate dataRealizada =
                LocalDate.now();

        LocalTime horarioRealizado =
                LocalTime.of(8, 30);

        Agenda agendaRealizada = criarAgenda(
                medico1,
                dataRealizada,
                horarioRealizado,
                StatusAgenda.AGENDADO
        );

        agendaRepository.save(agendaRealizada);

        Agendamento realizado =
                criarAgendamento(
                        agendaRealizada,
                        paciente1,
                        TipoAgendamento.CONSULTA,
                        StatusAgendamento.REALIZADO
                );

        agendamentoRepository.save(realizado);

        Consulta consulta = new Consulta();

        consulta.setData(
                LocalDateTime.of(
                        dataRealizada,
                        horarioRealizado
                )
        );

        consulta.setPaciente(paciente1);
        consulta.setMedico(medico1);
        consulta.setAgendamento(realizado);
        consulta.setValor(250.00);
        consulta.setObservacao(
                "Paciente atendido. Consulta de rotina."
        );

        consultaRepository.save(consulta);
    }

    /*
     * MÉTODOS AUXILIARES
     */

    private Usuario criarUsuario(
            String nomeUsuario,
            Perfil perfil) {

        Usuario usuario = new Usuario();

        usuario.setUsuario(nomeUsuario);

        usuario.setSenha(
                passwordEncoder.encode("123456")
        );

        usuario.getPerfis().add(perfil);

        return usuario;
    }

    private Paciente criarPaciente(
            String nome,
            String cpf,
            String email,
            String telefone,
            String login,
            Perfil perfilPaciente) {

        Paciente paciente = new Paciente();

        paciente.setNome(nome);
        paciente.setCpf(cpf);
        paciente.setEmail(email);
        paciente.setTelefone(telefone);

        paciente.setUsuario(
                criarUsuario(
                        login,
                        perfilPaciente
                )
        );

        return paciente;
    }

    private Agenda criarAgenda(
            Medico medico,
            LocalDate data,
            LocalTime horario,
            StatusAgenda status) {

        Agenda agenda = new Agenda();

        agenda.setMedico(medico);
        agenda.setData(data);
        agenda.setHorario(horario);
        agenda.setStatus(status);

        return agenda;
    }

    private Agendamento criarAgendamento(
            Agenda agenda,
            Paciente paciente,
            TipoAgendamento tipo,
            StatusAgendamento status) {

        Agendamento agendamento =
                new Agendamento();

        agendamento.setAgenda(agenda);
        agendamento.setPaciente(paciente);
        agendamento.setTipo(tipo);
        agendamento.setStatus(status);

        return agendamento;
    }
}