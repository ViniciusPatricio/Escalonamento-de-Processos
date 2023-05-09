import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class EscalonamentoMain {
    // função que exibe os comandos que o usuário pode realizar
    public static void getComandos(){
        System.out.println("|| Escolha algoritmo de escalonamento de processos ||");
        System.out.println("||         Digite o numero do algoritmo            ||");
        System.out.println("|| ------------------------------------------------||");
        System.out.println("|| 1 - FCFS                                        ||");
        System.out.println("|| 2 - SJF                                         ||");
        System.out.println("|| 3 - RR                                          ||");
        System.out.println("|| 4 - Prioridade                                  ||");
        System.out.println("|| ------------------------------------------------||");
        System.out.println("|| 9 - Novo Arquivo                                ||");
        System.out.println("|| ------------------------------------------------||");
        System.out.println("|| 0 - finalizar programa                          ||");
        System.out.println("|| ------------------------------------------------||");
    }
    // função de leitura do arquivo
    public static LinkedList<Tarefa> criarListaTarefas(String nomeArquivo)  throws Exception{
        LinkedList<Tarefa> listaTarefas = new LinkedList<Tarefa>();
        File file = new File(nomeArquivo);
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()) {

            String processo = sc.nextLine();

            // Realizando a criação dos objetos Tarefas

            String nome = processo.substring(0,processo.indexOf(' '));
            String aux = processo.substring(processo.indexOf(' ')+1);

            int tempoIngresso = Integer.parseInt(aux.substring(0,aux.indexOf(' ')));
            aux = aux.substring(aux.indexOf(' ')+1);

            int duracao = Integer.parseInt(aux.substring(0,aux.indexOf(' ')));
            aux = aux.substring(aux.indexOf(' ')+1);

            int prioridade = Integer.parseInt(aux.substring(0,aux.indexOf(' ')));
            aux = aux.substring(aux.indexOf(' ')+1);

            int indice = aux.indexOf(' ');
            if(indice <0) indice = 1;
            int tipo = Integer.parseInt(aux.substring(0,indice));

            Tarefa tf = new Tarefa(nome,tempoIngresso,duracao,prioridade,tipo);
            listaTarefas.add(tf);
            }
            sc.close();
        return listaTarefas;
    }
    public static LinkedList<Tarefa> filtrarTarefas(LinkedList<Tarefa> tarefas, int tipoEscalonador){
        LinkedList<Tarefa> tarefasFiltradas = new LinkedList<>();


        if(tipoEscalonador == 1){ // FCFS e SJF
            for(Tarefa i:tarefas){
                if(i.tipo == 1 || i.tipo == 3){
                    tarefasFiltradas.add(i);
                }
            }

        }else{ // RR e PRIO
            for(Tarefa i:tarefas){
                if(i.tipo == 2 || i.tipo == 3){
                    tarefasFiltradas.add(i);
                }
            }
        }

        return  tarefasFiltradas;
    }

    public static void getDescricaoTarefas(LinkedList<Tarefa> listaTarefas){
        int tipo1 = 0,tipo2 =0, tipo3 = 0;
        for(Tarefa i:listaTarefas){
            if(i.tipo == 1){
                tipo1++;
            }else if(i.tipo == 2){
                tipo2++;
            }else{
                tipo3++;
            }
        }
        System.out.printf("\n%d Processo(s) do tipo 1 (CPU-Bound)\n",tipo1);
        System.out.printf("%d Processo(s) do tipo 2 (IO-Bound)\n",tipo2);
        System.out.printf("%d Processo(s) do tipo 3 (Ambos)\n\n",tipo3);

    }

    public static void main(String[] args)  throws Exception {

        LinkedList<Tarefa> listaTarefas = new LinkedList<Tarefa>();
        Scanner scan = new Scanner(System.in);

        // ******************* Realizando a leitura do arquivo ******************* //

        // Nome do arquivo que será lido
        System.out.print("Nome do arquivo: ");
        String nomeArquivo = scan.next();
        listaTarefas = criarListaTarefas(nomeArquivo);
        getDescricaoTarefas(listaTarefas);

        // **************** Execução dos escalonadores escolhido pelo usuário ****************  //
        getComandos();

        System.out.print("Digite opção desejada: ");
        int opcao = scan.nextInt();
        ArrayList<Integer> opcaoValidas = new ArrayList<Integer>();
        opcaoValidas.add(0);opcaoValidas.add(1);opcaoValidas.add(2);opcaoValidas.add(3);opcaoValidas.add(4);opcaoValidas.add(9);

        do{
            if(opcao == 1){ //FCFS

                // primeiro será verificado as tarefas são para esse escalonador
                LinkedList <Tarefa> listaTarefasFiltradas = filtrarTarefas(listaTarefas,1);
                // tem-se dois casos: lista vazia ou não
                if(listaTarefasFiltradas.size() == 0){
                    System.out.println("\n ** Escalonador não funciona para esses processos **\n");
                }else{
                    FCFS escaloadorFCFS = new FCFS(listaTarefasFiltradas);
                    escaloadorFCFS.execFCFS();
                    escaloadorFCFS.getDescricaoCompleta();
                }

            }else if(opcao == 2){ // SJF
                // primeiro será verificado as tarefas são para esse escalonador
                LinkedList <Tarefa> listaTarefasFiltradas = filtrarTarefas(listaTarefas,1);
                // tem-se dois casos: lista vazia ou não
                if(listaTarefasFiltradas.size() == 0){
                    System.out.println("\nEscalonador não funciona para esses processos\n");
                }else{
                    SJF escaloadorSJF = new SJF(listaTarefasFiltradas);
                    escaloadorSJF.execSJF();
                    escaloadorSJF.getDescricaoCompleta();
                }
            }else if(opcao == 3){ // RR
                // primeiro será verificado as tarefas são para esse escalonador
                LinkedList <Tarefa> listaTarefasFiltradas = filtrarTarefas(listaTarefas,2);
                // tem-se dois casos: lista vazia ou não
                if(listaTarefasFiltradas.size() == 0){
                    System.out.println("\nEscalonador não funciona para esses processos\n");
                }else{
                    System.out.print("Infome quantum: ");
                    int quantum = scan.nextInt();
                    while (quantum<0){ // enquanto o quantum informado não for válido, ou seja, não for maior ou igual a zero
                        System.out.println("Quantum deve ser um número positivo");
                        System.out.print("Infome quantum: ");
                        quantum = scan.nextInt();
                    }
                    RR escaloadorRR = new RR(listaTarefasFiltradas,quantum);
                    escaloadorRR.execRR();
                    escaloadorRR.getDescricaoCompleta();
                }
            }else if(opcao == 4){
                // primeiro será verificado as tarefas são para esse escalonador
                LinkedList <Tarefa> listaTarefasFiltradas = filtrarTarefas(listaTarefas,2);
                // tem-se dois casos: lista vazia ou não
                if(listaTarefasFiltradas.size() == 0){
                    System.out.println("\nEscalonador não funciona para esses processos\n");
                }else{
                    System.out.print("Infome quantum: ");
                    int quantum = scan.nextInt();
                    while (quantum<0){ // enquanto o quantum informado não for válido, ou seja, não for maior ou igual a zero
                        System.out.println("Quantum deve ser um número positivo");
                        System.out.print("Infome quantum: ");
                        quantum = scan.nextInt();
                    }
                    PRIO escaloadorPrio = new PRIO(listaTarefasFiltradas,quantum);
                    escaloadorPrio.execPRIO();
                    escaloadorPrio.getDescricaoCompleta();
                }
            }else if(opcao == 9){ // realiza a leitura de outro arquivo
                System.out.print("Nome do arquivo: ");
                nomeArquivo = scan.next();
                listaTarefas = criarListaTarefas(nomeArquivo);
                getDescricaoTarefas(listaTarefas);

            }else if(opcao == 0){ // fechar programa
                break;
            }
            System.out.print("Deseja realizar outros testes?\ns - para sim\nn - para não\nInforme sua opção: ");
            String opcaoContinuacao = scan.next();
            if(opcaoContinuacao.equals("s")){getComandos();System.out.print("\nDigite opção desejada: "); opcao = scan.nextInt();}else break;

        }while (opcaoValidas.indexOf(opcao) != -1);

        scan.close();
    }

}
