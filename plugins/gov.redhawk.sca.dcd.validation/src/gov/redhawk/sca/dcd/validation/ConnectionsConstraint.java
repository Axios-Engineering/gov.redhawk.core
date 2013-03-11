/** 
 * This file is protected by Copyright. 
 * Please refer to the COPYRIGHT file distributed with this source distribution.
 * 
 * This file is part of REDHAWK IDE.
 * 
 * All rights reserved.  This program and the accompanying materials are made available under 
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 *
*/
package gov.redhawk.sca.dcd.validation;

import java.util.HashMap;

import mil.jpeojtrs.sca.dcd.DcdComponentInstantiation;
import mil.jpeojtrs.sca.dcd.DcdConnectInterface;
import mil.jpeojtrs.sca.dcd.DcdConnections;
import mil.jpeojtrs.sca.dcd.DcdPackage;
import mil.jpeojtrs.sca.dcd.DcdProvidesPort;
import mil.jpeojtrs.sca.dcd.DcdUsesPort;
import mil.jpeojtrs.sca.partitioning.ComponentSupportedInterface;
import mil.jpeojtrs.sca.partitioning.ComponentSupportedInterfaceStub;
import mil.jpeojtrs.sca.partitioning.FindByStub;
import mil.jpeojtrs.sca.partitioning.ProvidesPortStub;
import mil.jpeojtrs.sca.validator.EnhancedConstraintStatus;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.ConstraintStatus;

public class ConnectionsConstraint extends AbstractModelConstraint {

	public static final String SOURCE_ID = DcdValidationConstants.SOURCE_ID;
	public static final int STATUS_CODE = 1002;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		if (target instanceof DcdConnections) {
			final DcdConnections connections = (DcdConnections) target;

			return ConnectionsConstraint.validate(connections, ctx);
		}
		return Status.OK_STATUS;
	}

	/**
	 * Validate whether or not there are any duplicate connect interfaces contained in the connections container
	 * @param connections The container that holds all connect interfaces found in the node
	 * @param ctx The validation context which we shall report our results to
	 * @return IStatus Return OK Status if the connection container holds no duplicates or throw failure notice if it does
	 */
	public static IStatus validate(final DcdConnections connections, final IValidationContext ctx) {
		final HashMap<String, Integer> idsMap = new HashMap<String, Integer>();
		final HashMap<String, DcdConnectInterface> connectMap = new HashMap<String, DcdConnectInterface>();

		if (connections.getConnectInterface().size() > 0) {
			for (final DcdConnectInterface connect : connections.getConnectInterface()) {
				if (connect.getSource() != null && connect.getTarget() != null) {
					if (connect.getSource().eContainer() instanceof FindByStub || connect.getTarget().eContainer() instanceof FindByStub) {
						continue;
					}

					final String uniqueName = ConnectionsConstraint.getUniqueName(connect);

					if (!idsMap.containsKey(uniqueName)) {
						idsMap.put(uniqueName, 0);
					} else {
						final int count = idsMap.get(uniqueName) + 1;
						idsMap.put(uniqueName, count);

						if (!connectMap.containsKey(uniqueName)) {
							connectMap.put(uniqueName, connect);
						}
					}
				}
			}

			if (connectMap.size() > 0) {
				return new EnhancedConstraintStatus((ConstraintStatus) ctx.createFailureStatus(ConnectionsConstraint.getMessage(idsMap, connectMap)),
				        DcdPackage.Literals.DEVICE_CONFIGURATION__CONNECTIONS);
			}
		}

		return Status.OK_STATUS;
	}

	/**
	 * Determine if the passed connect interface is unique in the node
	 * @param conn The connection that we shall search for other instances of via its owning container
	 * @return Boolean Whether or not the connect interface is unique
	 */
	public static boolean uniqueConnection(final DcdConnectInterface conn) {
		final String uniqueName = ConnectionsConstraint.getUniqueName(conn);
		int occurrences = 0;
		if (conn.eContainer() instanceof DcdConnections) {
			final DcdConnections connections = (DcdConnections) conn.eContainer();

			if (connections.getConnectInterface().size() > 0) {
				for (final DcdConnectInterface connect : connections.getConnectInterface()) {
					if (ConnectionsConstraint.getUniqueName(connect).equals(uniqueName)) {
						occurrences++;
					}

					if (occurrences > 1) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Compile a warning message that details all duplicate connections found in the users' node
	 * @param idsMap HashMap that pairs unique ids to the number of occurrences in the node
	 * @param connectMap HashMap pair unique ids to connect interfaces
	 * @return The message as a string
	 */
	private static String getMessage(final HashMap<String, Integer> idsMap, final HashMap<String, DcdConnectInterface> connectMap) {
		final StringBuffer retVal = new StringBuffer();

		for (final String uniqueName : idsMap.keySet()) {
			if (retVal.toString().length() > 0) {
				retVal.append("\n");
			}

			final DcdConnectInterface connect = connectMap.get(uniqueName);

			if (connect != null && connect.getSource() != null && connect.getTarget() != null) {
				final DcdUsesPort uses = connect.getUsesPort();

				retVal.append(idsMap.get(uniqueName));
				retVal.append(" extra occurrence(s) of connection between ");
				retVal.append(uses.getUsesIndentifier() + " port of ");
				retVal.append(uses.getComponentInstantiationRef().getInstantiation().getUsageName() + " device and ");

				if (connect.getTarget() instanceof ProvidesPortStub) {
					final DcdProvidesPort provides = connect.getProvidesPort();
					retVal.append(provides.getProvidesIdentifier() + " port of ");
					retVal.append(provides.getComponentInstantiationRef().getInstantiation().getUsageName() + " device.");

				} else {
					final ComponentSupportedInterface inter = connect.getComponentSupportedInterface();
					retVal.append(inter.getSupportedIdentifier() + " port of ");
					retVal.append(inter.getComponentInstantiationRef().getInstantiation().getUsageName() + " device.");
				}
			}
		}

		return retVal.toString();
	}

	/**
	 * Return unique name that is of pattern <source_port_name> <parent_component> <target_port_name> <target_parent_component>
	 * @param connect The connect interface that we shall make a unique name for
	 * @return String Return the unique name as a string
	 */
	private static String getUniqueName(final DcdConnectInterface connect) {
		final StringBuffer retVal = new StringBuffer();

		if (connect.getSource() != null && connect.getSource().eContainer() instanceof DcdComponentInstantiation) {
			final DcdUsesPort uses = connect.getUsesPort();

			retVal.append(uses.getUsesIndentifier() + " " + uses.getComponentInstantiationRef().getRefid() + " ");
		}

		if (connect.getTarget() != null) {
			if (connect.getTarget().eContainer() instanceof DcdComponentInstantiation) {
				if (connect.getTarget() instanceof ProvidesPortStub) {
					final DcdProvidesPort provides = connect.getProvidesPort();

					retVal.append(provides.getProvidesIdentifier() + " " + provides.getComponentInstantiationRef().getRefid());
				} else if (connect.getTarget() instanceof ComponentSupportedInterfaceStub) {
					final ComponentSupportedInterface inter = connect.getComponentSupportedInterface();

					retVal.append(inter.getSupportedIdentifier() + " " + inter.getComponentInstantiationRef().getRefid());
				}
			}
		}
		return retVal.toString();
	}

}
