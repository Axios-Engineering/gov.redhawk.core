/**
 * This file is protected by Copyright.
 * Please refer to the COPYRIGHT file distributed with this source distribution.
 *
 * This file is part of REDHAWK IDE.
 *
 * All rights reserved.  This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package gov.redhawk.internal.ui.preferences;

import gov.redhawk.ui.port.nxmplot.PlotActivator;
import gov.redhawk.ui.port.nxmplot.PlotSettings;
import gov.redhawk.ui.port.nxmplot.PlotSettings.PlotMode;
import gov.redhawk.ui.port.nxmplot.preferences.PlotPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Section;

/**
 * This class represents a Port Plot preference page that
 * is contributed to the Preferences dialog. By
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class PlotPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private boolean isBlock;
	private IWorkbench workbench;
	private IPreferenceStore blockPreferenceStore;
	private List<FieldEditor> blockPreferences = new ArrayList<FieldEditor>();

	public PlotPreferencePage() {
		this(null, false, null);
	}

	protected PlotPreferencePage(String label, boolean isBlock, IPreferenceStore plotBlockStore) {
		super("Plot", FieldEditorPreferencePage.GRID);
		this.isBlock = isBlock;
		this.blockPreferenceStore = plotBlockStore;
		if (label != null) {
			setTitle(label);
		}
		setDescription("Change various settings on how the data is displayed within the plot.");
	}

	public PlotPreferencePage(String label, boolean isBlock) {
		this(label, isBlock, null);
	}

	public PlotPreferencePage(String label, IPreferenceStore plotBlockStore) {
		this(label, false, plotBlockStore);
	}

	@Override
	protected void initialize() {
		super.initialize();
		if (blockPreferenceStore != null) {
			Iterator<FieldEditor> e = blockPreferences.iterator();
			while (e.hasNext()) {
				FieldEditor pe = e.next();
				pe.setPage(this);
				pe.setPropertyChangeListener(this);
				pe.setPreferenceStore(blockPreferenceStore);
				pe.load();
			}
		}
	}

	@Override
	protected void performDefaults() {
		super.performDefaults();
		if (blockPreferenceStore != null) {
			Iterator<FieldEditor> e = blockPreferences.iterator();
			while (e.hasNext()) {
				FieldEditor pe = e.next();
				pe.loadDefault();
			}
		}
		// Force a recalculation of my error state.
		checkState();
	}

	@Override
	public boolean performOk() {
		if (!super.performOk()) {
			return false;
		}
		if (blockPreferenceStore != null) {
			Iterator<FieldEditor> e = blockPreferences.iterator();
			while (e.hasNext()) {
				FieldEditor pe = e.next();
				pe.store();
				// TODO How do we we do this for block preferences, I don't have access to the method
				//				pe.setPresentsDefaultValue(false);   
			}
		}
		return true;
	}

	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	@Override
	public void createFieldEditors() {
		if (workbench != null) {
			addField(createModesField());

			OverridableIntegerFieldEditor frameSizeField = new OverridableIntegerFieldEditor(PlotPreferences.FRAMESIZE.getName(),
				PlotPreferences.FRAMESIZE_OVERRIDE.getName(), "&Framesize:", getFieldEditorParent());
			frameSizeField.setErrorMessage("Framesize must be a positive integer >= 2");
			frameSizeField.setValidRange(2, Integer.MAX_VALUE);
			addField(frameSizeField);

			addField(new BooleanFieldEditor(PlotPreferences.ENABLE_CONFIGURE_MENU_USING_MOUSE.getName(), "&Enable plot configure menu using mouse",
				getFieldEditorParent()));
			addField(new BooleanFieldEditor(PlotPreferences.ENABLE_QUICK_CONTROLS.getName(), "Enable &quick access control widgets", getFieldEditorParent()));
		} else {
			if (isBlock) {
				addField(new OverridableIntegerFieldEditor(PlotPreferences.FRAMESIZE.getName(), PlotPreferences.FRAMESIZE_OVERRIDE.getName(), "&Framesize:",
					getFieldEditorParent()));
				//				creatBlockAdancedFields();

			} else {
				addField(createModesField());

				if (blockPreferenceStore != null) {
					OverridableIntegerFieldEditor frameSizeField = new OverridableIntegerFieldEditor(PlotPreferences.FRAMESIZE.getName(),
						PlotPreferences.FRAMESIZE_OVERRIDE.getName(), "&Framesize:", getFieldEditorParent());
					frameSizeField.setErrorMessage("Framesize must be a positive integer >= 2");
					frameSizeField.setValidRange(2, Integer.MAX_VALUE);
					blockPreferences.add(frameSizeField);
				}

				OverridableDoubleFieldEditor minField = new OverridableDoubleFieldEditor(PlotPreferences.MIN.getName(), PlotPreferences.MIN_OVERRIDE.getName(),
					"&Min:", getFieldEditorParent());
				addField(minField);

				OverridableDoubleFieldEditor maxField = new OverridableDoubleFieldEditor(PlotPreferences.MAX.getName(), PlotPreferences.MAX_OVERRIDE.getName(),
					"&Max:", getFieldEditorParent());
				addField(maxField);

				final Composite booleanControls = new Composite(getFieldEditorParent(), SWT.None);
				booleanControls.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).span(2, 1).create());
				addField(new BooleanFieldEditor(PlotPreferences.ENABLE_CONFIGURE_MENU_USING_MOUSE.getName(), "&Enable plot configure menu using mouse",
					booleanControls));
				if (blockPreferenceStore != null) {
					blockPreferences.add(new BooleanFieldEditor(PlotPreferences.ENABLE_QUICK_CONTROLS.getName(), "Enable &quick access control widgets",
						getFieldEditorParent()));
				}
				//				createAdvancedFields();
			}
		}
	}

	private void creatBlockAdancedFields() {
		final Composite parent = getFieldEditorParent();
		Section advancedComposite = new Section(parent, ExpandableComposite.TWISTIE);
		advancedComposite.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).span(2, 1).create());
		advancedComposite.setText("Advanced");
		advancedComposite.setFont(parent.getFont());
		advancedComposite.setExpanded(false);
		Composite section = new Composite(advancedComposite, SWT.None);
		advancedComposite.setClient(section);

		addField(new OverridableIntegerFieldEditor(PlotPreferences.PIPESIZE.getName(), PlotPreferences.PIPESIZE_OVERRIDE.getName(), "&Pipe Size:", section));
	}

	private void createAdvancedFields() {
		final Composite parent = getFieldEditorParent();
		Section advancedComposite = new Section(parent, ExpandableComposite.TWISTIE);
		advancedComposite.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).span(2, 1).create());
		advancedComposite.setText("Advanced");
		advancedComposite.setFont(parent.getFont());
		advancedComposite.setExpanded(false);
		Composite section = new Composite(advancedComposite, SWT.None);
		advancedComposite.setClient(section);

		if (blockPreferenceStore != null) {
			blockPreferences.add(new OverridableIntegerFieldEditor(PlotPreferences.PIPESIZE.getName(), PlotPreferences.PIPESIZE_OVERRIDE.getName(),
				"&Pipe Size:", section));
		}
		addField(new ReadOnlyStringFieldEditor(PlotPreferences.LAUNCH_ARGS.getName(), "&Launch Args:", section));
		addField(new ReadOnlyStringFieldEditor(PlotPreferences.LAUNCH_SWITCHES.getName(), "&Launch Switches:", section));
	}

	public void setBlockPreferenceStore(IPreferenceStore store) {
		this.blockPreferenceStore = store;
	}

	public IPreferenceStore getBlockPreferenceStore() {
		return blockPreferenceStore;
	}

	private static List<PlotMode> supportedModes = Arrays.asList(PlotMode.AUTO, PlotMode.MAGNITUDE, PlotMode.PHASE, PlotMode.REAL, PlotMode.IMAGINARY,
		PlotMode.REAL_AND_IMAGINARY, PlotMode.REAL_VS_IMAGINARY, PlotMode.TEN_LOG, PlotMode.TWENTY_LOG);

	private ComboFieldEditor createModesField() {
		List<String[]> modes = new ArrayList<String[]>();
		for (PlotSettings.PlotMode mode : PlotPreferencePage.supportedModes) {
			modes.add(new String[] { mode.getLabel(), mode.toString() });
		}
		String[][] modeValues = modes.toArray(new String[0][]);
		ComboFieldEditor modeField = new ComboFieldEditor(PlotPreferences.MODE.getName(), "&Mode:", modeValues, getFieldEditorParent());
		return modeField;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		this.workbench = workbench;
		setPreferenceStore(PlotActivator.getDefault().getPreferenceStore());
		setDescription("Change various default settings on how the data is displayed within the plots.");
	}

}
