package br.edu.ifto.clinica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth

                .requestMatchers(
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/login",
                    "/cadastro"
                ).permitAll()

                // Exclusões
                .requestMatchers(
                    "/medicos/excluir/**",
                    "/pacientes/excluir/**",
                    "/consultas/excluir/**"
                ).hasRole("ADMIN")

                // Médicos
                .requestMatchers(
                    "/medicos/**"
                ).hasRole("ADMIN")

                // Cadastro de pacientes pela clínica
                .requestMatchers(
                    "/pacientes/novo",
                    "/pacientes/salvar"
                ).hasAnyRole(
                    "SECRETARIA",
                    "ADMIN"
                )

                // Pacientes
                .requestMatchers(
                    "/pacientes/**"
                ).hasAnyRole(
                    "MEDICO",
                    "SECRETARIA",
                    "ADMIN"
                )

                // Agenda médica
                .requestMatchers(
                    "/agenda/**"
                ).hasAnyRole(
                    "MEDICO",
                    "ADMIN"
                )

                // Criação de agendamentos
                .requestMatchers(
                    "/agendamentos/novo",
                    "/agendamentos/salvar"
                ).hasAnyRole(
                    "PACIENTE",
                    "SECRETARIA",
                    "ADMIN"
                )

                // Cancelamento
                .requestMatchers(
                    "/agendamentos/cancelar/**"
                ).hasAnyRole(
                    "PACIENTE",
                    "MEDICO",
                    "SECRETARIA",
                    "ADMIN"
                )

                // Confirmação de presença
                .requestMatchers(
                    "/agendamentos/confirmar/**"
                ).hasAnyRole(
                    "SECRETARIA",
                    "ADMIN"
                )

                // Lista de agendamentos
                .requestMatchers(
                    "/agendamentos/**"
                ).hasAnyRole(
                    "PACIENTE",
                    "MEDICO",
                    "SECRETARIA",
                    "ADMIN"
                )

                // Edição da consulta
                .requestMatchers(
                    "/consultas/editar/**",
                    "/consultas/atualizar"
                ).hasAnyRole(
                    "MEDICO",
                    "ADMIN"
                )

                // Consultas
                .requestMatchers(
                    "/consultas/**"
                ).hasAnyRole(
                    "PACIENTE",
                    "MEDICO",
                    "SECRETARIA",
                    "ADMIN"
                )

                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )

            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}