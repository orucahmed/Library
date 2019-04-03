package ba.unsa.etf.rma.oruc_ahmed.spirala1;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class MojResultReceiver extends ResultReceiver {

    private Receiver mReceiver ;

    public MojResultReceiver(Handler handler) {
        super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if(mReceiver!=null) mReceiver.onReceiveResult(resultCode, resultData);
    }

    public void setmReceiver(Receiver mReceiver) {
        this.mReceiver = mReceiver;
    }

    public interface Receiver{
        public void onReceiveResult ( int resultCode , Bundle resultData );
    }

}
