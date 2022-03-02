
import controller.LoginController;
import repository.CursRepository;
import repository.UserRepository;
import repository.impl.CursRepositoryImpl;
import repository.impl.JDBConnectionWrapper;
import repository.impl.UserRepositoryImpl;
import service.StudentService;
import service.UserService;
import service.impl.StudentServiceImpl;
import service.impl.UserServiceImpl;
import view.LoginView;
/**
 * Clasa de unde se initializeaza aplicatia
 * */
public class Main {
    private static final String SCHEMA_NAME = "studiu";

    public static void main(String[] args) {
        JDBConnectionWrapper jdbcWrapper = new JDBConnectionWrapper(SCHEMA_NAME);
        new LoginController(new LoginView(),jdbcWrapper);
    }
}
