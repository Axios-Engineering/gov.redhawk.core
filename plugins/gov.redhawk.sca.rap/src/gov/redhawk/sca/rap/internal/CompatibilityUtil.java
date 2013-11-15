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
package gov.redhawk.sca.rap.internal;

import gov.redhawk.sca.ScaPlugin;
import gov.redhawk.sca.compatibility.ICompatibilityUtil;
import gov.redhawk.sca.rap.ScaRapPlugin;
import gov.redhawk.sca.rap.RapInit;

import java.io.File;
import java.lang.reflect.Field;
import java.security.Principal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.eclipse.core.runtime.Status;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.service.FileSettingStore;
import org.eclipse.rwt.service.ISettingStore;
import org.eclipse.rwt.service.SettingStoreException;
import org.eclipse.swt.widgets.Display;

public class CompatibilityUtil implements ICompatibilityUtil {

	private static final String FIELD_WORKDIR = "workDir";

	public String getUserSpecificPath(Object context) {
		if (Boolean.valueOf(System.getProperty(ScaRapPlugin.PROP_SHARED_DOMAINS))) {
			return "SHARED";
		}
				if (context == null || !(context instanceof Display)) {
					context = Display.getCurrent();
					if (context == null) {
						return "SHARED";
					}
				}
				Principal user = RapInit.getUserPrincipal((Display) context);
				if (user != null) {
					String dn = user.getName();
					MessageDigest hashSum = null;
					try {
						hashSum = MessageDigest.getInstance("SHA-256");
					} catch (NoSuchAlgorithmException e) {
						return "SHARED";
					}
					return new String(hashSum.digest(dn.getBytes()));
				}
				return "SHARED";

		//BEGIN TEMP CODE
		/* return user agent instead of DN, for dev testing */
		//Display display = (Display) context;
		//final String[] result = new String[1];
		//final boolean[] waitForResult = {true};
		//RWT.requestThreadExec(new Runnable() {

		//	@Override
		//	public void run() {
		//		String agent = RWT.getRequest().getHeader("User-Agent");
		//		if (agent.contains("Firefox")) {
		//			result[0] = "Firefox";
		//		} else if (agent.contains("Chrome")) {
		//			result[0] = "Chrome";
		//		} else {
		//			result[0] = "unknown";
		//		}
		//		waitForResult[0] = false;
		//	}

		//});

		//while (waitForResult[0] && display != null) {
		//	if (!display.readAndDispatch()) {
		//		display.sleep();
		//	}
		//}

		//return result[0];
		//END TEMP CODE
	}

	@Override
	public void initializeSettingStore(Object context) {
		ISettingStore store =  RWT.getSettingStore();
		try {
			store.loadById(getUserSpecificPath(context));
		} catch (SettingStoreException e) {
			ScaPlugin.getDefault().getLog().log(
					new Status(Status.ERROR, ScaPlugin.PLUGIN_ID, "Unable to initialize the SettingStore", e));
		}
	}

	@Override
	public File getSettingStoreWorkDir() {
		ISettingStore store = RWT.getSettingStore();
		if (store instanceof FileSettingStore) {
			Class<?> clazz = FileSettingStore.class;
			try {
				Field f = clazz.getDeclaredField(FIELD_WORKDIR);
				f.setAccessible(true);
				Object file = f.get(store);
				if (file instanceof File) {
					return ((File) file);
				}
			} catch (NoSuchFieldException e) {
				ScaPlugin.getDefault().getLog().log(
					new Status(Status.ERROR, ScaPlugin.PLUGIN_ID, "Unable to determine SettingStore work directory", e));
			} catch (SecurityException e) {
				ScaPlugin.getDefault().getLog().log(
					new Status(Status.ERROR, ScaPlugin.PLUGIN_ID, "Unable to determine SettingStore work directory", e));
			} catch (IllegalArgumentException e) {
				ScaPlugin.getDefault().getLog().log(
					new Status(Status.ERROR, ScaPlugin.PLUGIN_ID, "Unable to determine SettingStore work directory", e));
			} catch (IllegalAccessException e) {
				ScaPlugin.getDefault().getLog().log(
						new Status(Status.ERROR, ScaPlugin.PLUGIN_ID, "Unable to determine SettingStore work directory", e));
			}
		}
		return null;
	}
}
