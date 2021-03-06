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
package gov.redhawk.sca.preferences;

import gov.redhawk.sca.ScaPlugin;

public class ScaPreferenceUtil {
	private ScaPreferenceUtil() {

	}

	public static boolean shouldAutoConnect() {
		return ScaPlugin.getDefault().getScaPreferenceAccessor().getBoolean(ScaPreferenceConstants.SCA_CORBA_AUTOCONNECT_PREFERENCE);
	}
}
