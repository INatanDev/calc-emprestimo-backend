# Calculadora de Empréstimo (Back-end)

Repositório referente o back-end do desafio técnico da TOTVS.

## Pré-requisitos
- Java 17
- Spring Boot
- Maven

## Instalação
1. Clone o repositório:

    ```
    git clone https://github.com/heloisacst/calc-emprestimo-backend
    ```
## Execução

1. Executar na sua IDE de preferência

2. Se for utilizar alguma ferramenta tipo Insomnia ou Postman, o endpoint é ```http://localhost:8080/calcular```
    
    Método: POST
    
    Exemplo body de requisição:
    ```
    {
	"dataInicial": "2024-01-01",
	"dataFinal": "2034-01-01",
	"dataPrimeiroPagamento": "2024-02-15",
	"valorEmprestimo": 140000,
	"taxaJuros": 7
    }
    ```

#### Observação

Projeto feito no Intellij