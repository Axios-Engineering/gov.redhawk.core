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
package gov.redhawk.sca.util;

import gov.redhawk.sca.util.internal.ScaUtilPluginActivator;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;

/**
 * @since 3.2
 */
public class OrbSession {

	private static Map<String, OrbSession> sessionMap = Collections.synchronizedMap(new WeakHashMap<String, OrbSession>());

	/**
	 * Returns a "global" orb session
	 * @return Session
	 */
	@SuppressWarnings("null")
	public static OrbSession createSession() {
		return createSession(OrbSession.class.getName());
	}

	/**
	 * Returns an orb session with a given ID
	 * @param ID id of the session
	 * @return Session
	 */
	public static OrbSession createSession(@NonNull String id) {
		return createSession(id, Platform.getApplicationArgs(), System.getProperties());
	}

	/**
	 * Returns an orb session with a given ID
	 * @param id id of the session
	 * @param args args to pass to ORB init
	 * @param props props to pass to ORB init
	 * @return Session
	 */
	public static OrbSession createSession(@NonNull String id, String[] args, Properties props) {
		Assert.isNotNull(id, "ID must not be null");
		OrbSession session;
		synchronized (sessionMap) {
			session = sessionMap.get(id);
			if (session == null) {
				session = new OrbSession(id, args, props);
				sessionMap.put(id, session);
			}
		}
		session.connect();
		return session;
	}

	private final Properties props;
	private final String[] args;
	private final String id;
	private final AtomicInteger refs = new AtomicInteger();
	private ORB orb;
	private POA poa;

	private OrbSession(@NonNull String id, String[] args, Properties props) {
		this.id = id;
		this.args = args;
		this.props = props;
	}

	private synchronized void connect() {
		if (orb == null) {
			orb = ORBUtil.init(args, props);
		}
		refs.incrementAndGet();
	}

	@Nullable
	public String[] getArgs() {
		return args;
	}

	@NonNull
	@SuppressWarnings("null")
	public String getId() {
		return id;
	}

	@NonNull
	@SuppressWarnings("null")
	public ORB getOrb() {
		return orb;
	}

	@Nullable
	public Properties getProperties() {
		return props;
	}

	public int getInstances() {
		return refs.get();
	}

	public synchronized void dispose() {
		boolean destroy = false;
		synchronized (sessionMap) {
			if (refs.decrementAndGet() <= 0) {
				sessionMap.remove(id);
				destroy = true;
			}
		}
		if (destroy) {
			if (orb != null) {
				orb.destroy();
			}
			orb = null;
			poa = null;
		}
	}

	@NonNull
	@SuppressWarnings("null")
	public synchronized POA getPOA() throws CoreException {
		if (this.poa == null) {
			try {
				this.poa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
				this.poa.the_POAManager().activate();
			} catch (InvalidName e) {
				throw new CoreException(new Status(Status.ERROR, ScaUtilPluginActivator.ID, "Failed to find CORBA POA", e));
			} catch (AdapterInactive e) {
				throw new CoreException(new Status(Status.ERROR, ScaUtilPluginActivator.ID, "Failed to start CORBA POA", e));
			}
		}
		return this.poa;
	}

	@Override
	protected synchronized void finalize() throws Throwable {
		super.finalize();
		refs.set(-1);
		dispose();
	}

}
