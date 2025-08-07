package pronto;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Message;

public class RT {

    public static GeneratedMessage getProto(ProtoMap map) {
        return map.pmap_getProto();
    }

    public static boolean isBuilder(Object o) {
        return o instanceof Message.Builder;
    }
}
