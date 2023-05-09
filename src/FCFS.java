import java.util.LinkedList;


class FCFS{

    // ******** Lista encadeada de tarefas ******* //
    LinkedList<Tarefa> tarefas;

    // ******** Métricas ******** //
    double tempoMedioExecucao;
    double tempoMedioEspera;

    public FCFS(LinkedList<Tarefa> tarefas){
        this.tarefas = tarefas;
        this.tempoMedioExecucao = 0;
        this.tempoMedioEspera = 0;
    }
    public void execFCFS(){

        double auxiliarTempoExec = 0; // auxilia a determinar o tempoMedioExecucao
        double auxiliarTempoEspera= 0; // auxilia a determinar o tempoMedioEspera
        int tempoAtualSistema = 0; // tempoAtual do sistema permite saber em que instante de tempo o sistema se encontra


        for(Tarefa i : tarefas) { // Execução das tarefas

            if (i == tarefas.getFirst()) {  // Verificando se o processo é o primeiro a ser executado

                // A Tarefa vai ser executada e é a primeira;
                tempoAtualSistema = i.tempoIngresso;
                auxiliarTempoEspera += tempoAtualSistema - i.tempoIngresso; // sempre será zero
                tempoAtualSistema += i.duracao;
                auxiliarTempoExec = (tempoAtualSistema - i.tempoIngresso);
            } else { // Execução das outras tarefas


                if(tempoAtualSistema <= i.tempoIngresso){ // Verificando se o tempo Atual do sistema é antes ou igual do tempo de ingresso da tarefa a ser executada

                    tempoAtualSistema = i.tempoIngresso; // caso seja menor, significa que o tempo atual do sistema deve ser atualizado para chegada da tarefa a ser executada
                    auxiliarTempoEspera += (tempoAtualSistema - i.tempoIngresso); // sempre será zero
                    tempoAtualSistema += i.duracao; // Após a execução, o tempo atual do sistema é atualizado
                    auxiliarTempoExec += (tempoAtualSistema - i.tempoIngresso);

                }else{ // caso o tempo atual do sistema seja após o tempo de ingresso da tarefa
                    auxiliarTempoEspera += (tempoAtualSistema - i.tempoIngresso);
                    tempoAtualSistema += i.duracao;
                    auxiliarTempoExec += (tempoAtualSistema - i.tempoIngresso);
                }
            }

        }
        tempoMedioExecucao = auxiliarTempoExec/tarefas.size();
        tempoMedioEspera = auxiliarTempoEspera/tarefas.size();
    }

    //função exibe as tarefas em ordem de execução
    public void ordemExec(){
        for(Tarefa i : tarefas){
            System.out.print(i.nome+" ");
        }
    }
    // Função exibe a descrição do escalodor - ( Objetivo principal do trabalho )
    public void getDescricaoCompleta(){
        System.out.println("-------------------------------------------------");
        System.out.println("Escalonador FCFS");
        ordemExec();
        System.out.println("\nTempo medio de execução: "+tempoMedioExecucao);
        System.out.println("Tempo medio de espera: "+tempoMedioEspera+"\n");
        System.out.println("-------------------------------------------------");
    }
}