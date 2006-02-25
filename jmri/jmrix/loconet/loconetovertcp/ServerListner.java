package jmri.jmrix.loconet.loconetovertcp;

/**
 * Implementation of the LocoNetOverTcp LbServer Server Protocol
 *
 * @author      Alex Shepherd Copyright (C) 2006
 * @version	$Revision: 1.1 $
 */

public interface ServerListner {
  public void notifyServerStateChanged( Server s );
  public void notifyClientStateChanged( Server s );
}
