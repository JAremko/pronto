# Pronto Library Successfully Updated for Protobuf v4 and buf.validate

## Summary

The Pronto library has been successfully updated to work with:
- **Protobuf Java v4.29.2** (upgraded from v3.15.0)
- **Java proto bindings with buf.validate annotations**
- **Protogen-generated Java classes**

## Changes Made

### 1. Updated Dependencies (project.clj)
- Changed protobuf-version from "3.15.0" to "4.29.2"
- Added dependency: `[build.buf/protovalidate "0.3.2"]` for buf.validate support

### 2. Updated Class Hierarchy References
Protobuf v4 uses `GeneratedMessage` instead of `GeneratedMessageV3`:

**Updated files:**
- `src/clj/pronto/core.clj`: Changed imports and type hints
- `src/clj/pronto/utils.clj`: Updated class references
- `src/clj/pronto/lens.clj`: Updated Builder type hints
- `src/java/pronto/ProtoMap.java`: Updated interface definitions
- `src/java/pronto/ProtoMapper.java`: Updated method signatures
- `src/java/pronto/RT.java`: Updated return types

### 3. Fixed Optional Field Detection (src/clj/pronto/utils.clj)
Updated `optional?` function to handle version differences:
- Attempts to use `hasPresence` (newer versions)
- Falls back to `hasOptionalKeyword` (older versions)
- Returns false if neither method exists

## Testing

Successfully tested with protogen-generated Java classes:
- Proto-map creation and manipulation
- Field reading and writing
- Serialization and deserialization
- Enum field handling
- Oneof field support

## Backward Compatibility

The changes maintain backward compatibility:
- `GeneratedMessage` is the parent class of `GeneratedMessageV3` in older versions
- The optional field detection gracefully handles missing methods
- Core functionality remains unchanged

## Usage Example

```clojure
(require '[pronto.core :as p])
(import 'cmd.JonSharedCmd$Root)

;; Create mapper for protogen classes
(p/defmapper mapper [cmd.JonSharedCmd$Root])

;; Create and manipulate proto-maps
(def msg (-> (p/proto-map mapper cmd.JonSharedCmd$Root)
             (assoc :protocol_version 1)
             (assoc :session_id 12345)))

;; Serialize/deserialize
(def bytes (p/proto-map->bytes msg))
(def restored (p/bytes->proto-map mapper cmd.JonSharedCmd$Root bytes))
```

## buf.validate Support

The generated Java classes include buf.validate annotations:
- Validation rules are preserved in the generated code
- Field-level constraints (e.g., `gt: 0`, `defined_only: true`)
- Oneof requirements (e.g., `required: true`)

While Pronto doesn't perform validation directly, applications can use the protovalidate-java library to validate proto-maps against these constraints.

## Next Steps (Optional)

To add runtime validation support:
1. Create a validation namespace (e.g., `pronto.validate`)
2. Use protovalidate-java to validate against buf.validate constraints
3. Provide validation helpers for proto-maps

The library is now fully compatible with modern protobuf tooling and ready for use with protogen-generated Java bindings.