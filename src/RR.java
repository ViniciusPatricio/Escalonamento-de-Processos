import java.util.LinkedList;

public class RR {

    // ******** Lista encadeada de tarefas ******* //
    LinkedList<Tarefa> tarefas;

    // ******** Métricas ******** //
    double tempoMedioEspera;
    double tempoMedioExecucao;

    // ******* Quantum ******* //
    int quantum;

    public RR(LinkedList<Tarefa> tarefas, int quantum){
        this.tarefas = tarefas;
        this.quantum = quantum;
        this.tempoMedioEspera = 0;
        this.tempoMedioExecucao = 0;
    }

    // retorna o tempo de ingresso da tarefa
    public int getTempoIngresso(LinkedList<Tarefa> tarefas, String nomeTarefa){
        int tempoIngresso = 0;

        for(Tarefa i:tarefas){
            if(nomeTarefa.equals(i.nome)){
                tempoIngresso = i.tempoIngresso;
                break;
            }
        }
        return  tempoIngresso;
    };
    public void execRR() {

        // nova lista de Tarefas para serem realizadas

        double auxiliarTempoExec = 0; // auxilia a determinar o tempoMedioExecucao
        double auxiliarTempoEspera= 0; // auxilia a determinar o tempoMedioEspera
        int numeroTarefas = tarefas.size(); // numero de tarefas a serem executadas
        LinkedList<Tarefa> tarefasProntas = new LinkedList<Tarefa>();
        LinkedList<Tarefa> tarefasExecutadas = new LinkedList<Tarefa>();
        LinkedList<Tarefa> originalTarefas = new LinkedList<Tarefa>(tarefas);

        Tarefa primeiraTarefa = tarefas.getFirst();
        int tempoAtual = primeiraTarefa.tempoIngresso;

        String[] nomeTarefas = new String[tarefas.size()]; // registrar o nome das tarefas que serão exetucadas
        for(int i = 0; i<tarefas.size();i++){
            nomeTarefas[i] = tarefas.get(i).nome;
        }
        // alocando as tarefas prontas para serem executadas que tem
        // o mesmo tempo de ingresso da primeira tarefa
        int indice = 0;
        while( tarefas.size() > 0 && (tempoAtual == tarefas.getFirst().tempoIngresso)){
            tarefasProntas.add(tarefas.get(indice));
            tarefas.remove(tarefas.get(indice));
        }


        while(tarefasProntas.size()>0){

            Tarefa tarefaParaExecutar  = tarefasProntas.getFirst();

            if(quantum >= tarefaParaExecutar.duracao){

                tempoAtual += tarefaParaExecutar.duracao; // atualizo o tempo do sistema

                auxiliarTempoExec += tempoAtual - tarefaParaExecutar.tempoIngresso;
                tarefasProntas.remove(tarefaParaExecutar); // removo a tarefa pois foi executada

                // verificando se teve alguma tarefa que chegou e inserindo na lista encadeada tarefasProntas
                while(tarefas.size()> 0 && tempoAtual >= tarefas.getFirst().tempoIngresso){ // adicionando tarefas que chegaram enquanto a tarefa estava em execução
                    tarefasProntas.add(tarefas.get(indice));
                    tarefas.remove(tarefas.get(indice));
                }

                Tarefa tarefaParcialmenteExecutada = new Tarefa(tarefaParaExecutar.nome,tempoAtual-tarefaParaExecutar.duracao,tarefaParaExecutar.duracao,tarefaParaExecutar.prioridade,tarefaParaExecutar.tipo);
                tarefasExecutadas.add(tarefaParcialmenteExecutada); // adiciona na lista encadeada tarefasExecutadas
            }else{

                tempoAtual += quantum; // atualizo o tempo do sistema

                while( tarefas.size()> 0 &&(tempoAtual >= tarefas.getFirst().tempoIngresso)){ // adicionando tarefas que chegaram enquanto a tarefa estava em execução
                        tarefasProntas.add(tarefas.getFirst());
                        tarefas.remove(tarefas.getFirst());
                    }

                tarefaParaExecutar.duracao += -quantum; // atualizando o tempo de execução
                // criando uma representação da tarefa parcialmente executada para ser colocada na lista de tarefas executadas
                Tarefa tarefaParcialmenteExecutada = new Tarefa(tarefaParaExecutar.nome,tempoAtual-quantum,quantum,tarefaParaExecutar.prioridade,tarefaParaExecutar.tipo);
                tarefasExecutadas.add(tarefaParcialmenteExecutada); // adiciona na lista encadeada tarefasExecutadas
                tarefasProntas.remove(tarefaParaExecutar); // remover a tarefa do início
                tarefasProntas.add(tarefaParaExecutar); // iniciando a tarefa no final da lista
                }
            // Pode existir o caso onde não se encontrou nenhuma tarefa inicializada conforme o tempo atual
            // Então será pego a próxima tarefa da lista
            if(tarefasProntas.size() == 0 && tarefas.size()>0){
                tempoAtual = tarefas.getFirst().tempoIngresso;
                tarefasProntas.add(tarefas.getFirst());
            }

        }
        // determinando o tempo de espera
        for(String nomeTarefa : nomeTarefas){
            int flagPrimeiro = 0;
            int tempoAuxiliarEspera = 0;
            int tempoUltimaExec = 0;
            for(Tarefa taref:tarefasExecutadas){
                if(nomeTarefa.equals(taref.nome) && flagPrimeiro == 0){
                    int tempoIngresso = getTempoIngresso(originalTarefas,nomeTarefa);
                    tempoAuxiliarEspera = taref.tempoIngresso - tempoIngresso;
                    tempoUltimaExec = taref.tempoIngresso + taref.duracao;
                    flagPrimeiro = 1;
                }
                else if(nomeTarefa.equals(taref.nome) && flagPrimeiro == 1){
                    tempoAuxiliarEspera += taref.tempoIngresso - tempoUltimaExec;
                    tempoUltimaExec = taref.tempoIngresso + taref.duracao;
                }
            }

            auxiliarTempoEspera += tempoAuxiliarEspera;
        }
        tarefas = tarefasExecutadas;
        tempoMedioExecucao = auxiliarTempoExec/numeroTarefas;
        tempoMedioEspera = auxiliarTempoEspera/numeroTarefas;
    }
    //função exibe as tarefas em ordem de execução
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
        System.out.println("Escalonador RR");
        ordemExec();
        System.out.println("\nTempo medio de execução: "+tempoMedioExecucao);
        System.out.println("Tempo medio de espera: "+tempoMedioEspera+"\n");
        System.out.println("-------------------------------------------------");
    }

}
