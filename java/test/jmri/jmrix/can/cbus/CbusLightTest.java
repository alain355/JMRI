package jmri.jmrix.can.cbus;

import jmri.jmrix.can.TrafficControllerScaffold;
import jmri.jmrix.can.CanMessage;
import jmri.jmrix.can.CanReply;
import jmri.Light;
import jmri.util.JUnitAppender;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Paul Bender Copyright (C) 2017
 * @author Steve Young Copyright (C) 2018
 */
public class CbusLightTest extends jmri.implementation.AbstractLightTestBase {

    private TrafficControllerScaffold tcis = null;

    @Override
    public int numListeners() {
        return tcis.numListeners();
    }
    
    @Override
    public void checkOnMsgSent() {
        Assert.assertEquals("ON message", "[78] 90 01 C8 01 41",
        tcis.outbound.elementAt(tcis.outbound.size() - 1).toString());
        Assert.assertEquals("ON state", jmri.Light.ON, t.getState());
    }

    @Override
    public void checkOffMsgSent() {
        Assert.assertEquals("OFF message", "[78] 91 01 C8 01 41",
        tcis.outbound.elementAt(tcis.outbound.size() - 1).toString());
        Assert.assertEquals("OFF state", jmri.Light.OFF, t.getState());
    }    

    @Test
    public void testNullEvent() {
        try {
            CbusLight t = new CbusLight("ML",null,tcis);
            Assert.fail("Should have thrown an exception");
        } catch (NullPointerException e) {
            Assert.assertTrue(true);
        }
    }


    @Test
    public void testCTorShortEventSingle() {
        CbusLight t = new CbusLight("ML","+7",tcis);
        Assert.assertNotNull("exists",t);
    }
    
    @Test
    public void testCTorShortEventDouble() {
        CbusLight t = new CbusLight("ML","+1;-1",tcis);
        Assert.assertNotNull("exists",t);
    }
    
    
    @Test
    public void testLongEventSingleNoN() {
        CbusLight t = new CbusLight("ML","+654e321",tcis);
        Assert.assertNotNull("exists",t);
    }    


    @Test
    public void testLongEventDoubleNoN() {
        CbusLight t = new CbusLight("ML","-654e321;+123e456",tcis);
        Assert.assertNotNull("exists",t);
    }    
    
    
    @Test
    public void testCTorLongEventSingle() {
        CbusLight t = new CbusLight("ML","+n654e321",tcis);
        Assert.assertNotNull("exists",t);
    }    
    
    @Test
    public void testCTorLongEventDouble() {
        CbusLight t = new CbusLight("ML","+N299E17;-N123E456",tcis);
        Assert.assertNotNull("exists",t);
    }
    
    @Test
    public void testCTorHexEventJustOpsCode() {
        CbusLight t = new CbusLight("ML","X04;X05",tcis);
        Assert.assertNotNull("exists",t);
    }

    @Test
    public void testCTorHexEventOneByte() {
        CbusLight t = new CbusLight("ML","X2301;X30FF",tcis);
        Assert.assertNotNull("exists",t);
    }
    
    
    @Test
    public void testCTorHexEventTwoByte() {
        CbusLight t = new CbusLight("ML","X410001;X56FFFF",tcis);
        Assert.assertNotNull("exists",t);
    }

    
    @Test
    public void testCTorHexEventThreeByte() {
        CbusLight t = new CbusLight("ML","X6000010001;X72FFFFFF",tcis);
        Assert.assertNotNull("exists",t);
    }    
    
    
    
    @Test
    public void testCTorHexEventFourByte() {
        CbusLight t = new CbusLight("ML","X9000010001;X91FFFFFFFF",tcis);
        Assert.assertNotNull("exists",t);
    }


    @Test
    public void testCTorHexEventFiveByte() {
        CbusLight t = new CbusLight("ML","XB00D60010001;XB1FFFAAFFFFF",tcis);
        Assert.assertNotNull("exists",t);
    }


    @Test
    public void testCTorHexEventSixByte() {
        CbusLight t = new CbusLight("ML","XD00D0060010001;XD1FFFAAAFFFFFE",tcis);
        Assert.assertNotNull("exists",t);
    }
    
    
    @Test
    public void testCTorHexEventSevenByte() {
        CbusLight t = new CbusLight("ML","XF00D0A0600100601;XF1FFFFAAFAFFFFFE",tcis);
        Assert.assertNotNull("exists",t);
    }


