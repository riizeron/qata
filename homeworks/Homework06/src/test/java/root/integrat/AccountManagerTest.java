package root.integrat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountManagerTest {

    AccountManager accountManager = new AccountManager() {
        @Override
        protected String makeSecure(String password) {
            return password;
        }
    };

    IServer server = mock(IServer.class);
    
    AccountManagerResponse response;

    String login = "login";
    String passwd = "passwd";
    String incorrectLogin = "incorrectLogin";
    String incorrectPasswd = "incorrectPasswd";
    String correctPasswd = "correctPasswd";
    String correctLogin = "correctLogin";
    String loggedLogin = "loggedLogin";
    Long sessionId = 1l;
    Long incorrectSessionId = 0l;

    double toWithdraw = 66;
    double currBalance = 5;
    double deposit = 100;

    /*
     * Перед каждым запуском теста
     * этот метод заново инициализирует объект менеджера,
     * реализует абстрактный метод класс менеджера,
     * мокает интерфейс общения с сервером,
     * задает менеджеру замоканый интерфейс
     */
    @BeforeEach
    protected void init() {
        assertNotNull(server);
        accountManager.init(server);
    }

    @Test 
    protected void callLoginWithIncorrectLoginReturnsNoUserIncorrectPassword() {

        when(server.login(incorrectLogin, passwd)).thenReturn(new ServerResponse(ServerResponse.NO_USER_INCORRECT_PASSWORD, null));

        response = accountManager.callLogin(incorrectLogin, passwd);

        assertEquals(AccountManagerResponse.NO_USER_INCORRECT_PASSWORD, response.code);
        assertNull(response.response);

    }

    @Test
    protected void callLoginWithIncorrectPasswordReturnsNoUserIncorrectPassword() {

        when(server.login(login, incorrectPasswd)).thenReturn(new ServerResponse(ServerResponse.NO_USER_INCORRECT_PASSWORD, null));

        response = accountManager.callLogin(login, incorrectPasswd);

        assertEquals(AccountManagerResponse.NO_USER_INCORRECT_PASSWORD, response.code);
        assertNull(response.response);
    }

    @Test
    protected void callLoginWithCorrectDataReturnsSessionNumberAndSucceed() {

        when(server.login(correctLogin, correctPasswd)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, sessionId));

        response = accountManager.callLogin(correctLogin, correctPasswd);

        assertEquals(AccountManagerResponse.SUCCEED, response.code);
        assertEquals(response.response, sessionId);
    }

    @Test
    protected void callLoginOnAlreadyLoggedUserReturnsAlreadyLogged() {
        
        when(server.login(loggedLogin, passwd)).thenReturn(new ServerResponse(ServerResponse.ALREADY_LOGGED, null));

        response = accountManager.callLogin(loggedLogin, passwd);

        assertEquals(AccountManagerResponse.ALREADY_LOGGED, response.code);
        assertNull(response.response);
    }

    @Test
    protected void callLoginWhenServerUndefinedErrorReturnsUndefinedError() {
        
        when(server.login(login, passwd)).thenReturn(new ServerResponse(ServerResponse.UNDEFINED_ERROR, null));

        response = accountManager.callLogin(login, passwd);

        assertEquals(AccountManagerResponse.UNDEFINED_ERROR, response.code);
        assertNull(response.response);
    }

    @Test
    protected void callLogoutWhenServerLogoutSuccessReturnsSucceed() {

        when(server.login(correctLogin, correctPasswd)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, sessionId));
        when(server.logout(sessionId)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, null));

        accountManager.callLogin(correctLogin, correctPasswd);
        response = accountManager.callLogout(correctLogin, sessionId);

        assertEquals(AccountManagerResponse.SUCCEED, response.code);
        assertNull(response.response);
    }

    @Test
    protected void callLogoutWhenServerNotLoggedReturnsNotLogged() {

        when(server.login(correctLogin, correctPasswd)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, sessionId));
        when(server.logout(sessionId)).thenReturn(new ServerResponse(ServerResponse.NOT_LOGGED, null));

        accountManager.callLogin(correctLogin, correctPasswd);
        response = accountManager.callLogout(correctLogin, sessionId);

        assertEquals(AccountManagerResponse.NOT_LOGGED, response.code);
        assertNull(response.response);
    }

    @Test
    protected void callLogoutOnIncorrectSessionIdReturnsIncorrectSession() {

        when(server.login(correctLogin, correctPasswd)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, sessionId));

        accountManager.callLogin(correctLogin, correctPasswd);
        response = accountManager.callLogout(correctLogin, incorrectSessionId);

        assertEquals(AccountManagerResponse.INCORRECT_SESSION, response.code);
        assertNull(response.response);
    }

    @Test
    protected void callLogoutWherServerUndefinedErrorReturnsUndefinedError() {

        when(server.login(correctLogin, correctPasswd)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, sessionId));
        when(server.logout(sessionId)).thenReturn(new ServerResponse(ServerResponse.UNDEFINED_ERROR, null));

        accountManager.callLogin(correctLogin, correctPasswd);
        response = accountManager.callLogout(correctLogin, sessionId);

        assertEquals(AccountManagerResponse.UNDEFINED_ERROR, response.code);
        assertNull(response.response);
    }


    @Test
    protected void withdrawOnServerNotLoggedReturnsNotLogged() {
        
        when(server.login(correctLogin, correctPasswd)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, sessionId));
        when(server.withdraw(sessionId, 666)).thenReturn(new ServerResponse(ServerResponse.NOT_LOGGED, null));

        accountManager.callLogin(correctLogin, correctPasswd);
        response = accountManager.withdraw(correctLogin, sessionId, 666);

        assertEquals(AccountManagerResponse.NOT_LOGGED, response.code);
        assertNull(response.response);
    }

    @Test
    protected void withdrawOnIncorrectSessionReturnsIncorrectSession() {

        when(server.login(correctLogin, correctPasswd)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, sessionId));

        accountManager.callLogin(correctLogin, correctPasswd);
        response = accountManager.withdraw(correctLogin, incorrectSessionId, 666);

        assertEquals(AccountManagerResponse.INCORRECT_SESSION, response.code);
        assertNull(response.response);
    }

    @Test
    protected void withdrawOnServerUndefinedErrorShouldReturnUndefinedError() {
     
        when(server.login(correctLogin, correctPasswd)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, sessionId));
        when(server.withdraw(sessionId, 666)).thenReturn(new ServerResponse(ServerResponse.UNDEFINED_ERROR, null));

        accountManager.callLogin(correctLogin, correctPasswd);
        response = accountManager.withdraw(correctLogin, sessionId, 666);

        assertEquals(AccountManagerResponse.UNDEFINED_ERROR, response.code);
        assertNull(response.response);
    }

    @Test
    protected void withdrawWhenServerNoMoneyShouldReturnNoMoney() {
        
        double toWithdraw = 10;
        when(server.login(correctLogin, correctPasswd)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, sessionId));
        when(server.withdraw(sessionId, toWithdraw)).thenReturn(new ServerResponse(ServerResponse.NO_MONEY, currBalance));

        accountManager.callLogin(correctLogin, correctPasswd);
        response = accountManager.withdraw(correctLogin, sessionId, toWithdraw);

        assertEquals(AccountManagerResponse.NO_MONEY, response.code);
        assertEquals(response.response, currBalance);
    }

    @Test
    protected void withdrawOnServerSuccessShouldReturnSucceed() {
        
        when(server.login(correctLogin, correctPasswd)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, sessionId));
        when(server.withdraw(sessionId, toWithdraw)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, currBalance));

        accountManager.callLogin(correctLogin, correctPasswd);
        response = accountManager.withdraw(correctLogin, sessionId, toWithdraw);

        assertEquals(AccountManagerResponse.SUCCEED, response.code);
        assertEquals(response.response, currBalance);
    }

    @Test
    protected void depositOnServerNotLoggedShouldReturnNotLogged() {
 
        when(server.login(correctLogin, correctPasswd)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, sessionId));
        when(server.deposit(sessionId, deposit)).thenReturn(new ServerResponse(ServerResponse.NOT_LOGGED, null));

        accountManager.callLogin(correctLogin, correctPasswd);
        response = accountManager.deposit(correctLogin, sessionId, deposit);

        assertEquals(AccountManagerResponse.NOT_LOGGED, response.code);
        assertNull(response.response);
    }

    @Test
    protected void depositOnIncorrectSessionShouldReturnIncorrectSession() {
       
        when(server.login(correctLogin, correctPasswd)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, sessionId));

        accountManager.callLogin(correctLogin, correctPasswd);
        response = accountManager.deposit(correctLogin, incorrectSessionId, 666);

        assertEquals(AccountManagerResponse.INCORRECT_SESSION, response.code);
        assertNull(response.response);
    }

    @Test
    protected void depositOnServerUndefinedErrorShouldReturnUndefinedError() {
        
        when(server.login(correctLogin, correctPasswd)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, sessionId));
        when(server.deposit(sessionId, 666)).thenReturn(new ServerResponse(ServerResponse.UNDEFINED_ERROR, null));

        accountManager.callLogin(correctLogin, correctPasswd);
        response = accountManager.deposit(correctLogin, sessionId, 666);

        assertEquals(AccountManagerResponse.UNDEFINED_ERROR, response.code);
        assertNull(response.response);
    }

    @Test
    protected void depositOnServerSuccessShouldReturnSuccess() {

        when(server.login(correctLogin, correctPasswd)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, sessionId));
        when(server.deposit(sessionId, toWithdraw)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, currBalance));

        accountManager.callLogin(correctLogin, correctPasswd);
        response = accountManager.deposit(correctLogin, sessionId, toWithdraw);

        assertEquals(AccountManagerResponse.SUCCEED, response.code);
        assertEquals(currBalance, response.response);
    }

    @Test
    protected void getBalanceOnServerNotLoggedShouldReturnNotLogged() {

        when(server.login(correctLogin, correctPasswd)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, sessionId));
        when(server.getBalance(sessionId)).thenReturn(new ServerResponse(ServerResponse.NOT_LOGGED, null));

        accountManager.callLogin(correctLogin, correctPasswd);
        response = accountManager.getBalance(correctLogin, sessionId);

        assertEquals(AccountManagerResponse.NOT_LOGGED, response.code);
        assertNull(response.response);
    }

    @Test
    protected void getBalanceOnIncorrectSessionShouldReturnIncorrectSession() {
 
        when(server.login(correctLogin, correctPasswd)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, sessionId));

        accountManager.callLogin(correctLogin, correctPasswd);
        response = accountManager.getBalance(correctLogin, incorrectSessionId);

        assertEquals(AccountManagerResponse.INCORRECT_SESSION, response.code);
        assertNull(response.response);
    }

    @Test
    protected void getBalanceOnServerUndefinedErrorShouldReturnUndefinedError() {
  
        when(server.login(correctLogin, correctPasswd)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, sessionId));
        when(server.getBalance(sessionId)).thenReturn(new ServerResponse(ServerResponse.UNDEFINED_ERROR, null));

        accountManager.callLogin(correctLogin, correctPasswd);
        response = accountManager.getBalance(correctLogin, sessionId);

        assertEquals(AccountManagerResponse.UNDEFINED_ERROR, response.code);
        assertNull(response.response);
    }

    @Test
    protected void getBalanceOnServerSuccessShouldReturnSuccess() {
        
        when(server.login(correctLogin, correctPasswd)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, sessionId));
        when(server.getBalance(sessionId)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, currBalance));

        accountManager.callLogin(correctLogin, correctPasswd);
        response = accountManager.getBalance(correctLogin, sessionId);

        assertEquals(AccountManagerResponse.SUCCEED, response.code);
        assertEquals(currBalance, response.response);
    }

    @Test
    protected void callLogoutWhenUserNotLoggedReturnsNotLogged() {
        
        response = accountManager.callLogout(login, sessionId);

        assertEquals(AccountManagerResponse.NOT_LOGGED, response.code);
        assertNull(response.response);
    }

    @Test
    protected void withdrawOnNotLoggedUserReturnsNotLogged() {

        response = accountManager.withdraw(login, sessionId, 666);

        assertEquals(AccountManagerResponse.NOT_LOGGED, response.code);
        assertNull(response.response);
    }

    @Test
    protected void depositOnNotLoggedUserShouldReturnNotLogged() {

        response = accountManager.deposit(correctLogin, sessionId, 666);

        assertEquals(AccountManagerResponse.NOT_LOGGED, response.code);
        assertNull(response.response);
    }

    @Test
    protected void getBalanceOnNotLoggedUserShouldReturnNotLogged() {

        response = accountManager.getBalance(correctLogin, sessionId);

        assertEquals(AccountManagerResponse.NOT_LOGGED, response.code);
        assertNull(response.response);
    }

    @Test
    protected void scenarioPremier() {
        
        double deposit = 1000;

        when(server.login(incorrectLogin, correctPasswd)).thenReturn(new ServerResponse(ServerResponse.NO_USER_INCORRECT_PASSWORD, null));
        when(server.login(correctLogin, incorrectPasswd)).thenReturn(new ServerResponse(ServerResponse.NO_USER_INCORRECT_PASSWORD, null));
        when(server.login(correctLogin, correctPasswd)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, sessionId));
        
        when(server.getBalance(sessionId)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, currBalance));
        when(server.deposit(sessionId, deposit)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, currBalance + deposit));
        
        response = accountManager.callLogin(incorrectLogin, correctPasswd);
        assertEquals(AccountManagerResponse.NO_USER_INCORRECT_PASSWORD, response.code);
        assertNull(response.response);

        response = accountManager.callLogin(correctLogin, incorrectPasswd);
        assertEquals(AccountManagerResponse.NO_USER_INCORRECT_PASSWORD, response.code);
        assertNull(response.response);

        response = accountManager.callLogin(correctLogin, correctPasswd);
        assertEquals(AccountManagerResponse.SUCCEED, response.code);
        assertEquals(sessionId, response.response);

        response = accountManager.getBalance(correctLogin, sessionId);
        assertEquals(AccountManagerResponse.SUCCEED, response.code);
        assertEquals(currBalance, response.response);

        response = accountManager.deposit(correctLogin, sessionId, deposit);
        assertEquals(AccountManagerResponse.SUCCEED, response.code);
        assertEquals(currBalance + deposit, response.response);
    }

    @Test
    protected void scenarioDeuxieme() {

        when(server.login(correctLogin, correctPasswd)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, sessionId));
        when(server.withdraw(sessionId, toWithdraw)).thenReturn(new ServerResponse(ServerResponse.NO_MONEY, currBalance));
        when(server.getBalance(sessionId)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, currBalance));

        response = accountManager.callLogin(correctLogin, correctPasswd);
        assertEquals(AccountManagerResponse.SUCCEED, response.code);
        assertEquals(sessionId, response.response);

        response = accountManager.withdraw(correctLogin, sessionId, toWithdraw);
        assertEquals(AccountManagerResponse.NO_MONEY, response.code);
        assertEquals(currBalance, response.response);

        response = accountManager.getBalance(correctLogin, sessionId);
        assertEquals(AccountManagerResponse.SUCCEED, response.code);
        assertEquals(currBalance, response.response);

        when(server.deposit(sessionId, deposit)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, currBalance + deposit));
        when(server.withdraw(sessionId, toWithdraw)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, currBalance + deposit - toWithdraw));
        when(server.logout(sessionId)).thenReturn(new ServerResponse(ServerResponse.SUCCESS, null));

        AccountManagerResponse response = accountManager.deposit(correctLogin, sessionId, deposit);
        assertEquals(AccountManagerResponse.SUCCEED, response.code);
        assertEquals(currBalance + deposit, response.response);

        response = accountManager.withdraw(correctLogin, incorrectSessionId, toWithdraw);
        assertEquals(AccountManagerResponse.INCORRECT_SESSION, response.code);
        assertNull(response.response);

        response = accountManager.withdraw(correctLogin, sessionId, toWithdraw);
        assertEquals(AccountManagerResponse.SUCCEED, response.code);
        assertEquals(currBalance + deposit - toWithdraw, response.response);

        response = accountManager.callLogout(correctLogin, sessionId);
        assertEquals(AccountManagerResponse.SUCCEED, response.code);
        assertNull(response.response);
    }
}