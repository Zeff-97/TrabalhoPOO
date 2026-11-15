import controller.CursoController;
import view.ConsoleInput;

public class Menu {
    public static void Show() {
        while (true) {
            System.out.println("\nMenu principal");
            System.out.println("1 - Gerenciar alunos");
            System.out.println("2 - Gerenciar disciplinas");
            System.out.println("3 - Gerenciar cursos");
            System.out.println("4 - Sair");
            int opcao = ConsoleInput.readInt("Opção: ");
            switch (opcao) {
                case 1:
                    ConsoleInput.runAction(Submenu::Show);
                    break;
                case 2:
                    ConsoleInput.runAction(SubmenuDisciplina::Show);
                    break;
                case 3:
                    ConsoleInput.runAction(SubmenuCurso::Show);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}