    @Test
    public void threePartFail() {
        CbusLight t = new CbusLight("ML","+7;-5;+11",tcis);
        JUnitAppender.assertErrorMessageStartsWith("Can't parse CbusSensor system name");
    }

    @Test
    public void badSysNameErrorLog() {
        
        CbusLight t = new CbusLight("ML","+7;-5;+11",tcis);
        JUnitAppender.assertErrorMessageStartsWith("Can't parse CbusSensor system name");
        
        CbusLight t2 = new CbusLight("ML","X;+N15E6",tcis);
        JUnitAppender.assertErrorMessageStartsWith("Did not find usable sys");        
        
        CbusLight t3 = new CbusLight("ML","XA;+N15E6",tcis);
        JUnitAppender.assertErrorMessageStartsWith("Did not find usable sys");        
        
        CbusLight t4 = new CbusLight("ML","XABC;+N15E6",tcis);
        JUnitAppender.assertErrorMessageStartsWith("Did not find usable sys");        
        
        CbusLight t5 = new CbusLight("ML","XABCDE;+N15E6",tcis);
        JUnitAppender.assertErrorMessageStartsWith("Did not find usable sys");        
        
        CbusLight t6 = new CbusLight("ML","XABCDEF0;+N15E6",tcis);
        JUnitAppender.assertErrorMessageStartsWith("Did not find usable sys");        
        
        CbusLight t7 = new CbusLight("ML","XABCDEF",tcis);
        JUnitAppender.assertErrorMessageStartsWith("can't make 2nd event from");        
        
        CbusLight t8 = new CbusLight("ML",";XABCDEF",tcis);
        JUnitAppender.assertErrorMessageStartsWith("Did not find usable sys");

        CbusLight t9 = new CbusLight("ML","XABCDEF;",tcis);
        JUnitAppender.assertErrorMessageStartsWith("Did not find usable sys");

        CbusLight t10 = new CbusLight("ML",";",tcis);
        JUnitAppender.assertErrorMessageStartsWith("Did not find usable sys");

        CbusLight t11 = new CbusLight("ML",";+N15E6",tcis);
        JUnitAppender.assertErrorMessageStartsWith("Did not find usable sys");

        CbusLight t12 = new CbusLight("ML","++N156E77",tcis);
        JUnitAppender.assertErrorMessageStartsWith("Did not find usable sys");
        
        CbusLight t13 = new CbusLight("ML","--N156E77",tcis);
        JUnitAppender.assertErrorMessageStartsWith("Did not find usable sys");
        
        CbusLight t14 = new CbusLight("ML","N156E+77",tcis);
        JUnitAppender.assertErrorMessageStartsWith("Did not find usable sys");
        
        CbusLight t15 = new CbusLight("ML","N156+E77",tcis);
        JUnitAppender.assertErrorMessageStartsWith("Did not find usable sys");
        
        CbusLight t16 = new CbusLight("ML","XLKJK;XLKJK",tcis);
        JUnitAppender.assertErrorMessageStartsWith("Did not find usable sys");
        
    }

    @Test
    public void testShortEventSinglegetAddrOn() {
        CbusLight t = new CbusLight("ML","+7",tcis);
        CanMessage m1 = t.getAddrOn();
        CanMessage m2 = new CanMessage(tcis.getCanid());
        m2.setNumDataElements(5);
        m2.setElement(0, 0x98); // ASON OPC
        m2.setElement(1, 0x00);
        m2.setElement(2, 0x00);
        m2.setElement(3, 0x00);
        m2.setElement(4, 0x07);
        Assert.assertTrue("equals same", m1.equals(m2));
    }

    @Test
    public void testShortEventSinglegetAddrOff() {
        CbusLight t = new CbusLight("ML","+7",tcis);
        CanMessage m1 = t.getAddrOff();
        CanMessage m2 = new CanMessage(tcis.getCanid());
        m2.setNumDataElements(5);
        m2.setElement(0, 0x99); // ASOF OPC
        m2.setElement(1, 0x00);
        m2.setElement(2, 0x00);
        m2.setElement(3, 0x00);
        m2.setElement(4, 0x07);
        Assert.assertTrue("equals same", m1.equals(m2));
    }

