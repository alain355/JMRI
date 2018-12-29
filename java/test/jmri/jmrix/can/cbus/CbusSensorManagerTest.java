package jmri.jmrix.can.cbus;

import jmri.Sensor;
import jmri.jmrix.can.CanSystemConnectionMemo;
import jmri.jmrix.can.TrafficControllerScaffold;
import jmri.util.JUnitAppender;
import jmri.util.JUnitUtil;
import org.junit.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests for the jmri.jmrix.can.cbus.CbusSensorManager class.
 *
 * @author	Bob Jacobsen Copyright 2008
 * @author	Paul Bender Copyright (C) 2016
 */
public class CbusSensorManagerTest extends jmri.managers.AbstractSensorMgrTestBase {

    private CanSystemConnectionMemo memo = null;
    private TrafficControllerScaffold tcis;

    @Override
    public String getSystemName(int i) {
        return "MSX0A;+N15E" + i;
    }

    @Test
    @Override
    public void testCreate() {
        Assert.assertNotNull("createsSensor", l.provideSensor(memo.getSystemPrefix() + "SX0A;+N15E6"));
    }

    @Test
    @Override
    public void testDefaultSystemName() {
        // create
        Sensor t = l.provideSensor("MSX0A;+N15E" + getNumToTest1());
        // check
        Assert.assertTrue("real object returned ", t != null);
        Assert.assertTrue("system name correct ", t == l.getBySystemName(getSystemName(getNumToTest1())));
    }

    @Test
    @Override
    public void testProvideName() {
        // create
        Sensor t = l.provide("" + getSystemName(getNumToTest1()));
        // check
        Assert.assertTrue("real object returned ", t != null);
        Assert.assertTrue("system name correct ", t == l.getBySystemName(getSystemName(getNumToTest1())));
    }


    @Test
    public void testLowerLower() {
        Sensor t = l.provideSensor("ms+n1e77;-n1e45");
        Assert.assertNotNull("exists",t);
        Assert.assertTrue("event lowercase",t == l.getSensor(t.getSystemName()));
        Sensor t2 = l.provideSensor("msxabcdef;xfedcba");
        Assert.assertNotNull("exists",t2);
        Assert.assertTrue("hex lowercase",t2 == l.getSensor(t2.getSystemName()));
    }

    @Test
    public void testLowerUpper() {
        Sensor t = l.provideSensor("ms+n1e77;-n1e45");
        Assert.assertNotNull("exists",t);
        Assert.assertTrue("event lower upper",t == l.getSensor(t.getSystemName().toUpperCase()));
        Sensor t2 = l.provideSensor("msxabcdef;xfedcba");
        Assert.assertNotNull("exists",t2);
        Assert.assertTrue("hex lower upper",t2 == l.getSensor(t2.getSystemName().toUpperCase()));
    }

    @Override
    @Test
    public void testUpperLower() {
        Sensor t = l.provideSensor("MSXABCDEF01;XFFEDCCBA");
        Assert.assertTrue(" hex upper lower",t == l.getSensor(t.getSystemName().toLowerCase()));
        Sensor t2 = l.provideSensor("MS-N66E1;+N15E789");
        Assert.assertTrue("event upper lower",t2 == l.getSensor(t2.getSystemName().toLowerCase()));
    }

    @Override
    @Test
    public void testMoveUserName() {
        Sensor t1 = l.provideSensor("MSX0A;+N15E" + getNumToTest1());
        Sensor t2 = l.provideSensor("MSX0A;+N15E" + getNumToTest2());
        t1.setUserName("UserName");
        Assert.assertTrue(t1 == l.getByUserName("UserName"));

        t2.setUserName("UserName");
        Assert.assertTrue(t2 == l.getByUserName("UserName"));

        Assert.assertTrue(null == t1.getUserName());
    }
    
