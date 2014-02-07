/**
 */
package gov.redhawk.frontend.impl;

import gov.redhawk.frontend.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class FrontendFactoryImpl extends EFactoryImpl implements FrontendFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static FrontendFactory init()
  {
    try
    {
      FrontendFactory theFrontendFactory = (FrontendFactory)EPackage.Registry.INSTANCE.getEFactory(FrontendPackage.eNS_URI);
      if (theFrontendFactory != null)
      {
        return theFrontendFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new FrontendFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public FrontendFactoryImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EObject create(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case FrontendPackage.ANALOG_DEVICE: return createAnalogDevice();
      case FrontendPackage.TUNER: return createTuner();
      case FrontendPackage.TUNER_STATUS: return createTunerStatus();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AnalogDevice createAnalogDevice()
  {
    AnalogDeviceImpl analogDevice = new AnalogDeviceImpl();
    return analogDevice;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Tuner createTuner()
  {
    TunerImpl tuner = new TunerImpl();
    return tuner;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TunerStatus createTunerStatus()
  {
    TunerStatusImpl tunerStatus = new TunerStatusImpl();
    return tunerStatus;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public FrontendPackage getFrontendPackage()
  {
    return (FrontendPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static FrontendPackage getPackage()
  {
    return FrontendPackage.eINSTANCE;
  }

} //FrontendFactoryImpl
