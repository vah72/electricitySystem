package com.electricitysystem.controller;

import com.electricitysystem.entity.CustomerEntity;
import com.electricitysystem.service.CustomerService;
import com.electricitysystem.service.impl.AccountDetails;
import com.electricitysystem.dto.AccountDto;
import com.electricitysystem.entity.AccountEntity;
import com.electricitysystem.jwt.JwtResponse;
import com.electricitysystem.jwt.JwtUtility;
import com.electricitysystem.repository.AccountRepository;
import com.electricitysystem.repository.CustomerRepository;
import com.electricitysystem.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
public class AccountController {
    @Autowired(required = false)
    private JwtUtility jwtUtility;
    @Autowired(required = false)
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping(value = "/signin", consumes = {"multipart/form-data"})
    public ResponseEntity<?> login(@ModelAttribute AccountDto accountDto) {
        if (accountDto.getUsername().isBlank())
            return ResponseEntity.ok("Vui lòng nhập tên đăng nhập");
        if (accountDto.getPassword().isBlank())
            return ResponseEntity.ok("Vui lòng nhập mật khẩu");
        AccountEntity account = accountService.login(accountDto);
        if (account == null)
            return ResponseEntity.ok("Thông tin đăng nhập không chính xác");
        String jwt = jwtUtility.generateJwtToken(account.getUsername());
        String status = "admin";
        CustomerEntity customer = new CustomerEntity();
        if (!accountDto.getUsername().trim().equals("admin123")) {
            customer = customerService.updateStatus(account.getUsername(), "UNPAID");
            if(customer != null)
                status = customer.getStatus();
        }

        if ( account.getRole() == 0){
            return ResponseEntity.ok(
                    new JwtResponse(jwt,account.getId(),account.getUsername(), "ROLE_ADMIN", account.getStaff().getId().toString(),
                            status));
        }
        else
            return ResponseEntity.ok(
                    new JwtResponse(jwt,account.getId(),account.getUsername(), "ROLE_USER", account.getCustomer().getUsername(), customer.getStatus()));
    }

    @PutMapping(value = "/changepassword/{username}",  produces = "application/json")
    public String changePassword(@PathVariable String username,
                                 @RequestParam("oldpassword")String oldpassword,@RequestParam("newpassword")String password){

        if (oldpassword.trim().equals(""))
            return "Vui lòng nhập mật khẩu cũ";
        if (password.trim().equals(""))
            return "Vui lòng nhập mật khẩu mới";
        //validate password : ít nhất 1 số, 1 ký tự viết thường, 1 viết hoa, từ 8-20
        String regexNumber = "^(.*[0-9].*)$";
        String regexLowerCharacter = "^(.*[a-z].*)$";
        String regexUpperCharacter = "^(.*[A-Z].*)$";
        String regexSpecialChars = "^(.*[@,#,$,%,.,,,!,*,&,^,?].*$)";
        String regexLength = "^.{8,20}$";
        if ( !isValidPassword(password, regexLength))
            return "Mật khẩu phải có độ dài từ 8 đến 20 ký tự";
        if ( !isValidPassword(password,regexUpperCharacter))
            return "Mật khẩu phải có ít nhất một chữ hoa";
        if ( !isValidPassword(password, regexNumber))
            return "Mật khẩu phải có ít nhất một ký tự số";
        if ( !isValidPassword(password, regexLowerCharacter))
            return "Mật khẩu phải có ít nhất một chữ thường";
        if (isValidPassword(password, regexSpecialChars))
            return "Mật khẩu không chứa các ký tự đặc biệt";

        AccountEntity account = accountRepository.getAccountEntityByUsername(username);
        if ( account != null){
            if(!bCryptPasswordEncoder.matches(oldpassword, account.getPassword())){
                return  "Mật khẩu cũ không đúng";
            }
            account.setPassword(bCryptPasswordEncoder.encode(password));
            accountRepository.save(account);
            return "Đổi mật khẩu thành công";
        }
        return "Không tìm thấy người dùng";
    }

    private boolean isValidPassword(String password, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
