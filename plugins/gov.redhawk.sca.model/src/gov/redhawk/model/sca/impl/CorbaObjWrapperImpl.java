/*******************************************************************************
 * This file is protected by Copyright. 
 * Please refer to the COPYRIGHT file distributed with this source distribution.
 *
 * This file is part of REDHAWK IDE.
 *
 * All rights reserved.  This program and the accompanying materials are made available under 
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

 // BEGIN GENERATED CODE
package gov.redhawk.model.sca.impl;

import gov.redhawk.model.sca.CorbaObjWrapper;
import gov.redhawk.model.sca.RefreshDepth;
import gov.redhawk.model.sca.ScaModelPlugin;
import gov.redhawk.model.sca.ScaPackage;
import gov.redhawk.model.sca.commands.SetLocalAttributeCommand;
import gov.redhawk.model.sca.commands.UnsetLocalAttributeCommand;
import gov.redhawk.model.sca.commands.VersionedFeature;
import gov.redhawk.model.sca.commands.VersionedFeature.Transaction;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import mil.jpeojtrs.sca.util.ProtectedThreadExecutor;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.omg.CORBA.SystemException;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Corba Obj Wrapper</b></em>'.
 * 
 * @since 12.0 <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.redhawk.model.sca.impl.CorbaObjWrapperImpl#getIor <em>Ior</em>}</li>
 *   <li>{@link gov.redhawk.model.sca.impl.CorbaObjWrapperImpl#getObj <em>Obj</em>}</li>
 *   <li>{@link gov.redhawk.model.sca.impl.CorbaObjWrapperImpl#getCorbaObj <em>Corba Obj</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class CorbaObjWrapperImpl< T extends org.omg.CORBA.Object > extends DataProviderObjectImpl implements CorbaObjWrapper<T> {

	/**
	 * The default value of the '{@link #getIor() <em>Ior</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIor()
	 * @generated
	 * @ordered
	 */
	protected static final String IOR_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getIor() <em>Ior</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIor()
	 * @generated
	 * @ordered
	 */
	protected String ior = IOR_EDEFAULT;
	/**
	 * This is true if the Ior attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean iorESet;
	/**
	 * The cached value of the '{@link #getObj() <em>Obj</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObj()
	 * @generated
	 * @ordered
	 */
	protected T obj;
	/**
	 * This is true if the Obj attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean objESet;
	/**
	 * The default value of the '{@link #getCorbaObj() <em>Corba Obj</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCorbaObj()
	 * @generated
	 * @ordered
	 */
	protected static final org.omg.CORBA.Object CORBA_OBJ_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getCorbaObj() <em>Corba Obj</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCorbaObj()
	 * @generated
	 * @ordered
	 */
	protected org.omg.CORBA.Object corbaObj = CORBA_OBJ_EDEFAULT;
	/**
	 * This is true if the Corba Obj attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean corbaObjESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CorbaObjWrapperImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScaPackage.Literals.CORBA_OBJ_WRAPPER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getIor() {
		return ior;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIor(String newIor) {
		String oldIor = ior;
		ior = newIor;
		boolean oldIorESet = iorESet;
		iorESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScaPackage.CORBA_OBJ_WRAPPER__IOR, oldIor, ior, !oldIorESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetIor() {
		String oldIor = ior;
		boolean oldIorESet = iorESet;
		ior = IOR_EDEFAULT;
		iorESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ScaPackage.CORBA_OBJ_WRAPPER__IOR, oldIor, IOR_EDEFAULT, oldIorESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetIor() {
		return iorESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public T getObj() {
		return obj;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setObj(T newObj) {
		T oldObj = obj;
		obj = newObj;
		boolean oldObjESet = objESet;
		objESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScaPackage.CORBA_OBJ_WRAPPER__OBJ, oldObj, obj, !oldObjESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetObj() {
		T oldObj = obj;
		boolean oldObjESet = objESet;
		obj = null;
		objESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ScaPackage.CORBA_OBJ_WRAPPER__OBJ, oldObj, null, oldObjESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetObj() {
		return objESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public org.omg.CORBA.Object getCorbaObj() {
		return corbaObj;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCorbaObj(org.omg.CORBA.Object newCorbaObj) {
		org.omg.CORBA.Object oldCorbaObj = corbaObj;
		corbaObj = newCorbaObj;
		boolean oldCorbaObjESet = corbaObjESet;
		corbaObjESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScaPackage.CORBA_OBJ_WRAPPER__CORBA_OBJ, oldCorbaObj, corbaObj, !oldCorbaObjESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetCorbaObj() {
		org.omg.CORBA.Object oldCorbaObj = corbaObj;
		boolean oldCorbaObjESet = corbaObjESet;
		corbaObj = CORBA_OBJ_EDEFAULT;
		corbaObjESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ScaPackage.CORBA_OBJ_WRAPPER__CORBA_OBJ, oldCorbaObj, CORBA_OBJ_EDEFAULT, oldCorbaObjESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetCorbaObj() {
		return corbaObjESet;
	}

	private Map<String, Boolean> isAMap = Collections.synchronizedMap(new HashMap<String, Boolean>());

	/**
	 * @since 14.0
	 */
	@Override
	protected void notifyChanged(final Notification msg) {
		super.notifyChanged(msg);
		if (!msg.isTouch()) {
			switch (msg.getFeatureID(CorbaObjWrapper.class)) {
			case ScaPackage.CORBA_OBJ_WRAPPER__CORBA_OBJ:
				String ior = msg.getNewValue() == null ? null : msg.getNewValue().toString();
				Class< ? extends T> corbaType = getCorbaType();
				if (corbaType != null && corbaType.isInstance(msg.getNewValue())) {
					setObj(corbaType.cast(msg.getNewValue()));
				} else {
					unsetObj();
				}
				setIor(ior);
				clearAllStatus();
				break;
			case ScaPackage.CORBA_OBJ_WRAPPER__OBJ:
				clearAllStatus();
				if (msg.getNewValue() != null) {
					attachDataProviders();
				} else {
					detachDataProviders();
				}
				if (msg.getOldValue() instanceof org.omg.CORBA.Object) {
					Callable<Void> callable = new Callable<Void>() {

						public Void call() {
							((org.omg.CORBA.Object)msg.getOldValue())._release();
							return null;
						}
					};
					
                    try {
                        ProtectedThreadExecutor.submit(callable);
                    } catch (InterruptedException e) {
                    	ScaModelPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, ScaModelPlugin.ID, "Failed to release CORBA Object", e));
                    } catch (ExecutionException e) {
                    	ScaModelPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, ScaModelPlugin.ID, "Failed to release CORBA Object", e));
                    } catch (TimeoutException e) {
                    	ScaModelPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, ScaModelPlugin.ID, "Failed to release CORBA Object", e));
                    }
					
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * @noreference This method is not intended to be referenced by clients.
	 */
	protected Class< ? extends T> getCorbaType() {
		return null;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public boolean exists() {
		// END GENERATED CODE

		try {
			if ((getCorbaObj() != null) && (!getCorbaObj()._non_existent())) {
				return true;
			}
		} catch (final SystemException e) {
			// PASS
		}
		return false;
		// BEGIN GENERATED CODE
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @throws InterruptedException
	 * @generated NOT
	 */
	public void fetchAttributes(IProgressMonitor monitor) {
		fetchNarrowedObject(monitor);
	}

	/**
	 * @since 14.0
	 */
	protected abstract void internalFetchChildren(IProgressMonitor monitor) throws InterruptedException;

	private VersionedFeature narrowedObjectFeature = new VersionedFeature(this, ScaPackage.Literals.CORBA_OBJ_WRAPPER__OBJ);

	/**
	 * <!-- begin-user-doc -->
	 * @since 14.0 
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public T fetchNarrowedObject(IProgressMonitor monitor) {
		if (isSetObj()) {
			return getObj();
		}
		Transaction transaction = narrowedObjectFeature.createTransaction();
		SubMonitor subMonitor = SubMonitor.convert(monitor, 1);
		org.omg.CORBA.Object localCorbaObj = getCorbaObj();
		if (localCorbaObj != null) {
			try {
				T newObj = narrow(localCorbaObj);
				transaction.addCommand(new SetLocalAttributeCommand(this, newObj, ScaPackage.Literals.CORBA_OBJ_WRAPPER__OBJ));
			} catch (final SystemException e) {
				IStatus status = new Status(Status.ERROR, ScaModelPlugin.ID, "Failed to narrow corba object.", e);
				transaction.addCommand(new UnsetLocalAttributeCommand(this, status, ScaPackage.Literals.CORBA_OBJ_WRAPPER__OBJ));
			}
		} else {
			transaction.addCommand(new UnsetLocalAttributeCommand(this, null, ScaPackage.Literals.CORBA_OBJ_WRAPPER__OBJ));
		}
		transaction.commit();
		subMonitor.worked(1);
		subMonitor.done();
		return getObj();
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 14.0
	 * <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public boolean _is_a(final String repId) {
		// END GENERATED CODE

		final org.omg.CORBA.Object localCorbaObj = getCorbaObj();
		if (localCorbaObj == null || repId == null) {
			return false;
		}
		Boolean isA = this.isAMap.get(repId);
		if (isA == null) {
			Callable<Boolean> callable = new Callable<Boolean>() {

				public Boolean call() throws Exception {
					boolean newVal = localCorbaObj._is_a(repId);
					isAMap.put(repId, newVal);
					return newVal;
				}
			};

			try {
				return ProtectedThreadExecutor.submit(callable); 
			} catch (InterruptedException e1) {
				return false;
			} catch (ExecutionException e1) {
				return false;
			} catch (TimeoutException e1) {
				return false;
			}
		}
		return isA;
		// BEGIN GENERATED CODE
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ScaPackage.CORBA_OBJ_WRAPPER__IOR:
				return getIor();
			case ScaPackage.CORBA_OBJ_WRAPPER__OBJ:
				return getObj();
			case ScaPackage.CORBA_OBJ_WRAPPER__CORBA_OBJ:
				return getCorbaObj();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ScaPackage.CORBA_OBJ_WRAPPER__IOR:
				setIor((String)newValue);
				return;
			case ScaPackage.CORBA_OBJ_WRAPPER__OBJ:
				setObj((T)newValue);
				return;
			case ScaPackage.CORBA_OBJ_WRAPPER__CORBA_OBJ:
				setCorbaObj((org.omg.CORBA.Object)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ScaPackage.CORBA_OBJ_WRAPPER__IOR:
				unsetIor();
				return;
			case ScaPackage.CORBA_OBJ_WRAPPER__OBJ:
				unsetObj();
				return;
			case ScaPackage.CORBA_OBJ_WRAPPER__CORBA_OBJ:
				unsetCorbaObj();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ScaPackage.CORBA_OBJ_WRAPPER__IOR:
				return isSetIor();
			case ScaPackage.CORBA_OBJ_WRAPPER__OBJ:
				return isSetObj();
			case ScaPackage.CORBA_OBJ_WRAPPER__CORBA_OBJ:
				return isSetCorbaObj();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (ior: ");
		if (iorESet) result.append(ior); else result.append("<unset>");
		result.append(", obj: ");
		if (objESet) result.append(obj); else result.append("<unset>");
		result.append(", corbaObj: ");
		if (corbaObjESet) result.append(corbaObj); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

	/**
	 * Called by {@link #setObj(org.omg.CORBA.Object)}. Sub classes should
	 * return the narrowed version of the passed in obj. The returned value will
	 * be assigned as the new value and be able to be fetched by
	 * {@link #getObj()}.
	 * 
	 * @param obj
	 *            Object to narrow
	 * @return The narrowed object
	 */
	protected abstract T narrow(org.omg.CORBA.Object obj);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public void dispose() {
		// END GENERATED CODE
		unsetCorbaObj();
		super.dispose();
		// BEGIN GENERATED CODE
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @throws InterruptedException
	 * @generated NOT
	 */
	@Override
	public final void refresh(IProgressMonitor monitor, RefreshDepth depth) throws InterruptedException {
		if (isDisposed()) {
			return;
		}
		Assert.isNotNull(depth);
		if (depth == RefreshDepth.NONE) {
			return;
		}
		SubMonitor subMonitor = SubMonitor.convert(monitor, "Refresh...", 100);
		try {
			fetchAttributes(subMonitor.newChild(20));
			switch (depth) {
			case CHILDREN:
			case FULL:
				internalFetchChildren(subMonitor.newChild(20));
				break;
			default:
				break;
			}
			super.refresh(subMonitor.newChild(60), depth);
		} finally {
			subMonitor.done();
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
	    super.finalize();
	    dispose();
	    eAdapters().clear();
	}

} // CorbaObjWrapperImpl
