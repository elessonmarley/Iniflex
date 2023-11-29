package com.br.iniflex;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

class Pessoa {
    protected String nome;
    protected LocalDate dataNascimento;

    public Pessoa(String nome, LocalDate dataNascimento) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
    }
}

class Funcionario extends Pessoa {
    private BigDecimal salario;
    private String funcao;

    public Funcionario(String nome, LocalDate dataNascimento, BigDecimal salario, String funcao) {
        super(nome, dataNascimento);
        this.salario = salario;
        this.funcao = funcao;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void darAumento(BigDecimal percentual) {
        this.salario = this.salario.multiply(BigDecimal.ONE.add(percentual));
    }

    public String getFuncao() {
        return funcao;
    }
}

public class Principal {
    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<Funcionario> funcionarios = new ArrayList<>();


        funcionarios.add(new Funcionario("João", LocalDate.parse("01/01/1980", formatter), new BigDecimal("5000.00"), "Analista"));
        funcionarios.add(new Funcionario("Maria", LocalDate.parse("15/03/1990", formatter), new BigDecimal("6000.00"), "Gerente"));
        funcionarios.add(new Funcionario("Carlos", LocalDate.parse("20/05/1985", formatter), new BigDecimal("5500.00"), "Programador"));
        funcionarios.add(new Funcionario("Ana", LocalDate.parse("10/10/1982", formatter), new BigDecimal("7000.00"), "Analista"));


        funcionarios.removeIf(funcionario -> funcionario.nome.equals("João"));


        funcionarios.forEach(f -> System.out.println(
                "Nome: " + f.nome +
                        ", Data de Nascimento: " + f.dataNascimento.format(formatter) +
                        ", Salário: " + f.getSalario().setScale(2, BigDecimal.ROUND_HALF_UP) +
                        ", Função: " + f.getFuncao()
        ));


        funcionarios.forEach(f -> f.darAumento(new BigDecimal("0.10")));


        Map<String, List<Funcionario>> funcionariosPorFuncao = funcionarios.stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao));


        funcionariosPorFuncao.forEach((funcao, lista) -> {
            System.out.println("Funcionários na função " + funcao + ":");
            lista.forEach(f -> System.out.println("   " + f.nome));
        });


        int[] mesesAniversario = {10, 12};
        LocalDate hoje = LocalDate.now();
        funcionarios.stream()
                .filter(f -> Arrays.stream(mesesAniversario).anyMatch(mes -> f.dataNascimento.getMonthValue() == mes))
                .forEach(f -> System.out.println(f.nome + " faz aniversário este mês!"));


        Funcionario maisVelho = Collections.max(funcionarios, Comparator.comparing(f -> f.dataNascimento));
        int idadeMaisVelho = hoje.getYear() - maisVelho.dataNascimento.getYear();
        System.out.println("Funcionário mais velho: " + maisVelho.nome + ", Idade: " + idadeMaisVelho);


        List<Funcionario> funcionariosOrdenados = funcionarios.stream()
                .sorted(Comparator.comparing(f -> f.nome))
                .collect(Collectors.toList());
        System.out.println("Funcionários em ordem alfabética:");
        funcionariosOrdenados.forEach(f -> System.out.println("   " + f.nome));


        BigDecimal totalSalarios = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Total dos salários: " + totalSalarios.setScale(2, BigDecimal.ROUND_HALF_UP));

        
        BigDecimal salarioMinimo = new BigDecimal("1212.00");
        funcionarios.forEach(f -> {
            BigDecimal salarioEmSalariosMinimos = f.getSalario().divide(salarioMinimo, 2, BigDecimal.ROUND_HALF_UP);
            System.out.println(f.nome + " ganha " + salarioEmSalariosMinimos + " salários mínimos.");
        });
    }
}
