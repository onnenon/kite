package com.example.httpcommunicationwithjwt;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    /*

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    */

    private MainActivity main;

    @Before
    public void setup() {

        // main = new MainActivity();
        main = mock(MainActivity.class);
    }

    @Test
    public void foo() {

        String username = "fadmin";
        String password = "pass";

        main.login(username, password);

        verify(main, times(1)).login(username, password);
    }

    @Test
    public void foo2() {

        main.GetHTTPRequest();

        verify(main, times(1)).GetHTTPRequest();
    }


}