    @Test
    public void testLongEventgetAddrOn() {
        CbusLight t = new CbusLight("ML","+N54321E12345",tcis);
        CanMessage m1 = t.getAddrOn();
        CanMessage m2 = new CanMessage(tcis.getCanid());
        m2.setNumDataElements(5);
        m2.setElement(0, 0x90); // ACON OPC
        m2.setElement(1, 0xd4);
        m2.setElement(2, 0x31);
        m2.setElement(3, 0x30);
        m2.setElement(4, 0x39);
        Assert.assertTrue("equals same", m1.equals(m2));
    }

    @Test
    public void testLongEventgetAddrOff() {
        CbusLight t = new CbusLight("ML","+N54321E12345",tcis);
        CanMessage m1 = t.getAddrOff();
        CanMessage m2 = new CanMessage(tcis.getCanid());
        m2.setNumDataElements(5);
        m2.setElement(0, 0x91); // ACOF OPC
        m2.setElement(1, 0xd4);
        m2.setElement(2, 0x31);
        m2.setElement(3, 0x30);
        m2.setElement(4, 0x39);
        Assert.assertTrue("equals same", m1.equals(m2));
        m2.setElement(0, 0x90); // ACON OPC
        Assert.assertFalse("not equals same", m1.equals(m2));
    }    
    
    @Test
    public void testCbusLightCanMessage() throws jmri.JmriException {
        CbusLight t = new CbusLight("ML","+N54321E12345",tcis);
        Assert.assertTrue(t.getState() == Light.OFF); // Light.UNKNOWN ??
        CanMessage m = new CanMessage(tcis.getCanid());
        m.setNumDataElements(5);
        m.setElement(0, 0x95); // EVULN OPC
        m.setElement(1, 0xd4);
        m.setElement(2, 0x31);
        m.setElement(3, 0x30);
        m.setElement(4, 0x39);
        t.message(m);
        
        Assert.assertTrue(t.getState() == Light.OFF ); 

        m.setElement(0, 0x90); // ACON OPC
        t.message(m);
        Assert.assertTrue(t.getState() == Light.ON);

        m.setElement(0, 0x91); // ACOF OPC
        t.message(m);
        Assert.assertTrue(t.getState() == Light.OFF);
 
    }

    @Test
    public void testCbusLightCanReply() throws jmri.JmriException {
        CbusLight t = new CbusLight("ML","+N54321E12345",tcis);
        Assert.assertTrue(t.getState() == Light.OFF);  // Light.UNKNOWN ??
        CanReply r = new CanReply(tcis.getCanid());
        r.setNumDataElements(5);
        r.setElement(0, 0x95); // EVULN OPC
        r.setElement(1, 0xd4);
        r.setElement(2, 0x31);
        r.setElement(3, 0x30);
        r.setElement(4, 0x39);
        t.reply(r);
        Assert.assertTrue(t.getState() == Light.OFF);        

        r.setElement(0, 0x90); // ACON OPC
        t.reply(r);
        Assert.assertTrue(t.getState() == Light.ON);

        r.setElement(0, 0x91); // ACOF OPC
        t.reply(r);
        Assert.assertTrue(t.getState() == Light.OFF);
  
    }

    public void checkStatusRequestMsgSent() {
        Assert.assertEquals("same object", ("[78] 92 01 C8 01 41"), 
            (tcis.outbound.elementAt(tcis.outbound.size() - 1).toString()));
    }    

    public void checkShortStatusRequestMsgSent() {
        Assert.assertEquals("same object", ("[78] 9A 00 00 D4 31"), 
            (tcis.outbound.elementAt(tcis.outbound.size() - 1).toString()));
    } 

    @Test
    public void testRequestUpdate() {
        
        t.requestUpdateFromLayout();
        checkStatusRequestMsgSent();

        t = new CbusLight("ML","+54321",tcis);
        t.requestUpdateFromLayout();
        checkShortStatusRequestMsgSent();

    }
    
    @Test
    public void testAddRemoveListener() {    
        int testnum = numListeners();
        CbusLight t2 = new CbusLight("ML", "+N777E8321",tcis);
        int testnum2 = numListeners();
        Assert.assertEquals("number increased",testnum , testnum2-1 );
        t2.dispose();
        int testnum3 = numListeners();
        Assert.assertEquals("number decreased",testnum , testnum3 );
        
    }

    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        tcis = new TrafficControllerScaffold();
        t = new CbusLight("ML", "+N456E321",tcis);
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
        t.dispose();
        tcis=null;
    }

     private final static Logger log = LoggerFactory.getLogger(CbusLightTest.class);

}
