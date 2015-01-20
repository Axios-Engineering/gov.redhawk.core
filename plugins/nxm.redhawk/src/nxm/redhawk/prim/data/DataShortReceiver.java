/** 
 * This file is protected by Copyright. 
 * Please refer to the COPYRIGHT file distributed with this source distribution.
 * 
 * This file is part of REDHAWK IDE.
 * 
 * All rights reserved.  This program and the accompanying materials are made available under 
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 *
 */
package nxm.redhawk.prim.data;

import nxm.redhawk.prim.corbareceiver;
import nxm.sys.inc.DataTypes;

import org.omg.PortableServer.Servant;

import BULKIO.PrecisionUTCTime;
import BULKIO.dataShortOperations;
import BULKIO.dataShortPOATie;

/**
 * 
 */
public class DataShortReceiver extends BaseBulkIOReceiver implements dataShortOperations {

	/**
	 * @param receiver
	 */
	public DataShortReceiver(final corbareceiver receiver) {
		super(receiver);
	}

	public void pushPacket(final short[] dataArray, final PrecisionUTCTime time, final boolean endOfStream, final String arg3) {
		getReceiver().write(dataArray, dataArray.length, DataTypes.INT, endOfStream, time);
	}

	@Override
	public char getType() {
		return 'I';
	}

	@Override
	public Servant toServant() {
		return new dataShortPOATie(this);
	}

}
