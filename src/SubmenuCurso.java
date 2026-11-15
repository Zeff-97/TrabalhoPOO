import controller.CursoController;
import view.ConsoleInput;

public class SubmenuCurso {
    public static void Show() {
        while (true) {
            System.out.println("\nGerenciar cursos");
            System.out.println("1 - Cadastrar curso");
            System.out.println("2 - Consultar curso");
            System.out.println("3 - Remover curso");
            System.out.println("4 - Atualizar curso");
            System.out.println("5 - Listar cursos");
            System.out.println("6 - Voltar ao menu inicial");
            int opcao = ConsoleInput.readInt("Opção: ");
            switch (opcao) {
                case 1:
                    ConsoleInput.runAction(CursoController::create);
                    break;
                case 2:
                    ConsoleInput.runAction(CursoController::read);
                    break;
                case 3:
                    ConsoleInput.runAction(CursoController::delete);
                    break;
                case 4:
                    ConsoleInput.runAction(CursoController::update);
                    break;
                case 5:
                    ConsoleInput.runAction(CursoController::list);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}
