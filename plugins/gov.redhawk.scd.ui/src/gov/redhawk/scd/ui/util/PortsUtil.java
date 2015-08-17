package gov.redhawk.scd.ui.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.util.FeatureMap;

import gov.redhawk.eclipsecorba.idl.IdlInterfaceDcl;
import gov.redhawk.eclipsecorba.library.IdlLibrary;
import gov.redhawk.ui.RedhawkUiActivator;
import mil.jpeojtrs.sca.scd.AbstractPort;
import mil.jpeojtrs.sca.scd.InheritsInterface;
import mil.jpeojtrs.sca.scd.Interface;
import mil.jpeojtrs.sca.scd.Interfaces;
import mil.jpeojtrs.sca.scd.ScdFactory;
import mil.jpeojtrs.sca.scd.SoftwareComponent;

public class PortsUtil {

	private PortsUtil() {
	}

	public static void createRequiredInterfaces(String repId, List<Interface> interfaces) {
		IdlLibrary library = RedhawkUiActivator.getDefault().getIdlLibraryService().getLibrary();
		if (library != null) {
			IdlInterfaceDcl decl = (IdlInterfaceDcl) library.find(repId);
			if (decl != null) {
				PortsUtil.createRequiredInterfaces(decl, interfaces);
			}
		}
	}

	private static void createRequiredInterfaces(IdlInterfaceDcl decl, List<Interface> interfaces) {
		if (!PortsUtil.containsInterface(interfaces, decl.getRepId())) {
			Interface iface = ScdFactory.eINSTANCE.createInterface();
			iface.setRepid(decl.getRepId());
			iface.setName(decl.getName());
			interfaces.add(iface);
			for (IdlInterfaceDcl inheritedDecl : decl.getInheritedInterfaces()) {
				InheritsInterface inherits = ScdFactory.eINSTANCE.createInheritsInterface();
				inherits.setRepid(inheritedDecl.getRepId());
				iface.getInheritsInterfaces().add(inherits);
				PortsUtil.createRequiredInterfaces(inheritedDecl, interfaces);
			}
		}
	}

	public static boolean containsInterface(List<Interface> list, String repId) {
		for (Interface existing : list) {
			if (existing.getRepid().equals(repId)) {
				return true;
			}
		}
		return false;
	}

	public static Map<String, Interface> getInterfaceMap(Interfaces interfaces) {
		Map<String, Interface> map = new HashMap<String, Interface>();
		for (Interface iface : interfaces.getInterface()) {
			map.put(iface.getRepid(), iface);
		}
		return map;
	}

	public static Map<Interface, Integer> getInterfaceReferenceCount(SoftwareComponent scd) {
		Map<Interface, Integer> refCount = new HashMap<Interface, Integer>();
		PortsUtil.incrementReferenceCount(refCount, scd.getComponentRepID().getInterface());
		for (FeatureMap.Entry entry : scd.getComponentFeatures().getPorts().getGroup()) {
			AbstractPort port = (AbstractPort) entry.getValue();
			PortsUtil.incrementReferenceCount(refCount, port.getInterface());
		}
		return refCount;
	}

	private static void incrementReferenceCount(Map<Interface, Integer> refCount, Interface iface) {
		Integer count = refCount.get(iface);
		if (count != null) {
			count = count + 1;
		} else {
			count = 1;
		}
		refCount.put(iface, count);
		for (InheritsInterface inherits : iface.getInheritsInterfaces()) {
			PortsUtil.incrementReferenceCount(refCount, inherits.getInterface());
		}
	}

	public static void decrementReferenceCount(Map<Interface, Integer> refCount, Interface iface) {
		for (InheritsInterface inherits : iface.getInheritsInterfaces()) {
			PortsUtil.decrementReferenceCount(refCount, inherits.getInterface());
		}
		Integer count = refCount.get(iface);
		if (count != null) {
			count = Math.max(0, count - 1);
			refCount.put(iface, count);
		}
	}
}
