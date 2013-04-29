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
package gov.redhawk.sca.internal.ui.wizards;

import gov.redhawk.model.sca.ScaAbstractComponent;
import gov.redhawk.model.sca.ScaAbstractProperty;
import gov.redhawk.model.sca.ScaComponent;
import gov.redhawk.model.sca.ScaFactory;
import gov.redhawk.model.sca.ScaSimpleProperty;
import gov.redhawk.model.sca.ScaSimpleSequenceProperty;
import gov.redhawk.model.sca.ScaStructProperty;
import gov.redhawk.model.sca.ScaStructSequenceProperty;
import gov.redhawk.sca.ui.ScaComponentFactory;
import gov.redhawk.sca.ui.properties.ScaPropertiesAdapterFactory;
import gov.redhawk.sca.ui.wizards.ScaPropertyUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mil.jpeojtrs.sca.partitioning.ComponentProperties;
import mil.jpeojtrs.sca.partitioning.PartitioningPackage;
import mil.jpeojtrs.sca.prf.AbstractProperty;
import mil.jpeojtrs.sca.prf.AbstractPropertyRef;
import mil.jpeojtrs.sca.prf.Properties;
import mil.jpeojtrs.sca.prf.PropertyConfigurationType;
import mil.jpeojtrs.sca.prf.Simple;
import mil.jpeojtrs.sca.prf.SimpleRef;
import mil.jpeojtrs.sca.prf.SimpleSequence;
import mil.jpeojtrs.sca.prf.SimpleSequenceRef;
import mil.jpeojtrs.sca.prf.Struct;
import mil.jpeojtrs.sca.prf.StructRef;
import mil.jpeojtrs.sca.prf.StructSequence;
import mil.jpeojtrs.sca.prf.StructSequenceRef;
import mil.jpeojtrs.sca.prf.StructValue;
import mil.jpeojtrs.sca.sad.SadPackage;
import mil.jpeojtrs.sca.sad.SoftwareAssembly;
import mil.jpeojtrs.sca.spd.SoftPkg;
import mil.jpeojtrs.sca.util.AnyUtils;
import mil.jpeojtrs.sca.util.ScaEcoreUtils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import CF.DataType;

public class ApplicationCreationPropertyEditWizardPage extends WizardPage {

	private static final EStructuralFeature[] ASSEMBLY_SPD_PATH = new EStructuralFeature[] {
	        SadPackage.Literals.SOFTWARE_ASSEMBLY__ASSEMBLY_CONTROLLER,
	        SadPackage.Literals.ASSEMBLY_CONTROLLER__COMPONENT_INSTANTIATION_REF,
	        PartitioningPackage.Literals.COMPONENT_INSTANTIATION_REF__INSTANTIATION,
	        PartitioningPackage.Literals.COMPONENT_INSTANTIATION__PLACEMENT,
	        PartitioningPackage.Literals.COMPONENT_PLACEMENT__COMPONENT_FILE_REF,
	        PartitioningPackage.Literals.COMPONENT_FILE_REF__FILE,
	        PartitioningPackage.Literals.COMPONENT_FILE__SOFT_PKG,
	};
	private final AdapterFactory adapterFactory;
	private ScaComponent assemblyController = null;
	private String waveformId;
	private TreeViewer viewer;

	public ApplicationCreationPropertyEditWizardPage(final String pageName) {
		super(pageName);
		setTitle("Assign initial properties");
		this.setDescription("Provide the initial configuration for the waveform");
		this.adapterFactory = new ScaPropertiesAdapterFactory();
	}