    @Test
    public void testBadCbusSensorAddresses() {

        try {
            Sensor t1 = l.provideSensor("MS+N15E6");
            Assert.assertTrue( t1 != null );
        } catch (Exception e) {
            Assert.fail("Should NOT have thrown an exception");
        }

        try {
            l.provideSensor("MSX;+N15E6");
            Assert.fail("X Should have thrown an exception");
        } catch (Exception e) {
            JUnitAppender.assertErrorMessageStartsWith("java.lang.IllegalArgumentException: Did not find");
            Assert.assertTrue(true);
        }

        try {
            l.provideSensor("MSXA;+N15E6");
            Assert.fail("A Should have thrown an exception");
        } catch (Exception e) {
            JUnitAppender.assertErrorMessageStartsWith("java.lang.IllegalArgumentException: Did not find");
            Assert.assertTrue(true);
        }

        try {
            l.provideSensor("MSXABC;+N15E6");
            Assert.fail("AC Should have thrown an exception");
        } catch (Exception e) {
            JUnitAppender.assertErrorMessageStartsWith("java.lang.IllegalArgumentException: Did not find");
            Assert.assertTrue(true);
        }

        try {
            l.provideSensor("MSXABCDE;+N15E6");
            Assert.fail("ABCDE Should have thrown an exception");
        } catch (Exception e) {
            JUnitAppender.assertErrorMessageStartsWith("java.lang.IllegalArgumentException: Did not find");
            Assert.assertTrue(true);
        }
        
        try {
            l.provideSensor("MSXABCDEF0;+N15E6");
            Assert.fail("ABCDEF0 Should have thrown an exception");
        } catch (Exception e) {
            JUnitAppender.assertErrorMessageStartsWith("java.lang.IllegalArgumentException: Did not find");
            Assert.assertTrue(true);
        }

        try {
            l.provideSensor("MSXABCDEF");
            Assert.fail("Single hex Should have thrown an exception");
        } catch (Exception e) {
            JUnitAppender.assertErrorMessageStartsWith("java.lang.IllegalArgumentException: can't make 2nd event");
            Assert.assertTrue(true);
        }

        try {
            l.provideSensor("MS;XABCDEF");
            Assert.fail("Single hex ; Should have thrown an exception");
        } catch (Exception e) {
            JUnitAppender.assertErrorMessageStartsWith("java.lang.IllegalArgumentException: Did not find usable ");
            Assert.assertTrue(true);
        }

        try {
            l.provideSensor("MSXABCDEF;");
            Assert.fail("Single hex ; Should have thrown an exception");
        } catch (Exception e) {
            JUnitAppender.assertErrorMessageStartsWith("java.lang.IllegalArgumentException: Did not find usable ");
            Assert.assertTrue(true);
        }
        
        try {
            l.provideSensor("MS;");
            Assert.fail("; no arg Should have thrown an exception");
        } catch (Exception e) {
            JUnitAppender.assertErrorMessageStartsWith("java.lang.IllegalArgumentException: Did not find");
            Assert.assertTrue(true);
        }        
        
        try {
            l.provideSensor("MS;+N15E6");
            Assert.fail("MS Should have thrown an exception");
        } catch (Exception e) {
            JUnitAppender.assertErrorMessageStartsWith("java.lang.IllegalArgumentException: Did not find");
            Assert.assertTrue(true);
        }
        
        try {
            l.provideSensor(";+N15E6");
            Assert.fail("; Should have thrown an exception");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }

        try {
            l.provideSensor("S+N156E77;+N15E6");
            Assert.fail("S Should have thrown an exception");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
        
        try {
            l.provideSensor("M+N156E77;+N15E6");
            Assert.fail("M Should have thrown an exception");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }

        try {
            l.provideSensor("MS++N156E77");
            Assert.fail("++ Should have thrown an exception");
        } catch (Exception e) {
            JUnitAppender.assertErrorMessageStartsWith("java.lang.IllegalArgumentException: Did not find");
            Assert.assertTrue(true);
        }

        try {
            l.provideSensor("MS--N156E77");
            Assert.fail("-- Should have thrown an exception");
        } catch (Exception e) {
            JUnitAppender.assertErrorMessageStartsWith("java.lang.IllegalArgumentException: Did not find");
            Assert.assertTrue(true);
        }

        try {
            l.provideSensor("MSN156E+77");
            Assert.fail("E+ Should have thrown an exception");
        } catch (Exception e) {
            JUnitAppender.assertErrorMessageStartsWith("java.lang.IllegalArgumentException: Did not find");
            Assert.assertTrue(true);
        }

        try {
            l.provideSensor("MSN156+E77");
            Assert.fail("+E Should have thrown an exception");
        } catch (Exception e) {
            JUnitAppender.assertErrorMessageStartsWith("java.lang.IllegalArgumentException: Did not find");
            Assert.assertTrue(true);
        }

        try {
            l.provideSensor("MSXLKJK;XLKJK");
            Assert.fail("LKJK Should have thrown an exception");
        } catch (Exception e) {
            JUnitAppender.assertErrorMessageStartsWith("java.lang.IllegalArgumentException: Did not find");
            Assert.assertTrue(true);
        }

        try {
            l.provideSensor("MS+7;-5;+11");
            Assert.fail("3 split Should have thrown an exception");
        } catch (Exception e) {
            JUnitAppender.assertErrorMessageStartsWith("java.lang.IllegalArgumentException: Wrong number of events");
            Assert.assertTrue(true);
        }
    }
    
    @Test
    public void testGoodCbusSensorAddresses() {
        
        Sensor t = l.provideSensor("MS+7");
        Assert.assertNotNull("exists",t);

        Sensor t2 = l.provideSensor("MS+1;-1");
        Assert.assertNotNull("exists",t2);
        
        Sensor t3 = l.provideSensor("MS+654e321");
        Assert.assertNotNull("exists",t3);

        Sensor t4 = l.provideSensor("MS-654e321;+123e456");
        Assert.assertNotNull("exists",t4);

        Sensor t5 = l.provideSensor("MS+n654e321");
        Assert.assertNotNull("exists",t5);

        Sensor t6 = l.provideSensor("MS+N299E17;-N123E456");
        Assert.assertNotNull("exists",t6);

        Sensor t7 = l.provideSensor("MSX04;X05");
        Assert.assertNotNull("exists",t7);

        Sensor t8 = l.provideSensor("MSX2301;X30FF");
        Assert.assertNotNull("exists",t8);

        Sensor t9 = l.provideSensor("MSX410001;X56FFFF");
        Assert.assertNotNull("exists",t9);

        Sensor t10 = l.provideSensor("MSX6000010001;X72FFFFFF");
        Assert.assertNotNull("exists",t10);

        Sensor t11 = l.provideSensor("MSX9000010001;X91FFFFFFFF");
        Assert.assertNotNull("exists",t11);

        Sensor t12 = l.provideSensor("MSXB00D60010001;XB1FFFAAFFFFF");
        Assert.assertNotNull("exists",t12);

        Sensor t13 = l.provideSensor("MSXD00D0060010001;XD1FFFAAAFFFFFE");
        Assert.assertNotNull("exists",t13);

        Sensor t14 = l.provideSensor("MSXF00D0A0600100601;XF1FFFFAAFAFFFFFE");
        Assert.assertNotNull("exists",t14);
        
    }




    @Test
    public void testQueryAll() {
        tcis.outbound.clear();
        Sensor t1 = l.provideSensor("MS+N123E456");
        Sensor t2 = l.provideSensor("MS-N9875E45670");

        Assert.assertTrue(0 == tcis.outbound.size());
        
        l.updateAll();
        
        // log.warn("size {}",tcis.outbound);
        Assert.assertTrue(2 == tcis.outbound.size());
        
        Sensor t3 = l.provideSensor("MSX0A;X5E6DEEF4");
        tcis.outbound.clear();
        l.updateAll();
        Assert.assertTrue(3 == tcis.outbound.size());

    }













    
    @Test
    public void testgetEntryToolTip() {
        String x = l.getEntryToolTip();
        Assert.assertTrue(x.contains("<html>"));
    }
    
    // The minimal setup for log4J
    @Override
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        memo = new CanSystemConnectionMemo();
        tcis = new TrafficControllerScaffold();
        memo.setTrafficController(tcis);
        l = new CbusSensorManager(memo);
    }

    @After
    public void tearDown() {
        l.dispose();
        memo.dispose();
        JUnitUtil.tearDown();
    }
    private final static Logger log = LoggerFactory.getLogger(CbusSensorManagerTest.class);
}
