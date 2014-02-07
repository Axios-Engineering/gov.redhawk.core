package gov.redhawk.frontend.ui.wizard;

import gov.redhawk.frontend.TunerContainer;
import gov.redhawk.frontend.TunerStatus;

import java.util.Collections;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class AllocateMultipleRxDigitizerWizardPage extends WizardPage {

	private String[] tableCloumns = new String[] {"Tuner Instance", "RF Flow ID", "Center Frequency", "Bandwidth"};
	private TunerStatus[] tuners;

	protected AllocateMultipleRxDigitizerWizardPage(TunerContainer container) {
		super("Allocate Multiple RX Digitizer Tuners");
		this.tuners = container.getTunerStatus().toArray(new TunerStatus[0]);
	}
	
	protected AllocateMultipleRxDigitizerWizardPage(TunerStatus[] tuners) {
		super("Allocate Multiple RX Digitizer Tuners");
		this.tuners = tuners;
	}

	@Override
	public void createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		createGroupControls(comp);
		setControl(comp);
		
		setTitle("Tuner Selection");
		setDescription("Select the Tuners you would like to allocate. Click \"Next\" to specify" +
				" the allocation parameters for each selected Tuner.");
		
	}

	private void createGroupControls(Composite parent) {
		GridLayoutFactory.fillDefaults().applyTo(parent);
		Table table = new Table(parent, SWT.CHECK | SWT.MULTI);
		addColumns(table);
		table.setHeaderVisible(true);
		CheckboxTableViewer viewer = new CheckboxTableViewer(table);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(viewer.getControl());
		viewer.setContentProvider(new IStructuredContentProvider() {

			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				// TODO Auto-generated method stub

			}

			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof TunerStatus[]) {
					return (TunerStatus[]) inputElement;
				}
				return Collections.emptyList().toArray(new Object[0]);
			}

		});
		viewer.setLabelProvider(new ITableLabelProvider() {

			@Override
			public void addListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void removeListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				String retVal = "";
				switch (columnIndex) {
				case 0:
					retVal = ((TunerStatus) element).getTunerID();
					break;
				case 1:
					retVal = ((TunerStatus) element).getRfFlowID();
					if (retVal == null || "".equals(retVal)) {
						retVal = "[None]";
					}
					break;
				case 2:
					retVal = String.valueOf(((TunerStatus) element).getCenterFrequency());
					break;
				case 3:
					retVal = String.valueOf(((TunerStatus) element).getBandwidth());
					break;
				default:
				}
				return retVal;
			}

		});
		viewer.addCheckStateListener(new ICheckStateListener() {

			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				if (event.getChecked()) {
					((TunerAllocationDetailWizard) getWizard()).addTuner((TunerStatus) event.getElement());
				} else {
					((TunerAllocationDetailWizard) getWizard()).removeTuner((TunerStatus) event.getElement());
				}
				setPageComplete(validate());
			}
			
		});
		viewer.setInput(tuners);
		setPageComplete(validate());
	}
	

	private void addColumns(Table table) {
		for (String name : tableCloumns) {
			TableColumn col = new TableColumn(table, SWT.NONE);
			col.setText(name);
			col.setWidth(200);
		}
	}
	
	@Override
	public boolean canFlipToNextPage() {
		return ((TunerAllocationDetailWizard) getWizard()).getSelectedTunerCount() > 0;
	}
	
	@Override
	public IWizardPage getPreviousPage() {
		return null;
	}
	
	private boolean validate() {
		boolean valid = true;
		String msg = null;
		if (((TunerAllocationDetailWizard) getWizard()).getSelectedTunerCount() <= 0) {
			valid = false;
			msg = "Please select a Tuner to allocate";
		}
		setErrorMessage(msg);
		return valid;
	}		
	
}