	public void createControl(final Composite parent) {
		final Composite main = new Composite(parent, SWT.None);
		main.setLayout(new GridLayout());
		final Composite propComposite = new Composite(main, SWT.BORDER);
		propComposite.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		this.viewer = ScaComponentFactory.createPropertyTable(propComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE, this.adapterFactory);

		final Button resetButton = new Button(main, SWT.PUSH);
		resetButton.setText("Reset");
		resetButton.setToolTipText("Reset all the property values to default");
		resetButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				for (final ScaAbstractProperty< ? > prop : ApplicationCreationPropertyEditWizardPage.this.assemblyController.getProperties()) {
					prop.restoreDefaultValue();
				}
			}
		});
		resetButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.END, SWT.FILL).create());

		setControl(main);
	}

	public DataType[] getCreationProperties() {
		final List<DataType> retVal = new ArrayList<DataType>();
		if (this.assemblyController != null) {
			for (final ScaAbstractProperty< ? > prop : this.assemblyController.getProperties()) {
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
		if (this.assemblyController != null) {
			this.assemblyController.dispose();
			this.assemblyController = null;
		}
		final SoftPkg spd = ScaEcoreUtils.getFeature(sad, ApplicationCreationPropertyEditWizardPage.ASSEMBLY_SPD_PATH);
		if (spd == null) {
			this.assemblyController = null;
			this.viewer.setInput(null);
		} else {
			this.assemblyController = ScaFactory.eINSTANCE.createScaComponent();
			this.assemblyController.setDataProvidersEnabled(false);		
			this.assemblyController.setProfileObj(spd);
			try {
				getWizard().getContainer().run(true, false, new IRunnableWithProgress() {

					private EStructuralFeature [] PATH = new EStructuralFeature [] {
							SadPackage.Literals.SOFTWARE_ASSEMBLY__ASSEMBLY_CONTROLLER,
							SadPackage.Literals.ASSEMBLY_CONTROLLER__COMPONENT_INSTANTIATION_REF,
							PartitioningPackage.Literals.COMPONENT_INSTANTIATION_REF__INSTANTIATION,
							PartitioningPackage.Literals.COMPONENT_INSTANTIATION__COMPONENT_PROPERTIES
					};
					public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						monitor.beginTask("Fetching properties of assembly controller.", IProgressMonitor.UNKNOWN);
						ApplicationCreationPropertyEditWizardPage.this.assemblyController.fetchProperties(null);
						for (final ScaAbstractProperty< ? > prop : ApplicationCreationPropertyEditWizardPage.this.assemblyController.getProperties()) {
							prop.setIgnoreRemoteSet(true);
						}
						ComponentProperties compProperties = ScaEcoreUtils.getFeature(sad, PATH);
						if (compProperties != null) {
							FeatureMap properties = sad.getAssemblyController().getComponentInstantiationRef().getInstantiation().getComponentProperties().getProperties();
							for (Iterator<Entry> i = properties.iterator(); i.hasNext();) {
								Entry entry = i.next();
								Object obj = entry.getValue();
								if (obj instanceof AbstractPropertyRef<?>) {
									AbstractPropertyRef<?> prop = (AbstractPropertyRef<?>) obj;
									setValue(assemblyController.getProperty(prop.getRefID()), prop);
								}
							}
							restoreProperties();
						}
						monitor.done();
					}
				});
			} catch (final InvocationTargetException e) {
				// PASS
			} catch (final InterruptedException e) {
				// PASS
			}
			this.viewer.setInput(this.assemblyController);
		}
	}

	protected void setValue(ScaAbstractProperty< ? > property, AbstractPropertyRef<?> prop) {
		if (property instanceof ScaSimpleProperty) {
			setValue((ScaSimpleProperty)property, (SimpleRef)prop);
		} else if (property instanceof ScaSimpleSequenceProperty) {
			setValue((ScaSimpleSequenceProperty)property, (SimpleSequenceRef)prop);
		} else if (property instanceof ScaStructProperty) {
			setValue((ScaStructProperty)property, (StructRef)prop);
		} else if (property instanceof ScaStructSequenceProperty) {
			setValue((ScaStructSequenceProperty)property, (StructSequenceRef)prop);
		}
    }
	
	protected void setValue(ScaStructSequenceProperty scaProp, StructSequenceRef prop) {
		scaProp.getStructs().clear();
		for (StructValue value : prop.getStructValue()) {
			EList<SimpleRef> refs = value.getSimpleRef();
			ScaStructProperty struct = ScaFactory.eINSTANCE.createScaStructProperty();
			scaProp.getStructs().add(struct);
			struct.setDefinition(prop.getProperty().getStruct());
			for (SimpleRef ref : refs) {
				ScaSimpleProperty simple = struct.getSimple(ref.getRefID());
				simple.setValue(AnyUtils.convertString(ref.getValue(), simple.getDefinition().getType().getLiteral()));
			}
		}
	}
	
	protected void setValue(ScaStructProperty scaProp, StructRef prop) {
		for (SimpleRef simple : prop.getSimpleRef()) {
			setValue(scaProp.getSimple(simple.getRefID()), simple);
		}
	}
	
	protected void setValue(ScaSimpleSequenceProperty property, SimpleSequenceRef prop) {
		Object [] newValue = new Object[prop.getValues().getValue().size()];
		for (int i=0; i<newValue.length; i++ ){
			String value = prop.getValues().getValue().get(i);
			newValue[i] = AnyUtils.convertString(value, prop.getProperty().getType().getLiteral());
		}
		property.setValue(newValue);
	}
	
	protected void setValue(ScaSimpleProperty property, SimpleRef prop) {
		property.setValue(AnyUtils.convertString(prop.getValue(), prop.getProperty().getType().getLiteral()));
	}

	private void storeProperties(final boolean empty) {
		IDialogSettings propertySettings = getDialogSettings().getSection(getName());
		if (propertySettings == null) {
			propertySettings = getDialogSettings().addNewSection(getName());
		}
		final IDialogSettings waveformSettings = propertySettings.addNewSection(this.waveformId);
		if (!empty && this.assemblyController != null) {
			ScaPropertyUtil.save(this.assemblyController, waveformSettings);
		}
	}

	private void restoreProperties() {
		if (this.assemblyController != null) {
			final IDialogSettings propertySettings = getDialogSettings().getSection(getName());
			if (this.waveformId != null && propertySettings != null) {
				final IDialogSettings waveformSettings = propertySettings.getSection(this.waveformId);
				if (waveformSettings != null) {
					ScaPropertyUtil.load(this.assemblyController, waveformSettings);
				}
			}
		}
	}

	public ScaAbstractComponent< ? > getAssemblyController() {
		return this.assemblyController;
	}

	@Override
	public void dispose() {
		if (this.assemblyController != null) {
			this.assemblyController.dispose();
		}
		super.dispose();
	}
}
