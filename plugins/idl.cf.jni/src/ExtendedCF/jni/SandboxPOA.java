package ExtendedCF.jni;

public abstract class SandboxPOA extends omnijni.Servant implements ExtendedCF.SandboxOperations
{
  public SandboxPOA ()
  {
  }

  public org.omg.CORBA.Object _this_object (org.omg.CORBA.ORB orb)
  {
    this._activate();
    long ref = SandboxPOA.new_reference(this.servant_);
    ExtendedCF.jni._SandboxStub stub = new ExtendedCF.jni._SandboxStub(ref);
    String ior = omnijni.ORB.object_to_string(stub);
    return orb.string_to_object(ior);
  }

  public synchronized void _activate ()
  {
    if (this.servant_ == 0) {
      this.servant_ = SandboxPOA.new_servant();
      set_delegate(this.servant_, this);
    }
  }

  public synchronized void _deactivate ()
  {
    if (this.servant_ != 0) {
      SandboxPOA.del_servant(this.servant_);
      this.servant_ = 0;
    }
  }

  static {
    System.loadLibrary("ossiecfjni");
  }

  private static native long new_servant();
  private static native long del_servant(long servant);
  private static native long new_reference(long servant);
  private static native void set_delegate (long servant, ExtendedCF.SandboxOperations delegate);
  private long servant_;
}
