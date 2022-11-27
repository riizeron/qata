package root.vending;

import root.vending.VendingMachine.Response;
import root.vending.VendingMachine.Mode;

import org.junit.*;
import static org.junit.Assert.*;

public class VendingMachineTest {

    private long id = 117345294655382L;
    Response r;
    VendingMachine vm;

    @Test 
    public void InitialTest() {
        assertTrue(true);
    }

    @Test
    public void 


    @Test
    public void fillCoinsIfNotAdminShouldReturnIllegalOperation() {
        vm = new VendingMachine();
        r = vm.fillCoins(0, 0);
        if (vm.getCurrentMode() == Mode.ADMINISTERING) {
            assertEquals(r, Response.ILLEGAL_OPERATION);
        }
    }

    @Test
    public void enterAdminModeWithIncorrectIdShouldReturnInvalidParam() {
        vm = new VendingMachine();
        r = vm.enterAdminMode(0);
        assertEquals(r, Response.INVALID_PARAM);
    }

    @Test
    public void enterAdminModeOnNonZeroBalanceShouldReturnUnsuitableChange() {
        vm = new VendingMachine();
        vm.putCoin1();

        r = vm.enterAdminMode(id);
        assertEquals(r, Response.UNSUITABLE_CHANGE);
    } 

    @Test
    public void fillCoinsOnValueLowerThanZeroShouldReturnResponseInvalidParam() {
        vm = new VendingMachine();
        vm.enterAdminMode(id);
        
        vm.enterAdminMode(0);

        r = vm.fillCoins(-1, 1);
        assertEquals(r, Response.INVALID_PARAM);

        r = vm.fillCoins(1, -1);
        assertEquals(r, Response.INVALID_PARAM);

        r = vm.fillCoins(-1, -1);
        assertEquals(r, Response.INVALID_PARAM);
    }
}
