package gov.redhawk.frontend.edit.utils;

import gov.redhawk.frontend.FrontendFactory;
import gov.redhawk.frontend.ListenerAllocation;
import gov.redhawk.frontend.ModelDevice;
import gov.redhawk.frontend.TunerContainer;
import gov.redhawk.frontend.TunerStatus;
import gov.redhawk.frontend.UnallocatedTunerContainer;
import gov.redhawk.frontend.edit.utils.TunerProperties.TunerStatusAllocationProperties;
import gov.redhawk.model.sca.ScaDevice;
import gov.redhawk.model.sca.ScaSimpleProperty;
import gov.redhawk.model.sca.ScaStructProperty;
import gov.redhawk.model.sca.ScaStructSequenceProperty;

import java.util.List;

import mil.jpeojtrs.sca.scd.Interface;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.util.EContentAdapter;

import FRONTEND.DigitalTunerHelper;

public enum TunerUtils {
	INSTANCE;

	private TunerUtils() {
	}

	public void processChange(Notification notification) {

	}

	/**
	 * Checks device for tuners, and if found returns a TunerContainer object with an Object array
	 * 
	 * @param device
	 * @return container object for the devices tuners
	 */
	public TunerContainer getTunerContainer(final ScaDevice< ? > device) {
		//Create model device and tuner container
		ModelDevice modelDevice = FrontendFactory.eINSTANCE.createModelDevice();
		modelDevice.setScaDevice(device);
		modelDevice.setTunerContainer(FrontendFactory.eINSTANCE.createTunerContainer());

		EList<Interface> interfaceList = device.getProfileObj().getDescriptor().getComponent().getInterfaces().getInterface();
		for (Interface i : interfaceList) {
			// Check to see if this is a frontEndInterface
			if (i.getRepid().equals(DigitalTunerHelper.id())) {
				// Fetch list of tuners
				ScaStructSequenceProperty prop = (ScaStructSequenceProperty) device.getProperty("FRONTEND::tuner_status");
				List<ScaStructProperty> structs = prop.getStructs();

				// create TunerContainer model object
				TunerContainer container = modelDevice.getTunerContainer();
				EList<TunerStatus> tunerList = container.getTunerStatus();

				// populate container object with tuners from device
				int tunerIndex = 0;
				out: for (ScaStructProperty struct : structs) {
					final TunerStatus tuner = FrontendFactory.eINSTANCE.createTunerStatus();
					tuner.setTunerContainer(container);
					tuner.setTunerStatusStruct(struct);
					tuner.getSimples().addAll(struct.getSimples());
					tuner.setTunerID(String.valueOf(tunerIndex));
					tunerList.add(tuner);
					tunerIndex++;

					for (ScaSimpleProperty simple : tuner.getSimples()) {
						TunerStatusAllocationProperties.setValue(tuner, simple);
					}

					addNotificationAdapter(tuner, struct);

					// Create Listener Allocation
					ScaSimpleProperty allocSimple = struct.getSimple("FRONTEND::tuner_status::allocation_id_csv");
					if (allocSimple == null) {
						continue;
					}
					Object allocationValue = allocSimple.getValue();
					if (allocationValue == null) {
						continue;
					}
					String allocationIDs = allocationValue.toString();
					String[] allocations = allocationIDs.split(",");
					for (int index = 1; index < allocations.length; ++index) {
						if ("".equals(allocations[index])) {
							continue;
						}
						ListenerAllocation allocation = FrontendFactory.eINSTANCE.createListenerAllocation();
						allocation.setListenerID(allocations[index]);
						tuner.getListenerAllocations().add(allocation);
					}
					
					// Create Unallocated Tuner Container
					for (UnallocatedTunerContainer unallocatedContainer : container.getUnallocatedContainer()) {
						if (unallocatedContainer.getTunerType().equals(tuner.getTunerType())) {
							continue out; // If there is already an unallocated container with this type, 
											// go to the next tuner
						}
					}
					UnallocatedTunerContainer unallocatedContainer = FrontendFactory.eINSTANCE.createUnallocatedTunerContainer();
					unallocatedContainer.setTunerType(tuner.getTunerType());
					container.getUnallocatedContainer().add(unallocatedContainer);
					
				} // end tuner creation loop

				return container;
			}
		}
		return null;
	}

	private void addNotificationAdapter(final TunerStatus tuner, ScaStructProperty struct) {

		Adapter adapter = new AdapterImpl() {
			@Override
			public void notifyChanged(Notification notification) {
				super.notifyChanged(notification);
				TunerStatusAllocationProperties.updateDeviceValue(tuner, notification);
			}
		};
		tuner.eAdapters().add(adapter);

		Adapter structAdapter = new EContentAdapter() {
			@Override
			public void notifyChanged(Notification notification) {
				super.notifyChanged(notification);
				EAttribute attr = (EAttribute) notification.getFeature();
				if (attr.getName().equals("ignoreRemoteSet")) {
					return;
				}

				final Object notifier = notification.getNotifier();
				if (notifier instanceof ScaSimpleProperty) {
					ScaSimpleProperty simple = (ScaSimpleProperty) notifier;
					TunerStatusAllocationProperties.setValue(tuner, simple);
				}
			}
		};
		struct.eAdapters().add(structAdapter);
	}
}
