import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneratePass {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("admin hash: " + encoder.encode("password"));
        System.out.println("123 hash: " + encoder.encode("123"));
    }
}