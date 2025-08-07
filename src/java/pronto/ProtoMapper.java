package pronto;

import clojure.lang.IPersistentSet;
import clojure.lang.IPersistentMap;

import com.google.protobuf.GeneratedMessage;

public interface ProtoMapper {
    String getNamespace();

    IPersistentSet getClasses();

    ProtoMap fromBytes(Class<? extends GeneratedMessage> clazz, byte[] bytes);

    ProtoMap fromProto(Class<? extends GeneratedMessage> clazz, GeneratedMessage proto);

    ProtoMap getProto(Class<? extends GeneratedMessage> clazz);

    ProtoMap getTransient(Class<? extends GeneratedMessage> clazz);

    IPersistentMap getContext();
}
