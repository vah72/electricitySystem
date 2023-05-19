import com.electricitysystem.ElectricitySystemApplication;
import com.electricitysystem.controller.AccountControllerTest;
import com.electricitysystem.controller.ElectricBoardControllerTest;
import com.electricitysystem.service.CalculatorServiceTest;
import org.junit.Test;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Suite
@SelectPackages({"com.electricitysystem.controller","com.electricitysystem.repository"})
@SelectClasses({CalculatorServiceTest.class})
public class ElectricSystemApplicationTests {
    @Test
    public void context(){

    }
}
