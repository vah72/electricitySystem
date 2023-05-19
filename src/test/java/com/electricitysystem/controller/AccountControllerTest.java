package com.electricitysystem.controller;
import com.electricitysystem.dto.AccountDto;
import com.electricitysystem.entity.AccountEntity;
import com.electricitysystem.entity.CustomerEntity;
import com.electricitysystem.entity.StaffEntity;
import com.electricitysystem.jwt.JwtResponse;
import com.electricitysystem.jwt.JwtUtility;
import com.electricitysystem.repository.AccountRepository;
import com.electricitysystem.service.AccountService;
import com.electricitysystem.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @InjectMocks
    AccountController accountController;

    @Mock
    AccountService accountService;

    @Mock
    CustomerService customerService;

    @Mock
    JwtUtility jwtUtility;

    @Mock
    AccountRepository accountRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void testLoginSuccessWithRoleUserWithNoInvoiceUnpaid_ReturnJwtReponse() throws Exception {
        CustomerEntity customer = new CustomerEntity("HD11300001", "Hoàng Vân Anh", "10 Trần Phú, Mộ Lao, Hà Đông", "0961082342",
                                                    "hoangvananh7201@gmail.com", 1, "PAC001", "ACTIVE", false);
        AccountEntity account = new AccountEntity(1,"HD11300001","$2a$12$lqvL7eCoNwE.Kwb47qaLJO0Y6mWzw9KOt8IxDxMib3vUpmnp39pJy",1, customer, null);
        AccountDto input = new AccountDto("HD11300001", "Password123");
        String token = "jwt";

        when(accountService.login(input)).thenReturn(account);
        when(jwtUtility.generateJwtToken(input.getUsername())).thenReturn(token);
        when(customerService.updateStatus(input.getUsername(), "UNPAID")).thenReturn(null);

        ResponseEntity<?> response = accountController.login(input);
        verify(customerService, times(1)).updateStatus(input.getUsername(), "UNPAID");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals( new JwtResponse(token,account.getId(),account.getUsername(), "ROLE_USER", account.getCustomer().getUsername(), customer.getStatus()), response.getBody());
    }

    @Test
    public void testLoginSuccessWithRoleUserWithInvoiceUnpaid_ReturnJwtReponse() {
        CustomerEntity customer = new CustomerEntity("HD11300001", "Hoàng Vân Anh", "10 Trần Phú, Mộ Lao, Hà Đông", "0961082342",
                "hoangvananh7201@gmail.com", 1, "PAC001", "ACTIVE", false);
        AccountEntity account = new AccountEntity(1,"HD11300001","$2a$12$lqvL7eCoNwE.Kwb47qaLJO0Y6mWzw9KOt8IxDxMib3vUpmnp39pJy",1, customer, null);
        AccountDto input = new AccountDto("HD11300001", "Password123");
        String token = "jwt";

        when(accountService.login(input)).thenReturn(account);
        when(jwtUtility.generateJwtToken(input.getUsername())).thenReturn(token);
        customer.setStatus("INACTIVE");
        when(customerService.updateStatus(input.getUsername(), "UNPAID")).thenReturn(customer);

        ResponseEntity<?> response = accountController.login(input);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals( new JwtResponse(token,account.getId(),account.getUsername(), "ROLE_USER", account.getCustomer().getUsername(), customer.getStatus()), response.getBody());
        verify(customerService, times(1)).updateStatus(input.getUsername(), "UNPAID");
    }



    @Test
    public void testLoginSuccessWithRoleAdmin_ReturnJwtResonse(){
        StaffEntity staff = new StaffEntity();
        staff.setId(1);

        AccountEntity account = new AccountEntity();
        account.setId(1);
        account.setUsername("admin123");
        account.setStaff(staff);

        AccountDto accountDto = new AccountDto("admin123", "Password123");

        String status = "admin";
        String token = "jwt";

        when(accountService.login(accountDto)).thenReturn(account);
        when(jwtUtility.generateJwtToken(accountDto.getUsername())).thenReturn(token);

        ResponseEntity<?> response = accountController.login(accountDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals( new JwtResponse(token,account.getId(),account.getUsername(), "ROLE_ADMIN", account.getStaff().getId().toString(),status), response.getBody());
    }


    @Test
    public void testLoginWithMissingUsername_ReturnMessage(){
        AccountDto accountDto = new AccountDto();
        accountDto.setUsername("");
        accountDto.setPassword("password");

        ResponseEntity<?> response = accountController.login(accountDto);
        assertEquals("Vui lòng nhập tên đăng nhập", response.getBody());

    }
    @Test
    public void testLoginWithMissingPassword_ReturnMessage(){
        AccountDto accountDto = new AccountDto();
        accountDto.setUsername("username");
        accountDto.setPassword("");

        ResponseEntity<?> response = accountController.login(accountDto);
        assertEquals("Vui lòng nhập mật khẩu", response.getBody());

    }

    @Test
    public void testLoginWithInvalidInput_ReturnMessage(){
        AccountDto accountDto = new AccountDto();
        accountDto.setUsername("username");
        accountDto.setPassword("password");
        when(accountService.login(accountDto)).thenReturn(null);

        ResponseEntity<?> response = accountController.login(accountDto);
        assertEquals("Thông tin đăng nhập không chính xác", response.getBody());
    }


    @Test
    public void testChangePasswordSuccess_ReturnMessage() {
        String username = "username";
        String oldpassword = "Password123";
        String newpassword = "Password1";

        AccountEntity account = new AccountEntity();
        account.setUsername("username");
        account.setPassword("Password123");

        when(accountRepository.getAccountEntityByUsername(username))
                .thenReturn(account);
        when(bCryptPasswordEncoder.matches(oldpassword, account.getPassword()))
                .thenReturn(true);

        String response = accountController.changePassword(username, oldpassword, newpassword);
        assertEquals("Đổi mật khẩu thành công", response);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    public void testChangePasswordFailWithMissingOldPassword_ReturnMessage() {
        String username = "username";
        String oldpassword = "";
        String newpassword = "Password1";

        AccountEntity account = new AccountEntity();
        account.setUsername("username");
        account.setPassword("Password123");

        when(accountRepository.getAccountEntityByUsername(account.getUsername()))
                .thenReturn(account);

        String response = accountController.changePassword(username, oldpassword, newpassword);
        assertEquals("Vui lòng nhập mật khẩu cũ", response);
    }

    @Test
    public void testChangePasswordFailWithMissingNewPassword_ReturnMessage() {
        String username = "username";
        String oldpassword = "Password123";
        String newpassword = "";

        AccountEntity account = new AccountEntity();
        account.setUsername("username");
        account.setPassword("Password123");

        String response = accountController.changePassword(username, oldpassword, newpassword);
        assertEquals("Vui lòng nhập mật khẩu mới", response);
    }

    @Test
    public void testChangePasswordFailWithNotExistUsername_ReturnMessage() {
        String username = "username";
        String oldpassword = "Password123";
        String newpassword = "Password1";

        AccountEntity account = new AccountEntity();
        account.setUsername("username");
        account.setPassword("Password123");

        when(accountRepository.getAccountEntityByUsername(username))
                .thenReturn(null);

        String response = accountController.changePassword(username, oldpassword, newpassword);
        assertEquals("Không tìm thấy người dùng", response);
    }

    @Test
    public void testChangePasswordFailWithWrongOldPassword_ReturnMessage() {
        String username = "username";
        String oldpassword = "Password12";
        String newpassword = "Password1";

        AccountEntity account = new AccountEntity();
        account.setUsername("username");
        account.setPassword("Password123");

        when(accountRepository.getAccountEntityByUsername(username))
                .thenReturn(account);
        when(bCryptPasswordEncoder.matches(oldpassword, account.getPassword()))
                .thenReturn(false);

        String response = accountController.changePassword(username, oldpassword, newpassword);
        assertEquals("Mật khẩu cũ không đúng", response);
    }

    @Test
    public void testChangePasswordFailWithNewPasswordSimilarToOldPassword_ReturnMessage(){
        String username = "username";
        String oldpassword = "Password123";
        String newpassword = "Password123";

        AccountEntity account = new AccountEntity();
        account.setUsername("username");
        account.setPassword("Password123");

        String response = accountController.changePassword(username, oldpassword, newpassword);
        assertEquals("Mật khẩu mới phải khác mật khẩu cũ", response);
    }

    @Test
    public void testChangePasswordFailWithNewPasswordShorterThan8_ReturnMessage(){
        String username = "username";
        String oldpassword = "Password123";
        String newpassword = "Passwor";

        AccountEntity account = new AccountEntity();
        account.setUsername("username");
        account.setPassword("Password123");

        String response = accountController.changePassword(username, oldpassword, newpassword);
        assertEquals("Mật khẩu phải có độ dài từ 8 đến 20 ký tự", response);
    }

    @Test
    public void testChangePasswordFailWithNewPasswordLongerThan20_ReturnMessage(){
        String username = "username";
        String oldpassword = "Password123";
        String newpassword = "Password0123456789123";

        AccountEntity account = new AccountEntity();
        account.setUsername("username");
        account.setPassword("Password123");

        String response = accountController.changePassword(username, oldpassword, newpassword);
        assertEquals("Mật khẩu phải có độ dài từ 8 đến 20 ký tự", response);
    }

    @Test
    public void testChangePasswordFailWithNewPasswordMissingUpperChar_ReturnMessage(){
        String username = "username";
        String oldpassword = "Password123";
        String newpassword = "password1";

        AccountEntity account = new AccountEntity();
        account.setUsername("username");
        account.setPassword("Password123");

        String response = accountController.changePassword(username, oldpassword, newpassword);
        assertEquals("Mật khẩu phải có ít nhất một chữ hoa", response);
    }

    @Test
    public void testChangePasswordFailWithNewPasswordMissingLowerChar_ReturnMessage(){
        String username = "username";
        String oldpassword = "Password123";
        String newpassword = "PASSWORD1";

        AccountEntity account = new AccountEntity();
        account.setUsername("username");
        account.setPassword("Password123");

        String response = accountController.changePassword(username, oldpassword, newpassword);
        assertEquals("Mật khẩu phải có ít nhất một chữ thường", response);
    }

    @Test
    public void testChangePasswordFailWithNewPasswordMissingNumber_ReturnMessage(){
        String username = "username";
        String oldpassword = "Password123";
        String newpassword = "Password";

        AccountEntity account = new AccountEntity();
        account.setUsername("username");
        account.setPassword("Password123");

        String response = accountController.changePassword(username, oldpassword, newpassword);
        assertEquals("Mật khẩu phải có ít nhất một ký tự số", response);
    }

    @Test
    public void testChangePasswordFailWithNewPasswordHavingSpecialChar_ReturnMessage(){
        String username = "username";
        String oldpassword = "Password123";
        String newpassword = "Password1@";

        AccountEntity account = new AccountEntity();
        account.setUsername("username");
        account.setPassword("Password123");

        String response = accountController.changePassword(username, oldpassword, newpassword);
        assertEquals("Mật khẩu không chứa các ký tự đặc biệt", response);
    }




}