package com.example.kite_master;
import com.team100.kite_master.forum.forum_data_classes.DateUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DateUtilTest {

    DateUtil d;


    @Before
    public void setUp(){
        d = mock(DateUtil.class);
    }

    @Test
    public void testCleanDate()  {
        when(d.getCleanDate(1551997448, "MM/dd/yy hh:mma")).thenCallRealMethod();
        assertEquals(d.getCleanDate(1551997448, "MM/dd/yy hh:mma"), "3/07/19 4:24pm");
    }

    @Test
    public void testCleanDate2()  {
        when(d.getCleanDate(1554495076, "MM/dd/yy hh:mma")).thenCallRealMethod();
        assertEquals(d.getCleanDate(1554495076, "MM/dd/yy hh:mma"), "4/05/19 3:11pm");
    }

    @Test
    public void testCleanDate3()  {
        when(d.getCleanDate(1553969660, "MM/dd/yy hh:mma")).thenCallRealMethod();
        assertEquals(d.getCleanDate(1553969660, "MM/dd/yy hh:mma"), "3/30/19 1:14pm");
    }

    @Test
    public void testTimeAgo1()  {
        when(d.getTimeAgo(1551997448)).thenReturn("30 days ago");
        assertEquals(d.getTimeAgo(1551997448), "30 days ago");
    }

    @Test
    public void testTimeAgo2()  {
        when(d.getTimeAgo(1554585008)).thenReturn("3 hours ago");
        assertEquals(d.getTimeAgo(1554585008), "30 days ago");
    }

    @Test
    public void testTimeAgo3()  {
        when(d.getTimeAgo(1554239393)).thenReturn("4 days ago");
        assertEquals(d.getTimeAgo(1554239393), "4 days ago");
    }











}
