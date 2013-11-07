package BULKIO.jni;

public abstract class dataDoublePOA extends omnijni.Servant implements BULKIO.dataDoubleOperations
{
  public dataDoublePOA ()
  {
  }

  public org.omg.CORBA.Object _this_object (org.omg.CORBA.ORB orb)
  {
    this._activate();
    long ref = dataDoublePOA.new_reference(this.servant_);
    BULKIO.jni._dataDoubleStub stub = new BULKIO.jni._dataDoubleStub(ref);
    String ior = omnijni.ORB.object_to_string(stub);
    return orb.string_to_object(ior);
  }

  public synchronized void _activate ()
  {
    if (this.servant_ == 0) {
      this.servant_ = dataDoublePOA.new_servant();
      set_delegate(this.servant_, this);
    }
  }

  public synchronized void _deactivate ()
  {
    if (this.servant_ != 0) {
      dataDoublePOA.del_servant(this.servant_);
      this.servant_ = 0;
    }
  }

  static {
    System.loadLibrary("bulkiojni");
  }

  private static native long new_servant();
  private static native long del_servant(long servant);
  private static native long new_reference(long servant);
  private static native void set_delegate (long servant, BULKIO.dataDoubleOperations delegate);
  private long servant_;
}
