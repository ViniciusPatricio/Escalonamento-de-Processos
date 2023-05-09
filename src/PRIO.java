import java.util.LinkedList;

public class PRIO{

    // ******** Lista encadeada de tarefas ******* //
    LinkedList<Tarefa> tarefas;

    // ******** Métricas ******** //
    double tempoMedioEspera;
    double tempoMedioExecucao;

    // ******* Quantum ******* //
    int quantum;

    public PRIO(LinkedList<Tarefa> tarefas, int quatum){
        this.tarefas = tarefas;
        this.tempoMedioEspera = 0;
        this.tempoMedioExecucao = 0;
        this.quantum = quatum;

    }
    // retorna o tempo de ingresso de uma tarefa
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

    // Ordena a lista de tarefas conforme a proridade das tarefas listadas
    public LinkedList<Tarefa> ordenaTarefasPrioridade(LinkedList<Tarefa> tarefas){
        LinkedList<Tarefa> tarefasOrdenadas = new LinkedList<Tarefa>();

        int tamanho = tarefas.size();

        while (tamanho>0){
            int maiorPrioridade = tarefas.getFirst().prioridade;
            int indice = 0;
            for(int j = 0; j < tarefas.size();j++){
                if (tarefas.get(j).prioridade >= maiorPrioridade){
                    maiorPrioridade = tarefas.get(j).prioridade;
                    indice = j;
                }
            }
            tarefasOrdenadas.add(tarefas.get(indice));
            tarefas.remove(indice);
            tamanho--;
        }
        return  tarefasOrdenadas;
    }

    public void execPRIO(){
        // lista de tarefas que estão prontas para executar
        LinkedList<Tarefa> tarefasProntas = new LinkedList<Tarefa>();

        // lista de tarefas que foram executadas ( tanto 100% e parcialmente)
        LinkedList<Tarefa> tarefasExecutadas = new LinkedList<Tarefa>();

        LinkedList<Tarefa> originalTarefas = new LinkedList<Tarefa>(tarefas);
        // tempo atual do sistema
        int tempoAtual = tarefas.getFirst().tempoIngresso;
        int numeroTarefas = tarefas.size(); // numero de tarefas a serem executadas
        double auxiliarTempoExec = 0; // auxilia a determinar o tempoMedioExecucao
        double auxiliarTempoEspera= 0; // auxilia a determinar o tempoMedioEspera

        // verificando as tarefas que podem ser executdas nesse tempo atual

        String[] nomeTarefas = new String[tarefas.size()]; // registrar o nome das tarefas que serão exetucadas
        for(int i = 0; i<tarefas.size();i++){
            nomeTarefas[i] = tarefas.get(i).nome;
        }

        while(tarefas.size()>0 && tarefas.getFirst().tempoIngresso == tempoAtual){
            tarefasProntas.add(tarefas.getFirst());
            tarefas.remove(0);
        }
        // ordenando conforme a prioridade
        tarefasProntas = ordenaTarefasPrioridade(tarefasProntas);

        // realizando a execução das tarefas
        // Será feito enquanto tem tarefas para ser executadas
        while(tarefasProntas.size()>0){

            Tarefa tarefaProntaExecutar = tarefasProntas.getFirst();

            if (tarefaProntaExecutar.duracao <= quantum ){

                tempoAtual += tarefaProntaExecutar.duracao; // atualizando o tempo do sistema

                auxiliarTempoExec += tempoAtual - tarefaProntaExecutar.tempoIngresso;
                tarefasProntas.remove(tarefaProntaExecutar);

                // verificando se houve chegada de outras tarefas
                while(tarefas.size() > 0 && tarefas.getFirst().tempoIngresso <= tempoAtual){
                    tarefasProntas.add(tarefas.getFirst());
                    tarefas.remove(0);
                }
                // reorganizando as tarefas
                Tarefa tarefaParcialmenteExecutada = new Tarefa(tarefaProntaExecutar.nome,tempoAtual-tarefaProntaExecutar.duracao,tarefaProntaExecutar.duracao,tarefaProntaExecutar.prioridade,tarefaProntaExecutar.tipo);
                tarefasExecutadas.add(tarefaParcialmenteExecutada); // adiciona na lista encadeada tarefasExecutadas
                tarefasProntas = ordenaTarefasPrioridade(tarefasProntas); // ordena as tarefas prontas
            }else{

                tempoAtual += quantum;
                // verificando se houve chegada de outras tarefas
                while(tarefas.size() > 0 && tarefas.getFirst().tempoIngresso <= tempoAtual){
                    tarefasProntas.add(tarefas.getFirst());
                    tarefas.remove(0);
                }

                tarefaProntaExecutar.duracao -=quantum; // atualizando o tempo de execução
                // criando uma representação da tarefa parcialmente executada para ser colocada na lista de tarefas executadas
                Tarefa tarefaParcialmenteExecutada = new Tarefa(tarefaProntaExecutar.nome,tempoAtual-quantum,quantum,tarefaProntaExecutar.prioridade,tarefaProntaExecutar.tipo);
                tarefasExecutadas.add(tarefaParcialmenteExecutada); // adiciona na lista encadeada tarefasExecutadas
                tarefasProntas.remove(tarefaProntaExecutar); // remover a tarefa do início
                tarefasProntas.add(tarefaProntaExecutar);  // iniciando a tarefa no final da lista
                tarefasProntas = ordenaTarefasPrioridade(tarefasProntas);  // reorganizando as tarefas

            }
            // Pode existir o caso onde não se encontrou nenhuma tarefa inicializada conforme o tempo atual
            // Então será pego a próxima tarefa da lista
            if(tarefasProntas.size() == 0 && tarefas.size()>0){
                tempoAtual = tarefas.getFirst().tempoIngresso;
                // vericando se há outras tarefas com o mesmo tempo de chegada
                while(tarefas.size()>0 && tarefas.getFirst().tempoIngresso == tempoAtual){
                    tarefasProntas.add(tarefas.getFirst());
                    tarefas.remove(0);
                }
                // ordenando a lista de tarefas prontas para execução
                tarefasProntas = ordenaTarefasPrioridade(tarefasProntas);
            }
        }

        // calculando o tempo de espera

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
    public void getDescricaoTarefas(){
        for(Tarefa i:tarefas){
            i.getDescricao();
        }
    }

    // Função exibe a descrição do escalodor - ( Objetivo principal do trabalho )
    public void getDescricaoCompleta(){
        System.out.println("-------------------------------------------------");
        System.out.println("Escalonador PRIORIDADE");
        ordemExec();
        System.out.println("\nTempo medio de execução: "+tempoMedioExecucao);
        System.out.println("Tempo medio de espera: "+tempoMedioEspera+"\n");
        System.out.println("-------------------------------------------------");
    }

}
