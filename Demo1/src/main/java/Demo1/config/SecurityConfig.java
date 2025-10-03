package Demo1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        // Tạo người dùng 'trung' với mật khẩu '123' và vai trò 'ADMIN' [cite: 289-292]
        UserDetails admin = User.withUsername("trung")
                .password(encoder.encode("123")) // Mật khẩu phải được mã hóa
                .roles("ADMIN") // Spring Security sẽ tự động thêm tiền tố 'ROLE_'
                .build();
        
        // Tạo người dùng 'user' với mật khẩu '123' và vai trò 'USER' [cite: 293-296]
        UserDetails user = User.withUsername("user")
                .password(encoder.encode("123"))
                .roles("USER")
                .build();

        // Trả về một trình quản lý chứa 2 người dùng trên
        return new InMemoryUserDetailsManager(admin, user);
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable()) 
                .authorizeHttpRequests(auth -> auth
                        // Cho phép tất cả các yêu cầu tới '/hello' mà không cần đăng nhập [cite: 316] (Trong tài liệu ghi / nhưng code là /hello)
                        .requestMatchers("/","/hello").permitAll() 
                        // Tất cả các yêu cầu tới '/customer/**' đều phải được xác thực (đăng nhập) [cite: 317]
                        .requestMatchers("/customer/**").authenticated() 
                )
                // Kích hoạt form đăng nhập mặc định của Spring Security [cite: 319]
                .formLogin(form -> form
                        .defaultSuccessUrl("/hello", true)  // ✅ sau khi login luôn redirect về /hello
                        .permitAll()
                )
                .build();
    }
}