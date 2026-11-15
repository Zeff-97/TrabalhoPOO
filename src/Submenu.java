import controller.AlunoController;
import view.ConsoleInput;

public class Submenu {
    public static void Show() {
        while (true) {
            System.out.println("\nGerenciar alunos");
            System.out.println("1 - Cadastrar aluno");
            System.out.println("2 - Consultar aluno");
            System.out.println("3 - Remover aluno");
            System.out.println("4 - Atualizar aluno");
            System.out.println("5 - Voltar ao menu inicial");
            int opcao = ConsoleInput.readInt("Opção: ");
            switch (opcao) {
                case 1:
                    ConsoleInput.runAction(AlunoController::create);
                    break;
                case 2:
                    ConsoleInput.runAction(AlunoController::read);
                    break;
                case 3:
                    ConsoleInput.runAction(AlunoController::delete);
                    break;
                case 4:
                    ConsoleInput.runAction(AlunoController::update);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}
