import java.util.LinkedList;

public class SJF {
    // ******** Lista encadeada de tarefas ******* //
    LinkedList<Tarefa> tarefas;

    // ******** Métricas ******** //
    double tempoMedioExecucao;
    double tempoMedioEspera;

    public SJF(LinkedList<Tarefa> tarefas){
        this.tarefas = tarefas;
        this.tempoMedioEspera = 0;
        this.tempoMedioExecucao = 0;
    }

    public void organizarOrdemTarefas(){  // Função auxiliar que irá organizar as tarefas para serem executadas comforme o algoritmo SJF

        LinkedList<Tarefa> tarefasOrdem = new LinkedList<Tarefa>(); // nova lista que terá em ordem as tarefas que serão executadas de acordo com algoritmo SJF

        /* Como as tarefas estão em ordem de chegada, tomaremos que o primeiro da lista de tarefas é a tarefa que será executada primeiramente
         Depois verificaremos se essa tarefa é realmente a que possui o menor tempo de execução
         Caso não seja, será trocada com a que possuir o menor tempo de execução
         */

        int tempoChegadaPrimeiraTarefa = tarefas.getFirst().tempoIngresso;
        int menorTempoExecucao = tarefas.getFirst().duracao;
        int indiceTarefa = 0; // essa variável foi criada para facilitar a inserção da primeira tarefa na lista tarefasOrdem

        // Verificando dado o primeiro tempo de Ingresso qual será o processo com o menor tempo de execução
        for(Tarefa i : tarefas){
            if(tempoChegadaPrimeiraTarefa == i.tempoIngresso && i.duracao < menorTempoExecucao) {indiceTarefa = tarefas.indexOf(i); menorTempoExecucao = i.duracao;}
        }

        // Temos agora o indice da primeira tarefa a ser executada
        // Será armazedado essa tarefa na tarefasOrdem
        tarefasOrdem.add(tarefas.get(indiceTarefa));
        this.tarefas.remove(indiceTarefa); // remove a tarefa que já foi para execução


        // Será necessário saber o tempo atual do sistema para poder saber quais serão as próximas tarefas a serem executadas
        // tempo atual será dado pelo (tempo de chegada da primeira tarefa + o tempo de execução da mesma)
        double tempoAtualSistema = tarefasOrdem.getFirst().tempoIngresso+tarefasOrdem.getFirst().duracao;

        /*
        Até então tem-se a primeira tarefa executada, agora será necessário verificar as próximas tarefas.
        Essa verificação deve ocorrer enquanto todas as tarefas da lista encandeada tarefas não forem todas
        verificadas, ou seja, enquanto a lista tarefas não for zerada, pois caso uma tarefa pode ser executada
        será retirado da lista tarefas, então se todas forem executadas o tamanho da lista foi zerado.
        */



        while(tarefas.size()>0){

            // armazena as atividades que podem ser executadas dado o tempo atual do sistema
            LinkedList<Tarefa> tarefasPodemSerExecutadas = new LinkedList<Tarefa>();


            for(Tarefa i:tarefas){
                if(tempoAtualSistema >= i.tempoIngresso){ // verificando se a tarefa já poderia ser executada;
                    tarefasPodemSerExecutadas.add(i);
                }
            }
            // Pode haver casos onde a próxima tarefa é inicializada após o tempo que o sistema se encontra atualmente
            // o caso a seguir trata esse caso
            if(tarefasPodemSerExecutadas.size() == 0){

                tempoChegadaPrimeiraTarefa = tarefas.getFirst().tempoIngresso;
                menorTempoExecucao = tarefas.getFirst().duracao;
                indiceTarefa = 0; // essa variável foi criada para facilitar a inserção da primeira tarefa na lista tarefasOrdem

                // verificando se há outra tarefa com o mesmo tempo de chegada e se ela tem uma duração menor
                for(Tarefa i : tarefas){
                    if(tempoChegadaPrimeiraTarefa == i.tempoIngresso && i.duracao < menorTempoExecucao) {indiceTarefa = tarefas.indexOf(i); menorTempoExecucao = i.duracao;}
                }

                tarefasOrdem.add(tarefas.get(indiceTarefa)); // Colocando a tarefa com menor tempo de duração
                tempoAtualSistema = tarefas.get(indiceTarefa).tempoIngresso+tarefas.get(indiceTarefa).duracao; // atualizando o tempo atual do sistema

                this.tarefas.remove(indiceTarefa); // remove a tarefa que já foi para execução

            }else{ // Caso há tarefas que poderiam ser executadas antes do tempo atual do sistema

                indiceTarefa = 0;
                menorTempoExecucao = tarefasPodemSerExecutadas.getFirst().duracao;
                for(Tarefa i: tarefasPodemSerExecutadas){ // encontrando o indice da tarefa que tem o menor tempo de execução
                    if(i.duracao < menorTempoExecucao){
                        menorTempoExecucao = i.duracao;
                        indiceTarefa = tarefasPodemSerExecutadas.indexOf(i);
                    }
                }
                // uma vez encontrado o indice da tarefa com menor tempo de execução, inserimos a tarefa na lista encadeada tarefasOrdem
                tarefasOrdem.add(tarefasPodemSerExecutadas.get(indiceTarefa));
                // atualiza-se o tempo do sistema
                tempoAtualSistema += tarefasPodemSerExecutadas.get(indiceTarefa).duracao;
                // remove a tarefa da lista encadeada tarefas
                tarefas.remove(tarefasPodemSerExecutadas.get(indiceTarefa));

            }
        }
        this.tarefas = tarefasOrdem;
    }
    public void execSJF(){

        organizarOrdemTarefas(); // antes de realizar as métricas de avaliação, será feito a organização das tarefas para serem executadas segundo o algoritmo SJF

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

    // função exibe as tarefas em ordem de execução
    public void ordemExec(){
        for(Tarefa i : tarefas){
            System.out.print(i.nome+" ");
        }
    }
    //função retorna uma string que tem as tarefas em ordem de execução
    public String ordemExecS(){
        String resultado = tarefas.getFirst().nome+" ";
        for(int i = 1; i<tarefas.size();i++){
            resultado = resultado.concat(tarefas.get(i).nome+" ");
        }
        return resultado;
    }

    // Função exibe a descrição do escalodor - ( Objetivo principal do trabalho )
    public void getDescricaoCompleta(){
        System.out.println("-------------------------------------------------");
        System.out.println("Escalonador SJF");
        ordemExec();
        System.out.println("\nTempo medio de execução: "+tempoMedioExecucao);
        System.out.println("Tempo medio de espera: "+tempoMedioEspera+"\n");
        System.out.println("-------------------------------------------------");
    }

}
