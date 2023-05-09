public class Tarefa {

    String nome;
    int tempoIngresso;
    int duracao;
    int prioridade;
    int tipo;

    public Tarefa(String nome, int tempoIngresso, int duracao, int prioridade, int tipo){
        this.nome = nome;
        this.duracao = duracao;
        this.tempoIngresso = tempoIngresso;
        this.prioridade = prioridade;
        this.tipo = tipo;
    }

    public void getDescricao(){
        System.out.printf("|| nome: %s || tempo de ingresso: %d || duração: %d || prioridade: %d || tipo: %d ||\n",nome,tempoIngresso,duracao,prioridade,tipo);
    }
}
