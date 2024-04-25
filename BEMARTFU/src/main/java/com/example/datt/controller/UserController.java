package com.example.datt.controller;
import com.example.datt.Service.SendMailService;
import com.example.datt.config.JwtUtils;
import com.example.datt.dto.JwtResponse;
import com.example.datt.dto.LoginRequest;
import com.example.datt.dto.MessageResponse;
import com.example.datt.dto.SignupRequest;
import com.example.datt.entity.AppRole;
import com.example.datt.entity.Cart;
import com.example.datt.entity.User;
import com.example.datt.enumn.service.implement.UserDetailsImpl;
import com.example.datt.repository.CartRepository;
import com.example.datt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/auth")
@CrossOrigin("*")
@Transactional
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    SendMailService sendMailService;


    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userRepository.findByStatusTrue());
    }
    @GetMapping("{id}")
    public ResponseEntity<User> getById(@PathVariable ("id") Long id){
        if(!userRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userRepository.findById(id).get());
    }
   @GetMapping("email/{email}")
    public ResponseEntity<User> getByEmail(@PathVariable ("email") String email){
        if(!userRepository.existsByEmail(email)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userRepository.findByEmail(email).get());
   }
  @PostMapping("/signin")
    public ResponseEntity<?> signinUser(@Validated @RequestBody LoginRequest loginRequest){
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = jwtUtils.generateJwtToken(authentication);

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      List<String> roles =userDetails.getAuthorities().stream().map(item -> item.getAuthority())
              .collect(Collectors.toList());
      return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getName(),
              userDetails.getEmail(), userDetails.getPassword(), userDetails.getPhone(), userDetails.getAddress(),
              userDetails.getGender(), userDetails.getStatus(), userDetails.getImage(), userDetails.getRegisterDate(),
              roles));
  }

  @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@Validated @RequestBody SignupRequest signupRequest) {
      if (userRepository.existsByEmail(signupRequest.getEmail())) {
          return ResponseEntity.badRequest().body(new MessageResponse("Lỗi, email này đã được sử dụng"));
      }
      User user = new User(signupRequest.getName(), signupRequest.getEmail(),
              passwordEncoder.encode(signupRequest.getPassword()), signupRequest.getPhone(), signupRequest.getAddress(),
              signupRequest.getGender(), signupRequest.getStatus(), signupRequest.getImage(), signupRequest.getRegisterDate(),
              jwtUtils.doGenerateToken(signupRequest.getEmail()));

      Set<AppRole> roles = new HashSet<>();
      roles.add(new AppRole(1,null));
      user.setRoles(roles);
      userRepository.save(user);
      Cart cart = new Cart(0L,0.0,user.getAddress(), user.getPhone(), user);
      cartRepository.save(cart);
      return ResponseEntity.ok(new MessageResponse("Đăng kí thành công"));
  }
    @GetMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }
    @PostMapping
    public ResponseEntity<User> post(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.notFound().build();
        }
        if (userRepository.existsById(user.getUserId())) {
            return ResponseEntity.badRequest().build();
        }
        Set<AppRole> roles = new HashSet<>();
        roles.add(new AppRole(1, null));
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setToken(jwtUtils.doGenerateToken(user.getEmail()));
        User u = userRepository.save(user);
        Cart c = new Cart(0L, 0.0, u.getAddress(), u.getPhone(), u);
        cartRepository.save(c);
        return ResponseEntity.ok(u);
    }

    @PutMapping("{id}")
    public ResponseEntity<User> put(@PathVariable("id") Long id, @RequestBody User user) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        if (!id.equals(user.getUserId())) {
            return ResponseEntity.badRequest().build();
        }

        User temp = userRepository.findById(id).get();
        if (!user.getPassword().equals(temp.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        Set<AppRole> roles = new HashSet<>();
        roles.add(new AppRole(1, null));
        user.setRoles(roles);
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PutMapping("admin/{id}")
    public ResponseEntity<User> putAdmin(@PathVariable("id") Long id, @RequestBody User user) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        if (!id.equals(user.getUserId())) {
            return ResponseEntity.badRequest().build();
        }
        Set<AppRole> roles = new HashSet<>();
        roles.add(new AppRole(2, null));
        user.setRoles(roles);
        return ResponseEntity.ok(userRepository.save(user));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        if (cartRepository.existsByUserId(id)) {
            cartRepository.deleteByUserId(id);
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("send-mail-forgot-password-token")
    public ResponseEntity<String> sendToken(@RequestBody String email) {

        if (!userRepository.existsByEmail(email)) {
            return ResponseEntity.notFound().build();
        }
        User user = userRepository.findByEmail(email).get();
        String token = user.getToken();
        sendMaiToken(email, token, "Reset mật khẩu");
        return ResponseEntity.ok().build();

    }

    public void sendMaiToken(String email, String token, String title) {
        String body = "\r\n" + "    <h2>Hãy nhấp vào link để thay đổi mật khẩu của bạn</h2>\r\n"
                + "    <a href=\"http://localhost:8080/forgot-password/" + token + "\">Đổi mật khẩu</a>";
        sendMailService.queue(email, title, body);
    }

}
