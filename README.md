# meli-project

Projeto de teste para o mercado livre, para executar o programa fazer post dado o exemplo:

#POST http://35.231.118.59:8080/mutant/
{
	"dna": ["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
}

Obs: A matriz deve ser de N para N caso contrario irá retornar bad request

#Get http://35.231.118.59:8080/stats
{
    "count_human_dna": 1,
    "count_mutant_dna": 1,
    "ratio": 1
}
