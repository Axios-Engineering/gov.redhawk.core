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
package gov.redhawk.sca.internal.ui.properties;

import gov.redhawk.model.sca.ScaAbstractProperty;
import gov.redhawk.model.sca.ScaPackage;
import gov.redhawk.model.sca.ScaSimpleProperty;
import gov.redhawk.model.sca.ScaSimpleSequenceProperty;
import gov.redhawk.model.sca.ScaStructProperty;
import gov.redhawk.model.sca.ScaStructSequenceProperty;
import gov.redhawk.model.sca.provider.ScaSimplePropertyItemProvider;
import gov.redhawk.model.sca.provider.ScaSimpleSequencePropertyItemProvider;
import gov.redhawk.sca.ui.ScaComponentFactory;
import gov.redhawk.sca.ui.ScaUiPlugin;
import gov.redhawk.sca.ui.properties.ScaPropertiesAdapterFactory;

import java.math.BigInteger;
import java.util.Arrays;

import mil.jpeojtrs.sca.prf.PropertyValueType;
import mil.jpeojtrs.sca.prf.provider.RadixLabelProviderUtil;
import mil.jpeojtrs.sca.util.math.ComplexBoolean;
import mil.jpeojtrs.sca.util.math.ComplexDouble;
import mil.jpeojtrs.sca.util.math.ComplexFloat;
import mil.jpeojtrs.sca.util.math.ComplexLong;
import mil.jpeojtrs.sca.util.math.ComplexLongLong;
import mil.jpeojtrs.sca.util.math.ComplexShort;
import mil.jpeojtrs.sca.util.math.ComplexULong;
import mil.jpeojtrs.sca.util.math.ComplexULongLong;
import mil.jpeojtrs.sca.util.math.ComplexUShort;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener;
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class SequencePropertyValueWizardPage extends WizardPage {

	private final ScaAbstractProperty< ? > property;
	private final AdapterFactory adapterFactory = new ScaPropertiesAdapterFactory() {

		@Override
		public Adapter createScaSimpleSequencePropertyAdapter() {
			return new ScaSimpleSequencePropertyItemProvider(this) {
				@Override
				public void notifyChanged(final org.eclipse.emf.common.notify.Notification notification) {
					updateChildren(notification);

					switch (notification.getFeatureID(ScaSimpleSequenceProperty.class)) {
					case ScaPackage.SCA_SIMPLE_SEQUENCE_PROPERTY__VALUES:
						fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, true));
						return;
					default:
						break;
					}
					super.notifyChanged(notification);
				}
			};
		}

		@Override
		public Adapter createScaSimplePropertyAdapter() {
			return new ScaSimplePropertyItemProvider(this) {
				@Override
				public Object getImage(final Object object) {
					return null;
				}
			};
		}
	};
	private TableViewer tableViewer;
	private Button removeButton;
	private Button downButton;
	private Button upButton;
	private Button resetButton;

	protected SequencePropertyValueWizardPage(final ScaAbstractProperty< ? > property) {
		super("valuePage", "Edit Value", null);
		this.property = property;
		String propDesc = property.getDescription();
		if (propDesc == null) {
			propDesc = "";
		}
		setDescription("Edit the values of the sequence property.\n" + propDesc);
	}

	@Override
	public void createControl(final Composite parent) {
		final Composite root = new Composite(parent, SWT.None);
		root.setLayout(new GridLayout(2, false));

		final Composite tableComposite = new Composite(root, SWT.NO_FOCUS);
		final TableViewer viewer = createTableViewer(tableComposite);
		tableComposite.setLayoutData(GridDataFactory.fillDefaults().hint(500, 300).span(2, 1).create());
		this.tableViewer = viewer;

		// Create spacer
		new Label(root, SWT.None).setLayoutData(GridDataFactory.fillDefaults().span(1, 1).grab(true, false).create());

		final Composite buttonComposite = createButtons(root);
		buttonComposite.setLayoutData(GridDataFactory.fillDefaults().span(1, 1).grab(false, false).create());

		setControl(root);
	}

	private Object getDefaultValue(final PropertyValueType type, boolean isComplex) {
		if (isComplex) {
			switch (type) {
			case BOOLEAN:
				return new ComplexBoolean();
			case DOUBLE:
				return new ComplexDouble();
			case FLOAT:
				return new ComplexFloat();
			case LONG:
				return new ComplexLong();
			case LONGLONG:
				return new ComplexLongLong();
			case SHORT:
				return new ComplexShort();
			case ULONG:
				return new ComplexULong();
			case USHORT:
				return new ComplexUShort();
			case ULONGLONG:
				return new ComplexULongLong();
			case STRING:
			case OCTET:
			case OBJREF:
			case CHAR:
			default:
				throw new IllegalArgumentException("Unhandled property type");
			}
		} else {
			switch (type) {
			case BOOLEAN:
				return Boolean.FALSE;
			case CHAR:
				return 'a';
			case DOUBLE:
				return 0.00d;
			case FLOAT:
				return 0.00f;
			case LONG:
				return 0;
			case LONGLONG:
				return 0L;
			case OBJREF:
				return null;
			case OCTET:
				return (short) 0;
			case SHORT:
				return (short) 0;
			case STRING:
				return "newString";
			case ULONG:
				return 0L;
			case USHORT:
				return 0;
			case ULONGLONG:
				return BigInteger.ZERO;
			default:
				throw new IllegalArgumentException("Unhandled property type");
			}
		}
	}

	private void handleAddValue() {
		if (this.property instanceof ScaSimpleSequenceProperty) {
			final ScaSimpleSequenceProperty seqProperty = (ScaSimpleSequenceProperty) this.property;
			final Object newValue = getDefaultValue(seqProperty.getDefinition().getType(), seqProperty.getDefinition().isComplex());

			seqProperty.getValues().add(newValue);
		} else if (this.property instanceof ScaStructSequenceProperty) {
			final ScaStructSequenceProperty structSeqProperty = (ScaStructSequenceProperty) this.property;
			final ScaStructProperty newStruct = structSeqProperty.createScaStructProperty();
			for (final ScaSimpleProperty p : newStruct.getSimples()) {
				if (p.getValue() == null) {
					final Object value = getDefaultValue(p.getDefinition().getType(), p.getDefinition().isComplex());
					p.setValue(value);
				}
			}
			structSeqProperty.getStructs().add(newStruct);
		}
	}

	private EList< ? > getList() {
		if (this.property instanceof ScaSimpleSequenceProperty) {
			final ScaSimpleSequenceProperty seqProperty = (ScaSimpleSequenceProperty) this.property;
			return seqProperty.getValues();
		} else if (this.property instanceof ScaStructSequenceProperty) {
			final ScaStructSequenceProperty structSequenceProperty = (ScaStructSequenceProperty) this.property;
			return structSequenceProperty.getStructs();
		}
		return null;
	}

	private void handleRemoveValue() {
		EList< ? > list = getList();
		IStructuredSelection selection = tableViewer.getStructuredSelection();
		if (!selection.isEmpty()) {
			int index = list.indexOf(tableViewer.getStructuredSelection().getFirstElement());
			list.removeAll(selection.toList());
			if (!list.isEmpty()) {
				index = Math.min(index, list.size() - 1);
				tableViewer.setSelection(new StructuredSelection(list.get(index)));
			}
		}
	}

	private void handleMoveUp() {
		if (tableViewer.getSelection().isEmpty()) {
			return;
		}

		// Move the items up in ascending order to ensure that they do not overlap 
		int[] indices = tableViewer.getTable().getSelectionIndices();
		EList< ? > list = getList();
		Arrays.sort(indices);
		for (int index : indices) {
			list.move(index-1, index);
		}
		updateButtonState();
	}

	private void handleMoveDown() {
		if (tableViewer.getSelection().isEmpty()) {
			return;
		}

		// Move the items down in descending order to ensure that they do not overlap (requires reverse iteration over
		// the indices, which is not built into Java--nor is reverse sort of primitives)
		int[] indices = tableViewer.getTable().getSelectionIndices();
		Arrays.sort(indices);
		EList< ? > list = getList();
		for (int ii = indices.length-1; ii >= 0; --ii) {
			int index = indices[ii];
			list.move(index+1, index);
		}
		updateButtonState();
	}

	private Composite createButtons(final Composite root) {
		final Composite buttonRoot = new Composite(root, SWT.None);
		buttonRoot.setLayout(new GridLayout(5, false));
		final ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();

		final Button addButton = new Button(buttonRoot, SWT.PUSH);
		//			addButton.setText("Add");
		addButton.setToolTipText("Add");
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				handleAddValue();
			}
		});
		addButton.setImage(sharedImages.getImage(ISharedImages.IMG_OBJ_ADD));
		this.removeButton = new Button(buttonRoot, SWT.PUSH);
		this.removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				handleRemoveValue();
			}
		});
		this.removeButton.setToolTipText("Remove");
		this.removeButton.setImage(sharedImages.getImage(ISharedImages.IMG_ETOOL_DELETE));

		this.upButton = new Button(buttonRoot, SWT.PUSH);
		//			upButton.setText("Up");
		this.upButton.setImage(ScaUiPlugin.getDefault().getImage("icons/clcl16/up.png"));
		this.upButton.setToolTipText("Move value up");
		this.upButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				handleMoveUp();
			}
		});
		this.downButton = new Button(buttonRoot, SWT.PUSH);
		//			downButton.setText("Down");
		this.downButton.setImage(ScaUiPlugin.getDefault().getImage("icons/clcl16/down.png"));
		this.downButton.setToolTipText("Move value down");
		this.downButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				handleMoveDown();
			}
		});
		this.resetButton = new Button(buttonRoot, SWT.PUSH);
		resetButton.setText("Reset");
		this.resetButton.setToolTipText("Reset to default value");
		this.resetButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				handleReset();
			}
		});

		updateButtonState();

		return buttonRoot;
	}

	protected void handleReset() {
		property.restoreDefaultValue();
		tableViewer.refresh();
	}

	private TableViewer createTableViewer(final Composite parent) {
		final TableViewer viewer;
		if (this.property instanceof ScaSimpleSequenceProperty) {
			final TableColumnLayout layout = new TableColumnLayout();
			parent.setLayout(layout);
			viewer = new TableViewer(parent, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
			createSimpleSequenceColumns(viewer, layout, (ScaSimpleSequenceProperty) this.property);
			viewer.getTable().setHeaderVisible(true);
			viewer.getTable().setLinesVisible(true);

			viewer.setContentProvider(new AdapterFactoryContentProvider(this.adapterFactory));

		} else {
			viewer = ScaComponentFactory.createStructSequenceTable(parent, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL,
				this.adapterFactory, (ScaStructSequenceProperty) this.property);
			viewer.getColumnViewerEditor().addEditorActivationListener(new ColumnViewerEditorActivationListener() {

				@Override
				public void beforeEditorDeactivated(final ColumnViewerEditorDeactivationEvent event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void beforeEditorActivated(final ColumnViewerEditorActivationEvent event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterEditorDeactivated(final ColumnViewerEditorDeactivationEvent event) {
					viewer.refresh(true);
				}

				@Override
				public void afterEditorActivated(final ColumnViewerEditorActivationEvent event) {

				}
			});
		}
		viewer.setInput(this.property);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				updateButtonState();
			}
		});
		return viewer;
	}

	private void createSimpleSequenceColumns(final TableViewer viewer, final TableColumnLayout layout, final ScaSimpleSequenceProperty seqProperty) {
		final TableViewerColumn columnViewer = new TableViewerColumn(viewer, SWT.CENTER);
		columnViewer.setEditingSupport(new ScaSimpleSequenceValueEditingSupport(seqProperty, viewer));
		columnViewer.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				super.update(cell);
				if (seqProperty.getDefinition() != null
					&& RadixLabelProviderUtil.supports(seqProperty.getDefinition().getType(), seqProperty.getDefinition().isComplex())) {
					int[] radix = new int[0];
					if (seqProperty.getDefinition().getValues() != null) {
						radix = RadixLabelProviderUtil.getRadix(seqProperty.getDefinition().getValues().getValue());
					}
					final Widget item = cell.getItem();
					final int valueIndex = viewer.getTable().indexOf((TableItem) item);
					if (valueIndex < radix.length) {
						cell.setText(RadixLabelProviderUtil.getText(seqProperty.getValues().get(valueIndex), radix[valueIndex]));
					} else if (radix.length > 0) {
						cell.setText(RadixLabelProviderUtil.getText(seqProperty.getValues().get(valueIndex), radix[0]));
					}
				}
			}

			@Override
			public String getText(final Object element) {
				return super.getText(AdapterFactoryEditingDomain.unwrap(element));
			}
		});
		layout.setColumnData(columnViewer.getColumn(), new ColumnPixelData(100, true));
		columnViewer.getColumn().setText("Value < " + seqProperty.getDefinition().getType().getLiteral() + " >");
	}

	/**
	 * Setter to allow us to enable / disable the remove button
	 * 
	 * @param enabled Boolean representing whether we should or should not enable the remove button
	 */
	private void updateButtonState() {
		IStructuredSelection selection = tableViewer.getStructuredSelection();
		if (selection.isEmpty()) {
			removeButton.setEnabled(false);
			upButton.setEnabled(false);
			downButton.setEnabled(false);
		} else {
			int minIndex = Integer.MAX_VALUE;
			int maxIndex = -1;
			EList< ? > list = getList();
			for (Object item : selection.toList()) {
				int index = list.indexOf(item);
				minIndex = Math.min(minIndex, index);
				maxIndex = Math.max(maxIndex, index);
			}
			removeButton.setEnabled(true);
			upButton.setEnabled(minIndex > 0);
			downButton.setEnabled(maxIndex < (list.size() - 1));
		}
	}
}
