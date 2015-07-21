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
package gov.redhawk.sca.internal.ui.wizards;

import gov.redhawk.model.sca.ScaAbstractProperty;
import gov.redhawk.model.sca.ScaFactory;
import gov.redhawk.model.sca.ScaPropertyContainer;
import gov.redhawk.model.sca.ScaWaveform;
import gov.redhawk.sca.ui.ScaComponentFactory;
import gov.redhawk.sca.ui.properties.ScaPropertiesAdapterFactory;
import gov.redhawk.sca.ui.wizards.ScaPropertyUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import mil.jpeojtrs.sca.prf.util.PropertiesUtil;
import mil.jpeojtrs.sca.sad.SoftwareAssembly;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import CF.DataType;

public class ApplicationCreationPropertyEditWizardPage extends WizardPage {
	private final AdapterFactory adapterFactory;
	private ScaWaveform localWaveform = null;
	private String waveformId;
	private TreeViewer viewer;

	public ApplicationCreationPropertyEditWizardPage(final String pageName) {
		super(pageName);
		setTitle("Assign Initial Properties");
		this.setDescription("Provide the initial configuration for the Waveform");
		this.adapterFactory = new ScaPropertiesAdapterFactory();
	}

	@Override
	public void createControl(final Composite parent) {
		final Composite main = new Composite(parent, SWT.None);
		main.setLayout(new GridLayout());
		final Composite propComposite = new Composite(main, SWT.None);
		propComposite.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		this.viewer = ScaComponentFactory.createPropertyTable(propComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE, this.adapterFactory);
		this.viewer.addFilter(new ViewerFilter() {

			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (element instanceof ScaAbstractProperty< ? >) {
					ScaAbstractProperty< ? > prop = (ScaAbstractProperty< ? >) element;
					return PropertiesUtil.canOverride(prop.getDefinition());
				}
				return false;
			}
		});

		// ref #309 & #17  Restoring the previously set properties automatically was confusing
		// adding a button so users can do this themselves.
		final Button restoreLastButton = new Button(main, SWT.PUSH);
		restoreLastButton.setText("Restore Last");
		restoreLastButton.setToolTipText("Restore waveform properties from previous launch");
		restoreLastButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				restoreProperties();
			}
		});
		restoreLastButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.END, SWT.FILL).create());
		
		final Button resetButton = new Button(main, SWT.PUSH);
		resetButton.setText("Reset");
		resetButton.setToolTipText("Reset all the property values to default");
		resetButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				for (final ScaAbstractProperty< ? > prop : ApplicationCreationPropertyEditWizardPage.this.localWaveform.getProperties()) {
					prop.restoreDefaultValue();
				}
			}
		});
		resetButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.END, SWT.FILL).create());

		setControl(main);
	}

	public DataType[] getCreationProperties() {
		final List<DataType> retVal = new ArrayList<DataType>();
		if (this.localWaveform != null) {
			for (final ScaAbstractProperty< ? > prop : this.localWaveform.getProperties()) {
				if (!prop.isDefaultValue()) {
					retVal.add(prop.getProperty());
				}
			}
		}
		storeProperties(retVal.isEmpty());
		return retVal.toArray(new DataType[retVal.size()]);
	}

	public void init(final SoftwareAssembly sad) {
		if (sad != null) {
			this.waveformId = sad.getId();
		} else {
			this.waveformId = null;
		}
		if (this.localWaveform != null) {
			this.localWaveform.dispose();
			this.localWaveform = null;
		}

		this.localWaveform = ScaFactory.eINSTANCE.createScaWaveform();
		this.localWaveform.setDataProvidersEnabled(false);
		localWaveform.setProfileObj(sad);

		try {
			getWizard().getContainer().run(true, false, new IRunnableWithProgress() {

				@Override
				public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Fetching properties...", IProgressMonitor.UNKNOWN);

					for (final ScaAbstractProperty< ? > prop : localWaveform.fetchProperties(monitor)) {
						prop.setIgnoreRemoteSet(true);
					}

					monitor.done();
				}
			});
		} catch (final InvocationTargetException e) {
			// PASS
		} catch (final InterruptedException e) {
			// PASS
		}
		this.viewer.setInput(this.localWaveform);
	}

	private void storeProperties(final boolean empty) {
		IDialogSettings propertySettings = getDialogSettings().getSection(getName());
		if (propertySettings == null) {
			propertySettings = getDialogSettings().addNewSection(getName());
		}
		final IDialogSettings waveformSettings = propertySettings.addNewSection(this.waveformId);
		if (!empty && this.localWaveform != null) {
			ScaPropertyUtil.save(this.localWaveform, waveformSettings);
		}
	}

	private void restoreProperties() {
		if (this.localWaveform != null) {
			final IDialogSettings propertySettings = getDialogSettings().getSection(getName());
			if (this.waveformId != null && propertySettings != null) {
				final IDialogSettings waveformSettings = propertySettings.getSection(this.waveformId);
				if (waveformSettings != null) {
					ScaPropertyUtil.load(this.localWaveform, waveformSettings);
				}
			}
		}
	}
	
	@Override
	public void dispose() {
		if (this.localWaveform != null) {
			this.localWaveform.dispose();
		}
		super.dispose();
	}

	public ScaPropertyContainer< ? , ? > getPropertyContainer() {
		return this.localWaveform;
	}
}
