import controller.DisciplinaController;
import view.ConsoleInput;

public class SubmenuDisciplina {
    public static void Show() {
        while (true) {
            System.out.println("\nGerenciar disciplinas");
            System.out.println("1 - Cadastrar disciplina");
            System.out.println("2 - Consultar disciplina");
            System.out.println("3 - Remover disciplina");
            System.out.println("4 - Atualizar disciplina");
            System.out.println("5 - Listar disciplinas");
            System.out.println("6 - Voltar ao menu inicial");
            int opcao = ConsoleInput.readInt("Opção: ");
            switch (opcao) {
                case 1:
                    ConsoleInput.runAction(DisciplinaController::create);
                    break;
                case 2:
                    ConsoleInput.runAction(DisciplinaController::read);
                    break;
                case 3:
                    ConsoleInput.runAction(DisciplinaController::delete);
                    break;
                case 4:
                    ConsoleInput.runAction(DisciplinaController::update);
                    break;
                case 5:
                    ConsoleInput.runAction(DisciplinaController::list);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}
