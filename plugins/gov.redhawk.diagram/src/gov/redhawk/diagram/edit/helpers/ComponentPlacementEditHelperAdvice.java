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

// BEGIN GENERATED CODE
package gov.redhawk.diagram.edit.helpers;

import gov.redhawk.sca.util.PluginUtil;
import mil.jpeojtrs.sca.partitioning.ComponentFile;
import mil.jpeojtrs.sca.partitioning.ComponentFileRef;
import mil.jpeojtrs.sca.partitioning.ComponentFiles;
import mil.jpeojtrs.sca.partitioning.ComponentInstantiation;
import mil.jpeojtrs.sca.partitioning.ComponentPlacement;
import mil.jpeojtrs.sca.partitioning.PartitioningFactory;
import mil.jpeojtrs.sca.spd.SoftPkg;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;

/**
 * @since 3.0
 */
public abstract class ComponentPlacementEditHelperAdvice< CI extends ComponentInstantiation, CP extends ComponentPlacement<CI> > extends
        AbstractEditHelperAdvice {

	public static final String CONFIGURE_OPTIONS_SPD_URI = "SPD_URI";
	public static final String CONFIGURE_OPTIONS_CP_FILE = "CP_FILE";
	/**
     * @since 5.0
     */
	public static final String CONFIGURE_COMPONENT_INSTANTIATION = "COMP_INST";
	/**
     * @since 4.0
     */
	public static final String CONFIGURE_OPTIONS_INST_ID = "INST_ID";
	
	/**
     * @since 4.0
     */
	public static final String CONFIGURE_OPTIONS_INST_NAME = "INST_NAME";

	public ComponentPlacementEditHelperAdvice() {
	}

	@Override
	protected ICommand getBeforeConfigureCommand(final ConfigureRequest request) {
		return new ConfigureCommand(request);
	}

	protected class ConfigureCommand extends AbstractTransactionalCommand {

		protected final ConfigureRequest request;

		public ConfigureCommand(final ConfigureRequest req) {
			super(req.getEditingDomain(), "Configure Component Placement", null);
			this.request = req;
		}

		@Override
		public boolean canExecute() {
			final Object obj = this.request.getElementToConfigure();
			final ComponentPlacement< ? > cp = (ComponentPlacement< ? >) obj;
			if (cp.eResource() == null) {
				return false;
			}

			final SoftPkg spd = getSoftPkg();

			if (spd != null) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		protected CommandResult doExecuteWithResult(final IProgressMonitor monitor, final IAdaptable info) throws ExecutionException {

			final SoftPkg spd = getSoftPkg();
			if (spd == null) {
				return CommandResult.newErrorCommandResult("Invalid SPD refernce.");
			}

			// See if we have to add a new <componentfile>
			ComponentFile file = null;
			final Object cpFileParam = this.request.getParameter(ComponentPlacementEditHelperAdvice.CONFIGURE_OPTIONS_CP_FILE);
			if (cpFileParam instanceof ComponentFile) {
				file = (ComponentFile) cpFileParam;
			}
			if (file == null) {
				file = setupComponentFile(spd);
			}

			// Now add the component placement
			if (file != null) {
				final CI inst = createComponentInstantiation(this.request, spd);
				Assert.isNotNull(inst.getId());
				Assert.isNotNull(inst.getUsageName());
				final ComponentFileRef ref = PartitioningFactory.eINSTANCE.createComponentFileRef();
				ref.setFile(file);

				final CP obj = getObjectToConfigure(this.request);
				obj.setComponentFileRef(ref);
				obj.getComponentInstantiation().add(inst);
			}

			return CommandResult.newOKCommandResult();
		}

		public SoftPkg getSoftPkg() {
			final URI spdUri = getSPDURI();

			SoftPkg spd = null;
			if (spdUri != null) {
				final EObject eObj = this.request.getEditingDomain().getResourceSet().getEObject(spdUri, true);
				if (eObj instanceof SoftPkg) {
					spd = (SoftPkg) eObj;
				}
			}
			return spd;
		}

		

		public URI getSPDURI() {
			final Object spdParam = this.request.getParameter(ComponentPlacementEditHelperAdvice.CONFIGURE_OPTIONS_SPD_URI);
			URI spdUri = null;
			if (spdParam instanceof String) {
				spdUri = URI.createURI(spdParam.toString());
			} else if (spdParam instanceof URI) {
				spdUri = (URI) spdParam;
			}
			return spdUri;
		}
		


		public ComponentFile setupComponentFile(final SoftPkg spd) {
			final CP obj = getObjectToConfigure(this.request);
			ComponentFile file = null;
			ComponentFiles files = getComponentFiles(obj);
			if (files == null) {
				files = PartitioningFactory.eINSTANCE.createComponentFiles();
				setComponentFiles(obj, files);
			}
			for (final ComponentFile f : files.getComponentFile()) {
				if (f == null) {
					continue;
				}
				final SoftPkg fSpd = f.getSoftPkg();
				if (fSpd.eResource() != null && spd.eResource() != null && (spd.eResource().getURI().equals(fSpd.eResource().getURI()))) {
					file = f;
					break;
				}
			}

			if (file == null) {
				file = createComponentFile();
				files.getComponentFile().add(file);
				file.setSoftPkg(spd);
			}

			return file;
		}

	}

	/**
	 * @since 4.0
	 */
	public abstract CI createComponentInstantiation(ConfigureRequest request, SoftPkg spd);

	public abstract ComponentFile createComponentFile();

	public abstract CP getObjectToConfigure(ConfigureRequest request);

	public abstract void setComponentFiles(CP obj, ComponentFiles files);

	public abstract ComponentFiles getComponentFiles(CP obj);
	
	/**
     * @since 4.0
     */
	public String getInstantiationID(final ConfigureRequest request) {
		return (String) request.getParameter(ComponentPlacementEditHelperAdvice.CONFIGURE_OPTIONS_INST_ID);
	}
	
	/**
     * @since 4.0
     */
	public String getInstantiationName(final ConfigureRequest request) {
		return (String) request.getParameter(ComponentPlacementEditHelperAdvice.CONFIGURE_OPTIONS_INST_NAME);
	}
	
}
